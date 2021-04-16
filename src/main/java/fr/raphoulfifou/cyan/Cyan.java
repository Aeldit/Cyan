package fr.raphoulfifou.cyan;

import fr.raphoulfifou.cyan.commands.MiscellaneousCommands;
import fr.raphoulfifou.cyan.commands.TeleportationCommands;
import fr.raphoulfifou.cyan.config.CyanConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Cyan implements ModInitializer {

    public static final String MOD_ID = "cyan";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public final String MOD_NAME = "[Cyan]";

    @Override
    //Initialize the differents instances when the mod starts (config file, commands)
    public void onInitialize() {

        CyanConfig cyanCfg = new CyanConfig();
        try {
            cyanCfg.initialize();
            LOGGER.info("{} Successfully initialized configuration file", this.MOD_NAME);
        } catch (IOException e) {
            LOGGER.error("{} Failed to initialize configuration file", this.MOD_NAME);
            LOGGER.catching(Level.ERROR, e);
        }

        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {

            //InventoryCommands.register(dispatcher);
            TeleportationCommands.register(dispatcher);
            MiscellaneousCommands.register(dispatcher);

        });
        Cyan.LOGGER.info("{} Successfully initialized commands", this.MOD_NAME);
        Cyan.LOGGER.info("{} Successfully completed initialization", this.MOD_NAME);
    }
}