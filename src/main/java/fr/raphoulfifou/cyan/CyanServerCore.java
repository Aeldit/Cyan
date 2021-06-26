package fr.raphoulfifou.cyan;

import fr.raphoulfifou.cyan.commands.MiscellaneousCommands;
import fr.raphoulfifou.cyan.commands.TeleportationCommands;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.SERVER)
public class CyanServerCore implements DedicatedServerModInitializer {
    public static final String MODID = "cyan";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final String SERVERMODNAME = "[CyanServer]";

    @Override
    // Initialize the differents instances (here commands) when lauched on server
    public void onInitializeServer(){
        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            TeleportationCommands.register(dispatcher);
            MiscellaneousCommands.register(dispatcher);
        });
        CyanServerCore.LOGGER.info("{} Successfully initialized commands", SERVERMODNAME);
        CyanServerCore.LOGGER.info("{} Successfully completed initialization", SERVERMODNAME);
    }
}