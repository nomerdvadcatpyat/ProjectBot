package bot.tools.locator;

import bot.BotProperties;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class Locator {

    private Location location = null;
    private LocatorState locatorState = LocatorState.LOCATION_WAITING;
    private ArrayList<Place> suitablePlaces;
    private Settings settings = new Settings(this);
    private static final Logger logger = Logger.getLogger(Locator.class.getName());
    private boolean isLocationInitiallyUpdated = false;
    public boolean usingTestSearchMap = false;

    public String getAnswer(String text) throws IOException {
        if (text.equals("/settings")) {
            settings.setPreviousState(locatorState);
            locatorState = LocatorState.SETTINGS;
            //region Логи
            if (location != null)
                logger.info("Old settings:\nPlaces Count: " + settings.getPlacesCount() + "\nScale: " +
                        settings.getScale() + "\nLocation: Lat=" + location.getLatitude() + ", Lon=" + location.getLongitude());
            else
                logger.info("Old settings:\nPlaces Count: " + settings.getPlacesCount() + "\nScale: " +
                        settings.getScale() + "\nLocation: not received");
            //endregion
            return "Это настройки локатора. Если нужна помощь, введите /help";
        }
        switch (locatorState) {
            case LOCATION_WAITING:
                if (location == null)
                    return "Для начала отправте боту свою геопозицию. Чтобы сделать это нажмите на кнопку внизу";
                locatorState = LocatorState.WAITING_FOR_QUERY;
                return "Теперь можно начать. Введите запрос";
            case WAITING_FOR_QUERY:
                suitablePlaces = getSuitablePlaces(text);
                if (suitablePlaces.size() == 0)
                    return "К сожалению, по вашему запросу ничего не удалось найти. Пожалуйста, сделайте новый запрос";
                locatorState = LocatorState.WAITING_FOR_ADDITIONAL_QUERY_FIRST;
                return buildMainAnswer();
            case WAITING_FOR_ADDITIONAL_QUERY_FIRST:
            case WAITING_FOR_ADDITIONAL_QUERY_SECOND:
                return handleAdditionalQuery(text);
            case WAITING_FOR_PLACE_NUMBER:
                locatorState = LocatorState.WAITING_FOR_ADDITIONAL_QUERY_FIRST;
                int number = Integer.parseInt(text) - 1;
                if (number < 0 || number >= suitablePlaces.size())
                    return "Такого места нет. Введите только одну цифру, соответствующую номеру одного из доступных мест";
                return "Вот какая информация есть об этом месте:\n\n" +
                        suitablePlaces.get(number).getAdditionalInfo() + "\n\n" +
                        "Я могу дать информацию о других местах, или показать карту. Также вы можете сделать новый запрос";
            case WAITING_FOR_ADDITIONAL_FUNCTIONAL_NUMBER:
                locatorState = LocatorState.WAITING_FOR_ADDITIONAL_QUERY_FIRST;
                String newText;
                switch (text){
                    case "1":
                        newText = "подробн";
                        break;
                    case "2":
                        newText = "карт";
                        break;
                    case "3":
                        newText = "новый";
                        break;
                    default:
                        return "Такой опции нет. Введите только одну цифру, соответствующую номеру одной из доступных опций";
                }
                return getAnswer(newText);
            case SETTINGS:
                //region Обработка команд настроек
                String[] setting = text.split(" ");
                String command = setting[0];
                switch (command){
                    case"/placesCount":
                        try {
                            int placesCount = Integer.parseInt(setting[1]);
                            if (setting.length != 2 || placesCount <= 0 || placesCount > 99)
                                return "Setting error";
                            settings.setPlacesCount(placesCount);
                            return "Количество мест для поиска изменено";
                        }catch (Exception e) {
                            return "Setting error";
                        }
                    case "/newGeo":
                        if (settings.getPreviousState() == LocatorState.LOCATION_WAITING)
                            settings.setPreviousState(LocatorState.WAITING_FOR_QUERY);
                        try{
                            float lat = Float.parseFloat(setting[1]);
                            float lon = Float.parseFloat(setting[2]);
                            if (setting.length != 3)
                                return "Setting error";
                            if (settings.getPreviousState() == LocatorState.LOCATION_WAITING) {
                                settings.setPreviousState(LocatorState.WAITING_FOR_QUERY);
                                return "Теперь можно начать. Введите запрос";
                            }
                            return settings.updateLocationManually(new Location(lat, lon));
                        }catch (Exception e){
                            return "Setting error";
                        }
                    case "/scale":
                        try{
                            int scale = Integer.parseInt(setting[1]);
                            if (setting.length != 2 || scale < -1 || scale > 17)
                                return "Setting error";
                            settings.setScale(scale);
                            return "Масштаб карты изменен";
                        }
                        catch (Exception e){
                            return "Setting error";
                        }
                    case "/default":
                        if (setting.length != 1)
                            return "Setting error";
                        settings = settings.toDefault();
                        return "Настройки восстановлены до значений по умолчанию";
                    case "/help":
                        if (setting.length != 1)
                            return "Setting error";
                        return settings.getHelp();
                    case "/back":
                        if (setting.length != 1)
                            return "Setting error";
                        locatorState = settings.getPreviousState();
                        //region Логи
                        if (location != null)
                            logger.info("New settings:\nPlaces Count: " + settings.getPlacesCount() + "\nScale: " +
                                    settings.getScale() + "\nLocation: Lat=" + location.getLatitude() + ", Lon=" + location.getLongitude());
                        else
                            logger.info("New settings:\nPlaces Count: " + settings.getPlacesCount() + "\nScale: " +
                                    settings.getScale() + "\nLocation: not not received");
                        //endregion
                        return "Выход из меню настроек";
                }
                //endregion
                break;
        }
        return null;
    }

    private String handleAdditionalQuery(String text){
        text = text.toLowerCase();
        if (text.contains("подробн") || text.contains("доп") || text.contains("информ") || text.contains("больш") ||
                text.contains("расск")){
            locatorState = LocatorState.WAITING_FOR_PLACE_NUMBER;
            return "Конечно, пожалуйста введите номер нужного места:";
        }
        else if (text.contains("карт"))
            return "{\"message\":\"Вот карта с отмеченными на ней местами, которые удалось найти по вашему запросу." +
                    "Метка с буквой А - это ваше месторасположение. Если хотите, можете посмотреть дополнительную информацию о любом" +
                    "из этих мест или сделать новый запрос\",\"url\":\"" + getStaticMapURL() + "\"}";
        else if (text.contains("новый")) {
            locatorState = LocatorState.WAITING_FOR_QUERY;
            return "Конечно. Введите запрос:";
        }
        else {
            if (locatorState == LocatorState.WAITING_FOR_ADDITIONAL_QUERY_FIRST) {
                locatorState = LocatorState.WAITING_FOR_ADDITIONAL_QUERY_SECOND;
                return "К сожалению, ваш ответ не удалось обработать. Пожалуйста, повторите запрос";
            }
            locatorState = LocatorState.WAITING_FOR_ADDITIONAL_FUNCTIONAL_NUMBER;
            return "Снова не удалось обработать ответ. Пожалуйста выберите номер желаемой опции:\n\n" +
                    "1. Дополнительная информация о найденных местах\n\n2. Карта найденных мест\n\n3. Новый запрос";
        }
    }

    private String getStaticMapURL() {
        StringBuilder result = new StringBuilder();
        result.append("https://static-maps.yandex.ru/1.x/?l=map&size=650,450&");
        if (settings.getScale() != -1)
            result.append("ll="+location.getLongitude() + "," + location.getLatitude() + "&z=" + settings.getScale() + "&");
        //region Ставим метки
        result.append("pt=" + location.getLongitude() + "," + location.getLatitude() + ",pm2al~");
        for (int i = 0; i < suitablePlaces.size(); i++){
            Place place = suitablePlaces.get(i);
            Location location = place.getLocation();
            if (i == suitablePlaces.size() - 1)
                result.append(location.getLongitude() + "," + location.getLatitude() + ",pm2ntm" + (i + 1));
            else
                result.append(location.getLongitude() + "," + location.getLatitude() + ",pm2ntm" + (i + 1) + "~");
        }
        //endregion
        logger.info("Static Map request - " + result.toString());
        return result.toString();
    }

    private String buildMainAnswer() {
        StringBuilder result = new StringBuilder();
        result.append("Вот что удалось найти:\n");
        result.append("\n");
        for (int i = 0; i < suitablePlaces.size(); i++) {
            Place place = suitablePlaces.get(i);
            result.append(i+1);
            result.append(". ");
            result.append(place.getName());
            if (place.getType() != null) {
                result.append(" - ");
                result.append(place.getType());
            }
            result.append("\n\n");
        }
        result.append("Я могу дать дополнительную информацию об одном из этих мест или показать их на карте." +
                "Если хотите, можете сделать новый запрос");
        return result.toString();
    }

    public String updateLocation(Location location) {
        this.location = location;
        if (locatorState == LocatorState.LOCATION_WAITING) {
            locatorState = LocatorState.WAITING_FOR_QUERY;
            isLocationInitiallyUpdated = true;
            return "Геопозиция обновлена. Теперь можете сделать запрос";
        }
        return "Геопозиция обновлена";
    }

    private ArrayList<Place> getSuitablePlaces(String text) throws IOException {
        JSONObject object;
        if (usingTestSearchMap){
            String testJSON = new String(Files.readAllBytes(Paths.get("src/main/resources/testSearchMapContent.json")), StandardCharsets.UTF_8);
             object = new JSONObject(testJSON);
        }
        else {
            URL searchMapURL = new URL(buildSearchMapQuery(text));
            Scanner sc = new Scanner((InputStream) searchMapURL.getContent());
            String searchMapAnswer = "";
            while (sc.hasNext()) {
                searchMapAnswer += sc.nextLine();
            }
            object = new JSONObject(searchMapAnswer);
        }
        JSONArray features = object.getJSONArray("features");
        ArrayList<Place> result = new ArrayList<>();
        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            result.add(buildPlace(feature));
        }
        logger.info("Places find: " + result.size());
        return result;
    }

    private Place buildPlace(JSONObject feature) {
        JSONObject properties = feature.getJSONObject("properties");
        String metaDataKey = properties.has("CompanyMetaData") ? "CompanyMetaData" : "GeocoderMetaData";
        JSONObject metaData = properties.getJSONObject(metaDataKey);
        String name;
        if (metaDataKey.equals("CompanyMetaData"))
            name = properties.has("name") ? properties.getString("name") : metaData.getString("name");
        else
            name = properties.has("name") ? properties.getString("name") : metaData.getString("text");
        JSONArray coordinates = feature.getJSONObject("geometry").getJSONArray("coordinates");
        Location location = new Location(coordinates.getFloat(1), coordinates.getFloat(0));   //В апи яндекса идет сначала долгота, потом широта
        String type = null;
        if (metaData.has("Categories")) {
            type = metaData.getJSONArray("Categories").getJSONObject(0).getString("name");
        }
        else {
            String kind = metaData.getString("kind");
            switch (kind) {
                case "house":
                    type = "Дом";
                    break;
                case "street":
                    type = "Улица";
                    break;
                case "metro":
                    type = "Станция метро";
                    break;
                case "district":
                    type = "Район города";
                    break;
                case "locality":
                    type = "Населенный пункт";
                    break;
            }
        }
        String additionalInfo = buildAdditionalInfo(metaData);
        Place result = new Place(name, location, type, additionalInfo);
        return result;
    }

    private String buildSearchMapQuery(String text) throws UnsupportedEncodingException { //запрос к API поиска по организациям от яндекса
        StringBuilder searchMapQuery = new StringBuilder();
        searchMapQuery.append("https://search-maps.yandex.ru/v1/?text=");
        searchMapQuery.append(URLEncoder.encode(text, "UTF-8"));
        searchMapQuery.append("&lang=ru_RU&ll=");
        searchMapQuery.append(location.getLongitude());
        searchMapQuery.append(",");
        searchMapQuery.append(location.getLatitude());
        searchMapQuery.append("&spn=0.1,0.1&results=");
        searchMapQuery.append(settings.getPlacesCount());
        searchMapQuery.append("&apikey=");
        searchMapQuery.append(BotProperties.getProperty("SearchMapsKey"));
        logger.info("Search Map query - " + searchMapQuery.toString());
        return searchMapQuery.toString();
    }

    private String buildAdditionalInfo(JSONObject metaData) {
        StringBuilder result = new StringBuilder();
        if (metaData.has("address"))
            result.append("Адрес: " + metaData.get("address") + "\n");
        else if (metaData.has("kind")) {
            result.append("Адрес: " + metaData.get("text") + "\n");
            String kind = metaData.getString("kind");
            String type = null;
            switch (kind) {
                case "house":
                    type = "Дом";
                    break;
                case "street":
                    type = "Улица";
                    break;
                case "metro":
                    type = "Станция метро";
                    break;
                case "district":
                    type = "Район города";
                    break;
                case "locality":
                    type = "Населенный пункт";
                    break;
            }
            result.append("Вид топонима: " + type + "\n");
        }
        result.append("\n");
        if (metaData.has("url"))
            result.append("Сайт: " + metaData.get("url") + "\n");
        result.append("\n");
        if (metaData.has("Categories")) {
            result.append("Категории организации: ");
            JSONArray categories = metaData.getJSONArray("Categories");
            for (int i = 0; i < categories.length(); i++) {
                if (i == categories.length() - 1)
                    result.append(categories.getJSONObject(i).getString("name"));
                else
                    result.append(categories.getJSONObject(i).getString("name") + ", ");
            }
            result.append("\n");
        }
        result.append("\n");
        if (metaData.has("Phones")) {
            result.append("Телефон: \n");
            JSONArray phones = metaData.getJSONArray("Phones");
            for (int i = 0; i < phones.length(); i++) {
                JSONObject phone = phones.getJSONObject(i);
                if (!phone.has("type") || phone.getString("type").equals("phone"))
                    result.append("\t" + phone.getString("formatted") + "\n");
            }
        }
        result.append("\n");
        if (metaData.has("Hours"))
            result.append("Режим работы: " + metaData.getJSONObject("Hours").getString("text") + "\n");
        return result.toString();
    }

    public LocatorState getState() { return locatorState; }

    public boolean isLocationInitiallyUpdated() {
        return isLocationInitiallyUpdated;
    }
}
