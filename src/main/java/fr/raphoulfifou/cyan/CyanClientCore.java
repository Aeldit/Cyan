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
import net.fabricmc.loader.api.FabricLoader;

/**
 * @since 0.0.1
 * @author Raphoulfifou
 */
@Environment(EnvType.CLIENT)
public class CyanClientCore implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(CyanServerCore.MODID);
    public static final String CLIENTMODNAME = "[CyanClient]";

    private static CyanOptions CONFIG;

    @Override
    // Initialize the differents instances (here commands) when lauched on client (used when in singleplayer)
    public void onInitializeClient()
    {
        //CONFIG = loadConfig();

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

    public static CyanOptions getOptions() {
        if (CONFIG == null) {
            CONFIG = loadConfig();
        }

        return CONFIG;
    }

    private static CyanOptions loadConfig() {
        return CyanOptions.load(FabricLoader.getInstance().getConfigDir().resolve("cyan-options.json").toFile());
    }
}