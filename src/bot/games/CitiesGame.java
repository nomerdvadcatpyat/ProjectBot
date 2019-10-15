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
        Random rnd = new Random();
        String lastWord;
        Character lastChar;
        HashMap<Character, ArrayList<String>> data = Parser.parse(new File(System.getProperty("user.dir") +
                File.separator + "resources" + File.separator + "RussianCities.txt"));

        bot.printMessage("Назовите любой город:");
        lastWord = bot.getInput();
        lastChar = lastWord.toUpperCase().charAt(0);

        while (!bot.isStop()) {
            if (lastWord.equals("/back")) return;
            if (!data.containsKey(lastChar) ||
                    !data.get(lastChar).contains(lastWord)) {
                bot.printMessage("Вы проиграли.\n Сыграем еще?\n 1. Да\n 2. Нет");
                String command = bot.getInput();
                if (!bot.isStop() && command.equals("1")) {
                    run();
                    return;
                }
                if (command.equals("2"))
                    return;
            }

            lastChar = updateLastChar(lastWord, lastChar);
            data.get(lastChar).remove(lastWord);

            if (!data.get(lastChar).isEmpty()) {
                var index = rnd.nextInt(data.get(lastChar).size() - 1);
                lastWord = data.get(lastChar).get(index);
                data.get(lastChar).remove(index);
                bot.printMessage(lastWord);
                lastChar = updateLastChar(lastWord, lastChar);
            } else {
                bot.printMessage("Я проиграл");
                return;
            }

            lastWord = bot.getInput();
        }

    }

    public void getHelp() {
        bot.getHelp();
    }

    private Character updateLastChar(String lastWord, Character lastChar){
        lastChar = lastWord.toUpperCase().charAt(lastWord.length() - 1);
        if (lastChar == 'Ь' || lastChar == 'Ы' || lastChar == 'Ъ' || lastChar == 'Й' || lastChar == 'Ё')
            lastChar = lastWord.toUpperCase().charAt(lastWord.length() - 2);
        return lastChar;
    }
}
