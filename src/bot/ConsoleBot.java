package bot;

import java.util.*;

public class ConsoleBot implements IBot {

    private boolean isStop = false;
    private Scanner sc = new Scanner(System.in);

    public boolean isStop() {
        return isStop;
    }

    public String getCommand() {
        String command = sc.nextLine();
        if (command.equals("!стоп")){
            isStop=true;
        }
        while (command.equals("help")){
            printHelp();
            command = sc.nextLine();
        }
        return command;
    }

    public void printAnswer(String answer) {
        System.out.println(answer);
    }

    public void printHelp() {
        System.out.println("Консольный бот:\n!старт чтобы начать использовать бота\n" +
                "!стоп чтобы закончить использовать бота");
    }

    public static void main(String[] args) {
        ConsoleBot bot = new ConsoleBot();
        bot.initialize();
    }

}