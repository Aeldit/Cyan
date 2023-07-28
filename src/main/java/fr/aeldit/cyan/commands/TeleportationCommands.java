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
import fr.aeldit.cyan.config.Config;
import fr.aeldit.cyan.teleportation.BackTps;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
            if (CYAN_LIB_UTILS.isOptionAllowed(player, Config.ALLOW_BACK_TP.getValue(), "backTpDisabled"))
            {
                if (BACK_TPS.backTpExists(player.getUuidAsString()))
                {
                    BackTps.BackTp backTp = BACK_TPS.getBackTp(player.getUuidAsString());

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
            if (CYAN_LIB_UTILS.isOptionAllowed(player, Config.ALLOW_BED.getValue(), "bedDisabled"))
            {
                if (player.getSpawnPointPosition() != null)
                {
                    if (player.getSpawnPointDimension() == World.OVERWORLD)
                    {
                        player.teleport(Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD),
                                player.getSpawnPointPosition().getX(),
                                player.getSpawnPointPosition().getY(),
                                player.getSpawnPointPosition().getZ(),
                                player.getYaw(), player.getPitch()
                        );
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                                CYAN_LANGUAGE_UTILS.getTranslation("bed"),
                                "cyan.msg.bed"
                        );
                    }
                    else if (player.getSpawnPointDimension() == World.NETHER)
                    {
                        player.teleport(Objects.requireNonNull(player.getServer()).getWorld(World.NETHER),
                                player.getSpawnPointPosition().getX(),
                                player.getSpawnPointPosition().getY(),
                                player.getSpawnPointPosition().getZ(),
                                player.getYaw(), player.getPitch()
                        );
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                                CYAN_LANGUAGE_UTILS.getTranslation("respawnAnchor"),
                                "cyan.msg.respawnAnchor"
                        );
                    }
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
            if (CYAN_LIB_UTILS.isOptionAllowed(player, Config.ALLOW_SURFACE.getValue(), "surfaceDisabled"))
            {
                player.teleport(context.getSource().getWorld(),
                        player.getBlockPos().getX(),
                        player.getWorld().getTopY(Heightmap.Type.WORLD_SURFACE, player.getBlockPos().getX(), player.getBlockPos().getZ()),
                        player.getBlockPos().getZ(),
                        player.getYaw(), player.getPitch()
                );
                CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                        CYAN_LANGUAGE_UTILS.getTranslation("surface"),
                        "cyan.msg.surface"
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
