package bot;

public interface IBot {

    public void initialize();
    public boolean isStop();
    public String getInput();
    public void printMessage(String answer);
    public void printHelp();
}


