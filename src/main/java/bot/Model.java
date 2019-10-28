package bot;

public class Model {

    private MenuState menuState = MenuState.MainMenu;

    public MenuState getMenuState(){
        return menuState;
    }

    public void updateState(String message){
        getBack(message);
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

    void getBack(String message){
        if(message.equals("Back"))
            menuState = MenuState.MainMenu;
    }
}
