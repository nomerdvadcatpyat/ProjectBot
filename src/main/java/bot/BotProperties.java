package bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BotProperties {
    public static Properties getProperties(){
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(
                    "src/main/resources/bot.properties");
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
