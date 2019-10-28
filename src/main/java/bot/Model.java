package bot;

public class Model {

    private ModelState modelState = ModelState.MainMenu;

    public ModelState getModelState(){
        return modelState;
    }

    public void updateModel(String message){
        switch (modelState){
            case MainMenu:
                if(message.equals("Tools"))
                    modelState = ModelState.ToolsMenu;
                if(message.equals("Games"))
                    modelState = ModelState.GamesMenu;
                break;

            case ToolsMenu:
                if(message.equals("Back"))
                    modelState = ModelState.MainMenu;
                if(message.equals("PhotoGetter"))
                    modelState = ModelState.PhotoGetter;
                break;

            case PhotoGetter:
                if(message.equals("Back"))
                    modelState = ModelState.MainMenu;
                break;

            case GamesMenu:
                if(message.equals("Back"))
                    modelState = ModelState.MainMenu;
                if(message.equals("Cities"))
                    modelState = ModelState.CitiesGame;
                break;

            case CitiesGame:
                if(message.equals("Back"))
                    modelState = ModelState.MainMenu;
                break;
        }
    }

}
