package bot.tools.movieRandomizer;

import bot.tools.locator.Locator;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GenresConverter {
    private static final Logger logger = Logger.getLogger(GenresConverter.class.getName());
    private static HashMap<String, String> fromIdToName = new HashMap<>();
    private static HashMap<String, String> fromNameToId = new HashMap<>();

    static {
        fromIdToName.put("28", "боевик");
        fromIdToName.put("12", "приключения");
        fromIdToName.put("16", "мультфильм");
        fromIdToName.put("35", "комедия");
        fromIdToName.put("80", "криминал");
        fromIdToName.put("99", "документальный");
        fromIdToName.put("18", "драма");
        fromIdToName.put("53", "триллер");
        fromIdToName.put("10751", "семейный");
        fromIdToName.put("27", "ужасы");
        fromIdToName.put("14", "фэнтези");
        fromIdToName.put("36", "история");
        fromIdToName.put("37", "вестерн");
        fromIdToName.put("10752", "военный");
        fromIdToName.put("10770", "телефильм");
        fromIdToName.put("878", "фантастика");
        fromIdToName.put("10749", "мелодрама");
        fromIdToName.put("9648", "детектив");
        fromIdToName.put("10402", "музыка");
        for (Map.Entry<String, String> entry : fromIdToName.entrySet()) {
            fromNameToId.put(entry.getValue(), entry.getKey());
        }
    }


    public static String getGenresTitle (String ids){
        String[] res = ids.replaceAll(" ", "").split(",");
        StringBuilder genres = new StringBuilder();
        for (String id : res) {
            if (fromIdToName.containsKey(id)) {
                genres.append(fromIdToName.get(id)).append(", ");
            }
        }
        if (!genres.toString().isEmpty())
            genres.deleteCharAt(genres.length() - 2);
        return genres.toString();
    }

    public static String getGenreId(String genre){
        logger.info(genre);
        if (fromNameToId.containsKey(genre)) {
            logger.info("id genre " + fromNameToId.get(genre));
            return fromNameToId.get(genre);
        }
        return "";
    }

    public static boolean hasGenre(String genre){
        return fromNameToId.containsKey(genre);
    }
}
