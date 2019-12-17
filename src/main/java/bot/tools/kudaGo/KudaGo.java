package bot.tools.kudaGo;

import bot.tools.locator.Place;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

public class KudaGo {

    Random rnd = new Random();
    private boolean citySelected;
    public boolean isCitySelected() {
        return citySelected;
    }

    private String city;
    private final String baseUrl = "https://kudago.com/public-api/v1.4/";

    public String getAnswer(String message){
        if(!citySelected) {
            city = convertMessageToCity(message);
            if(citySelected) return "Вы выбрали город " + message;
            else return "Выберите город из предложенных";
        }
        String query;
        if(message.equals("Места")) {
            query = baseUrl + "places/?text_format=text&location=" + city;
            //int randomPage = getRandomPage(query);
            //query+="&page="+randomPage;
            //JSONArray results = obj.getJSONArray("results");
            //JSONObject randomJSON = results.getJSONObject(rnd.nextInt(results.length()))
            return getRandomPlace(query);
        }
        if(message.equals("События")) {
            return "События";
        }
        return "";
    }

    private String getRandomPlace(String query){
        Plcae plcae = new Plcae();
        try {
            URL url = new URL(query);
            JSONObject obj = getJSONObject(url);
            int randomPage = rnd.nextInt(obj.getInt("count") / 20) + 1;
            query+="&page="+randomPage;
            url = new URL(query);
            obj = getJSONObject(url);
            JSONArray results = obj.getJSONArray("results");
            JSONObject randomJSON = results.getJSONObject(rnd.nextInt(results.length()));
            url = new URL(baseUrl + "places/" + randomJSON.getInt("id") + "/?text_format=text");
            System.out.println(url);
            obj = getJSONObject(url);
            plcae.title = obj.getString("title");
            plcae.description = obj.getString("description");
            plcae.body_text = obj.getString("body_text");
            plcae.address = obj.getString("address");
            plcae.imageUrl = obj.getJSONArray("images").getJSONObject(0).getString("image");
        } catch (Exception e){
            e.printStackTrace();
        }
        return plcae.toString();
    }

    private String convertMessageToCity(String message){
        citySelected = true;
        switch (message){
            case "Екатеринбург": return "ekb";
            case "Красноярск": return "krasnoyarsk";
            case "Краснодар": return "krd";
            case "Казань": return "kzn";
            case "Москва": return "msk";
            case "Нижний Новгород": return "nnv";
            case "Новосибирск": return "nsk";
            case "Сочи": return "sochi";
            case "Санкт-Петербург": return "spb";
            default:
                citySelected = false;
                return "";
        }
    }

    private JSONObject getJSONObject(URL url){
        JSONObject obj = new JSONObject();
        try {
            Scanner sc = new Scanner((InputStream) url.getContent());
            StringBuilder JSONString = new StringBuilder();
            while (sc.hasNext()) {
                JSONString.append(sc.nextLine());
            }
            obj = new JSONObject(JSONString.toString());
        } catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        return obj;
    }
}
