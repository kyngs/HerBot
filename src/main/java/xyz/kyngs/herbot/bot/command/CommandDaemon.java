package xyz.kyngs.herbot.bot.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.JDACommandManager;
import co.aikar.commands.JDAOptions;
import cz.oneblock.core.SystemDaemon;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.kyngs.herbot.bot.HerBotDaemon;
import xyz.kyngs.herbot.bot.database.DatabaseDaemon;

public class CommandDaemon extends HerBotDaemon {
    private final JDACommandManager commandManager;

    public CommandDaemon(SystemDaemon systemD) {
        super(systemD);

        commandManager = JDAOptions.builder()
                .setGuildId(jdaDaemon.getGuildId())
                .buildManager(jda);
    }

    public void registerCommand(BaseCommand command) {
        commandManager.registerCommand(command);
    }

    @Override
    public void postStart() {
        commandManager.propagate();
    }

    @Override
    public void start() {
        registerCommand(new InfoCommand(this));
    }

    @Override
    public CommandDaemon getCommandDaemon() {
        return this;
    }

    @Override
    public void stop() {

    }

    @Override
    protected void handleNewMessage(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(".") && event.getChannel().getIdLong() == jdaDaemon.getBotChannel().getIdLong()) {
            var builder = EmbedHelper.ERROR.prepare(event.getAuthor());

            builder.setDescription("Zdá se, že ses právě pokoušel spustit příkaz. HerBot už pouze přijímá tzv. slash příkazy. To znamená, že místo tečky `.`, použij lomítko `/`");

            event.getMessage().replyEmbeds(builder.build()).queue();
        }
    }

    protected DatabaseDaemon getDatabaseDaemon() {
        return databaseDaemon;
    }
}
