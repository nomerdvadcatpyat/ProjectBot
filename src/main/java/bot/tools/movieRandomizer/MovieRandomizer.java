package bot.tools.movieRandomizer;

import bot.BotProperties;
import bot.telegram.TelegramBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class MovieRandomizer {
    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());
    private static final String api = BotProperties.getProperty("TMDB_api");
    private static final String baseUrl = "https://api.themoviedb.org/";
    private Random rnd = new Random();
    private int lastMovieID = getLastMovieID();
    private int randomID = rnd.nextInt(lastMovieID - 1) + 1;
    private HashSet<Integer> usedIDs = new HashSet<>();
    //private JSONObject obj = new JSONObject();

    public Movie getRandomMovie() {
        logger.info(api);
        Movie randomMovie = new Movie();
        try {
            JSONObject obj = randomizeMovieJSON();
            while(obj.isEmpty()){ //|| obj.getString("overview").isEmpty()){
                logger.info("empty or no review");
                obj = randomizeMovieJSON();
            }
            randomMovie.setTitle(obj.getString("title"));
            if (obj.getJSONArray("genres").isEmpty())
                randomMovie.setGenre("нет жанров.");
            else {
                JSONArray genres = obj.getJSONArray("genres");
                StringBuilder res= new StringBuilder();
                for(Object genre : genres)
                    res.append(((JSONObject) genre).getString("name")).append(", ");
                res.deleteCharAt(res.length()-2);
                randomMovie.setGenre(res.toString());
            }
            if(obj.getString("overview").isEmpty())
                randomMovie.setOverview("нет описания.");
            else
                randomMovie.setOverview(obj.getString("overview"));
            if(obj.isNull("poster_path")) // можно засунуть какую-нибудь стандартную картинку
                randomMovie.setPosterURL("");
            else
                randomMovie.setPosterURL("https://image.tmdb.org/t/p/original/" + obj.getString("poster_path"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return randomMovie;
    }

    private JSONObject randomizeMovieJSON(){
        while ( usedIDs.contains(randomID) )
            randomID = rnd.nextInt(lastMovieID - 1) + 1;
        JSONObject obj = new JSONObject();
        StringBuilder JSONString = new StringBuilder();
        try{
            URL randomMovieURL = new URL(baseUrl + "3/movie/"+ randomID + "?api_key=" + api + "&language=ru_RU");
            HttpURLConnection huc =  ( HttpURLConnection )  randomMovieURL.openConnection ();
            huc.setRequestMethod ("GET");
            huc.connect () ;
            if(huc.getResponseCode() == 404) {
                logger.info(""+huc.getResponseCode());
                return new JSONObject();
            }
            huc.disconnect();
            logger.info("norm url "+randomMovieURL);
            Scanner sc = new Scanner((InputStream) randomMovieURL.getContent());
            while (sc.hasNext()){
                JSONString.append(sc.nextLine());
            }
            obj = new JSONObject(JSONString.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            usedIDs.add(randomID);
        }
        return obj;
    }


    private int getLastMovieID(){
        int lastID = 0;
        try {
            URL lastMovie = new URL(baseUrl + "3/movie/latest?api_key=" + api + "&language=ru-RU");

            Scanner sc = new Scanner((InputStream) lastMovie.getContent());
            StringBuilder JSONString = new StringBuilder();
            while (sc.hasNext()) {
                JSONString.append(sc.nextLine());
            }

            JSONObject obj = new JSONObject(JSONString.toString());

            lastID = obj.getInt("id");
            logger.info("lastMovieID " + lastID);
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return lastID;
    }
}
