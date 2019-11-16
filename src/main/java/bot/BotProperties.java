package bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BotProperties {
    public static String getProperty(String key){
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(
                    "src/main/resources/bot.properties");
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String value = prop.getProperty(key);
        return value;
    }
}
