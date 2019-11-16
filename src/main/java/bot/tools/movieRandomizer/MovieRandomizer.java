package bot.tools.movieRandomizer;

import bot.BotProperties;
import bot.telegram.TelegramBot;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.logging.Logger;

public class MovieRandomizer {
    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());
    private static final String api = BotProperties.getProperty("TMDB_api");
    private static final String baseUrl = "https://api.themoviedb.org/";

    public String getRandomMovie() {
        logger.info(api);
        try {
            URL lastMovie = new URL(baseUrl + "3/movie/latest?api_key=" + api + "&language=ru-RU");
            logger.info(lastMovie.toString());
            Scanner sc = new Scanner((InputStream) lastMovie.getContent());
            String JSONString = "";
            while (sc.hasNext()){
                JSONString += sc.nextLine();
            }
            JSONObject obj = new JSONObject(JSONString);
            Long lastId = obj.getLong("id");
            logger.info(lastId.toString());
            return lastId.toString();
            //URL url = new URL("https://pixabay.com/api/?key= +
            //        URLEncoder.encode(query, "UTF-8") +"&image_type=all&per_page=200&safesearch=false");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
