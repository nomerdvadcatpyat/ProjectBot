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
        switch (command) {
            case "!стоп": {
                isStop = true;
                break;
            }
            case "help": {
                while (command.equals("help")) {
                    printHelp();
                    command = getCommand();
                }
                break;
            }
        }
        return command;
    }

    public void printAnswer(String answer) {
        System.out.println(answer);
    }

    public void printHelp() {
        printAnswer("Консольный бот:\n!старт чтобы начать использовать бота\n" +
                "!стоп чтобы закончить использовать бота");
    }

    public static void main(String[] args) {
        ConsoleBot bot = new ConsoleBot();
        bot.initialize();
    }

}