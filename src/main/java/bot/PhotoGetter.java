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
        URL url = new URL("https://pixabay.com/api/?key=14027120-bbc2ef0602a8cefada51f4654&q=" +
                URLEncoder.encode(query, "UTF-8") +"&image_type=all&per_page=200&safesearch=false");
        System.out.println(url.toString());
        Scanner sc = new Scanner((InputStream) url.getContent());
        String JSONString = "";
        while (sc.hasNext()){
            JSONString += sc.nextLine();
        }

        JSONObject obj = new JSONObject(JSONString);
        JSONArray hits = obj.getJSONArray("hits");
        System.out.println(obj.getInt("totalHits"));
        Random rnd = new Random();
        int index;
        if(hits.length() < 200)
            index = rnd.nextInt(hits.length() - 1);
        else  index = rnd.nextInt(199);
        JSONObject randomHit = hits.getJSONObject(index);
        String result = randomHit.getString("largeImageURL");
        System.out.println("out");
        return result;
    }
}
