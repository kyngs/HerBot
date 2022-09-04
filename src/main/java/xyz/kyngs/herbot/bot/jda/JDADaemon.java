package xyz.kyngs.herbot.bot.jda;

import cz.oneblock.core.SystemDaemon;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import xyz.kyngs.herbot.bot.HerBotDaemon;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class JDADaemon extends HerBotDaemon {
    private final JDA jda;

    public JDADaemon(SystemDaemon systemD) {
        super(systemD);

        logger.info("Logging in to the Discord API...");

        try {
            jda = JDABuilder.createDefault(getConfiguration().getString("token"), Arrays.asList(GatewayIntent.values()))
                    .setEnableShutdownHook(false)
                    .build();

            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            logger.error("Failed to login to the Discord API");
            throw new RuntimeException(e);
        }

        logger.info("Logged in to the Discord API");

    }

    public JDA getJda() {
        return jda;
    }

    @Override
    public void start() {
        if (getGuild() == null) {
            throw new IllegalArgumentException("Guild not found");
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void unLoad() {
        var manager = jda.getEventManager();

        for (Object listener : manager.getRegisteredListeners()) {
            manager.unregister(listener);
        }

        logger.info("Logging out from Discord API...");

        jda.shutdown();

        logger.info("Logged out from Discord API");
    }

    @Override
    public boolean useConfiguration() {
        return true;
    }

    public TextChannel getBotChannel() {
        return getGuild().getTextChannelById(getConfiguration().getLong("bot-channel"));
    }

    public Guild getGuild() {
        return jda.getGuildById(getGuildId());
    }

    public long getGuildId() {
        return getConfiguration().getLong("guild-id");
    }
}
