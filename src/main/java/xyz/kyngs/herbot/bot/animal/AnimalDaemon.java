package xyz.kyngs.herbot.bot.animal;

import cz.oneblock.core.SystemDaemon;
import xyz.kyngs.herbot.bot.HerBotDaemon;

public class AnimalDaemon extends HerBotDaemon {
    public AnimalDaemon(SystemDaemon systemDaemon) {
        super(systemDaemon);
    }

    @Override
    protected void start() {
        registerCommand(new CatCommand(this));
        registerCommand(new DogCommand(this));
        registerCommand(new DuccCommand(this));
    }
}
