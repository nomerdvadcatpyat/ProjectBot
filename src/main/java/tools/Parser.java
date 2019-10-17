package tools;

import java.io.*;
import java.util.*;

public class Parser {
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
            System.out.println(ex.getMessage());
        }
        return data;
    }
}