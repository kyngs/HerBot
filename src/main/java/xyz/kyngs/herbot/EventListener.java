package xyz.kyngs.herbot;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static xyz.kyngs.herbot.HerBot.EXECUTOR;

public class EventListener extends ListenerAdapter {

    private final HerBot herBot;

    public EventListener(HerBot herBot) {
        this.herBot = herBot;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);
        var userId = event.getAuthor().getId();
        if (event.getMessage().getContentRaw().contentEquals(".shutdown") && (userId.contentEquals("358962830902296576") || userId.contentEquals("264852356821286912"))) {
            System.exit(0);
            return;
        }
        EXECUTOR.execute(() -> {
            if (event.getAuthor().equals(herBot.getJda().getSelfUser())) return;
            herBot.getInfoMessageHandler().handleNewMessage(event);
            herBot.getAntiDuplicationHandler().handleNewMessage(event);
            herBot.getCommandHandler().processNewMessage(event);
        });
    }


}
