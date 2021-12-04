package fr.raphoulfifou.cyan;

import eu.midnightdust.lib.config.MidnightConfig;
import fr.raphoulfifou.cyan.commands.MiscellaneousCommands;
import fr.raphoulfifou.cyan.commands.SetCommands;
import fr.raphoulfifou.cyan.commands.TeleportationCommands;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @since 0.0.1
 * @author Raphoulfifou
 */
@Environment(EnvType.CLIENT)
public class CyanClientCore implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(CyanServerCore.MODID);
    public static final String CLIENTMODNAME = "[CyanClient]";

    @Override
    // Initialize the differents parts of the mod when lauched on client (used when in singleplayer)
    public void onInitializeClient()
    {
        MidnightConfig.init("cyan", CyanMidnightConfig.class);
        CyanClientCore.LOGGER.info("{} Successfully initialized config", CLIENTMODNAME);

        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) ->
        {
            TeleportationCommands.register(dispatcher);
            MiscellaneousCommands.register(dispatcher);
            SetCommands.register(dispatcher);
        });
        CyanClientCore.LOGGER.info("{} Successfully initialized commands", CLIENTMODNAME);
        CyanClientCore.LOGGER.info("{} Successfully completed initialization", CLIENTMODNAME);
    }
}