package fr.aeldit.cyan;

import fr.aeldit.cyan.commands.LocationCommands;
import fr.aeldit.cyan.commands.MiscellaneousCommands;
import fr.aeldit.cyan.commands.PermissionsCommands;
import fr.aeldit.cyan.commands.TeleportationCommands;
import fr.aeldit.cyan.teleportation.TPa;
import fr.aeldit.cyan.util.VersionUtils;
import fr.aeldit.cyanlib.events.PlayerMovedEvent;
import fr.aeldit.cyanlib.lib.CombatTracking;
import fr.aeldit.cyanlib.lib.commands.CyanLibConfigCommands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;

import java.nio.file.Files;

import static fr.aeldit.cyan.CyanCore.*;
import static fr.aeldit.cyan.config.CyanLibConfigImpl.XP_USE_POINTS;
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
                (handler, server) -> TPAS.removePlayerOnQuit(handler.getPlayer().getName().getString())
        );

        PlayerMovedEvent.AFTER_MOVE.register((player) -> {
            if (LOCATIONS.playerRequestedTp(player.getName().getString()))
            {
                LocationsCooldowns.cancelCooldown(player);
                LOCATIONS.endTpRequest(player.getName().getString());
            }
            if (TPAS.playerRequestedTp(player.getName().getString()))
            {
                TPAsCooldowns.cancelCooldown(player);
                TPAS.endTpRequest(player.getName().getString());
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> {
            LocationsCooldowns.getCanceledCooldowns().forEach(player -> CYAN_LANG_UTILS.sendPlayerMessage(
                    player, "error.movedWhileWaitingForTp"
            ));
            LocationsCooldowns.clearCanceledCooldowns();

            LocationsCooldowns.getPlayersCompletedCooldowns().forEach((player, t) -> {
                LOCATIONS.endTpRequest(player.getName().getString());
                t.loc().teleport(t.server(), player);

                if (XP_USE_POINTS.getValue())
                {
                    player.addExperience(-1 * t.requiredXpLevel());
                }
                else
                {
                    player.addExperienceLevels(-1 * t.requiredXpLevel());
                }

                CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.goToLocation", Formatting.YELLOW + t.loc().name());
            });

            TPAsCooldowns.getCanceledCooldowns().forEach(player -> CYAN_LANG_UTILS.sendPlayerMessage(
                    player, "error.movedWhileWaitingForTp"
            ));
            TPAsCooldowns.clearCanceledCooldowns();

            TPAsCooldowns.getPlayersCompletedCooldowns().forEach((requestingPlayer, t) -> {
                TPAS.endTpRequest(requestingPlayer.getName().getString());
                VersionUtils.tp(requestingPlayer, t.requestedPlayer());

                if (XP_USE_POINTS.getValue())
                {
                    requestingPlayer.addExperience(-1 * t.requiredXpLevel());
                }
                else
                {
                    requestingPlayer.addExperienceLevels(-1 * t.requiredXpLevel());
                }

                TPAS.removePlayerFromQueue(
                        requestingPlayer.getName().getString(), t.requestedPlayer().getName().getString());

                CYAN_LANG_UTILS.sendPlayerMessage(
                        requestingPlayer, "msg.tpaSuccessful", t.requestedPlayer().getName().getString());
                CYAN_LANG_UTILS.sendPlayerMessage(
                        t.requestedPlayer(), "msg.tpaAcceptedSelf", requestingPlayer.getName().getString());
            });
        });

        //? if >1.20.6 {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if (entity.isPlayer())
            {
                CombatTracking.addEntry(entity.getName().getString(), System.currentTimeMillis());
                Entity attacker = source.getAttacker();
                if (attacker != null)
                {
                    CombatTracking.addEntry(attacker.getName().getString(), System.currentTimeMillis());
                }
            }
        });
        //?} else {
        /*MissingLivingEntityEvent.AFTER_DAMAGE.register((entity, source, amount) -> {
            if (entity.isPlayer())
            {
                CombatTracking.addEntry(entity.getName().getString(), System.currentTimeMillis());
                Entity attacker = source.getAttacker();
                if (attacker != null)
                {
                    CombatTracking.addEntry(attacker.getName().getString(), System.currentTimeMillis());
                }
            }
        });
        *///?}

        // Register all the commands
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated, environment) -> {
                    new CyanLibConfigCommands(MODID, CYAN_LIB_UTILS).register(dispatcher);
                    TeleportationCommands.register(dispatcher);
                    MiscellaneousCommands.register(dispatcher);
                    LocationCommands.register(dispatcher);
                    PermissionsCommands.register(dispatcher);
                }
        );
        CYAN_LOGGER.info("[Cyan] Successfully initialized");
    }
}
