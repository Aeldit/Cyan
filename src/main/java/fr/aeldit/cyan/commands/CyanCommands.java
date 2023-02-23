package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.commands.argumentTypes.ArgumentSuggestion;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static fr.aeldit.cyan.util.LanguageUtils.*;
import static fr.aeldit.cyan.util.Utils.*;
import static fr.aeldit.cyanlib.util.ChatUtil.sendPlayerMessage;

public class CyanCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("cyan")
                .then(CommandManager.literal("config")
                        .then(CommandManager.literal("booleanOption")
                                .then(CommandManager.argument("option", StringArgumentType.string())
                                        .suggests((context4, builder4) -> ArgumentSuggestion.getBoolOptions(builder4))
                                        .then(CommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(CyanCommands::setBoolOption)
                                        )
                                )
                        )
                        .then(CommandManager.literal("integerOption")
                                .then(CommandManager.argument("option", StringArgumentType.string())
                                        .suggests((context4, builder4) -> ArgumentSuggestion.getIntegerOptions(builder4))
                                        .then(CommandManager.argument("value", IntegerArgumentType.integer())
                                                .executes(CyanCommands::setIntegerOption)
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("description")
                        .then(CommandManager.literal("commands")
                                .then(CommandManager.argument("commandName", StringArgumentType.string())
                                        .suggests((context2, builder2) -> ArgumentSuggestion.getCommands(builder2))
                                        .executes(CyanCommands::getCommandDescription)
                                )
                                .executes(CyanCommands::getAllCommandsDescription)
                        )
                        .then(CommandManager.literal("options")
                                .then(CommandManager.literal("booleanOption")
                                        .then(CommandManager.argument("option", StringArgumentType.string())
                                                .suggests((context4, builder4) -> ArgumentSuggestion.getBoolOptions(builder4))
                                                .executes(CyanCommands::getOptionDescription)
                                        )
                                )
                                .executes(CyanCommands::getAllOptionsDescription)
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

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            try
            {
                Properties properties = new Properties();
                properties.load(new FileInputStream(languagePath.toFile()));
                for (String key : properties.stringPropertyNames())
                {
                    translations.put(key, properties.getProperty(key));
                }
                sendPlayerMessage(player,
                        getTranslation("translationsReloaded"),
                        "cyan.message.translationsReloaded",
                        CyanMidnightConfig.msgToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    // Set functions

    /**
     * Called when a player execute the command {@code /cyan config booleanOptions [optionName] [true|false]}
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

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditConfig))
            {
                String option = StringArgumentType.getString(context, "option");
                boolean value = BoolArgumentType.getBool(context, "value");
                CyanMidnightConfig.setBoolOption(option, value);
                if (option.equals("useTranslations") && !CyanMidnightConfig.useTranslations)
                {
                    loadLanguage();
                }
                sendPlayerMessage(player,
                        getTranslation(SET + option),
                        "cyan.message.set.%s".formatted(option),
                        CyanMidnightConfig.msgToActionBar,
                        CyanMidnightConfig.useTranslations,
                        value ? on : off
                );
            } else
            {
                sendPlayerMessage(player,
                        getTranslation(ERROR + "notOp"),
                        "cyan.message.notOp",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the command {@code /cyan config integerOptions [optionName] [int]}
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

        String option = StringArgumentType.getString(context, "option");
        int value = IntegerArgumentType.getInteger(context, "value");

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            if (option.startsWith("minOpLevelExe") && (value < 0 || value > 4))
            {
                sendPlayerMessage(player,
                        getTranslation(ERROR + "wrongOPLevel"),
                        "cyan.message.wrongOPLevel",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            } else if (option.startsWith("distanceToEntitiesKgi") && (value < 1 || value > 128))
            {
                sendPlayerMessage(player,
                        getTranslation(ERROR + "wrongDistanceKgi"),
                        "cyan.message.wrongDistanceKgi",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            } else
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditConfig))
                {
                    CyanMidnightConfig.setIntOption(option, value);
                    sendPlayerMessage(player,
                            getTranslation(SET + option),
                            "cyan.message.set.%s".formatted(option),
                            CyanMidnightConfig.msgToActionBar,
                            CyanMidnightConfig.useTranslations,
                            Formatting.GOLD + String.valueOf(value)
                    );
                } else
                {
                    sendPlayerMessage(player,
                            getTranslation(ERROR + "notOp"),
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    // Get functions

    /**
     * Called when a player execute the command {@code /cyan config}
     * Send a player in the player's chat with all the mod's options and their values
     */
    public static int getConfigOptions(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        String currentTrad = null;

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            sendPlayerMessage(player,
                    getTranslation("dashSeparation"),
                    "cyan.message.getDescription.dashSeparation",
                    false,
                    CyanMidnightConfig.useTranslations
            );
            sendPlayerMessage(player,
                    getTranslation(GETCFG + "header"),
                    "cyan.message.getCfgOptions.header",
                    false,
                    CyanMidnightConfig.useTranslations
            );

            for (Map.Entry<String, Object> entry : CyanMidnightConfig.getAllOptionsMap().entrySet())
            {
                Object key2 = entry.getKey();
                if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
                {
                    currentTrad = getTranslation(GETCFG + key2);
                }

                if (entry.getValue() instanceof Boolean value)
                {
                    sendPlayerMessage(player,
                            currentTrad,
                            "cyan.message.getCfgOptions.%s".formatted(key2),
                            false,
                            CyanMidnightConfig.useTranslations,
                            value ? on : off
                    );
                } else if (entry.getValue() instanceof Integer value)
                {
                    sendPlayerMessage(player,
                            currentTrad,
                            "cyan.message.getCfgOptions.%s".formatted(key2),
                            false,
                            CyanMidnightConfig.useTranslations,
                            Formatting.GOLD + Integer.toString(value)
                    );
                }
            }
            sendPlayerMessage(player,
                    getTranslation("dashSeparation"),
                    "cyan.message.getDescription.dashSeparation",
                    false,
                    CyanMidnightConfig.useTranslations
            );
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the command {@code /cyan description commands [commandName]}
     * Send a message in the player's chat with the description of the command given as argument
     */
    public static int getCommandDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            String option = StringArgumentType.getString(context, "commandName");
            sendPlayerMessage(player,
                    getTranslation(DESC + option),
                    "cyan.message.getDescription.command.%s".formatted(option),
                    false,
                    CyanMidnightConfig.useTranslations
            );
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the command {@code /cyan description [booleanOption|integerOption] [option]}
     * Send a message in the player's chat with the description of the option given as argument
     */
    public static int getOptionDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            String option = StringArgumentType.getString(context, "option");
            sendPlayerMessage(player,
                    getTranslation(DESC + option),
                    "cyan.message.getDescription.options.%s".formatted(option),
                    false,
                    CyanMidnightConfig.useTranslations
            );
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the command {@code /cyan description commands}
     * Send a player in the player's chat with all the mod's commands and their description
     */
    public static int getAllCommandsDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            sendPlayerMessage(player,
                    getTranslation("dashSeparation"),
                    "cyan.message.getDescription.dashSeparation",
                    false,
                    CyanMidnightConfig.useTranslations
            );
            sendPlayerMessage(player,
                    getTranslation("headerDescCmd"),
                    "cyan.message.getDescription.headerDescCmd",
                    false,
                    CyanMidnightConfig.useTranslations
            );

            for (String command : getCommandsList())
            {
                sendPlayerMessage(player,
                        getTranslation(DESC + command),
                        "cyan.message.getDescription.command.%s".formatted(command),
                        false,
                        CyanMidnightConfig.useTranslations
                );
            }
            sendPlayerMessage(player,
                    getTranslation("dashSeparation"),
                    "cyan.message.getDescription.dashSeparation",
                    false,
                    CyanMidnightConfig.useTranslations
            );
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the command {@code /cyan description options}
     * Send a player in the player's chat with all the mod's options description
     */
    public static int getAllOptionsDescription(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            sendPlayerMessage(player,
                    getTranslation("dashSeparation"),
                    "cyan.message.getDescription.dashSeparation",
                    false,
                    CyanMidnightConfig.useTranslations
            );
            sendPlayerMessage(player,
                    getTranslation("headerDescOptions"),
                    "cyan.message.getDescription.headerDescOptions",
                    false,
                    CyanMidnightConfig.useTranslations
            );

            for (String option : getOptionsList())
            {
                sendPlayerMessage(player,
                        getTranslation(DESC + option),
                        "cyan.message.getDescription.options.%s".formatted(option),
                        false,
                        CyanMidnightConfig.useTranslations
                );
            }
            sendPlayerMessage(player,
                    getTranslation("dashSeparation"),
                    "cyan.message.getDescription.dashSeparation",
                    false,
                    CyanMidnightConfig.useTranslations
            );
        }
        return Command.SINGLE_SUCCESS;
    }
}
