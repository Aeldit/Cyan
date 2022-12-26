package fr.aeldit.cyan;

import eu.midnightdust.lib.config.MidnightConfig;
import fr.aeldit.cyan.commands.CyanCommands;
import fr.aeldit.cyan.commands.MiscellaneousCommands;
import fr.aeldit.cyan.commands.TeleportationCommands;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import fr.aeldit.cyan.util.ChatConstants;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        CyanServerCore.LOGGER.info("{} Successfully initialized config", MODNAME);

        ChatConstants.generateAllMaps();

        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) ->
        {
            TeleportationCommands.register(dispatcher);
            MiscellaneousCommands.register(dispatcher);
            CyanCommands.register(dispatcher);
        });
        CyanServerCore.LOGGER.info("{} Successfully initialized commands", MODNAME);
        CyanServerCore.LOGGER.info("{} Successfully completed initialization", MODNAME);

    }

}
