/*
 * Copyright (c) 2023-2024  -  Made by Aeldit
 *
 *              GNU LESSER GENERAL PUBLIC LICENSE
 *                  Version 3, 29 June 2007
 *
 *  Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *  Everyone is permitted to copy and distribute verbatim copies
 *  of this license document, but changing it is not allowed.
 *
 *
 * This version of the GNU Lesser General Public License incorporates
 * the terms and conditions of version 3 of the GNU General Public
 * License, supplemented by the additional permissions listed in the LICENSE.txt file
 * in the repo of this mod (https://github.com/Aeldit/Cyan)
 */

package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.commands.arguments.ArgumentSuggestion;
import fr.aeldit.cyan.teleportation.BackTps;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static fr.aeldit.cyan.config.CyanConfig.*;
import static fr.aeldit.cyan.teleportation.TPUtils.getRequiredXpLevelsToTp;
import static fr.aeldit.cyan.util.Utils.*;
import static fr.aeldit.cyanlib.lib.utils.TranslationsPrefixes.ERROR;

public class TeleportationCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("back")
                .executes(TeleportationCommands::back)
        );

        dispatcher.register(CommandManager.literal("bed")
                .executes(TeleportationCommands::bed)
        );
        dispatcher.register(CommandManager.literal("b")
                .executes(TeleportationCommands::bed)
        );

        dispatcher.register(CommandManager.literal("surface")
                .executes(TeleportationCommands::surface)
        );
        dispatcher.register(CommandManager.literal("s")
                .executes(TeleportationCommands::surface)
        );

        dispatcher.register(CommandManager.literal("tpa")
                .then(CommandManager.argument("player_name", StringArgumentType.string())
                        .suggests((context, builder) -> ArgumentSuggestion.getOnlinePlayersName(builder, context.getSource()))
                        .executes(TeleportationCommands::tpa)
                )
        );
        dispatcher.register(CommandManager.literal("tpa-from-server")
                .then(CommandManager.argument("player_name", StringArgumentType.string())
                        .then(CommandManager.argument("destination_player_name", StringArgumentType.string())
                                .executes(TeleportationCommands::tpaPlayer)
                        )
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

        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_BACK_TP.getValue(), "backTpDisabled"))
            {
                BackTps.BackTp backTp = BACK_TPS.getBackTp(player.getUuidAsString());
                if (backTp != null)
                {
                    switch (backTp.dimension())
                    {
                        case "overworld" ->
                                player.teleport(player.getServer().getWorld(World.OVERWORLD), backTp.x(), backTp.y(), backTp.z(), 0, 0);
                        case "nether" ->
                                player.teleport(player.getServer().getWorld(World.NETHER), backTp.x(), backTp.y(), backTp.z(), 0, 0);
                        case "end" ->
                                player.teleport(player.getServer().getWorld(World.END), backTp.x(), backTp.y(), backTp.z(), 0, 0);
                    }

                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                            CYAN_LANGUAGE_UTILS.getTranslation("backTp"),
                            "cyan.msg.backTp"
                    );
                }
                else
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                            CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "noLastPos"),
                            "cyan.msg.noLastPos"
                    );
                }
            }
        }
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

        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_BED.getValue(), "bedDisabled"))
            {
                int requiredXpLevel = 0;

                if (USE_XP_TO_TELEPORT.getValue())
                {
                    requiredXpLevel = getRequiredXpLevelsToTp(player, player.getSpawnPointPosition());

                    if (player.experienceLevel < requiredXpLevel)
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                                CYAN_LANGUAGE_UTILS.getTranslation("notEnoughXp"),
                                "cyan.msg.notEnoughXp",
                                Formatting.GOLD + String.valueOf(requiredXpLevel)
                        );
                        return Command.SINGLE_SUCCESS;
                    }
                }

                if (player.getSpawnPointPosition() != null)
                {
                    player.teleport(
                            player.getServer().getWorld(player.getSpawnPointDimension()),
                            player.getSpawnPointPosition().getX(),
                            player.getSpawnPointPosition().getY(),
                            player.getSpawnPointPosition().getZ(),
                            player.getYaw(), player.getPitch()
                    );

                    String key = player.getSpawnPointDimension() == World.OVERWORLD ? "bed" : "respawnAnchor";
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                            CYAN_LANGUAGE_UTILS.getTranslation(key),
                            "cyan.msg.%s".formatted(key)
                    );

                    player.addExperienceLevels(-1 * requiredXpLevel);
                }
                else
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                            CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "bedNotFound"),
                            "cyan.msg.bedNotFound"
                    );
                }
            }
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

        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_SURFACE.getValue(), "surfaceDisabled"))
            {
                int requiredXpLevel = 0;
                double topY = player.getWorld().getTopY(
                        Heightmap.Type.WORLD_SURFACE,
                        player.getBlockPos().getX(), player.getBlockPos().getZ()
                );

                if (USE_XP_TO_TELEPORT.getValue())
                {
                    int distanceY = (int) player.getY() - (int) topY;
                    System.out.println(distanceY);

                    // Converts to a positive distance
                    if (distanceY < 0)
                    {
                        distanceY *= -1;
                    }
                    // Minecraft doesn't center the position to the middle of the block but in 1 corner,
                    // so this allows for a better centering
                    distanceY += 1;

                    int coordinatesDistance = (int) distanceY;

                    if (coordinatesDistance < XP_REQUIRED_TO_TP_BASE_DISTANCE_Y.getValue())
                    {
                        requiredXpLevel = 1;
                    }
                    else
                    {
                        requiredXpLevel = 1 + coordinatesDistance / XP_REQUIRED_TO_TP_BASE_DISTANCE_Y.getValue();
                    }

                    if (player.experienceLevel < requiredXpLevel)
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                                CYAN_LANGUAGE_UTILS.getTranslation("notEnoughXp"),
                                "cyan.msg.notEnoughXp",
                                Formatting.GOLD + String.valueOf(requiredXpLevel)
                        );
                        return Command.SINGLE_SUCCESS;
                    }
                }

                player.teleport(context.getSource().getWorld(),
                        player.getBlockPos().getX(),
                        topY,
                        player.getBlockPos().getZ(),
                        player.getYaw(), player.getPitch()
                );
                CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                        CYAN_LANGUAGE_UTILS.getTranslation("surface"),
                        "cyan.msg.surface"
                );

                player.addExperienceLevels(-1 * requiredXpLevel);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int tpaPlayer(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        PlayerManager playerManager = context.getSource().getServer().getPlayerManager();

        if (player == null)
        {
            if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
            {
                String destinationPlayerName = StringArgumentType.getString(context, "destination_player_name");
                String playerNameToTp = StringArgumentType.getString(context, "player_name_to_tp");

                ServerPlayerEntity destinationPlayer = null;
                ServerPlayerEntity playerToTp = null;

                for (String s : playerManager.getPlayerNames())
                {
                    if (s.equals(playerNameToTp))
                    {
                        playerToTp = playerManager.getPlayer(s);
                    }
                    else if (s.equals(destinationPlayerName))
                    {
                        destinationPlayer = playerManager.getPlayer(destinationPlayerName);
                    }

                    if (destinationPlayer != null && playerToTp != null)
                    {
                        break;
                    }
                }

                if (destinationPlayer != null && playerToTp != null)
                {
                    ServerWorld serverWorld = destinationPlayer.getServerWorld();
                    double x = destinationPlayer.getX();
                    double y = destinationPlayer.getY();
                    double z = destinationPlayer.getZ();
                    playerToTp.teleport(serverWorld, x, y, z, 0, 0);

                    int requiredXpLevel = 0;

                    if (USE_XP_TO_TELEPORT.getValue())
                    {
                        requiredXpLevel = getRequiredXpLevelsToTp(playerToTp, destinationPlayer.getBlockPos());

                        if (playerToTp.experienceLevel < requiredXpLevel)
                        {
                            CYAN_LANGUAGE_UTILS.sendPlayerMessage(playerToTp,
                                    CYAN_LANGUAGE_UTILS.getTranslation("notEnoughXp"),
                                    "cyan.msg.notEnoughXp",
                                    Formatting.GOLD + String.valueOf(requiredXpLevel)
                            );
                            return Command.SINGLE_SUCCESS;
                        }
                    }

                    playerToTp.addExperienceLevels(-1 * requiredXpLevel);
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int tpa(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_TPA.getValue(), "tpaDisabled"))
            {
                String playerName = StringArgumentType.getString(context, "player_name");

                CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                        Objects.requireNonNull(context.getSource().getServer().getPlayerManager().getPlayer(playerName)),
                        CYAN_LANGUAGE_UTILS.getTranslation("tpaRequest"),
                        "cyan.msg.tpaRequest",
                        player.getName().getString()
                );
                Objects.requireNonNull(context.getSource().getServer().getPlayerManager().getPlayer(playerName)).sendMessage(
                        Text.of(Text.literal(Formatting.GREEN + "[Accept]").
                                setStyle(Style.EMPTY.withClickEvent(
                                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa-from-server %s %s".formatted(playerName, player.getName().getString())))
                                ).toString()
                                +
                                Text.literal(Formatting.GREEN + "[Refuse]").
                                        setStyle(Style.EMPTY.withClickEvent(
                                                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/title %s actionbar %s"
                                                                .formatted(playerName, Text.translatable("cyan.msg.tpaRefused")))
                                                )
                                        ).toString()
                        )
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
