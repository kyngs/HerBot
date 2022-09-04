package xyz.kyngs.herbot.bot;

import cz.oneblock.core.ProjectDaemon;
import cz.oneblock.core.SystemDaemon;
import net.dv8tion.jda.api.JDA;
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
    }

    public CommandDaemon getCommandDaemon() {
        return commandDaemon;
    }

    public HerBot getBot() {
        return bootLoader;
    }

}
