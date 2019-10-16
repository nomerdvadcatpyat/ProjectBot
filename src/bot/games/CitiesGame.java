package bot.games;

import bot.*;
import tools.Parser;

import java.io.*;
import java.util.*;

public class CitiesGame implements IGame {
    String NAME = "Города";
    private IBot bot;

    public CitiesGame(IBot bot) {
        this.bot = bot;
    }

    public String getName() {
        return NAME;
    }

    public void run() {
        var needToContinue = true;
        while(needToContinue){
            needToContinue = gameCycle();
        }

    }

    private boolean gameCycle(){
        Random rnd = new Random();
        String lastWord;
        Character lastChar;
        HashMap<Character, ArrayList<String>> data = Parser.parse(new File(System.getProperty("user.dir") +
                File.separator + "resources" + File.separator + "RussianCities.txt"));

        bot.printMessage("Назовите любой город:");
        lastWord = bot.getInput();
        lastChar = lastWord.charAt(0);

        while (true) {
            if (lastWord.equals("/back")) return false;
            if (!data.containsKey(lastChar) ||
                    !data.get(lastChar).contains(lastWord)) {
                bot.printMessage("Вы проиграли.\n Сыграем еще?\n 1. Да\n 2. Нет");
                String input = bot.getInput();
                if (input.equals("1")) return true;
                if (input.equals("2")) return false;
            }

            lastChar = updateLastChar(lastWord);
            data.get(lastChar).remove(lastWord);

            if (!data.get(lastChar).isEmpty()) {
                var index = rnd.nextInt(data.get(lastChar).size() - 1);
                lastWord = data.get(lastChar).get(index);
                data.get(lastChar).remove(index);
                bot.printMessage(lastWord.toUpperCase().charAt(0) + lastWord.substring(1));
                lastChar = updateLastChar(lastWord);
            } else {
                bot.printMessage("Я проиграл");
                return false;
            }

            lastWord = bot.getInput().toLowerCase();
        }
    }

    public void getHelp() {
        bot.getHelp();
    }

    private Character updateLastChar(String lastWord){
        Character lastChar;
        var i = 1;
        lastChar = lastWord.charAt(lastWord.length() - i);
        while (lastChar == 'ь' || lastChar == 'ы' || lastChar == 'ъ' || lastChar == 'й' || lastChar == 'ё'){
            i++;
            lastChar = lastWord.charAt(lastWord.length() - i);
        }
        return lastChar;
    }
}
