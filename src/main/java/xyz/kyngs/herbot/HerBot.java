package xyz.kyngs.herbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.Configuration;
import xyz.kyngs.herbot.database.DatabaseManager;
import xyz.kyngs.herbot.handlers.AntiDuplicationHandler;
import xyz.kyngs.herbot.handlers.InfoMessageHandler;
import xyz.kyngs.herbot.handlers.ThrowableHandler;
import xyz.kyngs.herbot.handlers.command.CommandHandler;
import xyz.kyngs.herbot.handlers.command.commands.admin.AllPermissionsCommand;
import xyz.kyngs.herbot.handlers.command.commands.admin.PermissionCommand;
import xyz.kyngs.herbot.handlers.command.commands.admin.UserPermissions;
import xyz.kyngs.herbot.handlers.command.commands.animal.CatCommand;
import xyz.kyngs.herbot.handlers.command.commands.animal.DogCommand;
import xyz.kyngs.herbot.handlers.command.commands.animal.DuckCommand;
import xyz.kyngs.herbot.handlers.command.commands.economy.BalanceCommand;
import xyz.kyngs.herbot.handlers.command.commands.economy.DailyCommand;
import xyz.kyngs.herbot.handlers.command.commands.economy.GambleCommand;
import xyz.kyngs.herbot.handlers.command.commands.economy.PayCommand;
import xyz.kyngs.herbot.handlers.command.commands.info.HelpCommand;
import xyz.kyngs.herbot.handlers.command.commands.info.InfoCommand;
import xyz.kyngs.herbot.handlers.command.commands.security.MyPermissionsCommand;
import xyz.kyngs.herbot.handlers.security.SecurityHandler;
import xyz.kyngs.herbot.handlers.user.UserHandler;
import xyz.kyngs.herbot.util.AnimalUtil;
import xyz.kyngs.logger.LogBuilder;
import xyz.kyngs.logger.Logger;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.List;
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
    public static final String VERSION = "1.4";
    public static final int BUILD = 6;

    private final JDA jda;
    @org.jetbrains.annotations.NotNull
    private final Configuration configuration;
    private final DatabaseManager databaseManager;
    private final InfoMessageHandler infoMessageHandler;
    private final AntiDuplicationHandler antiDuplicationHandler;
    private final CommandHandler commandHandler;
    private final AnimalUtil animalUtil;
    private final ThrowableHandler throwableHandler;
    private final UserHandler userHandler;
    private final SecurityHandler securityHandler;

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
            jda = JDABuilder.create(configuration.getString("token"), List.of(GatewayIntent.values()))
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
        animalUtil = new AnimalUtil(this);
        throwableHandler = new ThrowableHandler(this);
        userHandler = new UserHandler(this);
        securityHandler = new SecurityHandler(this);

        commandHandler.registerCommand(new CatCommand(this, "Po??le obr??zek ko??i??ky"), "cat", "??????a", "ko??i??ka", "??i??i", "ko??ka");
        commandHandler.registerCommand(new DuckCommand(this, "Po??le obr??zek kachni??ky"), "duck", "ducc", "kachni??ka");
        commandHandler.registerCommand(new HelpCommand(this, "Uk????e seznam v??ech p????kaz??"), "help", "pomoc", "sos");
        commandHandler.registerCommand(new DogCommand(this, "Po??le obr??zek pejska"), "dog", "haf", "pejsek", "pes");
        commandHandler.registerCommand(new InfoCommand(this, "Zobraz?? informace o botovi a kontakty"), "info", "kontakty");
        commandHandler.registerCommand(new DailyCommand(this, "D?? ti dne??n?? odm??nu"), "daily");
        commandHandler.registerCommand(new PermissionCommand(this, "P??id?? nebo odebere opr??vn??n??"), "perm");
        commandHandler.registerCommand(new BalanceCommand(this, "Zobraz?? z??statek na tv??m ????tu"), "bal", "balance");
        commandHandler.registerCommand(new PayCommand(this, "Po??le pen??ze jin??m u??ivatel??m"), "pay", "zaplatit");
        commandHandler.registerCommand(new GambleCommand(this, "Pom????e ti z??skat v??ce pen??z"), "gamble", "sazka");
        commandHandler.registerCommand(new AllPermissionsCommand(this, "Zobraz?? v??echna opr??vn??n??"), "allperms");
        commandHandler.registerCommand(new MyPermissionsCommand(this, "Zobraz?? tv?? opr??vn??n??"), "myperms", "perms");
        commandHandler.registerCommand(new UserPermissions(this, "Zobraz?? opr??vn??n?? vybran??ho u??ivatele"), "userperms");

        jda.addEventListener(new EventListener(this));

        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            thread.setUncaughtExceptionHandler((t, e) -> throwableHandler.reportThrowable(e));
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Shutdown Thread"));

    }

    public AnimalUtil getAnimalUtil() {
        return animalUtil;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public ThrowableHandler getThrowableHandler() {
        return throwableHandler;
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

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public SecurityHandler getSecurityHandler() {
        return securityHandler;
    }
}
