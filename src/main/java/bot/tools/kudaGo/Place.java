package bot.tools.kudaGo;

public class Place {
    String title;
    String timetable;
    String address;
    String description;
    String body_text;
    String imageUrl;

    public Place(String title, String description, String body_text, String address, String imageUrl, String timetable) {
        this.title = title;
        this.description = description;
        this.timetable = timetable;
        this.body_text = body_text;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    public Place() {
    }

    @Override
    public String toString(){
        if(timetable.isEmpty()) timetable = "Не указано";
        return "{\"message\":\"" +
        "\\n" + title +
                "\\nАдрес: "+ address +
                "\\nЧасы работы: " + timetable +
                "\\nОписание: " + description +
                "\\n" + body_text + "\",\"url\":\"" + imageUrl + "\"}";
    }
}
