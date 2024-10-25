package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.commands.arguments.ArgumentSuggestion;
import fr.aeldit.cyan.teleportation.BackTp;
import fr.aeldit.cyan.teleportation.TPa;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

import static fr.aeldit.cyan.CyanCore.*;
import static fr.aeldit.cyan.config.CyanLibConfigImpl.*;
import static fr.aeldit.cyan.teleportation.TPa.addPlayerToQueue;
import static fr.aeldit.cyan.teleportation.TPa.removePlayerFromQueue;
import static fr.aeldit.cyanlib.lib.utils.TPUtils.getRequiredXpLevelsToTp;

public class TeleportationCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("back").executes(TeleportationCommands::back));

        dispatcher.register(CommandManager.literal("bed").executes(TeleportationCommands::bed));
        dispatcher.register(CommandManager.literal("b").executes(TeleportationCommands::bed));

        dispatcher.register(CommandManager.literal("surface").executes(TeleportationCommands::surface));
        dispatcher.register(CommandManager.literal("s").executes(TeleportationCommands::surface));

        dispatcher.register(
                CommandManager.literal("tpa").then(
                        CommandManager.argument("player_name", StringArgumentType.string())
                                      .suggests((context, builder) -> ArgumentSuggestion.getOnlinePlayersName(
                                              builder, context.getSource()
                                      ))
                                      .executes(TeleportationCommands::tpa)
                )
        );
        dispatcher.register(
                CommandManager.literal("tpaAccept").then(
                        CommandManager.argument("player_name", StringArgumentType.string())
                                      .suggests((context, builder) -> ArgumentSuggestion.getRequestingPlayersNames(
                                              builder, context.getSource()
                                      ))
                                      .executes(TeleportationCommands::acceptTpa)
                )
        );
        dispatcher.register(
                CommandManager.literal("tpaRefuse").then(
                        CommandManager.argument("player_name", StringArgumentType.string())
                                      .suggests((context, builder) -> ArgumentSuggestion.getRequestingPlayersNames(
                                              builder, context.getSource())
                                      )
                                      .executes(TeleportationCommands::refuseTpa)
                )
        );
    }

    /**
     * Called by the command {@code /back}
     * <p>
     * Teleports the player to its last death position
     */
    public static int back(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_BACK_TP.getValue(), "backTpDisabled")
        )
        {
            return 0;
        }

        BackTp backTp = BACK_TPS.getBackTp(player.getUuidAsString());
        MinecraftServer server = player.getServer();

        if (backTp == null || server == null)
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.noLastPos");
            return 0;
        }

        backTp.teleport(server, player);
        CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.backTp");
        BACK_TPS.remove(player.getUuidAsString());
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the commands {@code /bed} or {@code /b}
     * <p>
     * Teleports the player to its bed
     */
    public static int bed(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_BED.getValue(), "bedDisabled")
        )
        {
            return 0;
        }

        BlockPos spawnPos = Commons.bed(player);
        if (spawnPos == null)
        {
            return 0;
        }

        int requiredXpLevel = 0;
        if (USE_XP_TO_TELEPORT.getValue() && !player.isCreative())
        {
            requiredXpLevel = getRequiredXpLevelsToTp(player, spawnPos, BLOCKS_PER_XP_LEVEL_BED.getValue());

            if ((XP_USE_POINTS.getValue() ? player.totalExperience : player.experienceLevel) < requiredXpLevel)
            {
                CYAN_LANG_UTILS.sendPlayerMessage(
                        player,
                        "error.notEnoughXp",
                        Formatting.GOLD + String.valueOf(requiredXpLevel),
                        Formatting.RED + (XP_USE_POINTS.getValue() ? "points" : "levels")
                );
                return 0;
            }
        }

        if (XP_USE_POINTS.getValue())
        {
            player.addExperience(-1 * requiredXpLevel);
        }
        else
        {
            player.addExperienceLevels(-1 * requiredXpLevel);
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the commands {@code /surface} or {@code /s}
     * <p>
     * Teleport the player to the highest block that was found on the player's coordinates
     */
    public static int surface(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_SURFACE.getValue(), "surfaceDisabled")
        )
        {
            return 0;
        }

        double topY = Commons.surface(player);

        int requiredXpLevel = 0;
        if (USE_XP_TO_TELEPORT.getValue() && !player.isCreative())
        {
            int distanceY = (int) player.getY() - (int) topY;

            // Converts to a positive distance
            if (distanceY < 0)
            {
                distanceY *= -1;
            }
            // Minecraft doesn't center the position to the middle of the block but in 1 corner,
            // so this allows for a better centering
            distanceY += 1;

            int coordinatesDistance = distanceY;

            if (coordinatesDistance < BLOCKS_PER_XP_LEVEL_SURFACE.getValue())
            {
                requiredXpLevel = 1;
            }
            else
            {
                requiredXpLevel = 1 + coordinatesDistance / BLOCKS_PER_XP_LEVEL_SURFACE.getValue();
            }

            if ((XP_USE_POINTS.getValue() ? player.totalExperience : player.experienceLevel) < requiredXpLevel)
            {
                CYAN_LANG_UTILS.sendPlayerMessage(
                        player,
                        "error.notEnoughXp",
                        Formatting.GOLD + String.valueOf(requiredXpLevel),
                        Formatting.RED + (XP_USE_POINTS.getValue() ? "points" : "levels")
                );
                return 0;
            }
        }

        if (XP_USE_POINTS.getValue())
        {
            player.addExperience(-1 * requiredXpLevel);
        }
        else
        {
            player.addExperienceLevels(-1 * requiredXpLevel);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int tpa(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_TPA.getValue(), "tpaDisabled")
        )
        {
            return 0;
        }

        String playerName = StringArgumentType.getString(context, "player_name");

        ServerPlayerEntity playerToSendMessage =
                context.getSource().getServer().getPlayerManager().getPlayer(playerName);
        if (playerToSendMessage == null)
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.playerNotFound");
            return 0;
        }

        String requestingPlayerName = player.getName().getString();
        if (TPa.isPlayerRequesting(requestingPlayerName, playerName))
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.tpaAlreadyRequested");
            return 0;
        }

        addPlayerToQueue(requestingPlayerName, playerName);

        CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.tpaRequestSend", requestingPlayerName);

        CYAN_LANG_UTILS.sendPlayerMessageActionBar(
                playerToSendMessage,
                "msg.tpaRequested",
                false,
                requestingPlayerName
        );

        playerToSendMessage.sendMessage(
                Text.literal(Formatting.GREEN + "[Accept]")
                    .setStyle(Style.EMPTY.withClickEvent(
                            new ClickEvent(
                                    ClickEvent.Action.RUN_COMMAND,
                                    "/tpaAccept %s".formatted(requestingPlayerName)
                            )))
                    .append(Text.literal(Formatting.RED + "    [Refuse]")
                                .setStyle(Style.EMPTY.withClickEvent(
                                        new ClickEvent(
                                                ClickEvent.Action.RUN_COMMAND,
                                                "/tpaRefuse %s".formatted(requestingPlayerName)
                                        ))
                                )
                    )
        );
        return Command.SINGLE_SUCCESS;
    }

    public static int acceptTpa(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_TPA.getValue(), "tpaDisabled")
        )
        {
            return 0;
        }

        String requestingPlayerName = StringArgumentType.getString(context, "player_name");
        ServerPlayerEntity requestingPlayer = context.getSource().getServer().getPlayerManager().getPlayer(
                requestingPlayerName
        );

        // If the player is not online
        // or
        // If the player has not requested a teleportation to the player running the command
        if (requestingPlayer == null
            || !TPa.isPlayerRequesting(requestingPlayerName, player.getName().getString())
        )
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.noRequestingPlayers");
            return 0;
        }

        int requiredXpLevel = 0;

        if (USE_XP_TO_TELEPORT.getValue() && !player.isCreative())
        {
            requiredXpLevel = getRequiredXpLevelsToTp(
                    requestingPlayer, player.getBlockPos(),
                    BLOCKS_PER_XP_LEVEL_TPA.getValue()
            );

            if ((XP_USE_POINTS.getValue() ? player.totalExperience : player.experienceLevel) < requiredXpLevel)
            {
                CYAN_LANG_UTILS.sendPlayerMessage(requestingPlayer, "error.notEnoughXpTpa", requestingPlayerName);
                return 0;
            }
        }

        //? if >=1.21.2-1.21.3 {
        requestingPlayer.teleport(
                player.getServerWorld(), player.getX(), player.getY(), player.getZ(), new HashSet<>(), 0, 0, false
        );
        //?} elif >1.19.4 {
        /*requestingPlayer.teleport(player.getServerWorld(), player.getX(), player.getY(), player.getZ(), 0, 0);
         *///?} else {
        /*requestingPlayer.teleport(player.getWorld(), player.getX(), player.getY(), player.getZ(), 0, 0);
         *///?}

        if (XP_USE_POINTS.getValue())
        {
            player.addExperience(-1 * requiredXpLevel);
        }
        else
        {
            player.addExperienceLevels(-1 * requiredXpLevel);
        }

        removePlayerFromQueue(requestingPlayerName, player.getName().getString());

        CYAN_LANG_UTILS.sendPlayerMessage(requestingPlayer, "msg.tpaSuccessful", player.getName().getString());
        CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.tpaAcceptedSelf", requestingPlayer.getName().getString());
        return Command.SINGLE_SUCCESS;
    }

    public static int refuseTpa(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_TPA.getValue(), "tpaDisabled")
        )
        {
            return 0;
        }

        String requestingPlayerName = StringArgumentType.getString(context, "player_name");
        ServerPlayerEntity requestingPlayer = context.getSource().getServer().getPlayerManager().getPlayer(
                requestingPlayerName);

        // If the player is not online
        // or
        // If the player has not requested a teleportation to the player running the command
        if (requestingPlayer == null || !TPa.isPlayerRequesting(
                requestingPlayerName, player.getName().getString()))
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.noRequestingPlayers");
            return 0;
        }

        removePlayerFromQueue(requestingPlayerName, player.getName().getString());

        CYAN_LANG_UTILS.sendPlayerMessage(requestingPlayer, "msg.tpaRefused", player.getName().getString());
        CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.tpaRefusedSelf", requestingPlayer.getName().getString());
        return Command.SINGLE_SUCCESS;
    }
}
