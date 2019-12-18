package bot.tools.kudaGo;

public class Event {
    //fields=title,description,body_text,address,timetable,dates,price,images
    String title;
    String description;
    String place;
    String dates;
    String body_text;
    String price;
    String imageUrl;

    public Event() {
    }

    public Event(String title, String description, String body_text, String place,
                 String dates, String price, String imageUrl) {
        this.title = title;
        this.description = description;
        this.place = place;
        this.dates = dates;
        this.body_text = body_text;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString(){
        if(price.isEmpty()) price = "Не указано";
        if(place.isEmpty()) place = "Не указано";
        return "{\"message\":\"" +
                "\\n" + title +
                "\\nМесто: "+ place +
                "\\nДаты: " + dates +
                "\\nОписание: " + description +
                "\\n" + body_text + "\",\"url\":\"" + imageUrl + "\"}";
    }
}
