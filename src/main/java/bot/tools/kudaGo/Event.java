package bot.tools.kudaGo;

public class Event {
    //fields=title,description,body_text,address,timetable,dates,price,images
    String title;
    String description;
    String place;
    String body_text;
    String startDate;
    String endDate;
    String price;
    String imageUrl;

    public Event() {
    }

    public Event(String title, String description, String place, String body_text,
                 String startDate, String endDate, String price, String imageUrl) {
        this.title = title;
        this.description = description;
        this.place = place;
        this.body_text = body_text;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString(){
        if(price.isEmpty()) price = "Не указано";
        return "{\"message\":\"" +
                "\\n" + title +
                "\\nМесто: "+ place +
                "\\nДата начала: " + startDate +
                "\\nДата окончания: " + endDate +
                "\\nОписание: " + description +
                "\\n" + body_text + "\",\"url\":\"" + imageUrl + "\"}";
    }
}
