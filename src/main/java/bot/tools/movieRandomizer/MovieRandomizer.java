package bot.tools.movieRandomizer;

import bot.BotProperties;
import bot.TelegramBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class MovieRandomizer {

    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());
    private static final String apiKey = BotProperties.getProperty("TMDB_api");
    private static final String baseUrl = "https://api.themoviedb.org/";
    private Random rnd = new Random();
    private StringBuilder genresIDsForQuery = new StringBuilder();
    private String genresTitles = "";

    public String getAnswer(String message){
        Movie movie;
        if(message.equals("Рандомный фильм")) {
            try {
                movie = getRandomMovie();
            } catch (Exception e){
                return e.getMessage();
            }
            return movie.toString();
        }
        else if(message.equals("Обнулить жанры")) {
            genresIDsForQuery = new StringBuilder();
            genresTitles = "";
            return "Жанры обнулены";
        }
        else if(message.equals("/help")) {
            return getHelp();
        }
        else
        for (String genre : message.replaceAll(" ", "").split(",")) {
            logger.info(genre);
            genre = genre.toLowerCase();
            if (GenresConverter.hasGenre(genre)) {
                logger.info("has genre");
                updateGenres(GenresConverter.getGenreId(genre));
            }
        }
        logger.info("Текущие жанры " + genresIDsForQuery);
        return "Жанры обновлены. Ваши текущие жанры: " + genresTitles;
    }

    private void updateGenres(String id){
        logger.info("message "+ id);
        if(genresTitles.isEmpty()) genresTitles = GenresConverter.getGenreTitle(id);
        else genresTitles += ", " + GenresConverter.getGenreTitle(id);
        if(!genresIDsForQuery.toString().contains(id))
            if(!genresIDsForQuery.toString().isEmpty())
                genresIDsForQuery.append("%2C").append(id);
            else genresIDsForQuery.append("&with_genres=").append(id);
    }

    private Movie getRandomMovie() throws MovieException {
        Movie randomMovie = new Movie();
        String sortValue = getRandomSortingValue();
        int randomPageNumber = getRandomPage(sortValue);
        try {
            logger.info("random page " + randomPageNumber);
            URL url = new URL(baseUrl + "3/discover/movie?api_key="+ apiKey +"&language=ru"+ sortValue +
                    "&include_adult=true&include_video=false&page=" + randomPageNumber +"&vote_count.gte=300"+ genresIDsForQuery);
            logger.info(url.toString());
            JSONObject obj = getJSONObject(url);
            JSONArray results = obj.getJSONArray("results");
            logger.info("results " + results);
            logger.info(((Integer)results.length()).toString());
            JSONObject randomJSON = results.getJSONObject(rnd.nextInt(results.length()));
            logger.info(randomJSON.toString());
            randomMovie.title = randomJSON.getString("title");
            if(randomJSON.getString("release_date").isEmpty())
                randomMovie.release_date = "дата не указанна.";
            else {
                SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                Date date = oldDateFormat.parse(randomJSON.getString("release_date"));
                randomMovie.release_date = newDateFormat.format(date);
            }
            if (randomJSON.getJSONArray("genre_ids").isEmpty())
                randomMovie.genre = "нет жанров.";
            else {
                JSONArray genreIds = randomJSON.getJSONArray("genre_ids");
                StringBuilder res= new StringBuilder();
                for(Object genreId : genreIds) {
                    logger.info("getRandomMovie genreID "+ genreId );
                    if(randomMovie.genre == null)
                        randomMovie.genre = GenresConverter.getGenreTitle(genreId.toString());
                    else
                        randomMovie.genre += ", " + GenresConverter.getGenreTitle(genreId.toString());
                }
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
            e.getMessage();
            e.printStackTrace();
        }
        return randomMovie;
    }

    private int getRandomPage(String sortValue) throws MovieException {
        int randomPage = 0;
        URL url = null;
        try {
            url = new URL(baseUrl + "3/discover/movie?api_key="+ apiKey +"&language=ru"+ sortValue +
                    "&include_adult=true&include_video=false&page=1&vote_count.gte=300"+ genresIDsForQuery);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        logger.info(url.toString());
        JSONObject obj = getJSONObject(url);
        int lastPage = obj.getInt("total_pages");
        if (lastPage == 0)
            throw new MovieException();
        logger.info("lastPage " + lastPage);
        randomPage = rnd.nextInt(lastPage) + 1;
        return randomPage;
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

    String getGenresTitles() {
        return genresTitles;
    }

    private String getHelp() {
        return "Нажмите на кнопку \"Рандомный фильм\" для того, чтобы бот отправил вам случайный фильм в соответствии с жанрами, которые Вы указали." +
                "\nВ любой момент Вы можете написать через запятую жанры, по которым нужно сортировать фильмы." +
                "\nДоступные жанры: боевик, приключения, мультфильм, комедия, криминал, документальный, драма, триллер, семейный, ужасы, фэнтези, история, вестерн, военный, телефильм, фантастика, мелодрама, детектив, музыка";
    }
}
