package bot;

public class Model {

    private MenuState menuState;

    public MenuState getMenuState(){
        return menuState;
    }

    public void updateState(String message){
        goToMain(message);
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

            case PhotoGetter:
                break;

            case GamesMenu:
                if(message.equals("Cities"))
                    menuState = MenuState.CitiesGame;
                break;

            case CitiesGame:
                break;
        }
    }

    public String getStateMessage(){
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

    void goToMain(String message){
        if(message.equals("Main"))
            menuState = MenuState.MainMenu;
    }
}
