package bot.tools.locator;

@Deprecated
public class Location {        //Возможно, этот класс будет не нужен
    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
