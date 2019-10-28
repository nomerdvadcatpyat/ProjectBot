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
    private GameState gameState = GameState.InGame;

    public GameState getGameState() {
        return gameState;
    }

    private String lastWord;


    public CitiesGame() {
        data = Parser.parse(new File(System.getProperty("user.dir") +
                File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "Cities.txt"));
    }

    public CitiesGame(HashMap<Character, ArrayList<String>> data) {
        this.data = data;
    }


    public String getAnswer(String message){
        logger.info("В начале - "+ lastWord);

        message = message.toLowerCase();
        char firstC = message.charAt(0);

        if(lastWord != null)
            if(firstC != lastWord.charAt(lastWord.length()-1) || !data.get(firstC).contains(message)) {

                //логи
                logger.info(firstC + " " + lastWord.charAt(lastWord.length()-1) + " " + data.containsKey(firstC));
                if(data.containsKey(firstC))
                    logger.info(data.get(firstC).contains(message)+"");
                //логи

                gameState = GameState.Lose;
                return "Вы проиграли.";
            }

        data.get(firstC).remove(message);

        char lastC = message.charAt(message.length() - 1);
        String res="Я проиграл";

        if(!data.get(lastC).isEmpty()){
            int index = rnd.nextInt(data.get(lastC).size());
            res = data.get(lastC).get(index);
            data.get(lastC).remove(res);
            res = res.toUpperCase().charAt(0) + res.substring(1);
            lastWord = res;
        }

        if(res.equals("Я проиграл"))
            gameState = GameState.Win;

        logger.info("В конце - " + lastWord);

        return res;
    }
}
