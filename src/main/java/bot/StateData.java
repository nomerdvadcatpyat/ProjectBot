package bot;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import java.util.List;

public class StateData {

    private String name;
    private String infoText;
    private List<MenuState> children;
    private MenuState parent;
    public InlineKeyboardMarkup keyboard;

    public StateData(String name, String infoText, List<MenuState> children, MenuState parent) {
        this.name = name;
        this.infoText = infoText;
        this.children = children;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public String getInfoText() {
        return infoText;
    }

    public List<MenuState> getChildren() {
        return children;
    }

    public MenuState getParent() {
        return parent;
    }
}
