package xyz.kyngs.herbot.bot.info;

import cz.oneblock.core.SystemDaemon;
import cz.oneblock.core.configuration.ConfigurateSection;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.kyngs.herbot.bot.HerBotDaemon;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InfoDaemon extends HerBotDaemon {

    private Map<Long, String> messages;
    private Map<Long, Set<String>> sentMessages;

    public InfoDaemon(SystemDaemon systemDaemon) {
        super(systemDaemon);
    }

    @Override
    protected void handleNewMessage(MessageReceivedEvent event) {
        var channelId = event.getChannel().getIdLong();
        var message = messages.get(channelId);
        if (message == null) return;

        event.getChannel().sendMessage(message).queue(sentMessage -> {
            deleteLastMessage(channelId);
            sentMessages.computeIfAbsent(channelId, k -> Collections.newSetFromMap(new ConcurrentHashMap<>())).add(sentMessage.getId());
        });
    }

    private void deleteLastMessage(long channelId) {
        var set = sentMessages.computeIfAbsent(channelId, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()));
        var channel = jda.getTextChannelById(channelId);
        for (var message : set) {
            try {
                channel.retrieveMessageById(message).queue(message1 -> message1.delete().queue());
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            set.remove(message);
        }
    }

    @Override
    protected void loadConfiguration(ConfigurateSection configuration) {
        if (sentMessages != null) deleteMessages();

        messages = new HashMap<>();
        sentMessages = new HashMap<>();

        var channels = configuration.getSection("channels");

        for (var channel : channels.getKeys()) {
            messages.put(Long.valueOf(channel), channels.getString(channel));
        }

        messages.forEach((id, message) -> {
            var channel = jda.getTextChannelById(id);
            if (channel == null) {
                logger.warn("Channel with id {} doesn't exist!", id);
                return;
            }
            var sent = channel.sendMessage(message).complete();

            sentMessages.computeIfAbsent(id, k -> Collections.newSetFromMap(new ConcurrentHashMap<>())).add(sent.getId());
        });
    }

    @Override
    protected void stop() {
        deleteMessages();
    }

    private void deleteMessages() {
        sentMessages.forEach((id, messages) -> {
            var channel = jda.getTextChannelById(id);
            if (channel == null) {
                logger.warn("Channel with id {} doesn't exist!", id);
                return;
            }
            for (var message : messages) {
                channel.deleteMessageById(message).complete();
            }
        });
    }

    @Override
    public boolean useConfiguration() {
        return true;
    }

    @Override
    protected void start() {
        loadConfiguration(getConfiguration());
    }
}
