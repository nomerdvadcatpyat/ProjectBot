import bot.Model;
import bot.MenuState;
import org.junit.Assert;
import org.junit.Test;

public class ModelTests {
    @Test
    public void ChangeStateFromMainToTools(){
        Model model = new Model();
        model.updateState("Tools");
        Assert.assertEquals(MenuState.ToolsMenu,model.getMenuState());
    }
}
