package bot.tools.movieRandomizer;

public class Movie {
    private String title;
    private String genre;
    private String overview;
    private String posterURL;

    public Movie() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }
    
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        String message = title +
                " Жанр: " + genre +
                " Описание: " + overview;
        if(!posterURL.isEmpty()) {
            System.out.println("{\"message\":\"" + message + "\",\"url\":\"" + posterURL + "\"}");
            return "{\"message\":\"" + message + "\",\"url\":\"" + posterURL + "\"}";
        }
        return  message;
    }

}
