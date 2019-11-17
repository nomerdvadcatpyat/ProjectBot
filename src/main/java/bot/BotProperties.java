package bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BotProperties {
    public static String getProperty(String key){
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/bot.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }
}
