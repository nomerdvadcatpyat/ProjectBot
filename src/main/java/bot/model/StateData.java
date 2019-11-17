package bot.model;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

public class StateData {

    private String name;
    private String infoText;
    private List<MenuState> submenus;
    private MenuState parent;
    public ReplyKeyboard keyboard;

    public StateData(String name, String infoText, List<MenuState> submenus, MenuState parent) {
        this.name = name;
        this.infoText = infoText;
        this.submenus = submenus;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public String getInfoText() {
        return infoText;
    }

    public List<MenuState> getSubmenus() {
        return submenus;
    }

    public boolean hasParent() { return parent != null; }

    public MenuState getParent() {
        return parent;
    }
}
