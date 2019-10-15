package bot;

import bot.games.CitiesGame;

import java.util.*;

public class Games implements IActivity {

    public static final String NAME = "Игры";
    private IBot bot;

    private List<CitiesGame> gameList;

    public Games(IBot bot){
        this.bot = bot;
        gameList = Arrays.asList(new CitiesGame(bot));
    }

    public void start() {
        var startMessage = new StringBuilder();
        startMessage.append("Введите номер игры:\n");
        for (int i = 0; i < gameList.size(); i++) {
            startMessage.append(i+1);
            startMessage.append(". ");
            startMessage.append(gameList.get(i).NAME);
            startMessage.append("\n");
        }
        while (true){
            bot.printMessage("Для того чтобы вернуться введите /back");
            bot.printMessage(startMessage.toString());
            var input = bot.getInput();
            if (input.equals("/back")) break;
            if (input.equals("/help")) {
                getHelp();
                continue;
            }
            int gameNumber;
            try{
                gameNumber = Integer.parseInt(input);
            }
            catch (Exception e){
                bot.printMessage("Ошибка");
                continue;
            }
            if (gameNumber <= 0 || gameNumber > gameList.size()+1) {
                bot.printMessage("Игры с таким номером нет");
                continue;
            }
            gameList.get(gameNumber-1).run();
        }
    }

    public void getHelp(){
        bot.printHelp(); //возможно стоит добавить хелп по разделу
    }
}
