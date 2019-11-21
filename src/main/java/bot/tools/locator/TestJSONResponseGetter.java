package bot.tools.locator;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

class TestJSONResponseGetter implements JSONResponseGetter {

    @Override
    public JSONObject getSearchMapResponse(String text) throws IOException {
        String testJSON = new String(Files.readAllBytes(Paths.get("src/main/resources/testSearchMapContent.json")), StandardCharsets.UTF_8);
        return new JSONObject(testJSON);
    }
}
