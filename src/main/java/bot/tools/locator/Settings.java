package bot.tools.locator;

public class Settings {
    private int placesCount;
    private Locator locator;
    private int scale;

    private LocatorState previousState;

    public Settings(Locator locator) {
        placesCount = 5;
        scale = -1;
        this.locator = locator;
    }

    public Settings toDefault() {
        Settings newSettings = new Settings(locator);
        newSettings.setPreviousState(previousState);
        return newSettings;
    }

    public String getHelp(){
        return "Чтобы вернутся введите /back\n\nПараметры доступные для настройки: (1) количество мест для поиска, " +
                "(2) ручная установка геолокации, (3) масштаб карты, (4) " +
                "установка настроек к значениям по умолчанию. Далее идет описание настроек\n\n(1): для того чтобы установить" +
                "своё значение количества мест для поиска нужно ввести команду /placesCount " +
                "через пробел указать требуемое количество мест. Например: команда \"/placesCount 10\" установит значение " +
                "количества мест для поиска равным 10. Значение не должно быть отрицательным, равным 0 или большим чем 99(не рекомендуется устанавливать " +
                "значения больше 15 для лучшей производительности и удобства расположения отметок на карте)\n\n(3): для ручной установки геолокации " +
                "нужно ввести команду /newGeo и через пробел указать новые значения широты и долготы. Например команда \"/newGeo 27.987922 86.924927\" " +
                "установит новую геолокацию на вершину Эвереста\n\n(4): для установки произвольного масштаба карты нужно ввести команду /scale " +
                "и через пробел указать новый масштаб от 0 до 17. Не все уровни масштабирования доступны для всех областей карты. " +
                "Если запрошенный уровень не доступен для данной местности, то карта возвращаться не будет. По умолчанию установлено значение -1, что " +
                "задает автоматический масштаб карты\n\n(5): для сброса настроек нужно ввести команду /default";
    }

    public int getPlacesCount() {
        return placesCount;
    }

    public void setPlacesCount(int placesCount) {
        this.placesCount = placesCount;
    }

    public String updateLocationManually(Location newLocation) {
        return locator.updateLocation(newLocation);
    }

    public LocatorState getPreviousState() {
        return previousState;
    }

    public void setPreviousState(LocatorState previousState) {
        this.previousState = previousState;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
