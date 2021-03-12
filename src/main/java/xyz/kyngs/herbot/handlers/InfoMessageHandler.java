package xyz.kyngs.herbot.handlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.kyngs.herbot.HerBot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InfoMessageHandler extends AbstractHandler {

    private final Map<String, String> messages;
    private final Map<String, Set<String>> sentMessages;

    public InfoMessageHandler(HerBot herBot) {
        super(herBot);
        messages = new HashMap<>();
        sentMessages = new HashMap<>();

        herBot.getDatabaseManager().getMySQL().sync().schedule(connection -> {
            var rs = connection.prepareStatement("SELECT channel_id, message FROM info_messages;").executeQuery();
            while (rs.next()) {
                messages.put(rs.getString("channel_id"), rs.getString("message"));
            }
        });

        for (var entry : messages.entrySet()) {
            var channel = herBot.getJda().getTextChannelById(entry.getKey());
            channel.sendMessage(entry.getValue()).queue(sentMessage -> {
                sentMessages.computeIfAbsent(entry.getKey(), k -> Collections.newSetFromMap(new ConcurrentHashMap<>())).add(sentMessage.getId());
            });
        }

    }

    public void handleNewMessage(GuildMessageReceivedEvent event) {
        var channelId = event.getChannel().getId();
        var message = messages.get(channelId);
        if (message == null) return;
        event.getChannel().sendMessage(message).queue(sentMessage -> {
            deleteLastMessage(channelId);
            sentMessages.computeIfAbsent(channelId, k -> Collections.newSetFromMap(new ConcurrentHashMap<>())).add(sentMessage.getId());
        });

    }

    private void deleteLastMessage(String channelId) {
        Set<String> set = sentMessages.computeIfAbsent(channelId, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()));
        var channel = herBot.getJda().getTextChannelById(channelId);
        for (String message : set) {
            try {
                channel.retrieveMessageById(message).queue(message1 -> message1.delete().queue());
            } catch (RuntimeException ignored) {
                ignored.printStackTrace();
            }
            set.remove(message);
        }

    }

    public void shutdown() {
        for (Map.Entry<String, Set<String>> entry : sentMessages.entrySet()) {
            var channel = herBot.getJda().getTextChannelById(entry.getKey());
            for (String id : entry.getValue()) {
                channel.deleteMessageById(id).complete();
            }
        }
    }
}
