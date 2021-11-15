package fr.raphoulfifou.cyan;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.raphoulfifou.cyan.commands.MiscellaneousCommands;
import fr.raphoulfifou.cyan.commands.SetCommands;
import fr.raphoulfifou.cyan.commands.TeleportationCommands;
import fr.raphoulfifou.cyan.config.options.CyanOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

/**
 * @since 0.0.1
 * @author Raphoulfifou
 */
@Environment(EnvType.CLIENT)
public class CyanClientCore implements ClientModInitializer
{
    public static final Logger LOGGER = LogManager.getLogger(CyanServerCore.MODID);
    public static final String CLIENT_MOD_NAME = "[CyanClient]";

    private static CyanOptions CONFIG;

    @Override
    // Initialize the differents instances (here commands) when lauched on server
    public void onInitializeClient()
    {
        //loadConfig();

        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) ->
        {
            TeleportationCommands.register(dispatcher);
            MiscellaneousCommands.register(dispatcher);
            SetCommands.register(dispatcher);
        });
        CyanClientCore.LOGGER.info("{} Successfully initialized commands", CLIENT_MOD_NAME);
        CyanClientCore.LOGGER.info("{} Successfully completed initialization", CLIENT_MOD_NAME);
    }

    public static CyanOptions getOptions()
    {
        if (CONFIG == null)
        {
            CONFIG = loadConfig();
        }

        return CONFIG;
    }

    private static CyanOptions loadConfig()
    {
        return CyanOptions.load(CyanOptions.DEFAULT_FILE_NAME);
    }
    
}
