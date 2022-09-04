package xyz.kyngs.herbot.bot.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.JDACommandManager;
import co.aikar.commands.JDAOptions;
import cz.oneblock.core.SystemDaemon;
import xyz.kyngs.herbot.bot.HerBotDaemon;

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
    }

    @Override
    public void stop() {

    }
}
