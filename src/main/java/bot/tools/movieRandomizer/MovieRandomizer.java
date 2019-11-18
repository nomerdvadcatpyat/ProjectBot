package bot.tools.movieRandomizer;

import bot.BotProperties;
import bot.telegram.TelegramBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class MovieRandomizer {

    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());
    private static final String api = BotProperties.getProperty("TMDB_api");
    private static final String baseUrl = "https://api.themoviedb.org/";
    private Random rnd = new Random();

    private String genres = "";

    public Movie getRandomMovie(){
        Movie randomMovie = new Movie();
        String sortValue = getRandomSortingValue();
        int randomPageNumber = getRandomPage(sortValue);
        logger.info("random page" + randomPageNumber);
        try {
            URL url = new URL(baseUrl + "3/discover/movie?api_key="+ api +"&language=ru"+ sortValue +
                    "&include_adult=false&include_video=false&page=" + randomPageNumber +"&vote_count.gte=10"+genres);
            logger.info(url.toString());
            JSONObject obj = getJSONObject(url);
            JSONArray results = obj.getJSONArray("results");
            logger.info("results " + results);
            JSONObject randomJSON = results.getJSONObject(0);
            logger.info(randomJSON.toString());
            randomMovie.title = randomJSON.getString("title");
            if(randomJSON.getString("release_date").isEmpty())
                randomMovie.release_date = "дата не указанна.";
            else
                randomMovie.release_date = randomJSON.getString("release_date");
            if (randomJSON.getJSONArray("genre_ids").isEmpty())
                randomMovie.genre = "нет жанров.";
            else {
                JSONArray genreIds = randomJSON.getJSONArray("genre_ids");
                StringBuilder res= new StringBuilder();
                for(Object genreId : genreIds)
                    res.append(genreId) .append(", "); // нужно запилить конвертер или мапу и выводить не айдишники а значения.
                res.deleteCharAt(res.length()-2);
                randomMovie.genre = res.toString();
            }
            if(randomJSON.getString("overview").isEmpty())
                randomMovie.overview = "нет описания.";
            else
                randomMovie.overview = randomJSON.getString("overview");
            if(randomJSON.isNull("poster_path")) // можно засунуть какую-нибудь стандартную картинку
                randomMovie.posterURL = "";
            else
                randomMovie.posterURL = "https://image.tmdb.org/t/p/original/" + randomJSON.getString("poster_path");
        } catch (Exception e){
            e.printStackTrace();
        }
        return randomMovie;
    }

    private int getRandomPage(String sortValue){
        int randomPage = 0;
        try {
            URL url = new URL(baseUrl + "3/discover/movie?api_key="+ api +"&language=ru"+ sortValue+
                    "&include_adult=false&include_video=false&page=1&vote_count.gte=10"+genres);
            logger.info(url.toString());
            JSONObject obj = getJSONObject(url);
            int lastPage = obj.getInt("total_pages");
            logger.info("lastPage " + lastPage);
            randomPage = rnd.nextInt(lastPage) + 1;
        } catch (Exception e){
            e.printStackTrace();
        }
        return randomPage;
    }

    private String getRandomSortingValue(){
        String res = "";
        int method = rnd.nextInt(7);
        int sortDirection = rnd.nextInt(2);
        switch (method){
            case 0:
                res+="popularity";
                break;
            case 1:
                res+="release_date";
                break;
            case 2:
                res+="revenue";
                break;
            case 3:
                res+="primary_release_date";
                break;
            case 4:
                res+="original_title";
                break;
            case 5:
                res+="vote_average";
                break;
            case 6:
                res+="vote_count";
                break;
        }
        switch (sortDirection){
            case 0:
                res+=".asc";
                break;
            case 1:
                res+=".desc";
                break;
        }
        return "&sort_by=" + res;
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
            e.printStackTrace();
        }
        return obj;
    }
}
