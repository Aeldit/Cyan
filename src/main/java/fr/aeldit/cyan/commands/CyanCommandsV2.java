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
import net.minecraft.util.Formatting;
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
                        .then(CommandManager.literal("boolOption")
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
                                .then(CommandManager.argument("optionType", StringArgumentType.string())
                                        .suggests((context3, builder3) -> ArgumentSuggestion.getOptionsTypes(builder3))
                                        .executes(CyanCommandsV2::getOptionTypeDescription)
                                )
                                .executes(CyanCommandsV2::getAllOptionTypesDescription)
                        )
                )
        );
    }

    // Set functions

    /**
     * <p>Called when a player execute the command <code>/cyan config boolOptions [optionName] [true|false]</code></p>
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

        assert player != null;
        if (player.hasPermissionLevel((Integer) options.get("minOpLevelExe").get("minOpLevelExeModifConfig")))
        {
            CyanMidnightConfig.setBoolOption(option, value);
            sendPlayerMessage(player,
                    "§3%s".formatted(option + "have been set to %s"),
                    value ? on : off,
                    "cyan.message.set.%s".formatted(option),
                    CyanMidnightConfig.msgToActionBar,
                    CyanMidnightConfig.useOneLanguage
            );
        } else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    CyanMidnightConfig.errorToActionBar,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int setIntegerOption(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        Map<String, Map<String, Object>> options = CyanMidnightConfig.generateOptionsMap();

        String option = StringArgumentType.getString(context, "option");
        int value = IntegerArgumentType.getInteger(context, "value");

        assert player != null;
        if (player.hasPermissionLevel((Integer) options.get("minOpLevelExe").get("minOpLevelExeModifConfig")))
        {
            CyanMidnightConfig.setIntOption(option, value);
            sendPlayerMessage(player,
                    "§3%s".formatted(option + "have been set to %s"),
                    gold + String.valueOf(value),
                    "cyan.message.set.%s".formatted(option),
                    CyanMidnightConfig.msgToActionBar,
                    CyanMidnightConfig.useOneLanguage
            );
        } else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    CyanMidnightConfig.errorToActionBar,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }
        return Command.SINGLE_SUCCESS;
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

        assert player != null;
        sendPlayerMessage(player,
                "\n§l§3Options defined for the Cyan mod :",
                null,
                "cyan.message.getCfgOptions.header",
                false,
                CyanMidnightConfig.useOneLanguage
        );

        for (Map.Entry<String, Object> entry : options.entrySet())
        {
            Object key2 = entry.getKey();
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
            {
                currentTrad = ChatConstants.getAllOptionTraduction(entry.getKey());
            }

            if (entry.getValue() instanceof Boolean value)
            {
                sendPlayerMessage(player,
                        currentTrad,
                        value ? on : off,
                        "cyan.message.getCfgOptions.%s".formatted(key2),
                        false,
                        CyanMidnightConfig.useOneLanguage
                );
            } else if (entry.getValue() instanceof Integer value)
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

        return Command.SINGLE_SUCCESS;
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
}
