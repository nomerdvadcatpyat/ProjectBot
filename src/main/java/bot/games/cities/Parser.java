package bot.games.cities;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class Parser {
    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    public static HashMap<Character,ArrayList<String>> parse(File file){
        HashMap<Character,ArrayList<String>> data = new HashMap<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String s;
            while((s=br.readLine()) != null){
                s=s.toLowerCase();
                if( data.get(s.charAt(0)) == null )
                    data.put(s.charAt(0), new ArrayList<>());
                data.get(s.charAt(0)).add(s);
            }
        }
        catch(IOException ex){
            logger.info(ex.getMessage());
        }
        return data;
    }
}