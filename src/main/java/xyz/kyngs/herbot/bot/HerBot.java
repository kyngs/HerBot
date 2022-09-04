package xyz.kyngs.herbot.bot;

import ch.qos.logback.classic.Level;
import cz.oneblock.core.BootLoader;
import cz.oneblock.core.SystemDaemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.kyngs.herbot.bot.command.CommandDaemon;
import xyz.kyngs.herbot.bot.dupe.AntiDuplicationDaemon;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

public class HerBot implements BootLoader {

    static {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.mongodb.driver")).setLevel(Level.WARN);
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(HerBot.class);
    public static final String VERSION = "@version@";

    public static void main(String[] args) {
        var system = new SystemDaemon(new HerBot(), d -> true);

        system.registerDaemon(CommandDaemon.class);
        system.registerDaemon(AntiDuplicationDaemon.class);

        system.load();
        system.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            system.stop();
            system.unLoad();
        }, "Shutdown Hook"));

        var scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            if (line.equalsIgnoreCase("stop")) {
                System.exit(0);
                break;
            }
        }

    }

    @Override
    public Logger getSystemLogger() {
        return LOGGER;
    }

    @Override
    public File getDataFolder() {
        return new File(".");
    }

    @Override
    public InputStream getResourceAsStream(String resource) {
        return getClass().getClassLoader().getResourceAsStream(resource);
    }
}
