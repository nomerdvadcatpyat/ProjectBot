package bot.games.cities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

public class CitiesGame {

    private static final Logger logger = Logger.getLogger(CitiesGame.class.getName());
    private static HashMap<Character, ArrayList<String>> data;
    private Random rnd = new Random();

    public GameState getGameState() {
        return gameState;
    }

    private GameState gameState = GameState.IN_GAME;

    private String lastWord;
    private char lastC;

    public CitiesGame() {
        data = Parser.parse(new File("src/main/resources/Cities.txt"));
    }

    public CitiesGame(HashMap<Character, ArrayList<String>> data) {
        this.data = data;
    }


    public String getAnswer(String message){

        if(gameState == GameState.LOSE)
            return "Вы проиграли. Напишите Cities для новой игры или Main Menu для выхода в главное меню.";
        if(gameState == GameState.WIN)
            return "Вы выиграли. Напишите Cities для новой игры или Main Menu для выхода в главное меню.";

        logger.info("В начале - "+ lastWord);

        message = message.toLowerCase();
        char firstC = message.charAt(0);
        logger.info("message - " + message);

        // проверка введенного слова
        logger.info("firstC " + firstC + " lastC " + lastC);
            if (lastWord != null && firstC != lastC)
                return "Вы проиграли";
            if(!data.containsKey(firstC) || !data.get(firstC).contains(message)) {
                logger.info("Проигрыш - " + lastWord);
                gameState = GameState.LOSE;
                return "Вы проиграли";
        }
        data.get(firstC).remove(message);

        lastC = updateLastChar(message);
        logger.info("last char - " + lastC);

        //ответ бота
        String res="Я проиграл";
        if(!data.isEmpty() && data.containsKey(lastC) && !data.get(lastC).isEmpty()){
            int index = rnd.nextInt(data.get(lastC).size());
            res = data.get(lastC).get(index);
            data.get(lastC).remove(res);
            res = res.toUpperCase().charAt(0) + res.substring(1);
            lastWord = res;
            lastC = updateLastChar(lastWord);
            logger.info("В конце - " + lastWord);
            logger.info("last char - " + lastC);
        }
        if(res.equals("Я проиграл"))
            gameState = GameState.WIN;
        return res;
    }

    private char updateLastChar(String message){
        char lastC = message.charAt(message.length() - 1);
        int i = 1;
        while(lastC == 'й' || lastC =='ы' || lastC =='ь' || lastC =='ъ' || lastC =='ё') {
            lastC = message.charAt(message.length() - i);
            i++;
        }
        return lastC;
    }
}
