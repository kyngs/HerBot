package xyz.kyngs.herbot.bot.dupe;

import com.mongodb.client.model.Filters;
import cz.oneblock.core.SystemDaemon;
import cz.oneblock.core.configuration.ConfigurateSection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.Document;
import xyz.kyngs.herbot.bot.HerBotDaemon;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class AntiDuplicationDaemon extends HerBotDaemon {

    //I have no idea how it works, but it works
    public static final Pattern URL_PATTERN = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?)://|www\\.)(([\\w\\-]+\\.)+?([\\w\\-.~]+/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );

    private List<Long> channelsToWatch;

    public AntiDuplicationDaemon(SystemDaemon systemDaemon) {
        super(systemDaemon);

        loadConfiguration(getConfiguration());
    }

    public static String tryNormalizeUrl(String url) {
        URI uri;

        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException ex) {
            return url;
        }

        URI normalizedUri;
        try {
            var scheme = uri.getScheme();
            if (scheme != null && scheme.equals("http")) {
                scheme = "https";
            }

            normalizedUri = new URI(scheme, uri.getHost(), uri.getPath(), null);
        } catch (URISyntaxException e) {
            return url;
        }

        return normalizedUri.toString();
    }

    @Override
    protected void loadConfiguration(ConfigurateSection configuration) {
        channelsToWatch = configuration.getList(Long.class, "to-watch");
    }

    @Override
    public boolean useConfiguration() {
        return true;
    }

    @Override
    protected void handleNewMessage(MessageReceivedEvent event) {
        var message = event.getMessage();
        var channel = event.getChannel();
        var text = message.getContentRaw();

        if (!channelsToWatch.contains(channel.getIdLong())) return;

        var urls = new HashSet<String>();

        var matcher = URL_PATTERN.matcher(text);

        while (matcher.find()) {
            var matchStart = matcher.start(1);
            var matchEnd = matcher.end();
            urls.add(text.substring(matchStart, matchEnd));
        }

        // I am not really aware of what the following code does, but it appears that it just works. I've just ported it from the original bot

        var spamChannel = jdaDaemon.getBotChannel();
        var collection = databaseDaemon.getCollection("anti-duplication");
        var deleted = false;

        for (String url : urls) {
            var normalized = tryNormalizeUrl(url);
            //A very dirty fix, but I'm too lazy to implement a proper solution

            var exceptions = new String[]{"https://youtube.com/", "www.youtube.com", "https://www.youtube.com/"};
            var cont = false;
            for (var exception : exceptions) {
                if (normalized.startsWith(exception)) {
                    cont = true;
                    break;
                }
            }

            if (cont) continue;

            var existing = collection.find(Filters.and(Filters.eq("url", normalized), Filters.eq("channel", channel.getIdLong()))).first();

            if (existing != null) {
                deleted = true;

                message.delete().queue();

                var builder = new EmbedBuilder();
                builder.setTitle("Tento odkaz již někdo vložil.");
                builder.setDescription(url);
                builder.setColor(Color.GREEN);

                spamChannel.sendMessageEmbeds(builder.build()).append(String.format("<@%s>", event.getAuthor().getId())).queue();

                break;
            }
        }

        if (!deleted) {
            var res = urls.stream().map(url -> {
                var normalized = tryNormalizeUrl(url);
                var document = new Document();
                document.put("url", normalized);
                document.put("channel", channel.getIdLong());
                return document;
            }).toList();

            if (res.size() > 0) {
                collection.insertMany(res);
            }
        }
    }
}
