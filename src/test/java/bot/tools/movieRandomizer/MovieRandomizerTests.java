package bot.tools.movieRandomizer;

import org.junit.Assert;
import org.junit.Test;

public class MovieRandomizerTests {
    @Test
    public void RightGenresFilter(){
        MovieRandomizer mr = new MovieRandomizer();
        mr.getAnswer("Комедия, Драма, Мелодрама");
        Assert.assertEquals("комедия, драма, мелодрама",mr.getGenresTitles());
    }

    @Test
    public void WrongGenresFilter(){
        MovieRandomizer mr = new MovieRandomizer();
        mr.getAnswer("Комеasdasdasd,asdadasd, asd a s, Драма, sadw22323");
        Assert.assertEquals("драма",mr.getGenresTitles());
    }

    @Test
    public void NullifyGenres(){
        MovieRandomizer mr = new MovieRandomizer();
        mr.getAnswer("Комедия, Драма, Мелодрама");
        mr.getAnswer("Обнулить жанры");
        Assert.assertEquals("",mr.getGenresTitles());
    }

    @Test
    public void NoSuchMovies(){
        MovieRandomizer mr = new MovieRandomizer();
        mr.getAnswer("Комедия, Драма, Мелодрама, боевик, мультик, триллер, ужасы, телефильм, семейный, вестерн, история");
        Assert.assertEquals("Таких фильмов нет",mr.getAnswer("Рандомный фильм"));
    }
}
