package xyz.kyngs.herbot;

import org.simpleyaml.configuration.Configuration;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static xyz.kyngs.herbot.HerBot.LOGGER;

public class Bootstrap {

    public static void main(String[] args) throws IOException {

        LOGGER.info("Loading configuration!");

        var configFile = new File("config.yml");
        if (!configFile.exists()) {
            Files.copy(ClassLoader.getSystemResourceAsStream("config.yml"), Paths.get("config.yml"));
        }

        var configuration = YamlConfiguration.loadConfiguration(configFile);
        validateSchema(configuration);
        configuration.save(configFile);

        LOGGER.info("Loaded, performing boot sequence.");

        new HerBot(configuration);

        LOGGER.info("Started!");


    }

    private static void validateSchema(Configuration configuration) {
        var original = YamlConfiguration.loadConfiguration(ClassLoader.getSystemResourceAsStream("config.yml"));
        var keys = configuration.getKeys(true);
        for (var key : original.getKeys(true)) {
            if (!keys.contains(key)) {
                LOGGER.warn("Key " + key + " is not present in config.yml, putting it in now.");
                configuration.set(key, original.get(key));
            }
        }
    }


}
