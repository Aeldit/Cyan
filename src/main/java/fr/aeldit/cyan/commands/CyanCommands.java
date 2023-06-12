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
import fr.aeldit.cyan.config.CyanMidnightConfig;
import fr.aeldit.cyan.util.GsonUtils;
import fr.aeldit.cyan.util.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static fr.aeldit.cyan.util.GsonUtils.transferPropertiesToGson;
import static fr.aeldit.cyan.util.Utils.CyanLanguageUtils;
import static fr.aeldit.cyan.util.Utils.CyanLibUtils;
import static fr.aeldit.cyanlib.util.Constants.*;

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
                                                .then(CommandManager.argument("redisplayGetConfig", BoolArgumentType.bool())
                                                        .executes(CyanCommands::setBoolOption)
                                                )
                                                .executes(CyanCommands::setBoolOptionFromCommand)
                                        )
                                        .then(CommandManager.argument("integerValue", IntegerArgumentType.integer())
                                                .suggests((context, builder) -> ArgumentSuggestion.getInts(builder))
                                                .then(CommandManager.argument("redisplayGetConfig", BoolArgumentType.bool())
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
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        }
        else
        {
            CyanLanguageUtils.loadLanguage(Utils.getDefaultTranslations(true));
            CyanLibUtils.sendPlayerMessage(player,
                    CyanLanguageUtils.getTranslation("translationsReloaded"),
                    "cyan.message.translationsReloaded"
            );
        }
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

        if (CyanLibUtils.hasPermission(player, CyanMidnightConfig.minOpLevelExeEditConfig))
        {
            GsonUtils.removePropertiesFiles(player);
        }
        return Command.SINGLE_SUCCESS;
    }

    // Set functions

    /**
     * Called by the command {@code /cyan <optionName> set [boolValue] [redisplayGetConfig]}
     * <p>
     * Changes the option in the {@link CyanMidnightConfig} class to the value [boolValue] and executes the
     * * {@code /cyan getConfig} command to see the changed option in the chat
     */
    public static int setBoolOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        }
        else
        {
            if (CyanLibUtils.hasPermission(player, CyanMidnightConfig.minOpLevelExeEditConfig))
            {
                String option = StringArgumentType.getString(context, "optionName");
                boolean value = BoolArgumentType.getBool(context, "booleanValue");

                if (Utils.getOptionsList().get("booleans").contains(option))
                {
                    CyanMidnightConfig.setBoolOption(option, value);
                    source.getServer().getCommandManager().executeWithPrefix(source, "/cyan getConfig");
                }
                else
                {
                    CyanLibUtils.sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "optionNotFound"),
                            "cyan.message.error.optionNotFound"
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan <optionName> set [boolValue]}
     * <p>
     * Changes the option in the {@link CyanMidnightConfig} class to the value [boolValue]
     */
    public static int setBoolOptionFromCommand(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player == null)
        {
            context.getSource().getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        }
        else
        {
            if (CyanLibUtils.hasPermission(player, CyanMidnightConfig.minOpLevelExeEditConfig))
            {
                String option = StringArgumentType.getString(context, "optionName");
                boolean value = BoolArgumentType.getBool(context, "booleanValue");

                if (Utils.getOptionsList().get("booleans").contains(option))
                {
                    CyanMidnightConfig.setBoolOption(option, value);
                    CyanLibUtils.sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(SET + option),
                            "cyan.message.set.%s".formatted(option),
                            value ? Formatting.GREEN + "ON" : Formatting.RED + "OFF"
                    );
                }
                else
                {
                    CyanLibUtils.sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "optionNotFound"),
                            "cyan.message.error.optionNotFound"
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan <optionName> set [intValue] [redisplayGetConfig]}
     * <p>
     * Changes the option in the {@link CyanMidnightConfig} class to the value [intValue] and executes the
     * {@code /cyan getConfig} command to see the changed option in the chat
     */
    public static int setIntOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        }
        else
        {
            if (CyanLibUtils.hasPermission(player, CyanMidnightConfig.minOpLevelExeEditConfig))
            {
                String option = StringArgumentType.getString(context, "optionName");
                int value = IntegerArgumentType.getInteger(context, "integerValue");

                if (Utils.getOptionsList().get("integers").contains(option))
                {
                    if (option.equals("distanceToEntitiesKgi") && (value < 1 || value > 128))
                    {
                        CyanLibUtils.sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "wrongDistanceKgi"),
                                "cyan.message.wrongDistanceKgi"
                        );
                    }
                    else if (option.equals("daysToRemoveBackTp") && (value < 1))
                    {
                        CyanLibUtils.sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "daysMustBePositive"),
                                "cyan.message.daysMustBePositive"
                        );
                    }
                    else if (option.contains("minOpLevelExe") && (value < 0 || value > 4))
                    {
                        CyanLibUtils.sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "wrongOPLevel"),
                                "cyan.message.wrongDistanceKgi"
                        );
                    }
                    else
                    {
                        CyanMidnightConfig.setIntOption(option, value);
                        source.getServer().getCommandManager().executeWithPrefix(source, "/cyan getConfig");
                    }
                }
                else
                {
                    CyanLibUtils.sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "optionNotFound"),
                            "cyan.message.error.optionNotFound"
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan <optionName> set [intValue]}
     * <p>
     * Changes the option in the {@link CyanMidnightConfig} class to the value [intValue]
     */
    public static int setIntOptionFromCommand(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player == null)
        {
            context.getSource().getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        }
        else
        {
            if (CyanLibUtils.hasPermission(player, CyanMidnightConfig.minOpLevelExeEditConfig))
            {
                String option = StringArgumentType.getString(context, "optionName");
                int value = IntegerArgumentType.getInteger(context, "integerValue");

                if (Utils.getOptionsList().get("integers").contains(option))
                {
                    if (option.equals("distanceToEntitiesKgi") && (value < 1 || value > 128))
                    {
                        CyanLibUtils.sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "wrongDistanceKgi"),
                                "cyan.message.wrongDistanceKgi"
                        );
                    }
                    else if (option.equals("daysToRemoveBackTp") && (value < 1))
                    {
                        CyanLibUtils.sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "daysMustBePositive"),
                                "cyan.message.daysMustBePositive"
                        );
                    }
                    else if (option.contains("minOpLevelExe") && (value < 0 || value > 4))
                    {
                        CyanLibUtils.sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "wrongOPLevel"),
                                "cyan.message.wrongDistanceKgi"
                        );
                    }
                    else
                    {
                        CyanMidnightConfig.setIntOption(option, value);
                        CyanLibUtils.sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(SET + option),
                                "cyan.message.set.%s".formatted(option),
                                Formatting.GOLD + String.valueOf(value)
                        );
                    }
                }
                else
                {
                    CyanLibUtils.sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "optionNotFound"),
                            "cyan.message.error.optionNotFound"
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    // Get functions

    /**
     * Called by the command {@code /cyan <optionName>}
     * <p>
     * Displays the option description and its current values in the player's chat
     */
    public static int getOptionChatConfig(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        }
        else
        {
            if (CyanLibUtils.hasPermission(player, CyanMidnightConfig.minOpLevelExeEditConfig))
            {
                String optionName = StringArgumentType.getString(context, "optionName");

                if (Utils.getOptionsList().get("booleans").contains(optionName) || Utils.getOptionsList().get("integers").contains(optionName))
                {
                    Object key = CyanMidnightConfig.getAllOptionsMap().get(optionName);

                    CyanLibUtils.sendPlayerMessageActionBar(player,
                            CyanLanguageUtils.getTranslation("dashSeparation"),
                            "cyan.message.getDescription.dashSeparation",
                            false
                    );
                    CyanLibUtils.sendPlayerMessageActionBar(player,
                            CyanLanguageUtils.getTranslation(DESC + optionName),
                            "cyan.message.getDescription.options.%s".formatted(optionName),
                            false
                    );

                    if (key instanceof Boolean currentValue)
                    {
                        CyanLibUtils.sendPlayerMessageActionBar(player,
                                CyanLanguageUtils.getTranslation("currentValue"),
                                "cyan.message.currentValue",
                                false,
                                currentValue ? Text.literal(Formatting.GREEN + "ON (click to change)").
                                        setStyle(Style.EMPTY.withClickEvent(
                                                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set false".formatted(optionName)))
                                        ) : Text.literal(Formatting.RED + "OFF (click to change)").
                                        setStyle(Style.EMPTY.withClickEvent(
                                                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set true".formatted(optionName)))
                                        )
                        );
                    }
                    else if (key instanceof Integer currentValue)
                    {
                        CyanLibUtils.sendPlayerMessageActionBar(player,
                                CyanLanguageUtils.getTranslation("currentValue"),
                                "cyan.message.currentValue",
                                false,
                                Formatting.GOLD + String.valueOf(currentValue)
                        );

                        if (optionName.startsWith("minOpLevelExe"))
                        {
                            CyanLibUtils.sendPlayerMessageActionBar(player,
                                    CyanLanguageUtils.getTranslation("setValue"),
                                    "cyan.message.setValue",
                                    false,
                                    Text.literal(Formatting.DARK_GREEN + (Formatting.BOLD + "0")).
                                            setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set 0".formatted(optionName)))
                                            ),
                                    Text.literal(Formatting.DARK_GREEN + (Formatting.BOLD + "1")).
                                            setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set 1".formatted(optionName)))
                                            ),
                                    Text.literal(Formatting.DARK_GREEN + (Formatting.BOLD + "2")).
                                            setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set 2".formatted(optionName)))
                                            ),
                                    Text.literal(Formatting.DARK_GREEN + (Formatting.BOLD + "3")).
                                            setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set 3".formatted(optionName)))
                                            ),
                                    Text.literal(Formatting.DARK_GREEN + (Formatting.BOLD + "4")).
                                            setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set 4".formatted(optionName)))
                                            )
                            );
                        }
                        else
                        {
                            CyanLibUtils.sendPlayerMessageActionBar(player,
                                    CyanLanguageUtils.getTranslation("setValue"),
                                    "cyan.message.setValue",
                                    false,
                                    Text.literal(Formatting.DARK_GREEN + (Formatting.BOLD + "8")).
                                            setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set 8".formatted(optionName)))
                                            ),
                                    Text.literal(Formatting.DARK_GREEN + (Formatting.BOLD + "16")).
                                            setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set 16".formatted(optionName)))
                                            ),
                                    Text.literal(Formatting.DARK_GREEN + (Formatting.BOLD + "32")).
                                            setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set 32".formatted(optionName)))
                                            ),
                                    Text.literal(Formatting.DARK_GREEN + (Formatting.BOLD + "64")).
                                            setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set 64".formatted(optionName)))
                                            ),
                                    Text.literal(Formatting.DARK_GREEN + (Formatting.BOLD + "128")).
                                            setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set 128".formatted(optionName)))
                                            )
                            );
                        }
                    }

                    CyanLibUtils.sendPlayerMessageActionBar(player,
                            CyanLanguageUtils.getTranslation("dashSeparation"),
                            "cyan.message.getDescription.dashSeparation",
                            false
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /cyan getConfig}
     * <p>
     * Send a player in the player's chat with all the mod's options and their values
     */
    public static int getConfigOptions(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        String currentTrad = null;

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        }
        else
        {
            if (CyanLibUtils.hasPermission(player, CyanMidnightConfig.minOpLevelExeEditConfig))
            {
                CyanLibUtils.sendPlayerMessageActionBar(player,
                        CyanLanguageUtils.getTranslation("dashSeparation"),
                        "cyan.message.getDescription.dashSeparation",
                        false
                );
                CyanLibUtils.sendPlayerMessageActionBar(player,
                        CyanLanguageUtils.getTranslation(GETCFG + "header"),
                        "cyan.message.getCfg.header",
                        false
                );

                for (Map.Entry<String, Object> entry : CyanMidnightConfig.getAllOptionsMap().entrySet())
                {
                    Object key = entry.getKey();

                    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
                    {
                        currentTrad = CyanLanguageUtils.getTranslation(GETCFG + key);
                    }

                    if (entry.getValue() instanceof Boolean value)
                    {
                        CyanLibUtils.sendPlayerMessageActionBar(player,
                                currentTrad,
                                "cyan.message.getCfg.%s".formatted(key),
                                false,
                                value ? Text.literal(Formatting.GREEN + "ON").
                                        setStyle(Style.EMPTY.withClickEvent(
                                                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set false true".formatted(key)))
                                        ) : Text.literal(Formatting.RED + "OFF").
                                        setStyle(Style.EMPTY.withClickEvent(
                                                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set true true".formatted(key)))
                                        )
                        );
                    }
                    else if (entry.getValue() instanceof Integer value)
                    {
                        CyanLibUtils.sendPlayerMessageActionBar(player,
                                currentTrad,
                                "cyan.message.getCfg.%s".formatted(key),
                                false,
                                Formatting.GOLD + Integer.toString(value)
                        );
                    }
                }

                CyanLibUtils.sendPlayerMessageActionBar(player,
                        CyanLanguageUtils.getTranslation("dashSeparation"),
                        "cyan.message.getDescription.dashSeparation",
                        false
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
