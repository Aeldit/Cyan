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
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.config.CyanConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static fr.aeldit.cyan.config.CyanConfig.*;
import static fr.aeldit.cyan.teleportation.BackTps.BACK_TP_PATH;
import static fr.aeldit.cyan.teleportation.Locations.LOCATIONS_PATH;
import static fr.aeldit.cyan.util.GsonUtils.transferPropertiesToGson;
import static fr.aeldit.cyan.util.Utils.CYAN_LANGUAGE_UTILS;
import static fr.aeldit.cyan.util.Utils.CYAN_LIB_UTILS;
import static fr.aeldit.cyanlib.lib.utils.TranslationsPrefixes.ERROR;

public class MiscellaneousCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("kill-ground-items")
                .then(CommandManager.argument("radius_in_chunks", IntegerArgumentType.integer())
                        .executes(MiscellaneousCommands::kgir)
                )
                .executes(MiscellaneousCommands::kgi)
        );
        dispatcher.register(CommandManager.literal("kgi")
                .then(CommandManager.argument("radius_in_chunks", IntegerArgumentType.integer())
                        .executes(MiscellaneousCommands::kgir)
                )
                .executes(MiscellaneousCommands::kgi)
        );

        dispatcher.register(CommandManager.literal("remove-properties-files")
                .executes(MiscellaneousCommands::removePropertiesFiles)
        );
    }

    /**
     * Called when a player execute the command {@code /kill-ground-items} or {@code /kgi}
     * <p>
     * Kills all the items on the ground in the default radius (defined if {@link CyanConfig})
     */
    public static int kgi(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (CYAN_LIB_UTILS.isPlayer(source))
        {
            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_KGI.getValue(), "kgiDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_KGI.getValue()))
                {
                    source.getServer().getCommandManager().executeWithPrefix(source, "/kill @e[type=minecraft:item,distance=..%d]"
                            .formatted(DISTANCE_TO_ENTITIES_KGI.getValue() * 16));
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                            CYAN_LANGUAGE_UTILS.getTranslation("kgi"),
                            "cyan.msg.kgi"
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the command {@code /kill-ground-items [int]} or {@code /kgi [int]}
     * <p>
     * Kills all the items on the ground in the specified radius
     */
    public static int kgir(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (CYAN_LIB_UTILS.isPlayer(source))
        {
            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_KGI.getValue(), "kgiDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_KGI.getValue()))
                {
                    int arg = IntegerArgumentType.getInteger(context, "radius_in_chunks");
                    source.getServer().getCommandManager().executeWithPrefix(source, "/kill @e[type=item,distance=..%d]".formatted(arg * 16));
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                            CYAN_LANGUAGE_UTILS.getTranslation("kgir").formatted(Formatting.GOLD + Integer.toString(arg)),
                            "cyan.msg.kgir",
                            Formatting.GOLD + Integer.toString(arg)
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }


    /**
     * Called by the command {@code /remove-properties-files}
     * <p>
     * Removes all the properties files
     */
    public static int removePropertiesFiles(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        transferPropertiesToGson();
        boolean fileDeleted = false;

        try
        {
            Path path = Path.of(LOCATIONS_PATH.toString().replace("json", "properties"));

            if (Files.exists(path))
            {
                Files.delete(path);
                fileDeleted = true;
            }

            path = Path.of(BACK_TP_PATH.toString().replace("json", "properties"));

            if (Files.exists(path))
            {
                Files.delete(path);
                fileDeleted = true;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        if (fileDeleted)
        {
            CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                    CYAN_LANGUAGE_UTILS.getTranslation("propertiesFilesDeleted"),
                    "cyan.msg.propertiesFilesDeleted"
            );
        }
        else
        {
            CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                    CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "noPropertiesFiles"),
                    "cyan.msg.noPropertiesFiles"
            );
        }
        return Command.SINGLE_SUCCESS;
    }
}
