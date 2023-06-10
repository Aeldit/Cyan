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
import fr.aeldit.cyan.util.BackTp;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

import static fr.aeldit.cyan.util.GsonUtils.BACK_TP_PATH;
import static fr.aeldit.cyan.util.GsonUtils.readBackTpFile;
import static fr.aeldit.cyan.util.Utils.*;
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
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (CyanLibUtils.isPlayer(context.getSource()))
        {
            if (CyanLibUtils.isOptionAllowed(player, CyanMidnightConfig.allowBackTp, "backTpDisabled"))
            {
                try
                {
                    if (Files.exists(BACK_TP_PATH) || (Files.exists(BACK_TP_PATH) && !Files.readAllLines(BACK_TP_PATH).isEmpty()))
                    {
                        ArrayList<BackTp> backTps = readBackTpFile();

                        if (backTpExists(backTps, player.getUuidAsString()))
                        {
                            BackTp backTp = backTps.get(getBackTpIndex(backTps, player.getUuidAsString()));

                            switch (backTp.dimension())
                            {
                                case "overworld" ->
                                        player.teleport(player.getServer().getWorld(World.OVERWORLD), backTp.x(), backTp.y(), backTp.z(), 0, 0);
                                case "nether" ->
                                        player.teleport(player.getServer().getWorld(World.NETHER), backTp.x(), backTp.y(), backTp.z(), 0, 0);
                                case "end" ->
                                        player.teleport(player.getServer().getWorld(World.END), backTp.x(), backTp.y(), backTp.z(), 0, 0);
                            }

                            CyanLibUtils.sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation("backTp"),
                                    "cyan.message.backTp"
                            );
                        }
                    }
                    else
                    {
                        CyanLibUtils.sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "noLastPos"),
                                "cyan.message.noLastPos"
                        );
                    }
                }
                catch (IOException e)
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
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (CyanLibUtils.isPlayer(context.getSource()))
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
                        CyanLibUtils.sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("bed"),
                                "cyan.message.bed"
                        );
                    }
                    else if (player.getSpawnPointDimension() == World.NETHER)
                    {
                        player.teleport(Objects.requireNonNull(player.getServer()).getWorld(World.NETHER), x, y, z, yaw, pitch);
                        CyanLibUtils.sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("respawnAnchor"),
                                "cyan.message.respawnAnchor"
                        );
                    }
                }
                else
                {
                    CyanLibUtils.sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "bedNotFound"),
                            "cyan.message.bedNotFound"
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

        if (CyanLibUtils.isPlayer(context.getSource()))
        {
            if (CyanLibUtils.isOptionAllowed(player, CyanMidnightConfig.allowSurface, "surfaceDisabled"))
            {
                int x = player.getBlockPos().getX();
                int z = player.getBlockPos().getZ();
                int y = player.world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);

                player.teleport(context.getSource().getWorld(), x, y, z, player.getYaw(), player.getPitch());
                CyanLibUtils.sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation("surface"),
                        "cyan.message.surface"
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
