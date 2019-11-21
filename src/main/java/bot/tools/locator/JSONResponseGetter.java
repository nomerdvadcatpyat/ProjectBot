package bot.tools.locator;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

interface JSONResponseGetter {
    JSONObject getSearchMapResponse(String text) throws IOException;
}
