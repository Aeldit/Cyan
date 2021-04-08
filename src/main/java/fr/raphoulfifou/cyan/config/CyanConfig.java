package fr.raphoulfifou.cyan.config;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class CyanConfig {

    private static Path configPath;
    Properties properties = new Properties();

    private final String CONFIG_HEADER = StringUtils.repeat("-", 73) +
            "\n This is the configuration file where values are stored for the Cyan mod" +
            "\n" + StringUtils.repeat("-", 73);

    public CyanConfig() {
        configPath = FabricLoader.getInstance().getConfigDir().resolve("Cyan.properties");
    }

    /**
     * Initializes the configuration, loading it if it is present and creating a default config otherwise.
     * @throws IOException file exceptions
     */
    public void initialize() throws IOException {
        load();
        if(!Files.exists(configPath)) {
            save();
        }
    }

    /**
     * Loads the config file and then populates the string, int, and boolean entries with the parsed entries
     * @throws IOException if the file cannot be loaded
     */

    public void load() throws IOException {
        if (!Files.exists(configPath)) {
            return;
        }
        properties.load(Files.newInputStream(configPath));
    }

    /**
     * Save the config into the file. Should be called when a value is modified
     * @throws IOException file exceptions
     */
    public void save() throws IOException {
        properties.store(Files.newOutputStream(configPath), CONFIG_HEADER);
    }
}