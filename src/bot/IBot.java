package bot;

public interface IBot {

    void initialize();
    boolean isStop();
    String getInput();
    void printMessage(String answer);
    void printHelp();
}


