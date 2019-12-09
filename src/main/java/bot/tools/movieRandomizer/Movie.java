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
                    "\\nДата выхода: " + release_date +
                    "\\nЖанр: " + genre +
                    "\\nОписание: " + overview + "\",\"url\":\"" + posterURL + "\"}";
        return  title +
                "\nДата выхода: " + release_date +
                "\nЖанр: " + genre +
                "\nОписание: " + overview;
    }

}