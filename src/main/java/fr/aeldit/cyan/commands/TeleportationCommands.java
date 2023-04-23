/*
 * Copyright (c) 2023  -  Made by Aeldit
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
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import static fr.aeldit.cyan.util.EventUtils.backTpPath;
import static fr.aeldit.cyan.util.Utils.*;
import static fr.aeldit.cyanlib.util.ChatUtils.sendPlayerMessage;
import static fr.aeldit.cyanlib.util.Constants.ERROR;

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
    }

    /**
     * Called by the command {@code /back}
     * <p>
     * Teleports the player to its last death position
     */
    public static int back(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (CyanLibUtils.isPlayer(source))
        {
            if (CyanLibUtils.isOptionAllowed(player, CyanMidnightConfig.allowBackTp, "backTpDisabled"))
            {
                checkOrCreateFile(backTpPath);
                try
                {
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(backTpPath.toFile()));
                    if (properties.containsKey(player.getUuidAsString()))
                    {
                        String pos = (String) properties.get(player.getUuidAsString());
                        if (Objects.equals(pos.split(" ")[0], "overworld"))
                        {
                            player.teleport(
                                    Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD),
                                    Double.parseDouble(pos.split(" ")[1]),
                                    Double.parseDouble(pos.split(" ")[2]),
                                    Double.parseDouble(pos.split(" ")[3]),
                                    0,
                                    0
                            );
                        }
                        else if (Objects.equals(pos.split(" ")[0], "nether"))
                        {
                            player.teleport(
                                    Objects.requireNonNull(player.getServer()).getWorld(World.NETHER),
                                    Double.parseDouble(pos.split(" ")[1]),
                                    Double.parseDouble(pos.split(" ")[2]),
                                    Double.parseDouble(pos.split(" ")[3]),
                                    0,
                                    0
                            );
                        }
                        else if (Objects.equals(pos.split(" ")[0], "end"))
                        {
                            player.teleport(
                                    Objects.requireNonNull(player.getServer()).getWorld(World.END),
                                    Double.parseDouble(pos.split(" ")[1]),
                                    Double.parseDouble(pos.split(" ")[2]),
                                    Double.parseDouble(pos.split(" ")[3]),
                                    0,
                                    0
                            );
                        }
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("backTp"),
                                "cyan.message.backTp",
                                CyanMidnightConfig.errorToActionBar,
                                CyanMidnightConfig.useCustomTranslations
                        );
                    }
                    else
                    {
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "noLastPos"),
                                "cyan.message.noLastPos",
                                CyanMidnightConfig.errorToActionBar,
                                CyanMidnightConfig.useCustomTranslations
                        );
                    }
                } catch (IOException e)
                {
                    throw new RuntimeException(e);
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
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (CyanLibUtils.isPlayer(source))
        {
            if (CyanLibUtils.isOptionAllowed(player, CyanMidnightConfig.allowBed, "bedDisabled"))
            {
                if (player.getSpawnPointPosition() != null)
                {
                    double x = player.getSpawnPointPosition().getX();
                    double y = player.getSpawnPointPosition().getY();
                    double z = player.getSpawnPointPosition().getZ();
                    float yaw = player.getYaw();
                    float pitch = player.getPitch();

                    if (player.getSpawnPointDimension() == World.OVERWORLD)
                    {
                        player.teleport(Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD), x, y, z, yaw, pitch);
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("bed"),
                                "cyan.message.bed",
                                CyanMidnightConfig.msgToActionBar,
                                CyanMidnightConfig.useCustomTranslations
                        );
                    }
                    else if (player.getSpawnPointDimension() == World.NETHER)
                    {
                        player.teleport(Objects.requireNonNull(player.getServer()).getWorld(World.NETHER), x, y, z, yaw, pitch);
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("respawnAnchor"),
                                "cyan.message.respawnAnchor",
                                CyanMidnightConfig.msgToActionBar,
                                CyanMidnightConfig.useCustomTranslations
                        );
                    }
                }
                else
                {
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "bedNotFound"),
                            "cyan.message.bedNotFound",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useCustomTranslations
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
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (CyanLibUtils.isPlayer(source))
        {
            if (CyanLibUtils.isOptionAllowed(player, CyanMidnightConfig.allowSurface, "surfaceDisabled"))
            {
                int x = player.getBlockPos().getX();
                int z = player.getBlockPos().getZ();
                int y = player.world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);

                player.teleport(context.getSource().getWorld(), x, y, z, player.getYaw(), player.getPitch());
                sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation("surface"),
                        "cyan.message.surface",
                        CyanMidnightConfig.msgToActionBar,
                        CyanMidnightConfig.useCustomTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
