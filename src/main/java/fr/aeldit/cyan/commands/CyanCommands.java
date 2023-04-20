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

import static fr.aeldit.cyan.util.Utils.CyanLanguageUtils;
import static fr.aeldit.cyanlib.util.ChatUtils.sendPlayerMessage;
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
                                                .executes(CyanCommands::setBoolOption)
                                        )
                                        .then(CommandManager.argument("integerValue", IntegerArgumentType.integer())
                                                .suggests((context, builder) -> ArgumentSuggestion.getInts(builder))
                                                .executes(CyanCommands::setIntOption)
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
        );
    }

    public static int reloadTranslations(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null && !CyanMidnightConfig.allowConsoleEditConfig)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            CyanLanguageUtils.loadLanguage(Utils.getDefaultTranslations(true));
            if (player == null)
            {
                source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation("translationsReloaded")));
            } else
            {
                sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation("translationsReloaded"),
                        "cyan.message.translationsReloaded",
                        CyanMidnightConfig.msgToActionBar,
                        CyanMidnightConfig.useCustomTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    // Set functions

    public static int setBoolOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null && !CyanMidnightConfig.allowConsoleEditConfig)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            if (player == null)
            {
                String option = StringArgumentType.getString(context, "optionName");
                boolean value = BoolArgumentType.getBool(context, "booleanValue");

                if (Utils.getOptionsList().get("booleans").contains(option))
                {
                    CyanMidnightConfig.setBoolOption(option, value);
                    source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(SET + option)));
                } else
                {
                    source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "optionNotFound")));
                }
            } else
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditConfig))
                {
                    String option = StringArgumentType.getString(context, "optionName");
                    boolean value = BoolArgumentType.getBool(context, "booleanValue");

                    if (Utils.getOptionsList().get("booleans").contains(option))
                    {
                        CyanMidnightConfig.setBoolOption(option, value);

                        source.getServer().getCommandManager().executeWithPrefix(source, "/cyan config %s".formatted(option));
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(SET + option),
                                "cyan.message.set.%s".formatted(option),
                                CyanMidnightConfig.msgToActionBar,
                                CyanMidnightConfig.useCustomTranslations,
                                value ? Formatting.GREEN + "ON" : Formatting.RED + "OFF"
                        );
                    } else
                    {
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "optionNotFound"),
                                "cyan.message.error.optionNotFound",
                                CyanMidnightConfig.msgToActionBar,
                                CyanMidnightConfig.useCustomTranslations
                        );
                    }
                } else
                {
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "notOp"),
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useCustomTranslations
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int setIntOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null && !CyanMidnightConfig.allowConsoleEditConfig)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            if (player == null)
            {
                String option = StringArgumentType.getString(context, "optionName");
                int value = IntegerArgumentType.getInteger(context, "integerValue");

                if (Utils.getOptionsList().get("integers").contains(option))
                {
                    CyanMidnightConfig.setIntOption(option, value);
                    source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(SET + option)));
                } else
                {
                    source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "optionNotFound")));
                }
            } else
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditConfig))
                {
                    String option = StringArgumentType.getString(context, "optionName");
                    int value = IntegerArgumentType.getInteger(context, "integerValue");

                    if (Utils.getOptionsList().get("integers").contains(option))
                    {
                        CyanMidnightConfig.setIntOption(option, value);

                        source.getServer().getCommandManager().executeWithPrefix(source, "/cyan config %s".formatted(option));
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(SET + option),
                                "cyan.message.set.%s".formatted(option),
                                CyanMidnightConfig.msgToActionBar,
                                CyanMidnightConfig.useCustomTranslations,
                                Formatting.GOLD + String.valueOf(value)
                        );
                    } else
                    {
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "optionNotFound"),
                                "cyan.message.error.optionNotFound",
                                CyanMidnightConfig.msgToActionBar,
                                CyanMidnightConfig.useCustomTranslations
                        );
                    }
                } else
                {
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "notOp"),
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useCustomTranslations
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    // Get functions
    public static int getOptionChatConfig(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditConfig))
            {
                String optionName = StringArgumentType.getString(context, "optionName");
                if (Utils.getOptionsList().get("booleans").contains(optionName) || Utils.getOptionsList().get("integers").contains(optionName))
                {
                    Object key = CyanMidnightConfig.getAllOptionsMap().get(optionName);

                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation("dashSeparation"),
                            "cyan.message.getDescription.dashSeparation",
                            false,
                            CyanMidnightConfig.useCustomTranslations
                    );

                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(DESC + optionName),
                            "cyan.message.getDescription.%s".formatted(optionName),
                            false,
                            CyanMidnightConfig.useCustomTranslations
                    );

                    if (key instanceof Boolean currentValue)
                    {
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("currentValue"),
                                "cyan.message.currentValue",
                                false,
                                CyanMidnightConfig.useCustomTranslations,
                                currentValue ? Text.literal(Formatting.GREEN + "ON (click to change)").
                                        setStyle(Style.EMPTY.withClickEvent(
                                                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set false".formatted(optionName)))
                                        ) : Text.literal(Formatting.RED + "OFF (click to change)").
                                        setStyle(Style.EMPTY.withClickEvent(
                                                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set true".formatted(optionName)))
                                        )
                        );
                    } else if (key instanceof Integer currentValue)
                    {
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("currentValue"),
                                "cyan.message.currentValue",
                                false,
                                CyanMidnightConfig.useCustomTranslations,
                                Formatting.GOLD + String.valueOf(currentValue)
                        );
                        if (optionName.startsWith("minOpLevelExe"))
                        {
                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation("setValue"),
                                    "cyan.message.setValue",
                                    false,
                                    CyanMidnightConfig.useCustomTranslations,
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
                        } else
                        {
                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation("setValue"),
                                    "cyan.message.setValue",
                                    false,
                                    CyanMidnightConfig.useCustomTranslations,
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
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation("dashSeparation"),
                            "cyan.message.getDescription.dashSeparation",
                            false,
                            CyanMidnightConfig.useCustomTranslations
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
        } else
        {
            if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditConfig))
            {
                sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation("dashSeparation"),
                        "cyan.message.getDescription.dashSeparation",
                        false,
                        CyanMidnightConfig.useCustomTranslations
                );
                sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation(GETCFG + "header"),
                        "cyan.message.getCfg.header",
                        false,
                        CyanMidnightConfig.useCustomTranslations
                );

                for (Map.Entry<String, Object> entry : CyanMidnightConfig.getAllOptionsMap().entrySet())
                {
                    Object key2 = entry.getKey();
                    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
                    {
                        currentTrad = CyanLanguageUtils.getTranslation(GETCFG + key2);
                    }

                    if (entry.getValue() instanceof Boolean value)
                    {
                        sendPlayerMessage(player,
                                currentTrad,
                                "cyan.message.getCfg.%s".formatted(key2),
                                false,
                                CyanMidnightConfig.useCustomTranslations,
                                value ? Text.literal(Formatting.GREEN + "ON").
                                        setStyle(Style.EMPTY.withClickEvent(
                                                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set false".formatted(key2)))
                                        ) : Text.literal(Formatting.RED + "OFF").
                                        setStyle(Style.EMPTY.withClickEvent(
                                                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cyan config %s set true".formatted(key2)))
                                        )
                        );
                    } else if (entry.getValue() instanceof Integer value)
                    {
                        sendPlayerMessage(player,
                                currentTrad,
                                "cyan.message.getCfg.%s".formatted(key2),
                                false,
                                CyanMidnightConfig.useCustomTranslations,
                                Formatting.GOLD + Integer.toString(value)
                        );
                    }
                }
                sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation("dashSeparation"),
                        "cyan.message.getDescription.dashSeparation",
                        false,
                        CyanMidnightConfig.useCustomTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
