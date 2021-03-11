package xyz.kyngs.herbot.database;

import cz.kyngs.easymysql.MySQL;
import cz.kyngs.easymysql.MySQLBuilder;
import org.simpleyaml.configuration.ConfigurationSection;

import java.sql.SQLException;

public class DatabaseManager {

    private final MySQL mySQL;

    public DatabaseManager(ConfigurationSection databaseConf) throws SQLException {

        var builder = new MySQLBuilder();

        builder.setDatabase(databaseConf.getString("database"));
        builder.setHost(databaseConf.getString("host"));
        builder.setPassword(databaseConf.getString("password"));
        builder.setPort(databaseConf.getInt("port"));
        builder.setUsername(databaseConf.getString("user"));

        mySQL = builder.build();

        prepareTables();

    }

    public MySQL getMySQL() {
        return mySQL;
    }

    private void prepareTables() {
        mySQL.sync().schedule(connection -> {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS info_messages (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, channel_id VARCHAR(256) NOT NULL, message TEXT NOT NULL)").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS anti_duplication (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, channel_id VARCHAR(256) NOT NULL, link TEXT NOT NULL)").execute();
        });
    }

    public void shutdown() {
        mySQL.close();
    }
}
