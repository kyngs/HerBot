package xyz.kyngs.herbot.handlers.command;

import xyz.kyngs.herbot.HerBot;

public abstract class AbstractCommand implements CommandExecutor {

    protected final HerBot herBot;
    protected final String description;

    public AbstractCommand(HerBot herBot, String description) {
        this.herBot = herBot;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
