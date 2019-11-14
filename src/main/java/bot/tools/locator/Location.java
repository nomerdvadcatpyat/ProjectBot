package bot.tools.locator;


public class Location {
    private final float latitude;
    private final float longitude;

    public Location(float latitude, float longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
