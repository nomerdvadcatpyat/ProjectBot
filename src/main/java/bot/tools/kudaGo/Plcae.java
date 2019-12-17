package bot.tools.kudaGo;

public class Plcae {
    String title;
    String description;
    String body_text;
    String address;
    String imageUrl;

    @Override
    public String toString(){
        return "{\"message\":\"" + title + description + body_text + address + "\",\"url\":\"" + imageUrl + "\"}";

        /*if(!posterURL.isEmpty())
            return "{\"message\":\"" + title +
                    "\\nДата выхода: " + release_date +
                    "\\nЖанр: " + genre +
                    "\\nОписание: " + overview + "\",\"url\":\"" + posterURL + "\"}";*/

    }
}
