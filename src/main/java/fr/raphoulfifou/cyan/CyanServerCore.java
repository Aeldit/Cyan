package fr.raphoulfifou.cyan;

import eu.midnightdust.lib.config.MidnightConfig;
import fr.raphoulfifou.cyan.commands.*;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
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
