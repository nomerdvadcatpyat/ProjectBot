package bot.games.cities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

public class CitiesGame {

    private static final Logger logger = Logger.getLogger(CitiesGame.class.getName());
    private static HashMap<Character, ArrayList<String>> data;

    private String lastWord;

    public CitiesGame() {
        data = Parser.parse(new File(System.getProperty("user.dir") +
                File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "Cities.txt"));
    }

    public CitiesGame(HashMap<Character, ArrayList<String>> data) {
        this.data = data;
    }


    public String getAnswer(String message){
        char firstC = message.toLowerCase().charAt(0);
        if(data.containsKey(firstC) && data.get(firstC).contains(message))
            data.remove(firstC ,message);
        char lastC = message.charAt(message.length() - 1);
        Random rnd = new Random();
       //String res =
        return message;
    }

/*    public static void main(String[] args) {
       var list = new ArrayList<String>();
        list.add("Алапаевск");
        var data = new HashMap<Character, ArrayList<String>>();
        data.put('а',list);
        CitiesGame citiesGame = new CitiesGame(data);
        System.out.println(data.get('а').get(0));
    }*/
}
