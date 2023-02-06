package fr.aeldit.cyan;

import eu.midnightdust.lib.config.MidnightConfig;
import fr.aeldit.cyan.commands.CyanCommands;
import fr.aeldit.cyan.commands.LocationCommands;
import fr.aeldit.cyan.commands.MiscellaneousCommands;
import fr.aeldit.cyan.commands.TeleportationCommands;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import fr.aeldit.cyan.util.Utils;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;

import static fr.aeldit.cyan.util.Utils.locationsPath;

/**
 * @since 0.0.1
 */
public class CyanServerCore implements DedicatedServerModInitializer
{
    public static final String MODID = "cyan";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String MODNAME = "[Cyan]";

    @Override
    // Initialize the differents parts of the mod when lauched on client (used when in singleplayer)
    public void onInitializeServer()
    {
        MidnightConfig.init(MODID, CyanMidnightConfig.class);
        LOGGER.info("{} Successfully initialized config", MODNAME);

        Utils.generateAllMaps();

        if (!Files.exists(locationsPath))
        {
            try
            {
                Files.createDirectory(FabricLoader.getInstance().getConfigDir().resolve("cyan"));
                Files.createFile(locationsPath);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
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
