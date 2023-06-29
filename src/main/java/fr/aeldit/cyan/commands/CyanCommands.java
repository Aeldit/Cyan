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
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.commands.argumentTypes.ArgumentSuggestion;
import fr.aeldit.cyanlib.lib.CyanLibCommands;
import fr.aeldit.cyanlib.lib.CyanLibLanguageUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;

import static fr.aeldit.cyan.util.GsonUtils.transferPropertiesToGson;
import static fr.aeldit.cyan.util.Utils.*;

public class CyanCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("cyan")
                .then(CommandManager.literal("config")
                        .then(CommandManager.argument("optionName", StringArgumentType.string())
                                .suggests((context, builder) -> ArgumentSuggestion.getOptions(builder))
                                .then(CommandManager.literal("set")
                                        .then(CommandManager.argument("booleanValue", BoolArgumentType.bool())
                                                .then(CommandManager.argument("mode", BoolArgumentType.bool())
                                                        .executes(CyanCommands::setBoolOption)
                                                )
                                                .executes(CyanCommands::setBoolOptionFromCommand)
                                        )
                                        .then(CommandManager.argument("integerValue", IntegerArgumentType.integer())
                                                .suggests((context, builder) -> ArgumentSuggestion.getInts(builder))
                                                .then(CommandManager.argument("mode", BoolArgumentType.bool())
                                                        .executes(CyanCommands::setIntOption)
                                                )
                                                .executes(CyanCommands::setIntOptionFromCommand)
                                        )
                                )
                                .executes(CyanCommands::getOptionChatConfig)
                        )
                )
                .then(CommandManager.literal("getConfig")
                        .executes(CyanCommands::getConfigOptions)
                )
                .then(CommandManager.literal("reloadTranslations")
                        .executes(CyanCommands::reloadTranslations)
                )
                .then(CommandManager.literal("removePropertiesFiles")
                        .executes(CyanCommands::removePropertiesFiles)
                )
        );
    }

    /**
     * Called by the command {@code /cyan reloadTranslations}
     * <p>
     * Reloads the custom translations
     */
    public static int reloadTranslations(@NotNull CommandContext<ServerCommandSource> context)
    {
        CyanLibCommands.reloadTranslations(context, getDefaultTranslations(), LibUtils);

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan removePropertiesFiles}
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
            if (Files.exists(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/back.properties")))
            {
                Files.delete(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/back.properties"));
                fileDeleted = true;
            }

            if (Files.exists(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/locations.properties")))
            {
                Files.delete(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/locations.properties"));
                fileDeleted = true;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        if (fileDeleted)
        {
            CyanLibLanguageUtils.sendPlayerMessage(player,
                    LanguageUtils.getTranslation("propertiesFilesDeleted"),
                    "cyan.message.propertiesFilesDeleted"
            );
        }
        else
        {
            CyanLibLanguageUtils.sendPlayerMessage(player,
                    LanguageUtils.getTranslation("noPropertiesFiles"),
                    "cyan.message.noPropertiesFiles"
            );
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan <optionName> set [booleanValue] [mode]}
     * <p>
     * Changes the option in the {@link fr.aeldit.cyanlib.lib.CyanLibConfig} class to the value [booleanValue] and executes the
     * {@code /cyan getConfig} command if {@code [mode]} is true, and the command {@code /cyan config <optionName>} otherwise.
     * This allows to see the changed option in the chat
     */
    public static int setBoolOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        CyanLibCommands.setBoolOption(context, defaultTranslations, LibUtils,
                StringArgumentType.getString(context, "optionName"), BoolArgumentType.getBool(context, "booleanValue"),
                false, BoolArgumentType.getBool(context, "mode")
        );
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan <optionName> set [boolValue]}
     * <p>
     * Changes the option in the {@link fr.aeldit.cyanlib.lib.CyanLibConfig} class to the value [boolValue]
     */
    public static int setBoolOptionFromCommand(@NotNull CommandContext<ServerCommandSource> context)
    {
        CyanLibCommands.setBoolOption(context, defaultTranslations, LibUtils,
                StringArgumentType.getString(context, "optionName"), BoolArgumentType.getBool(context, "booleanValue"),
                true, false
        );
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan <optionName> set [intValue] [mode]}
     * <p>
     * Changes the option in the {@link fr.aeldit.cyanlib.lib.CyanLibConfig} class to the value [intValue] and executes the
     * {@code /cyan getConfig} command if {@code [mode]} is true, and the command {@code /cyan config <optionName>} otherwise.
     * This allows to see the changed option in the chat
     */
    public static int setIntOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        CyanLibCommands.setIntOption(context, LibUtils,
                StringArgumentType.getString(context, "optionName"), IntegerArgumentType.getInteger(context, "integerValue"),
                false, BoolArgumentType.getBool(context, "mode")
        );
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan <optionName> set [intValue]}
     * <p>
     * Changes the option in the {@link fr.aeldit.cyanlib.lib.CyanLibConfig} class to the value [intValue]
     */
    public static int setIntOptionFromCommand(@NotNull CommandContext<ServerCommandSource> context)
    {
        CyanLibCommands.setIntOption(context, LibUtils,
                StringArgumentType.getString(context, "optionName"), IntegerArgumentType.getInteger(context, "integerValue"),
                true, false
        );
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan <optionName>}
     * <p>
     * Displays the option description and its current values in the player's chat
     */
    public static int getOptionChatConfig(@NotNull CommandContext<ServerCommandSource> context)
    {
        CyanLibCommands.getOptionChatConfig(context, LibUtils, StringArgumentType.getString(context, "optionName"));

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan getConfig}
     * <p>
     * Send a player in the player's chat with all the mod's options and their values
     */
    public static int getConfigOptions(@NotNull CommandContext<ServerCommandSource> context)
    {
        CyanLibCommands.getConfigOptions(context, LibUtils);

        return Command.SINGLE_SUCCESS;
    }
}
