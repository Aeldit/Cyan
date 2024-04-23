package fr.aeldit.cyan;

import fr.aeldit.cyan.commands.LocationCommands;
import fr.aeldit.cyan.commands.MiscellaneousCommands;
import fr.aeldit.cyan.commands.TeleportationCommands;
import fr.aeldit.cyan.teleportation.TPa;
import fr.aeldit.cyanlib.lib.commands.CyanLibConfigCommands;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import static fr.aeldit.cyan.CyanCore.*;
import static fr.aeldit.cyan.util.EventUtils.removeOutdatedBackTps;
import static fr.aeldit.cyan.util.EventUtils.saveDeadPlayersPos;

public class CyanServerCore implements DedicatedServerModInitializer
{
    @Override
    public void onInitializeServer()
    {
        CYAN_LIB_UTILS.init(CYAN_MODID, CYAN_OPTIONS_STORAGE);

        LOCATIONS.readServer();
        BACK_TPS.readServer();

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> saveDeadPlayersPos(entity));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> removeOutdatedBackTps());
        ServerPlayConnectionEvents.DISCONNECT.register(
                (handler, server) -> TPa.removePlayerOnQuit(handler.getPlayer().getName().getString())
        );

        // Register all the commands
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated, environment) -> {
                    TeleportationCommands.register(dispatcher);
                    MiscellaneousCommands.register(dispatcher);
                    new CyanLibConfigCommands(CYAN_MODID, CYAN_LIB_UTILS).register(dispatcher);
                    LocationCommands.register(dispatcher);
                }
        );
        CYAN_LOGGER.info("[Cyan] Successfully initialized");
    }
}
