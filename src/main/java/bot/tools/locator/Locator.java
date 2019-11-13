package bot.tools.locator;

import java.net.MalformedURLException;
import java.net.URL;

public class Locator {

    public String getNearestPlace(String name, Location location) throws MalformedURLException {
        URL overpassQuery = new URL("https://overpass.openstreetmap.ru/api/interpreter?data=[out:json];");
        return "";
    }
}
