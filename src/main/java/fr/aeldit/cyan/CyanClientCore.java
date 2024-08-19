package fr.aeldit.cyan;

import fr.aeldit.cyan.commands.LocationCommands;
import fr.aeldit.cyan.commands.MiscellaneousCommands;
import fr.aeldit.cyan.commands.TeleportationCommands;
import fr.aeldit.cyan.teleportation.TPa;
import fr.aeldit.cyanlib.lib.commands.CyanLibConfigCommands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import java.nio.file.Files;

import static fr.aeldit.cyan.CyanCore.*;
import static fr.aeldit.cyan.teleportation.BackTps.BACK_TP_PATH;
import static fr.aeldit.cyan.util.EventUtils.saveDeadPlayersPos;

public class CyanClientCore implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity.isPlayer())
            {
                saveDeadPlayersPos(entity);
            }
        });

        ServerPlayConnectionEvents.JOIN.register(
                (handler, sender, server) -> {
                    LOCATIONS.readClient(server.getSaveProperties().getLevelName());
                    BACK_TPS.readClient(server.getSaveProperties().getLevelName());

                    if (Files.exists(BACK_TP_PATH))
                    {
                        BACK_TPS.removeAllOutdated();
                    }
                }
        );

        ServerPlayConnectionEvents.DISCONNECT.register(
                (handler, server) -> TPa.removePlayerOnQuit(handler.getPlayer().getName().getString())
        );

        // Register all the commands
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated, environment) -> {
                    new CyanLibConfigCommands(MODID, CYAN_LIB_UTILS).register(dispatcher);
                    TeleportationCommands.register(dispatcher);
                    MiscellaneousCommands.register(dispatcher);
                    LocationCommands.register(dispatcher);
                }
        );
        CYAN_LOGGER.info("[Cyan] Successfully initialized");
    }
}
