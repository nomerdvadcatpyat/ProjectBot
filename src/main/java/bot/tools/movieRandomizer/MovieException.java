package bot.tools.movieRandomizer;

public class MovieException extends Exception{
    @Override
    public String getMessage(){
        return "Таких фильмов нет";
    }
}
