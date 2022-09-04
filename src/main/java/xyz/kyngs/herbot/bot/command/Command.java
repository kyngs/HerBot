package xyz.kyngs.herbot.bot.command;

import co.aikar.commands.BaseCommand;
import xyz.kyngs.herbot.bot.HerBot;
import xyz.kyngs.herbot.bot.HerBotDaemon;

public class Command<T extends HerBotDaemon> extends BaseCommand {

    protected final HerBot bot;
    protected final T parent;
    protected final CommandDaemon commandDaemon;

    public Command(T parent) {
        bot = parent.getBot();
        this.parent = parent;
        commandDaemon = parent.getCommandDaemon();
    }

}
