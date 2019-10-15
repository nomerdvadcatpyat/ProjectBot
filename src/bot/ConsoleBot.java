package bot;

import bot.games.CitiesGame;


import java.util.*;

public class ConsoleBot implements IBot {

    private boolean isStop = false;
    private Scanner sc = new Scanner(System.in);
    private List<Games> activities = Arrays.asList(new Games(this));


    public void initialize(){
        var helloMessage = new StringBuilder();
        helloMessage.append("Привет,я (еще не(а может и совсем не)) универсальный бот!\n" +
                "Выбери категорию:\n");
        for (int i = 0; i < activities.size(); i++){
            helloMessage.append(Integer.toString(i+1));
            helloMessage.append(". " + activities.get(i).NAME + "\n");
        }
        printMessage(helloMessage.toString());
        while (!isStop()){
            if (getInput().equals("/help")){
                printHelp();
                continue;
            }
            if (getInput().equals("/stop")) isStop = true;
            int categoryNumber;
            try{
                categoryNumber = Integer.parseInt(getInput());
            }
            catch (Exception e){
                printMessage("Error");
                continue;
            }
            if (categoryNumber <= 0 || categoryNumber > activities.size()+1){
                printMessage("Такой категории нет");
                continue;
            }
            activities.get(categoryNumber-1).start();
        }
    }

    public boolean isStop() {
        return isStop;
    }

    public String getInput() {

        String command = sc.nextLine();
        /*switch (command) {
            case "/stop": {
                isStop = true;
                break;
            }
            case "/help": {
                while (command.equals("/help")) {
                    printHelp();
                    command = getInput();
                }
                break;
            }
        }*/
        return command;
    }

    public void printMessage(String answer) {
        System.out.println(answer);
    }

    public void printHelp() {
        printMessage("Консольный бот:\n/start чтобы начать использовать бота\n/stop чтобы закончить использовать бота");
    }

    public static void main(String[] args) {
        ConsoleBot bot = new ConsoleBot();
        bot.initialize();
    }

}