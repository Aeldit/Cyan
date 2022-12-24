package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.commands.argumentTypes.ArgumentSuggestion;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import fr.aeldit.cyan.util.ChatConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static fr.aeldit.cyan.util.ChatConstants.*;
import static fr.aeldit.cyanlib.util.ChatUtil.sendPlayerMessage;

public class CyanCommandsV2
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("cyan")
                .then(CommandManager.literal("getConfig")
                        .executes(CyanCommandsV2::getConfigOptions)
                )
                .then(CommandManager.literal("config")
                        .then(CommandManager.literal("booleanOption")
                                .then(CommandManager.argument("option", StringArgumentType.string())
                                        .suggests((context4, builder4) -> ArgumentSuggestion.getBoolOptions(builder4))
                                        .then(CommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(CyanCommandsV2::setBoolOption)
                                        )
                                )
                        )
                        .then(CommandManager.literal("integerOption")
                                .then(CommandManager.argument("option", StringArgumentType.string())
                                        .suggests((context4, builder4) -> ArgumentSuggestion.getIntegerOptions(builder4))
                                        .then(CommandManager.argument("value", IntegerArgumentType.integer())
                                                .executes(CyanCommandsV2::setIntegerOption)
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("description")
                        .then(CommandManager.literal("commands")
                                .then(CommandManager.argument("commandName", StringArgumentType.string())
                                        .suggests((context2, builder2) -> ArgumentSuggestion.getCommands(builder2))
                                        .executes(CyanCommandsV2::getCommandDescription)
                                )
                                .executes(CyanCommandsV2::getAllCommandsDescription)
                        )
                        .then(CommandManager.literal("options")
                                .then(CommandManager.literal("booleanOption")
                                        .then(CommandManager.argument("option", StringArgumentType.string())
                                                .suggests((context4, builder4) -> ArgumentSuggestion.getBoolOptions(builder4))
                                                .executes(CyanCommandsV2::getOptionDescription)
                                        )
                                )
                                .then(CommandManager.literal("integerOption")
                                        .then(CommandManager.argument("option", StringArgumentType.string())
                                                .suggests((context4, builder4) -> ArgumentSuggestion.getIntegerOptions(builder4))
                                                .executes(CyanCommandsV2::getOptionDescription)
                                        )
                                )
                                .executes(CyanCommandsV2::getAllOptionsDescription)
                        )
                )
        );
    }

    // Set functions

    /**
     * <p>Called when a player execute the command <code>/cyan config booleanOptions [optionName] [true|false]</code></p>
     *
     * <ul>If the player has a permission level equal to the option MinOpLevelExeModifConfig (see {@link CyanMidnightConfig})
     *      <li>-> Set the options to the given value</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setBoolOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, Map<String, Object>> options = CyanMidnightConfig.generateOptionsMap();

        String option = StringArgumentType.getString(context, "option");
        boolean value = BoolArgumentType.getBool(context, "value");

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(playerOnlyCmd));
            return 0;
        } else
        {
            if (player.hasPermissionLevel((Integer) options.get("minOpLevelExe").get("minOpLevelExeModifConfig")))
            {
                CyanMidnightConfig.setBoolOption(option, value);
                sendPlayerMessage(player,
                        "§3Toogled§6%s".formatted(option + "have been set to %s"),
                        value ? on : off,
                        "cyan.message.set.%s".formatted(option),
                        CyanMidnightConfig.msgToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            } else
            {
                sendPlayerMessage(player,
                        notOP,
                        null,
                        "cyan.message.notOp",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
                return 0;
            }
            return Command.SINGLE_SUCCESS;
        }
    }

    /**
     * <p>Called when a player execute the command <code>/cyan config integerOptions [optionName] [int]</code></p>
     *
     * <ul>If the player has a permission level equal to the option MinOpLevelExeModifConfig (see {@link CyanMidnightConfig})
     *      <li>-> Set the options to the given value</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setIntegerOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, Map<String, Object>> options = CyanMidnightConfig.generateOptionsMap();

        String option = StringArgumentType.getString(context, "option");
        int value = IntegerArgumentType.getInteger(context, "value");

        String tmpOption = option.substring(0, 13);

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(playerOnlyCmd));
            return 0;
        } else
        {
            if (tmpOption.equals("minOpLevelExe") && (value < 0 || value > 4))
            {
                sendPlayerMessage(player,
                        wrongOPLevel,
                        null,
                        "cyan.message.wrongOPLevel",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
                return 0;
            } else
            {
                if (player.hasPermissionLevel((Integer) options.get("minOpLevelExe").get("minOpLevelExeModifConfig")))
                {
                    CyanMidnightConfig.setIntOption(option, value);
                    sendPlayerMessage(player,
                            "§3%s".formatted(option + "have been set to %s"),
                            gold + String.valueOf(value),
                            "cyan.message.set.%s".formatted(option),
                            CyanMidnightConfig.msgToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                } else
                {
                    sendPlayerMessage(player,
                            notOP,
                            null,
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                    return 0;
                }
                return Command.SINGLE_SUCCESS;
            }
        }
    }

    // Get functions

    /**
     * <p>Called when a player execute the command <code>/cyan config</code></p>
     * <p>Send a player in the player's chat with all the mod's options and their values</p>
     */
    public static int getConfigOptions(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        String currentTrad = null;

        Map<String, Object> options = CyanMidnightConfig.generateAllOptionsMap();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(playerOnlyCmd));
            return 0;
        } else
        {
            sendPlayerMessage(player,
                    "§e------------------------------------",
                    null,
                    "cyan.message.getDescription.headerTop",
                    false,
                    CyanMidnightConfig.useTranslations
            );
            sendPlayerMessage(player,
                    "\n§3Options defined for the Cyan mod :",
                    null,
                    "cyan.message.getCfgOptions.header",
                    false,
                    CyanMidnightConfig.useTranslations
            );

            for (Map.Entry<String, Object> entry : options.entrySet())
            {
                Object key2 = entry.getKey();
                if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
                {
                    currentTrad = ChatConstants.getConfigTraduction(entry.getKey());
                }

                if (entry.getValue() instanceof Boolean value)
                {
                    sendPlayerMessage(player,
                            currentTrad,
                            value ? on : off,
                            "cyan.message.getCfgOptions.%s".formatted(key2),
                            false,
                            CyanMidnightConfig.useTranslations
                    );
                } else if (entry.getValue() instanceof Integer value)
                {
                    sendPlayerMessage(player,
                            currentTrad,
                            gold + Integer.toString(value),
                            "cyan.message.getCfgOptions.%s".formatted(key2),
                            false,
                            CyanMidnightConfig.useTranslations
                    );
                }
            }

            return Command.SINGLE_SUCCESS;
        }
    }

    /**
     * <p>Called when a player execute the command <code>/cyan description commands [commandName]</code></p>
     * <p>Send a message in the player's chat with the description of the command given as argument</p>
     */
    public static int getCommandDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        String option = StringArgumentType.getString(context, "commandName");
        Map<String, Map<String, String>> traductions = generateDescriptionTraductionsMap();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(playerOnlyCmd));
            return 0;
        } else
        {
            sendPlayerMessage(player,
                    traductions.get("commands").get("header").formatted(gold + option),
                    gold + option,
                    "cyan.message.getDescription.command.header",
                    false,
                    CyanMidnightConfig.useTranslations
            );
            sendPlayerMessage(player,
                    traductions.get("commands").get(option),
                    null,
                    "cyan.message.getDescription.command.%s".formatted(option),
                    false,
                    CyanMidnightConfig.useTranslations
            );


            return Command.SINGLE_SUCCESS;
        }
    }

    /**
     * <p>Called when a player execute the command <code>/cyan description [booleanOptions|integerOption] [option]</code></p>
     * <p>Send a message in the player's chat with the description of the option given as argument</p>
     */
    public static int getOptionDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        String option = StringArgumentType.getString(context, "option");
        Map<String, String> optionsTraductions = generateDescriptionTraductionsMap().get("options");

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(playerOnlyCmd));
            return 0;
        } else
        {
            sendPlayerMessage(player,
                    optionsTraductions.get("header").formatted(gold + option),
                    gold + option,
                    "cyan.message.getDescription.options.header",
                    false,
                    CyanMidnightConfig.useTranslations
            );
            sendPlayerMessage(player,
                    optionsTraductions.get(option),
                    null,
                    "cyan.message.getDescription.options.%s".formatted(option),
                    false,
                    CyanMidnightConfig.useTranslations
            );


            return Command.SINGLE_SUCCESS;
        }
    }

    /**
     * <p>Called when a player execute the command <code>/cyan description commands</code></p>
     * <p>Send a player in the player's chat with all the mod's commands and their description</p>
     */
    public static int getAllCommandsDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, String> commandsTraductions = generateDescriptionTraductionsMap().get("commands");
        List<String> commands = generateCommandsMap();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(playerOnlyCmd));
            return 0;
        } else
        {
            sendPlayerMessage(player,
                    commandsTraductions.get("headerTop"),
                    null,
                    "cyan.message.getDescription.headerTop",
                    false,
                    CyanMidnightConfig.useTranslations
            );

            for (String command : commands)
            {
                sendPlayerMessage(player,
                        commandsTraductions.get("header").formatted(gold + command),
                        gold + command,
                        "cyan.message.getDescription.command.header",
                        false,
                        CyanMidnightConfig.useTranslations
                );
                sendPlayerMessage(player,
                        commandsTraductions.get(command),
                        null,
                        "cyan.message.getDescription.command.%s".formatted(command),
                        false,
                        CyanMidnightConfig.useTranslations
                );
            }


            return Command.SINGLE_SUCCESS;
        }
    }

    /**
     * <p>Called when a player execute the command <code>/cyan description options</code></p>
     * <p>Send a player in the player's chat with all the mod's options description</p>
     */
    public static int getAllOptionsDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, String> optionsTraductions = generateDescriptionTraductionsMap().get("options");

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(playerOnlyCmd));
            return 0;
        } else
        {
            sendPlayerMessage(player,
                    traductions.get("options").get("headerTop"),
                    null,
                    "cyan.message.getDescription.headerTop",
                    false,
                    CyanMidnightConfig.useTranslations
            );

            for (String option : optionsTraductions.keySet())
            {
                sendPlayerMessage(player,
                        optionsTraductions.get("header").formatted(gold + option),
                        gold + option,
                        "cyan.message.getDescription.options.header",
                        false,
                        CyanMidnightConfig.useTranslations
                );
                sendPlayerMessage(player,
                        optionsTraductions.get(option),
                        null,
                        "cyan.message.getDescription.options.%s".formatted(option),
                        false,
                        CyanMidnightConfig.useTranslations
                );
            }


            return Command.SINGLE_SUCCESS;
        }
    }
}
