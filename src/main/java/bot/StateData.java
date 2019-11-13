package bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import java.util.List;

public class StateData {

    private String name;
    private String infoText;
    private List<MenuState> childs;
    private MenuState parent;
    public InlineKeyboardMarkup keyboard;

    public StateData(String name, String infoText, List<MenuState> childs, MenuState parent) {
        this.name = name;
        this.infoText = infoText;
        this.childs = childs;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public String getInfoText() {
        return infoText;
    }

    public List<MenuState> getChilds() {
        return childs;
    }

    public MenuState getParent() {
        return parent;
    }
}
