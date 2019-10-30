package bot;

import bot.games.cities.CitiesGame;
import bot.tools.PhotoGetter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Model {

    private MenuState menuState;
    private CitiesGame citiesGame;
    private static final Logger logger = Logger.getLogger(Model.class.getName());

    public MenuState getMenuState(){
        return menuState;
    }

    public void updateMenuState(String message){
        toMainMenu(message);
        switch (menuState){
            case MAIN_MENU:
                if(message.equals("Tools"))
                    menuState = MenuState.TOOLS_MENU;
                if(message.equals("Games"))
                    menuState = MenuState.GAMES_MENU;
                break;

            case TOOLS_MENU:
                if(message.equals("PHOTO_GETTER"))
                    menuState = MenuState.PHOTO_GETTER;
                break;

            case GAMES_MENU:
                if(message.equals("Cities"))
                    menuState = MenuState.CITIES_GAME;
                break;
        }
    }


    public String getStateAnswer(String message) throws IOException {
        switch (menuState){
            case CITIES_GAME:
                if (message.equals("Cities")) {
                    citiesGame = new CitiesGame();
                    break;
                }
                return citiesGame.getAnswer(message);

            case PHOTO_GETTER:
                if (message.equals("PHOTO_GETTER"))
                    break;
                    return PhotoGetter.getPhotoURL(message);

            case MAIN_MENU:
                if (message.equals("Shrek"))
                    return new File(System.getProperty("user.dir") +
                            File.separator + "src" + File.separator + "main" +
                            File.separator + "resources" + File.separator + "Shrek.gif").getAbsolutePath();
                break;
        }
        return "";
    }

    public String getStateHelloMessage(){
        switch (menuState){
            case MAIN_MENU:
                return "Ты в MAIN_MENU. Доступные опции: \n1)Tools \n2)Games";
            case TOOLS_MENU:
                return "Ты в TOOLS_MENU. Доступные опции: \n1)PHOTO_GETTER";
            case GAMES_MENU:
                return "Ты в GAMES_MENU. Доступные игры: \n1)Cities";
            case CITIES_GAME:
                return "Ты в CITIES_GAME. Назови любой город: ";
            case PHOTO_GETTER:
                return "Ты в PHOTO_GETTER.";
        }
        return "";
    }

    private void toMainMenu(String message){
        if(message.equals("Main") || message.equals("/start"))
            menuState = MenuState.MAIN_MENU;
    }
}
