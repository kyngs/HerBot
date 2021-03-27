package xyz.kyngs.herbot.handlers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import static xyz.kyngs.herbot.util.UrlUtil.*;

public class AntiDuplicationHandler extends AbstractHandler {

    private final List<String> channelsToWatch;
    private final String spamChannel;

    public AntiDuplicationHandler(HerBot herBot) {
        super(herBot);
        var configuration = herBot.getConfiguration();
        channelsToWatch = configuration.getStringList("channels_to_check_for_dupes");
        spamChannel = configuration.getString("spam_channel");
    }

    public void handleNewMessage(GuildMessageReceivedEvent event) {

        var message = event.getMessage();
        var channel = event.getChannel();
        var text = event.getMessage().getContentRaw();

        if (!channelsToWatch.contains(channel.getId())) return;

        Set<String> urls = new HashSet<>();

        Matcher matcher = HerBot.URL_PATTERN.matcher(text);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            urls.add(text.substring(matchStart, matchEnd));
        }

        var mySQL = herBot.getDatabaseManager().getMySQL();

        mySQL.async().schedule(connection -> {

            var spamChannel = herBot.getJda().getTextChannelById(this.spamChannel);
            boolean deleted = false;

            for (String url : urls) {
                var normalizedUrl = tryNormalizeUrl(url);
                var ps = connection.prepareStatement("SELECT id FROM anti_duplication WHERE channel_id=? AND link=?");
                ps.setString(1, channel.getId());
                ps.setString(2, normalizedUrl);
                var rs = ps.executeQuery();
                if (rs.next()) {
                    if (!deleted) {
                        message.delete().queue();
                        deleted = true;
                    }
                    var builder = new EmbedBuilder();
                    builder.setTitle("Odkaz již někdo submitnul");
                    builder.setDescription(url);
                    builder.setColor(Color.GREEN);
                    spamChannel.sendMessage(builder.build()).append(String.format("<@%s>", event.getAuthor().getId())).queue();
                } else {
                    ps = connection.prepareStatement("INSERT INTO anti_duplication (channel_id, link) VALUES (?, ?)");
                    ps.setString(1, channel.getId());
                    ps.setString(2, normalizedUrl);
                    ps.execute();
                }
            }


        });

    }

}
