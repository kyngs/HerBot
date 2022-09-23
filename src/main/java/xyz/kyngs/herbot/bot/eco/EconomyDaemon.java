package xyz.kyngs.herbot.bot.eco;

import cz.oneblock.core.SystemDaemon;
import xyz.kyngs.herbot.bot.HerBotDaemon;

public class EconomyDaemon extends HerBotDaemon {
    public EconomyDaemon(SystemDaemon systemDaemon) {
        super(systemDaemon);
    }

    @Override
    protected void start() {
        registerCommand(new BalanceCommand(this));
        registerCommand(new DailyCommand(this));
        registerCommand(new GambleCommand(this));
    }
}
