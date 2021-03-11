package xyz.kyngs.herbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.Configuration;
import xyz.kyngs.herbot.database.DatabaseManager;
import xyz.kyngs.herbot.handlers.AntiDuplicationHandler;
import xyz.kyngs.herbot.handlers.InfoMessageHandler;
import xyz.kyngs.herbot.handlers.command.CommandHandler;
import xyz.kyngs.herbot.handlers.command.commands.*;
import xyz.kyngs.logger.LogBuilder;
import xyz.kyngs.logger.Logger;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class HerBot {

    public static final Logger LOGGER = LogBuilder
            .async()
            .build();
    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static final Pattern URL_PATTERN = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?)://|www\\.)(([\\w\\-]+\\.)+?([\\w\\-.~]+/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );

    private final JDA jda;
    @org.jetbrains.annotations.NotNull
    private final Configuration configuration;
    private final DatabaseManager databaseManager;
    private final InfoMessageHandler infoMessageHandler;
    private final AntiDuplicationHandler antiDuplicationHandler;
    private final CommandHandler commandHandler;

    public HerBot(Configuration configuration) {
        this.configuration = configuration;

        try {
            databaseManager = new DatabaseManager(configuration.getConfigurationSection("database"));
        } catch (SQLException e) {
            LOGGER.info("Failed to connect to database!");
            System.exit(1);
            throw new RuntimeException(e); //Here to prevent compile error: "Variable databaseManager might have not been initialized"
        }

        try {
            jda = JDABuilder.createDefault(configuration.getString("token"))
                    .setEnableShutdownHook(false)
                    .build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            LOGGER.error("Failed to connect to discord! Is token valid?");
            System.exit(1);
            throw new RuntimeException(e); //Here to prevent compile error: "Variable jda might have not been initialized"
        }

        infoMessageHandler = new InfoMessageHandler(this);
        antiDuplicationHandler = new AntiDuplicationHandler(this);
        commandHandler = new CommandHandler(this);

        commandHandler.registerCommand(new CatCommand(this, "Pošle obrázek kočičky"), "cat", "číča", "kočička", "čiči", "kočka");
        commandHandler.registerCommand(new DuckCommand(this, "Pošle obrázek kachinčky"), "duck", "ducc", "kachnička");
        commandHandler.registerCommand(new HelpCommand(this, "Ukáže seznam všech příkazů"), "help", "pomoc", "sos");
        commandHandler.registerCommand(new DogCommand(this, "Pošle obrázek pejska."), "dog", "haf", "pejsek", "pes");
        commandHandler.registerCommand(new InfoCommand(this, "Zobrazí informace o botovi a kontakty."), "info", "kontakty");

        jda.addEventListener(new EventListener(this));

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Shutdown Handler"));
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    private void shutdown() {

        LOGGER.info("Shutting down.");

        var manager = jda.getEventManager();

        for (Object listener : manager.getRegisteredListeners()) {
            manager.unregister(listener);
        }

        infoMessageHandler.shutdown();

        databaseManager.shutdown();

        jda.shutdown();

        LOGGER.info("Shutdown complete, goodbye!");

        LOGGER.destroy();

    }

    public JDA getJda() {
        return jda;
    }

    @NotNull
    public Configuration getConfiguration() {
        return configuration;
    }

    public InfoMessageHandler getInfoMessageHandler() {
        return infoMessageHandler;
    }

    public AntiDuplicationHandler getAntiDuplicationHandler() {
        return antiDuplicationHandler;
    }
}
