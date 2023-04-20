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
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import static fr.aeldit.cyan.util.Utils.*;
import static fr.aeldit.cyanlib.util.ChatUtils.sendPlayerMessage;

public class MiscellaneousCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("killgrounditems")
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
    }

    /**
     * Called when a player execute the command {@code /killgrounditems} or {@code /kgi}
     * <p>
     * Kills all the items on the ground in the default radius (defined if {@link CyanMidnightConfig})
     */
    public static int kgi(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (isPlayer(source))
        {
            if (isOptionAllowed(player, CyanMidnightConfig.allowKgi, "kgiDisabled"))
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
                {
                    source.getServer().getCommandManager().executeWithPrefix(source, "/kill @e[type=minecraft:item,distance=..%d]".formatted(CyanMidnightConfig.distanceToEntitiesKgi * 16));
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation("kgi"),
                            "cyan.message.kgi",
                            CyanMidnightConfig.msgToActionBar,
                            CyanMidnightConfig.useCustomTranslations
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the command {@code /killgrounditems [int]} or {@code /kgi [int]}
     * <p>
     * Kills all the items on the ground in the specified radius
     */
    public static int kgir(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (isPlayer(source))
        {
            if (isOptionAllowed(player, CyanMidnightConfig.allowKgi, "kgiDisabled"))
            {
                if (hasPermission(player, CyanMidnightConfig.minOpLevelExeKgi))
                {
                    int arg = IntegerArgumentType.getInteger(context, "radius_in_chunks");
                    source.getServer().getCommandManager().executeWithPrefix(source, "/kill @e[type=item,distance=..%d]".formatted(arg * 16));
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation("kgir").formatted(Formatting.GOLD + Integer.toString(arg)),
                            "cyan.message.kgir",
                            CyanMidnightConfig.msgToActionBar,
                            CyanMidnightConfig.useCustomTranslations,
                            Formatting.GOLD + Integer.toString(arg)
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
