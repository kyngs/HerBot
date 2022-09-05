package xyz.kyngs.herbot.bot;

import co.aikar.commands.BaseCommand;
import cz.oneblock.core.ProjectDaemon;
import cz.oneblock.core.SystemDaemon;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.kyngs.herbot.bot.command.CommandDaemon;
import xyz.kyngs.herbot.bot.database.DatabaseDaemon;
import xyz.kyngs.herbot.bot.jda.JDADaemon;

public class HerBotDaemon extends ProjectDaemon<HerBot> {

    protected final JDA jda;
    protected final JDADaemon jdaDaemon;
    protected final DatabaseDaemon databaseDaemon;
    protected CommandDaemon commandDaemon;

    public HerBotDaemon(SystemDaemon systemDaemon) {
        super(systemDaemon);

        jdaDaemon = obtainWeakDependency(JDADaemon.class);
        databaseDaemon = obtainDependency(DatabaseDaemon.class);

        whenLoaded(CommandDaemon.class, daemon -> commandDaemon = daemon);

        jda = jdaDaemon == null ? null : jdaDaemon.getJda();

        if (jda != null) {
            jda.addEventListener(new ListenerAdapter() {
                @Override
                public void onMessageReceived(@NotNull MessageReceivedEvent event) {
                    if (event.isWebhookMessage() || !event.isFromGuild() || event.getAuthor().isBot()) return;

                    handleNewMessage(event);
                }
            });
        }
    }

    public void registerCommand(BaseCommand command) {
        commandDaemon.registerCommand(command);
    }

    protected void handleNewMessage(MessageReceivedEvent event) {
    }

    public CommandDaemon getCommandDaemon() {
        return commandDaemon;
    }

    public HerBot getBot() {
        return bootLoader;
    }

}
