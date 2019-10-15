package bot;

public interface IBot {

    void initialize();
    String getInput();
    void printMessage(String answer);
    void getHelp();
}


