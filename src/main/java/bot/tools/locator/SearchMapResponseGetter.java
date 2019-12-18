package bot.tools.locator;

import bot.BotProperties;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.logging.Logger;

class SearchMapResponseGetter implements JSONResponseGetter {

    Locator locator;
    private static final Logger logger = Logger.getLogger(SearchMapResponseGetter.class.getName());

    SearchMapResponseGetter(Locator locator){
        this.locator = locator;
    }

    @Override
    public JSONObject getSearchMapResponse(String text) throws IOException {
        URL searchMapURL = new URL(buildSearchMapQuery(text));
        Scanner sc = new Scanner((InputStream) searchMapURL.getContent());
        String searchMapAnswer = "";
        while (sc.hasNext()) {
            searchMapAnswer += sc.nextLine();
        }
        return new JSONObject(searchMapAnswer);
    }

    private String buildSearchMapQuery(String text) throws UnsupportedEncodingException { //запрос к API поиска по организациям от яндекса
        StringBuilder searchMapQuery = new StringBuilder();
        searchMapQuery.append("https://search-maps.yandex.ru/v1/?text=");
        searchMapQuery.append(URLEncoder.encode(text, "UTF-8"));
        searchMapQuery.append("&lang=ru_RU&ll=");
        searchMapQuery.append(locator.getLocation().getLongitude());
        searchMapQuery.append(",");
        searchMapQuery.append(locator.getLocation().getLatitude());
        searchMapQuery.append("&spn=0.1,0.1&results=");
        searchMapQuery.append(locator.getSettings().getPlacesCount());
        searchMapQuery.append("&apikey=");
        searchMapQuery.append(System.getenv("SEARCH_MAP_KEY"));
        logger.info("Search Map query - " + searchMapQuery.toString());
        return searchMapQuery.toString();
    }
}
