package bot.tools.locator;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class LocatorTests {

    Locator locator = new Locator();

    @Test
    public void makeQueryWithoutLocation() throws IOException {
        locator.getAnswer("Аптека");
        Assert.assertEquals(LocatorState.LOCATION_WAITING, locator.getState());
    }

    @Test
    public void initialLocationUpdate() {
        locator.updateLocation(new Location(56.827085f,60.594069f));
        Assert.assertEquals(LocatorState.WAITING_FOR_QUERY, locator.getState());
    }

    @Test
    public void makeQuery() throws IOException {
        prepareLocator();
        locator.getAnswer("Аптека");
        Assert.assertEquals(LocatorState.WAITING_FOR_ADDITIONAL_QUERY_FIRST, locator.getState());
    }

    @Test
    public void makeAdditionalQuery() throws IOException {
        prepareLocator();
        locator.getAnswer("Аптека");
        locator.getAnswer("Подробнее");
        Assert.assertEquals(LocatorState.WAITING_FOR_PLACE_NUMBER, locator.getState());
        locator.getAnswer("1");
        Assert.assertEquals(LocatorState.WAITING_FOR_ADDITIONAL_QUERY_FIRST, locator.getState());
    }

    @Test
    public void makeMapQuery() throws IOException {
        prepareLocator();
        locator.getAnswer("Аптека");
        locator.getAnswer("Карта");
        Assert.assertEquals(LocatorState.WAITING_FOR_ADDITIONAL_QUERY_FIRST, locator.getState());
    }

    @Test
    public void makeNewQuery() throws IOException {
        prepareLocator();
        locator.getAnswer("Аптека");
        locator.getAnswer("Новый");
        Assert.assertEquals(LocatorState.WAITING_FOR_QUERY, locator.getState());
    }

    @Test
    public void makeWrongAdditionalQuery() throws IOException {
        prepareLocator();
        locator.getAnswer("Аптека");
        locator.getAnswer("Каво");
        Assert.assertEquals(LocatorState.WAITING_FOR_ADDITIONAL_QUERY_SECOND, locator.getState());
        locator.getAnswer("Не понял");
        Assert.assertEquals(LocatorState.WAITING_FOR_ADDITIONAL_FUNCTIONAL_NUMBER, locator.getState());
        locator.getAnswer("2");
        Assert.assertEquals(LocatorState.WAITING_FOR_ADDITIONAL_QUERY_FIRST, locator.getState());
        locator.getAnswer("Шо");
        locator.getAnswer("Я кроль?");
        locator.getAnswer("3");
        Assert.assertEquals(LocatorState.WAITING_FOR_QUERY, locator.getState());
    }

    @Test
    public void toSettingsFromLocationWaiting() throws IOException {
        locator.getAnswer("/settings");
        Assert.assertEquals(LocatorState.SETTINGS, locator.getState());
        locator.getAnswer("/back");
        Assert.assertEquals(LocatorState.LOCATION_WAITING, locator.getState());
    }

    @Test
    public void toSettingsFromWaitingForQuery() throws IOException {
        prepareLocator();
        locator.getAnswer("/settings");
        Assert.assertEquals(LocatorState.SETTINGS, locator.getState());
        locator.getAnswer("/back");
        Assert.assertEquals(LocatorState.WAITING_FOR_QUERY, locator.getState());
    }

    @Test
    public void toSettingsFromWaitingAdditionalQuery() throws IOException {
        prepareLocator();
        locator.getAnswer("Аптека");
        locator.getAnswer("/settings");
        Assert.assertEquals(LocatorState.SETTINGS, locator.getState());
        locator.getAnswer("/back");
        Assert.assertEquals(LocatorState.WAITING_FOR_ADDITIONAL_QUERY_FIRST, locator.getState());
        locator.getAnswer("ААААыыа");
        locator.getAnswer("/settings");
        Assert.assertEquals(LocatorState.SETTINGS, locator.getState());
        locator.getAnswer("/back");
        Assert.assertEquals(LocatorState.WAITING_FOR_ADDITIONAL_QUERY_SECOND, locator.getState());
    }

    @Test
    public void toSettingsFromWaitingForPlaceNumber() throws IOException {
        prepareLocator();
        locator.getAnswer("Аптека");
        locator.getAnswer("Подробнее");
        locator.getAnswer("/settings");
        Assert.assertEquals(LocatorState.SETTINGS, locator.getState());
        locator.getAnswer("/back");
        Assert.assertEquals(LocatorState.WAITING_FOR_PLACE_NUMBER, locator.getState());
    }

    @Test
    public void toSettingsFromWaitingForAdditionalFunctionalNumber() throws IOException {
        prepareLocator();
        locator.getAnswer("Аптека");
        locator.getAnswer("Где я?");
        locator.getAnswer("OOOOOOOOO! CJ!");
        locator.getAnswer("/settings");
        Assert.assertEquals(LocatorState.SETTINGS, locator.getState());
        locator.getAnswer("/back");
        Assert.assertEquals(LocatorState.WAITING_FOR_ADDITIONAL_FUNCTIONAL_NUMBER, locator.getState());
    }

    @Test
    public void manualInitialLocationSetup() throws IOException {
        Assert.assertEquals(LocatorState.LOCATION_WAITING, locator.getState());
        locator.getAnswer("/settings");
        locator.getAnswer("/newGeo 56.827085 60.594069");
        locator.getAnswer("/back");
        Assert.assertEquals(LocatorState.WAITING_FOR_QUERY, locator.getState());
    }

    @Test
    public void manualUpdateLocation() throws IOException {
        prepareLocator();
        locator.getAnswer("/settings");
        locator.getAnswer("/newGeo 43.779787 11.265817");
        locator.getAnswer("/back");
        Assert.assertEquals(new Location(43.779787f, 11.265817f), locator.getLocation());
    }

    @Test
    public void changeSpacesCount() throws IOException {
        prepareLocator();
        locator.getAnswer("/settings");
        locator.getAnswer("/placesCount 20");
        locator.getAnswer("/back");
        Assert.assertEquals(20, locator.getSettings().getPlacesCount());
    }

    @Test
    public void changeMapScale() throws IOException {
        prepareLocator();
        locator.getAnswer("/settings");
        locator.getAnswer("/scale 16");
        locator.getAnswer("/back");
        Assert.assertEquals(16, locator.getSettings().getScale());
    }

    @Test
    public void toDefaultSettings() throws IOException {
        prepareLocator();
        Settings defaultSettings = new Settings(locator);
        locator.getAnswer("/settings");
        locator.getAnswer("/placesCount 20");
        locator.getAnswer("/scale 17");
        locator.getAnswer("/back");
        Assert.assertNotEquals(defaultSettings, locator.getSettings());
        locator.getAnswer("/settings");
        locator.getAnswer("/default");
        locator.getAnswer("/back");
        Assert.assertEquals(defaultSettings, locator.getSettings());
    }

    @Test
    public void wrongSettings() throws IOException {
        prepareLocator();
        Location expectedLocation = locator.getLocation();
        locator.getAnswer("/settings");
        locator.getAnswer("/pasecNumber 7");
        locator.getAnswer("/scale");
        locator.getAnswer("/newGeo 50.000000");
        locator.getAnswer("/back");
        Assert.assertEquals(expectedLocation, locator.getLocation());
        Assert.assertEquals(new Settings(locator), locator.getSettings());
    }

    private void prepareLocator() {
        locator.searchMapReceivingMethod = new TestJSONResponseGetter();
        locator.updateLocation(new Location(56.827085f,60.594069f));
    }
}
