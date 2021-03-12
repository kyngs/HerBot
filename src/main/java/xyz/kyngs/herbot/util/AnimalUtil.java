package xyz.kyngs.herbot.util;

import com.google.gson.JsonParser;
import xyz.kyngs.herbot.HerBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AnimalUtil {

    private final HerBot herBot;

    public AnimalUtil(HerBot herBot) {
        this.herBot = herBot;
    }

    private static String readJsonUrl(InputStream inputStream) throws IOException {
        var bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));

        var sb = new StringBuilder();
        int cp;
        while ((cp = bufferedReader.read()) != -1) {
            sb.append((char) cp);
        }
        var root = JsonParser.parseString(sb.toString().replace("[", "").replace("]", "")).getAsJsonObject();
        bufferedReader.close();
        return root.getAsJsonPrimitive("url").getAsString();
    }

    public CompletableFuture<String> readJsonURL(String url, String errMessage) {
        var future = new CompletableFuture<String>();

        future.completeAsync(() -> {
            try {
                var inputStream = new URL(url).openStream();
                return readJsonUrl(inputStream);
            } catch (Exception e) {
                herBot.getThrowableHandler().reportThrowable(e);
                return errMessage;
            }
        });

        return future;
    }

}
