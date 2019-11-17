package bot;

import bot.model.MenuState;
import bot.model.Model;
import org.junit.Assert;
import org.junit.Test;

public class MenuStateTests {

    private Model model = new Model();

    @Test
    public void MainMenuTest(){
        Model.setupStatesInfo();
        model.updateMenuState("Main Menu");
        Assert.assertEquals(MenuState.MAIN_MENU,model.getMenuState());
    }

    @Test
    public void MainMenuTest2(){
        Model.setupStatesInfo();
        model.updateMenuState("/start");
        Assert.assertEquals(MenuState.MAIN_MENU,model.getMenuState());
    }

    @Test
    public void FromMainToToolsAndGames(){
        Model.setupStatesInfo();
        model.updateMenuState("Main Menu");
        model.updateMenuState("Tools Menu");
        Assert.assertEquals(MenuState.TOOLS_MENU,model.getMenuState());
        model.updateMenuState("Main Menu");
        Assert.assertEquals(MenuState.MAIN_MENU,model.getMenuState());
        model.updateMenuState("Games Menu");
        Assert.assertEquals(MenuState.GAMES_MENU,model.getMenuState());
    }

    @Test
    public void FromMainToCitiesGame(){
        Model.setupStatesInfo();
        model.updateMenuState("Main Menu");
        model.updateMenuState("Cities Game");
        Assert.assertEquals(MenuState.MAIN_MENU,model.getMenuState());
    }

    @Test
    public void FromGamesToCitiesGame(){
        Model.setupStatesInfo();
        model.updateMenuState("Main Menu");
        model.updateMenuState("Games Menu");
        model.updateMenuState("Cities Game");
        Assert.assertEquals(MenuState.CITIES_GAME,model.getMenuState());
    }

    @Test
    public void FromCitiesGameToMain(){
        Model.setupStatesInfo();
        model.updateMenuState("Main Menu");
        model.updateMenuState("Games Menu");
        model.updateMenuState("Cities Game");
        model.updateMenuState("Main Menu");
        Assert.assertEquals(MenuState.MAIN_MENU,model.getMenuState());
    }

    @Test
    public void FromGamesToTools(){
        Model.setupStatesInfo();
        model.updateMenuState("Main Menu");
        model.updateMenuState("Games Menu");
        model.updateMenuState("Tools Menu");
        Assert.assertEquals(MenuState.GAMES_MENU,model.getMenuState());
    }

}
