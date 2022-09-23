package xyz.kyngs.herbot.bot.animal;

import com.google.gson.JsonParser;
import xyz.kyngs.herbot.bot.command.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AnimalCommand extends Command<AnimalDaemon> {
    public AnimalCommand(AnimalDaemon parent) {
        super(parent);
    }

    protected String readJsonUrl(InputStream inputStream) throws IOException {
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

    protected String readJsonURL(String url, String errMessage) {
        try {
            var inputStream = new URL(url).openStream();
            return readJsonUrl(inputStream);
        } catch (Exception e) {
            //herBot.getThrowableHandler().reportThrowable(e);
            return errMessage;
        }
    }


}
