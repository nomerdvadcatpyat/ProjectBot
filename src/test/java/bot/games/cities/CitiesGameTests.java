package bot.games.cities;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class CitiesGameTests {
    @Test
    public void EmptyData(){
        CitiesGame citiesGame = new CitiesGame(new HashMap<>());
        citiesGame.getAnswer("Москва");
        Assert.assertEquals(GameState.LOSE,citiesGame.getGameState());
    }

    @Test
    public void OneWordData(){
        HashMap<Character, ArrayList<String>> data = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("архангельск");
        data.put('а',list);
        CitiesGame citiesGame = new CitiesGame(data);
        citiesGame.getAnswer("Архангельск");
        Assert.assertEquals(GameState.WIN,citiesGame.getGameState());
    }

    @Test
    public void TwoWordData(){
        HashMap<Character, ArrayList<String>> data = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("москва");
        data.put('м',list);
        list.add("архангельск");
        data.put('а',list);
        CitiesGame citiesGame = new CitiesGame(data);
        Assert.assertEquals("Архангельск", citiesGame.getAnswer("Москва"));
    }

    @Test
    public void WrongCity(){
        CitiesGame citiesGame = new CitiesGame();
        citiesGame.getAnswer("ФЫВФЫАФЫВ");
        Assert.assertEquals(GameState.LOSE,citiesGame.getGameState());
    }

    @Test
    public void BadSymbolFromUser(){
        HashMap<Character, ArrayList<String>> data = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("казань");
        data.put('к',list);
        list.add("новосибирск");
        data.put('н',list);
        CitiesGame citiesGame = new CitiesGame(data);
        Assert.assertEquals("Новосибирск",citiesGame.getAnswer("казань"));
    }

    @Test
    public void BadSymbolFromBot(){
        HashMap<Character, ArrayList<String>> data = new HashMap<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        list1.add("казань");
        data.put('к', list1);
        list2.add("новосибирск");
        list2.add("невьянск");
        data.put('н', list2);
        CitiesGame citiesGame = new CitiesGame(data);
        citiesGame.getAnswer("новосибирск");
        citiesGame.getAnswer("невьянск");
        Assert.assertEquals(GameState.WIN,citiesGame.getGameState());
    }
}