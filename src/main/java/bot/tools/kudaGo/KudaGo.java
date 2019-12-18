package bot.tools.kudaGo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class KudaGo {

    Random rnd = new Random();
    private boolean citySelected;
    public boolean isCitySelected() {
        return citySelected;
    }

    private String city;
    private final String baseUrl = "https://kudago.com/public-api/v1.4/";

    public String getAnswer(String message){
        if(!citySelected) {
            city = convertMessageToCity(message);
            if(citySelected) return "Вы выбрали город " + message;
            else return "Выберите город из предложенных";
        }
        String query;
        if(message.equals("Места")) {
            query = baseUrl + "places/?fields=title,description,body_text,address,timetable,price,images&text_format=text&location=" + city;
            JSONObject jsonPlace = getRandomJSON(query);
            String title = jsonPlace.getString("title");
            return new Place(title.substring(0,1).toUpperCase() + title.substring(1),
            jsonPlace.getString("description").replaceAll("\n", ""),
            jsonPlace.getString("body_text").replaceAll("\n", ""),
            jsonPlace.getString("address"),
            jsonPlace.getJSONArray("images").getJSONObject(0).getString("image"),
            jsonPlace.getString("timetable")).toString();
        }
        if(message.equals("События")) {
            Event resEvent = new Event();
            long since = (System.currentTimeMillis() / 1000L) - 432000;
            long until = (System.currentTimeMillis() / 1000L) + 157680000;
            query = baseUrl + "events/?actual_since="+ since + "&actual_until=" + until + "&fields=title,description,body_text,place,dates,price,images&text_format=text&location=" + city;
            JSONObject jsonEvent = getRandomJSON(query);
            System.out.println(query);
            System.out.println(jsonEvent.toString());
            while (!isActualEvent(jsonEvent)) {
                System.out.println("wrong event");
                jsonEvent = getRandomJSON(query);
            }
            String title = jsonEvent.getString("title");
            resEvent.title = title.substring(0,1).toUpperCase() + title.substring(1);
            String place = "";
            if(!jsonEvent.isNull("place"))
                try {
                    int placeID = jsonEvent.getJSONObject("place").getInt("id");
                    System.out.println(placeID);
                    place = getJSONObject(new URL(baseUrl + "places/" + placeID + "/")).getString("title") + ", "
                        + getJSONObject(new URL(baseUrl + "places/" + placeID + "/")).getString("address");
                }catch (Exception e){
                    e.printStackTrace();
                }
            resEvent.place = place;
            JSONObject dates = jsonEvent.getJSONArray("dates").getJSONObject(0);
            if(dates.getLong("start") == -62135433000L || dates.getLong("end") == 253370754000L)
                resEvent.dates = "Круглый год.";
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                Date startDate = new Date(dates.getLong("start") * 1000L);
                Date endDate = new Date(dates.getLong("end") * 1000L);
                resEvent.dates = "Начало: " + sdf.format(startDate) + ", Конец: " + sdf.format(endDate);
            }
            resEvent.description = jsonEvent.getString("description").replaceAll("\n", "");
            resEvent.body_text = jsonEvent.getString("body_text").replaceAll("\n", "");
            resEvent.price = jsonEvent.getString("price");
            resEvent.imageUrl =  jsonEvent.getJSONArray("images").getJSONObject(0).getString("image");
            return resEvent.toString();
        }
        return "";
    }

    private boolean isActualEvent(JSONObject event){
        long currentUnixTime = System.currentTimeMillis() / 1000L;
        JSONObject dates = event.getJSONArray("dates").getJSONObject(0);
        System.out.println(currentUnixTime);
        System.out.println(dates.getLong("start") + " " + dates.getLong("end"));
        return dates.getLong("start") < currentUnixTime &&
                currentUnixTime < dates.getLong("end");
    }

    private JSONObject getRandomJSON(String query){
        JSONObject randomJSON = new JSONObject();
        try {
            URL url = new URL(query);
            JSONObject obj = getJSONObject(url);
            int randomPage = rnd.nextInt(obj.getInt("count") / 20) + 1;
            System.out.println("randomPage " + randomPage);
            query += "&page=" + randomPage;
            url = new URL(query);
            obj = getJSONObject(url);
            JSONArray results = obj.getJSONArray("results");
            int rand = rnd.nextInt(results.length());
            System.out.println("el " + rand);
            randomJSON = results.getJSONObject(rand);
        }catch (Exception e){
            e.printStackTrace();
        }
        return randomJSON;
    }

    private String convertMessageToCity(String message){
        citySelected = true;
        switch (message){
            case "Екатеринбург": return "ekb";
            case "Красноярск": return "krasnoyarsk";
            case "Краснодар": return "krd";
            case "Казань": return "kzn";
            case "Москва": return "msk";
            case "Нижний Новгород": return "nnv";
            case "Новосибирск": return "nsk";
            case "Сочи": return "sochi";
            case "Санкт-Петербург": return "spb";
            default:
                citySelected = false;
                return "";
        }
    }

    private JSONObject getJSONObject(URL url){
        JSONObject obj = new JSONObject();
        try {
            Scanner sc = new Scanner((InputStream) url.getContent());
            StringBuilder JSONString = new StringBuilder();
            while (sc.hasNext()) {
                JSONString.append(sc.nextLine());
            }
            obj = new JSONObject(JSONString.toString());
        } catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        return obj;
    }
}
