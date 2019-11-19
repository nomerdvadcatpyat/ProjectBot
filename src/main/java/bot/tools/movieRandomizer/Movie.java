package bot.tools.movieRandomizer;

public class Movie {

    String title;
    String genre;
    String overview;
    String posterURL;
    String release_date;


    @Override
    public String toString() {
        if(!posterURL.isEmpty())
            return "{\"message\":\"" + title +
                    "\\n Дата выхода: " + release_date +
                    "\\n Жанр: " + genre +
                    "\\n Описание: " + overview + "\",\"url\":\"" + posterURL + "\"}";
        return  "\t" + title +
                "\n Дата выхода: " + release_date +
                "\n Жанр: " + genre +
                "\n Описание: " + overview;
    }

}