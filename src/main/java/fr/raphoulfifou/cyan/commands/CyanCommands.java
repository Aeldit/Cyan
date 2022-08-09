package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.raphoulfifou.cyan.commands.argumentTypes.ArgumentSuggestion;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import fr.raphoulfifou.cyan.util.ChatConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static fr.raphoulfifou.cyan.util.ChatConstants.*;
import static fr.raphoulfifou.cyanlib.util.ChatUtil.sendPlayerMessage;

public class CyanCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("cyan")
                .then(CommandManager.literal("config")
                        .then(CommandManager.literal("allow")
                                .then(CommandManager.argument("allowOption", StringArgumentType.string())
                                        .suggests(ArgumentSuggestion::getOptions)
                                        .then(CommandManager.argument("boolValue", BoolArgumentType.bool())
                                                .executes(CyanCommands::setAllowOption)
                                        )
                                )

                        )
                        .then(CommandManager.literal("minOpLevelExe")
                                .then(CommandManager.argument("minOpLevelExeOption", StringArgumentType.string())
                                        .suggests(ArgumentSuggestion::getOptionsGeneral)
                                        .then(CommandManager.argument("intValue", StringArgumentType.string())
                                                .suggests(ArgumentSuggestion::getOpLevels)
                                                .executes(CyanCommands::setMinOpLevelExeOption)
                                        )
                                )
                        )
                        .then(CommandManager.literal("other")
                                .then(CommandManager.literal("boolean")
                                        .then(CommandManager.argument("otherOption", StringArgumentType.string())
                                                .suggests(ArgumentSuggestion::getOtherBoolOptions)
                                                .then(CommandManager.argument("boolValue", BoolArgumentType.bool())
                                                        .executes(CyanCommands::setOtherBoolOption)
                                                )
                                        )
                                )
                                .then(CommandManager.literal("integer")
                                        .then(CommandManager.argument("otherOption", StringArgumentType.string())
                                                .suggests(ArgumentSuggestion::getOtherIntOptions)
                                                .then(CommandManager.argument("intValue", IntegerArgumentType.integer())
                                                        .executes(CyanCommands::setOtherIntOption)
                                                )
                                        )
                                )
                        )
                        .executes(CyanCommands::getConfigOptions)
                )
                .then(CommandManager.literal("description")
                        .then(CommandManager.literal("commands")
                                .then(CommandManager.argument("commandName", StringArgumentType.string())
                                        .suggests(ArgumentSuggestion::getCommands)
                                        .executes(CyanCommands::getCommandDescription)
                                )
                                .executes(CyanCommands::getAllCommandsDescription)
                        )
                        .then(CommandManager.literal("options")
                                .then(CommandManager.argument("optionType", StringArgumentType.string())
                                        .suggests(ArgumentSuggestion::getOptionsTypes)
                                        .executes(CyanCommands::getOptionTypeDescription)
                                )
                                .executes(CyanCommands::getAllOptionTypesDescription)
                        )
                        .executes(CyanCommands::getAllDescriptions)
                )
        );
    }

    /**
     * <p>Called when a player execute the command <code>/cyan description options [optionType]</code></p>
     * <p>Send a message in the player's chat with the description of the option type given as argument</p>
     * <p>optionType can be [allow | minOpLevelExe | other]</p>
     */
    public static int getOptionTypeDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        String option = StringArgumentType.getString(context, "optionType");
        Map<String, String> optionsTraductions = generateTraductionsMap().get("options");

        assert player != null;
        sendPlayerMessage(player,
                optionsTraductions.get("headerTop"),
                null,
                "cyan.message.getDescription.options.headerTop",
                false,
                CyanMidnightConfig.useOneLanguage
        );
        sendPlayerMessage(player,
                optionsTraductions.get("header").formatted(Formatting.YELLOW + option),
                Formatting.YELLOW + option,
                "cyan.message.getDescription.options.header",
                false,
                CyanMidnightConfig.useOneLanguage
        );

        sendPlayerMessage(player,
                optionsTraductions.get(option),
                null,
                "cyan.message.getDescription.options.%s".formatted(option),
                false,
                CyanMidnightConfig.useOneLanguage
        );


        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command <code>/cyan description options</code></p>
     * <p>Send a player in the player's chat with all the mod's options and their values</p>
     */
    public static int getAllOptionTypesDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, String> optionsTraductions = generateTraductionsMap().get("options");
        List<String> optionTypes = generateOptionsTypesMap();

        assert player != null;
        sendPlayerMessage(player,
                traductions.get("options").get("headerTop"),
                null,
                "cyan.message.getDescription.options.headerTop",
                false,
                CyanMidnightConfig.useOneLanguage
        );

        for (String option : optionTypes)
        {
            sendPlayerMessage(player,
                    optionsTraductions.get("header").formatted(Formatting.YELLOW + option),
                    Formatting.YELLOW + option,
                    "cyan.message.getDescription.options.header",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );

            sendPlayerMessage(player,
                    optionsTraductions.get(option),
                    null,
                    "cyan.message.getDescription.options.%s".formatted(option),
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
        }


        return Command.SINGLE_SUCCESS;
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
        Map<String, Map<String, String>> traductions = generateTraductionsMap();

        assert player != null;
        sendPlayerMessage(player,
                Formatting.BOLD + traductions.get("commands").get("header").formatted(Formatting.YELLOW + option),
                Formatting.YELLOW + option,
                "cyan.message.getDescription.command.header",
                false,
                CyanMidnightConfig.useOneLanguage
        );

        sendPlayerMessage(player,
                traductions.get("commands").get(option),
                null,
                "cyan.message.getDescription.command.%s".formatted(option),
                false,
                CyanMidnightConfig.useOneLanguage
        );


        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command <code>/cyan description commands</code></p>
     * <p>Send a player in the player's chat with all the mod's options and their values</p>
     */
    public static int getAllCommandsDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, String> commandsTraductions = generateTraductionsMap().get("commands");
        List<String> commands = generateCommandsMap();

        assert player != null;
        sendPlayerMessage(player,
                Formatting.BOLD + commandsTraductions.get("headerTop"),
                null,
                "cyan.message.getDescription.command.headerTop",
                false,
                CyanMidnightConfig.useOneLanguage
        );

        for (String command : commands)
        {
            sendPlayerMessage(player,
                    Formatting.BOLD + commandsTraductions.get("header").formatted(Formatting.YELLOW + command),
                    Formatting.YELLOW + command,
                    "cyan.message.getDescription.command.header",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );

            sendPlayerMessage(player,
                    commandsTraductions.get(command),
                    null,
                    "cyan.message.getDescription.command.%s".formatted(command),
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
        }


        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command <code>/cyan description</code></p>
     * <p>Send a player in the player's chat with all the mod's options and their values</p>
     */
    public static int getAllDescriptions(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        String option = StringArgumentType.getString(context, "optionName");
        Map<String, Map<String, Object>> options = CyanMidnightConfig.generateOptionsMap();

        assert player != null;
        sendPlayerMessage(player,
                "\n§6|--> §3Description of the Cyan mod's options :",
                null,
                "cyan.message.getDescription.header",
                false,
                CyanMidnightConfig.useOneLanguage
        );

        for (Map.Entry<String, Map<String, Object>> entry : options.entrySet())
        {
            Map<String, Object> key = entry.getValue();
            for (Map.Entry<String, Object> entry2 : key.entrySet())
            {
                Object key2 = entry2.getKey();
                String currentTrad = null;
                if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
                {
                    currentTrad = ChatConstants.getOptionTraduction(entry2.getKey());
                }

                if (entry2.getValue() instanceof Boolean value)
                {
                    if (value)
                    {
                        sendPlayerMessage(player,
                                currentTrad,
                                green + Boolean.toString(value),
                                "cyan.message.getDescription.%s.%s".formatted(key, key2),
                                false,
                                CyanMidnightConfig.useOneLanguage
                        );
                    } else
                    {
                        sendPlayerMessage(player,
                                currentTrad,
                                red + Boolean.toString(value),
                                "cyan.message.getDescription.%s.%s".formatted(key, key2),
                                false,
                                CyanMidnightConfig.useOneLanguage
                        );
                    }
                } else if (entry2.getValue() instanceof Integer value)
                {
                    sendPlayerMessage(player,
                            currentTrad,
                            gold + Integer.toString(value),
                            "cyan.message.getDescription.%s.%s".formatted(key, key2),
                            false,
                            CyanMidnightConfig.useOneLanguage
                    );
                }
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command <code>/cyan allow [optionName] [true|false]</code></p>
     *
     * <ul>If the player has a permission level equal to the option MinOpLevelExeModifConfig (see {@link CyanMidnightConfig})
     *      <li>-> Set the options to the given value</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setAllowOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, Map<String, Object>> options = CyanMidnightConfig.generateOptionsMap();

        String option = StringArgumentType.getString(context, "allowOption");
        boolean boolValue = BoolArgumentType.getBool(context, "boolValue");

        // Used for the translationPath (we geet the word in lowercase but we need the first letter in uppercase)
        String upperCaseOptionName = String.valueOf(option.charAt(0)).toUpperCase();
        String tmpOption = option.substring(1);
        upperCaseOptionName = upperCaseOptionName.concat(tmpOption);

        // If OP with minimum defined level (minOpLevelExe option)
        assert player != null;
        if (player.hasPermissionLevel((Integer) options.get("minOpLevelExe").get("minOpLevelExeModifConfig")))
        {
            CyanMidnightConfig.setBoolOption(option, boolValue);
            if (boolValue)
            {
                if ("all".equals(option))
                {
                    sendPlayerMessage(player,
                            line_start + "§3Allow options have been set to %s",
                            green + Boolean.toString(boolValue),
                            "cyan.message.setAllow",
                            false,
                            CyanMidnightConfig.useOneLanguage
                    );
                } else
                {
                    sendPlayerMessage(player,
                            line_start + "§3setAllow%s %s".formatted(upperCaseOptionName, "option have been set to %s"),
                            green + Boolean.toString(boolValue),
                            "cyan.message.setAllow%s".formatted(upperCaseOptionName),
                            false,
                            CyanMidnightConfig.useOneLanguage
                    );
                }
            } else
            {
                if ("all".equals(option))
                {
                    sendPlayerMessage(player,
                            line_start + "§3Allow options have been set to %s",
                            red + Boolean.toString(boolValue),
                            "cyan.message.setAllow",
                            false,
                            CyanMidnightConfig.useOneLanguage
                    );
                } else
                {
                    sendPlayerMessage(player,
                            line_start + "§3setAllow%s %s".formatted(upperCaseOptionName, "option have been set to %s"),
                            red + Boolean.toString(boolValue),
                            "cyan.message.setAllow%s".formatted(upperCaseOptionName),
                            false,
                            CyanMidnightConfig.useOneLanguage
                    );
                }
            }
        }
        // If not OP or not OP with max level
        else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command <code>/cyan minOpLevelExe [optionName] [0|1|2|3|4]</code></p>
     *
     * <ul>If the player has a permission level equal to the option MinOpLevelExeModifConfig (see {@link CyanMidnightConfig})
     *      <li>-> Set the options to the given value</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setMinOpLevelExeOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, Map<String, Object>> options = CyanMidnightConfig.generateOptionsMap();

        String option = StringArgumentType.getString(context, "minOpLevelExeOption");
        String stringArg = StringArgumentType.getString(context, "intValue");
        int intValue = Integer.parseInt(stringArg);

        // If the argument passed to the command isn't in [0;4],
        // the config file will not be modified and the functionstops here
        if (intValue < 0 || intValue > 4)
        {
            assert player != null;
            sendPlayerMessage(player,
                    line_start_error + "The OP level must be [0;1;2;3 or 4]",
                    null,
                    "cyan.message.incorrectIntOp",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }

        // Used for the translationPath (we geet the word in lowercase but we need the first letter in uppercase)
        String upperCaseOptionName = String.valueOf(option.charAt(0)).toUpperCase();
        String tmpOption = option.substring(1);
        upperCaseOptionName = upperCaseOptionName.concat(tmpOption);

        // If OP with minimum defined level (minOpLevelExe option)
        assert player != null;
        if (player.hasPermissionLevel((Integer) options.get("minOpLevelExe").get("minOpLevelExeModifConfig")))
        {
            CyanMidnightConfig.setIntOption(option, intValue);
            if ("all".equals(option))
            {
                sendPlayerMessage(player,
                        line_start + "§3MinOpLevelExe options have been set to %s",
                        gold + Integer.toString(intValue),
                        "cyan.message.setMinOpLevelExe",
                        false,
                        CyanMidnightConfig.useOneLanguage
                );
            } else
            {
                sendPlayerMessage(player,
                        line_start + "§3MinOpLevelExe%s %s".formatted(upperCaseOptionName, "option have been set to %s"),
                        gold + Integer.toString(intValue),
                        "cyan.message.setMinOpLevelExe%s".formatted(upperCaseOptionName),
                        false,
                        CyanMidnightConfig.useOneLanguage
                );
            }
        } else  // If not OP or not OP with max level
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command <code>/cyan other boolean [optionName] [true|false]</code></p>
     *
     * <ul>If the player has a permission level equal to the option MinOpLevelExeModifConfig (see {@link CyanMidnightConfig})
     *      <li>-> Set the options to the given value</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setOtherBoolOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, Map<String, Object>> options = CyanMidnightConfig.generateOptionsMap();

        String option = StringArgumentType.getString(context, "otherOption");
        boolean boolValue = BoolArgumentType.getBool(context, "boolValue");

        option = option.substring(0, 1).toUpperCase() + option.substring(1);

        // If OP with minimum defined level (minOpLevelExe option)
        assert player != null;
        if (player.hasPermissionLevel((Integer) options.get("minOpLevelExe").get("minOpLevelExeModifConfig")))
        {
            CyanMidnightConfig.setBoolOption(option, boolValue);
            if (boolValue)
            {

                sendPlayerMessage(player,
                        line_start + "§3%s %s".formatted(option, "option have been set to %s"),
                        green + Boolean.toString(boolValue),
                        "cyan.message.set%s".formatted(option),
                        false,
                        CyanMidnightConfig.useOneLanguage
                );
            } else
            {
                sendPlayerMessage(player,
                        line_start + "§3%s %s".formatted(option, "option have been set to %s"),
                        red + Boolean.toString(boolValue),
                        "cyan.message.set%s".formatted(option),
                        false,
                        CyanMidnightConfig.useOneLanguage
                );
            }
        }
        // If not OP or not OP with max level
        else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command <code>/cyan other integer [optionName] [int]</code></p>
     *
     * <ul>If the player has a permission level equal to the option MinOpLevelExeModifConfig (see {@link CyanMidnightConfig})
     *      <li>-> Set the options to the given value</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setOtherIntOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, Map<String, Object>> options = CyanMidnightConfig.generateOptionsMap();

        String option = StringArgumentType.getString(context, "otherOption");
        int intValue = IntegerArgumentType.getInteger(context, "intValue");

        option = option.substring(0, 1).toUpperCase() + option.substring(1);

        // If OP with minimum defined level (minOpLevelExe option)
        assert player != null;
        if (player.hasPermissionLevel((Integer) options.get("minOpLevelExe").get("minOpLevelExeModifConfig")))
        {
            CyanMidnightConfig.setIntOption(option, intValue);
            sendPlayerMessage(player,
                    line_start + "§3%s %s".formatted(option, "option have been set to %s"),
                    gold + Integer.toString(intValue),
                    "cyan.message.set%s".formatted(option),
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
        }
        // If not OP or not OP with max level
        else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command <code>/cyan config</code></p>
     * <p>Send a player in the player's chat with all the mod's options and their values</p>
     */
    public static int getConfigOptions(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, Map<String, Object>> options = CyanMidnightConfig.generateOptionsMap();

        assert player != null;
        sendPlayerMessage(player,
                "\n§6|--> §3Options defined for the Cyan mod :",
                null,
                "cyan.message.getCfgOptions.header",
                false,
                CyanMidnightConfig.useOneLanguage
        );

        for (Map.Entry<String, Map<String, Object>> entry : options.entrySet())
        {
            Map<String, Object> key = entry.getValue();
            for (Map.Entry<String, Object> entry2 : key.entrySet())
            {
                Object key2 = entry2.getKey();
                String currentTrad = null;
                if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
                {
                    currentTrad = ChatConstants.getOptionTraduction(entry2.getKey());
                }

                if (entry2.getValue() instanceof Boolean value)
                {
                    if (value)
                    {
                        sendPlayerMessage(player,
                                currentTrad,
                                green + Boolean.toString(value),
                                "cyan.message.getCfgOptions.%s".formatted(key2),
                                false,
                                CyanMidnightConfig.useOneLanguage
                        );
                    } else
                    {
                        sendPlayerMessage(player,
                                currentTrad,
                                red + Boolean.toString(value),
                                "cyan.message.getCfgOptions.%s".formatted(key2),
                                false,
                                CyanMidnightConfig.useOneLanguage
                        );
                    }
                } else if (entry2.getValue() instanceof Integer value)
                {
                    sendPlayerMessage(player,
                            currentTrad,
                            gold + Integer.toString(value),
                            "cyan.message.getCfgOptions.%s".formatted(key2),
                            false,
                            CyanMidnightConfig.useOneLanguage
                    );
                }
            }
        }

        return Command.SINGLE_SUCCESS;
    }


}
