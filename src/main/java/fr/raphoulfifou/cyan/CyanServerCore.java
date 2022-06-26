package fr.raphoulfifou.cyan;

import eu.midnightdust.lib.config.MidnightConfig;
import fr.raphoulfifou.cyan.commands.GetCommand;
import fr.raphoulfifou.cyan.commands.MiscellaneousCommands;
import fr.raphoulfifou.cyan.commands.SetCommands;
import fr.raphoulfifou.cyan.commands.TeleportationCommands;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 0.0.1
 */
@Environment(EnvType.SERVER)
public class CyanServerCore implements DedicatedServerModInitializer
{

    public static final String MODID = "cyan";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String SERVERMODNAME = "[CyanServer]";

    @Override
    // Initialize the differents parts of the mod when lauched on server
    public void onInitializeServer()
    {
        MidnightConfig.init("cyan", CyanMidnightConfig.class);
        CyanServerCore.LOGGER.info("{} Successfully initialized config", SERVERMODNAME);

        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) ->
        {
            TeleportationCommands.register(dispatcher);
            MiscellaneousCommands.register(dispatcher);
            SetCommands.register(dispatcher);
            GetCommand.register(dispatcher);
        });
        CyanServerCore.LOGGER.info("{} Successfully initialized commands", SERVERMODNAME);
        CyanServerCore.LOGGER.info("{} Successfully completed initialization", SERVERMODNAME);
    }

}