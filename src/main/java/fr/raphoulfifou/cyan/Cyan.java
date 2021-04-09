package fr.raphoulfifou.cyan;

import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.raphoulfifou.cyan.commands.BedCommand;
import fr.raphoulfifou.cyan.commands.KgiCommand;
import fr.raphoulfifou.cyan.commands.SurfaceCommand;
import fr.raphoulfifou.cyan.config.CyanConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Cyan implements ModInitializer {

    public static final String MOD_ID = "cyan";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public final String MOD_NAME = "Cyan";

    @Override
    //Initialize the differents instances when the mod starts (config file, commands)
    public void onInitialize() {

        CyanConfig cyanCfg = new CyanConfig();
        try {
            cyanCfg.initialize();
            LOGGER.info("[{}] Successfully initialized configuration file", this.MOD_NAME);
        } catch (IOException e) {
            LOGGER.error("[{}] Failed to initialize configuration file", this.MOD_NAME);
            LOGGER.catching(Level.ERROR, e);
        }

        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            LiteralCommandNode<ServerCommandSource> bedNode = CommandManager
                    .literal("bed")
                    .executes(new BedCommand())
                    .build();

            /*
            LiteralCommandNode<ServerCommandSource> enderchestNode = CommandManager
                    .literal("enderchest")
                    .executes(new EnderchestCommand())
                    .build();
            */
            LiteralCommandNode<ServerCommandSource> kgiNode = CommandManager
                    .literal("kgi")
                    .executes(new KgiCommand())
                    .build();

            LiteralCommandNode<ServerCommandSource> surfaceNode = CommandManager
                    .literal("surface")
                    .executes(new SurfaceCommand())
                    .build();

            //usage: /bed
            dispatcher.getRoot().addChild(bedNode);

            //usage: /enderchest
            //dispatcher.getRoot().addChild(enderchestNode);

            //usage: /kgi
            dispatcher.getRoot().addChild(kgiNode);

            //usage: /surface
            dispatcher.getRoot().addChild(surfaceNode);
        });
        Cyan.LOGGER.info("[{}] Successfully initialized commands", this.MOD_NAME);
        Cyan.LOGGER.info("[{}] Successfully completed initialization", this.MOD_NAME);
    }
}