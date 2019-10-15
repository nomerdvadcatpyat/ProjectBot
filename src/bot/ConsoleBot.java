package bot;

import java.util.*;

public class ConsoleBot implements IBot {

    private boolean isStop = false;
    private Scanner sc = new Scanner(System.in);
    private List<Games> activities = Arrays.asList(new Games(this));


    public void initialize(){
        var helloMessage = "Привет,я (еще не(а может и совсем не)) универсальный бот!\n";
        var selectCategoryMes = new StringBuilder();
        selectCategoryMes.append("Выбери категорию:\n");
        for (int i = 0; i < activities.size(); i++){
            selectCategoryMes.append(i+1);
            selectCategoryMes.append(". " + activities.get(i).NAME + "\n");
        }
        printMessage(helloMessage);
        while (true){
            printMessage(selectCategoryMes.toString());
            var input = getInput();
            if (input.equals("/help")){
                printHelp();
                continue;
            }
            if (input.equals("/stop")) break;
            int categoryNumber;
            try{
                categoryNumber = Integer.parseInt(input);
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
        return sc.nextLine();
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