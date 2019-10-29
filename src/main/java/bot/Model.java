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
            case MainMenu:
                if(message.equals("Tools"))
                    menuState = MenuState.ToolsMenu;
                if(message.equals("Games"))
                    menuState = MenuState.GamesMenu;
                break;

            case ToolsMenu:
                if(message.equals("PhotoGetter"))
                    menuState = MenuState.PhotoGetter;
                break;

            case GamesMenu:
                if(message.equals("Cities"))
                    menuState = MenuState.CitiesGame;
                break;
        }
    }


    public String getStateAnswer(String message){
        switch (menuState){
            case CitiesGame:
                if (message.equals("Cities")) {
                    citiesGame = new CitiesGame();
                    break;
                }
                return citiesGame.getAnswer(message);

            case PhotoGetter:
                if (message.equals("PhotoGetter"))
                    break;
                try {
                    return PhotoGetter.getPhotoURL(message);
                } catch (IOException e){
                    logger.info(e.getMessage());
                }
                break;

            case MainMenu:
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
            case MainMenu:
                return "Ты в MainMenu. Доступные опции: \n1)Tools \n2)Games";
            case ToolsMenu:
                return "Ты в ToolsMenu. Доступные опции: \n1)PhotoGetter";
            case GamesMenu:
                return "Ты в GamesMenu. Доступные игры: \n1)Cities";
            case CitiesGame:
                return "Ты в CitiesGame. Назови любой город: ";
            case PhotoGetter:
                return "Ты в PhotoGetter.";
        }
        return "";
    }

    private void toMainMenu(String message){
        if(message.equals("Main") || message.equals("/start"))
            menuState = MenuState.MainMenu;
    }
}
