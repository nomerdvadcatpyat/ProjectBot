package games.cities;

import bot.games.cities.CitiesGame;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class CitiesGameTests {
    @Test
    public void EmptyData(){
        CitiesGame citiesGame = new CitiesGame(new HashMap<>());
        String answer = citiesGame.getAnswer("Москва");
        Assert.assertEquals("Я проиграл.",answer);
    }

    /*@Test
    public void OneAnswerData(){
        HashMap<Character, ArrayList<String>> data = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("Архангельск");
        data.put('А',list);
        CitiesGame citiesGame = new CitiesGame(data);
        citiesGame.getAnswer("Москва");
        String answer = citiesGame.getAnswer("Курган");
        Assert.assertEquals("Я проиграл.",answer);
    }чето не то */
}
