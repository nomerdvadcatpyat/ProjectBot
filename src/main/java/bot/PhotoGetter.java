package bot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Scanner;

public class PhotoGetter {

    public static String getPhotoURL(String query) throws IOException {
        URL url = new URL("https://pixabay.com/api/?key=KEY&q=" +
                URLEncoder.encode(query, "UTF-8") +"&image_type=photo");
        Scanner sc = new Scanner((InputStream) url.getContent());
        String JSONString = "";
        while (sc.hasNext()){
            JSONString += sc.nextLine();
        }

        JSONObject obj = new JSONObject(JSONString);
        JSONArray hits = obj.getJSONArray("hits");
        Random rnd = new Random();
        JSONObject randomHit = hits.getJSONObject(rnd.nextInt(hits.length()));
        String result = randomHit.getString("largeImageURL");
        return result;
    }
}
