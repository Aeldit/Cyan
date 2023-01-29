package fr.aeldit.cyan;

import eu.midnightdust.lib.config.MidnightConfig;
import fr.aeldit.cyan.commands.CyanCommands;
import fr.aeldit.cyan.commands.LocationCommands;
import fr.aeldit.cyan.commands.MiscellaneousCommands;
import fr.aeldit.cyan.commands.TeleportationCommands;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import static fr.aeldit.cyan.util.Constants.locationsPath;

/**
 * @since 0.0.1
 */
public class CyanClientCore implements ClientModInitializer {
    public static final String MODID = "cyan";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String MODNAME = "[Cyan]";

    @Override
    // Initialize the differents parts of the mod when lauched on client (used when in singleplayer)
    public void onInitializeClient() {
        MidnightConfig.init(MODID, CyanMidnightConfig.class);
        LOGGER.info("{} Successfully initialized config", MODNAME);

        CyanMidnightConfig.setBoolOption("useTranslations", true);

        if (!Files.exists(locationsPath)) {
            try {
                Files.createDirectory(FabricLoader.getInstance().getConfigDir().resolve("cyan"));
                Files.createFile(locationsPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(locationsPath.toFile());
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("{} Successfully initialized properties file", MODNAME);

        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) ->
        {
            TeleportationCommands.register(dispatcher);
            MiscellaneousCommands.register(dispatcher);
            CyanCommands.register(dispatcher);
            LocationCommands.register(dispatcher);
        });
        LOGGER.info("{} Successfully initialized commands", MODNAME);


        LOGGER.info("{} Successfully completed initialization", MODNAME);
    }

}
