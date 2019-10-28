package bot;

import bot.Model;
import bot.MenuState;
import org.junit.Assert;
import org.junit.Test;

public class MenuStateTests {

    private Model model = new Model();

    @Test
    public void MainMenuTest(){
        model.updateMenuState("Main");
        Assert.assertEquals(MenuState.MainMenu,model.getMenuState());
    }

    @Test
    public void MainMenuTest2(){
        model.updateMenuState("/start");
        Assert.assertEquals(MenuState.MainMenu,model.getMenuState());
    }

    @Test
    public void FromMainToToolsAndGames(){
        model.updateMenuState("Main");
        model.updateMenuState("Tools");
        Assert.assertEquals(MenuState.ToolsMenu,model.getMenuState());
        model.updateMenuState("Main");
        Assert.assertEquals(MenuState.MainMenu,model.getMenuState());
        model.updateMenuState("Games");
        Assert.assertEquals(MenuState.GamesMenu,model.getMenuState());
    }

    @Test
    public void FromMainToCitiesGame(){
        model.updateMenuState("Main");
        model.updateMenuState("Cities");
        Assert.assertEquals(MenuState.MainMenu,model.getMenuState());
    }

    @Test
    public void FromGamesToCitiesGame(){
        model.updateMenuState("Main");
        model.updateMenuState("Games");
        model.updateMenuState("Cities");
        Assert.assertEquals(MenuState.CitiesGame,model.getMenuState());
    }

    @Test
    public void FromGamesToTools(){
        model.updateMenuState("Main");
        model.updateMenuState("Games");
        model.updateMenuState("Tools");
        Assert.assertEquals(MenuState.GamesMenu,model.getMenuState());
    }

}
