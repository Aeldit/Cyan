package fr.raphoulfifou.cyan;

import fr.raphoulfifou.cyan.commands.MiscellaneousCommands;
import fr.raphoulfifou.cyan.commands.TeleportationCommands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cyan implements ModInitializer {

    public static final String MODID = "cyan";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final String MODNAME = "[Cyan]";

    @Override
    //Initialize the differents instances when the mod starts (config file, commands)
    public void onInitialize(){
        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {

            //InventoryCommands.register(dispatcher);
            TeleportationCommands.register(dispatcher);
            MiscellaneousCommands.register(dispatcher);
        });
        Cyan.LOGGER.info("{} Successfully initialized commands", MODNAME);
        Cyan.LOGGER.info("{} Successfully completed initialization", MODNAME);
    }
}