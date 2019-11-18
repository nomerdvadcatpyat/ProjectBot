package bot.locator;

import bot.tools.locator.Location;
import bot.tools.locator.Locator;
import bot.tools.locator.LocatorState;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class LocatorStateTests {

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

    private void prepareLocator() {
        locator.usingTestSearchMap = true;
        locator.updateLocation(new Location(56.827085f,60.594069f));
    }
}
