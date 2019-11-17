package bot.tools.movieRandomizer;

public class Movie {

    String title;
    String genre;
    String overview;
    String posterURL;
    String release_date;


    @Override
    public String toString() {
        if(!posterURL.isEmpty()) {
            System.out.println(
                    "{\"message\":\"" + title +
                    "\\n Дата выхода: " + release_date +
                    "\\n Жанр: " + genre +
                    "\\n Описание: " + overview + "\",\"url\":\"" + posterURL + "\"}");

            return "{\"message\":\"" + title +
                    "\\n Дата выхода: " + release_date +
                    "\\n Жанр: " + genre +
                    "\\n Описание: " + overview + "\",\"url\":\"" + posterURL + "\"}";
        }
        return  "\t" + title +
                "\n Дата выхода: " + release_date +
                "\n Жанр: " + genre +
                "\n Описание: " + overview;
    }

}
/*String json =
            "[{\"id\":\"{ID1}\",\"time\":123}, {\"id\":\"{ID2}\",\"time\":124}]";
        JSONArray array = new JSONArray(json);*/