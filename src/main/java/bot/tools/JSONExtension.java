package bot.tools;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class JSONExtension {

    public static JSONObject getJSONByUrl(URL url){
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

    public static boolean isJSON(String str){
        return str.contains("{"); // смешно тебе да
    }
}
