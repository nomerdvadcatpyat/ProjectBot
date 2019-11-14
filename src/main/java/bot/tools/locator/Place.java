package bot.tools.locator;

import java.util.ArrayList;

public class Place {
    private final String name;
    private final Location location;
    private final String additionalInfo;
    private final String type;

    public Place(String name, Location location, String type, String additionalInfo) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.additionalInfo = additionalInfo;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getType() {
        return type;
    }
}
