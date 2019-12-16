package bot.tools.kudaGo;

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
            query = baseUrl + "places/?location=" + city;
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
        String res = "";
        try {
            URL url = new URL(query);
            JSONObject obj = getJSONObject(url);
            int randomPage = rnd.nextInt(obj.getInt("count") / 20) + 1;
            query+="&page="+randomPage;
            url = new URL(query);
            obj = getJSONObject(url);
            JSONArray results = obj.getJSONArray("results");
            JSONObject randomJSON = results.getJSONObject(rnd.nextInt(results.length()));
            // ну кароче нужно https://kudago.com/public-api/v1.4/places/randomJSON.getInt("id")/ и от туда все доставать.
            // распилить все это на методы кста нужно точно
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
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
