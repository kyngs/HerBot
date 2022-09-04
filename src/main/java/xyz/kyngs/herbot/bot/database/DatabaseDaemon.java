package xyz.kyngs.herbot.bot.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import cz.oneblock.core.SystemDaemon;
import org.bson.Document;
import xyz.kyngs.herbot.bot.HerBotDaemon;

public class DatabaseDaemon extends HerBotDaemon {

    private final MongoClient client;
    private final MongoDatabase database;

    public DatabaseDaemon(SystemDaemon systemDaemon) {
        super(systemDaemon);

        var configuration = getConfiguration();

        var user = configuration.getString("user");
        var password = configuration.getString("password");

        var connectionString = new ConnectionString("mongodb://" + configuration.getString("host") + ":" + configuration.getInteger("port") + "/");
        var credential = user.isBlank() && password.isBlank() ? null : MongoCredential.createCredential(
                user,
                configuration.getString("db"),
                password.toCharArray()
        );

        var settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString);

        if (credential != null) settings.credential(credential);

        logger.info("Connecting to the database...");

        client = MongoClients.create(settings.build());

        database = client.getDatabase(configuration.getString("db"));

        logger.info("Connected to the database!");
    }

    public MongoCollection<Document> getCollection(String name) {
        return database.getCollection(name);
    }

    @Override
    public boolean useConfiguration() {
        return true;
    }

    @Override
    protected void unLoad() {
        logger.info("Disconnecting from the database...");

        client.close();

        logger.info("Disconnected from the database!");
    }
}
