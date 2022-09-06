package xyz.kyngs.herbot.bot.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import cz.oneblock.core.SystemDaemon;
import org.bson.Document;
import xyz.kyngs.herbot.bot.HerBotDaemon;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

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

        var validations = new Document();

        validations.put("balance", new Document("minimum", 0));

        try {
            database.createCollection("users", new CreateCollectionOptions()
                    .validationOptions(new ValidationOptions()
                            .validator(new Document("$jsonSchema", new Document("properties", validations)))
                    )
            );
        } catch (MongoCommandException ignored) {
        }

        logger.info("Connected to the database!");
    }

    public Document getUser(long id) {
        var collection = database.getCollection("users");
        var document = collection.find(new Document("_id", id)).first();
        if (document == null) {
            document = new Document("_id", id);
            document.put("balance", 5000L);
            document.put("streak", -1);
            document.put("last-claim", Date.from(LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC)));
            collection.insertOne(document);
        }
        return document;
    }

    public MongoCollection<Document> getCollection(String name) {
        return database.getCollection(name);
    }

    public MongoCollection<Document> getUserCollection() {
        return getCollection("users");
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
