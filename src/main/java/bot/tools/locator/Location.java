package bot.tools.locator;

public class Location implements Cloneable {
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

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;
        Location guest = (Location) object;
        return latitude == guest.getLatitude() && longitude == guest.getLongitude();
    }

    @Override
    public Location clone() throws CloneNotSupportedException{
        return (Location) super.clone();
    }
}
