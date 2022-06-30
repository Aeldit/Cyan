package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import static fr.raphoulfifou.cyan.util.ChatConstants.*;
import static fr.raphoulfifou.cyanlib.util.ChatUtil.sendPlayerMessage;

/**
 * @since 0.2.6
 */
public class SetCommands
{

    public static Formatting color = Formatting.GREEN;

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("allowBed")
                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                        .executes(SetCommands::setAllowBed)
                )
                .executes(SetCommands::getAllowBed)
        );
        dispatcher.register(CommandManager.literal("allowKgi")
                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                        .executes(SetCommands::setAllowKgi)
                )
                .executes(SetCommands::getAllowKgi)
        );
        dispatcher.register(CommandManager.literal("allowSurface")
                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                        .executes(SetCommands::setAllowSurface)
                )
                .executes(SetCommands::getAllowSurface)
        );
        dispatcher.register(CommandManager.literal("useOneLanguage")
                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                        .executes(SetCommands::setUseOneLanguage)
                )
                .executes(SetCommands::getUseOneLanguage)
        );

        dispatcher.register(CommandManager.literal("distanceToEntitiesKgi")
                .then(CommandManager.argument("int", IntegerArgumentType.integer())
                        .executes(SetCommands::setDistanceToEntitiesKgi)
                )
                .executes(SetCommands::getDistanceToEntitiesKgi)
        );
        dispatcher.register(CommandManager.literal("requiredOpLevelKgi")
                .then(CommandManager.argument("int", IntegerArgumentType.integer())
                        .executes(SetCommands::setRequiredOpLevelKgi)
                )
                .executes(SetCommands::getRequiredOpLevelKgi)
        );
    }

    // Set

    /**
     * <p>Called when a player execute the command "/allowBed [true | false]"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Enables/disables the use of the /bed command</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setAllowBed(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        boolean arg = BoolArgumentType.getBool(context, "bool");
        assert player != null;

        if (arg)
        {
            color = green;
        } else
        {
            color = red;
        }

        // If OP with minimum defined level
        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            CyanMidnightConfig.setAllowBed(arg);
            sendPlayerMessage(player,
                    line_start + "§3setAllowBed option have been set to %s",
                    color + Boolean.toString(arg),
                    "cyan.message.setAllowBed",
                    false,
                    CyanMidnightConfig.useOneLanguage);

        }
        // If not OP or not OP with max level
        else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true,
                    CyanMidnightConfig.useOneLanguage);
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/allowKgi [true | false]"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Enables/disables the use of the /kgi command</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setAllowKgi(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        boolean arg = BoolArgumentType.getBool(context, "bool");
        assert player != null;

        if (arg)
        {
            color = green;
        } else
        {
            color = red;
        }

        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            CyanMidnightConfig.setAllowKgi(arg);
            sendPlayerMessage(player,
                    line_start + "§3setAllowKgi option have been set to %s",
                    color + Boolean.toString(arg),
                    "cyan.message.setAllowKgi",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
        } else
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
     * <p>Called when a player execute the command "/allowSurface [true | false]"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Enables/disables the use of the /surface command</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setAllowSurface(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        boolean arg = BoolArgumentType.getBool(context, "bool");
        assert player != null;

        if (arg)
        {
            color = green;
        } else
        {
            color = red;
        }

        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            CyanMidnightConfig.setAllowSurface(arg);
            sendPlayerMessage(player,
                    line_start + "§3AllowSurface option have been set to %s",
                    color + Boolean.toString(arg),
                    "cyan.message.setAllowSurface",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
        } else
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
     * <p>Called when a player execute the command "/useOneLanguage [true | false]"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Set the option to use only one language to the value</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setUseOneLanguage(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        boolean arg = BoolArgumentType.getBool(context, "bool");
        assert player != null;

        if (arg)
        {
            color = green;
        } else
        {
            color = red;
        }

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
        {
            if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
            {
                CyanMidnightConfig.setUseOneLanguage(arg);
                sendPlayerMessage(player,
                        line_start + "§3UseOneLanguage option have been set to %s",
                        color + String.valueOf(arg),
                        "cyan.message.setUseOneLanguage",
                        false,
                        CyanMidnightConfig.useOneLanguage
                );
            } else
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
        } else
        {
            sendPlayerMessage(player,
                    "§cThis command can only be used on servers",
                    null,
                    "cyan.message.only_serv",
                    true,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/requiredOpLevelKgi [int]"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Set the minimum OP level required to execute the /kgi command</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setRequiredOpLevelKgi(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        int arg = IntegerArgumentType.getInteger(context, "int");
        assert player != null;

        // If the argument passed to the command isn't in [0;4], the config file will not be modified and the function
        // stops here
        if (arg < 0 || arg > 4)
        {
            sendPlayerMessage(player,
                    line_start_error + "The OP level must be [0;1;2;3 or 4]",
                    null,
                    "cyan.message.incorrectIntOp",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }

        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            CyanMidnightConfig.setMinOpLevelExeKgi(arg);
            sendPlayerMessage(player,
                    line_start + "§3RequiredOpLevelKgi option have been set to %s",
                    Formatting.GOLD + Integer.toString(arg),
                    "cyan.message.setRequiredOpLevelKgi",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
        } else
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
     * <p>Called when a player execute the command "/distanceToEntitiesKgi [int]"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Set the distance (in chunks) in which the ground items will be removed</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int setDistanceToEntitiesKgi(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        int arg = IntegerArgumentType.getInteger(context, "int");
        assert player != null;

        if (arg < 1 || arg > 64)
        {
            sendPlayerMessage(player,
                    line_start_error + "The distance must be in [1;64]",
                    null,
                    "cyan.message.incorrectIntKgi",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }

        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            CyanMidnightConfig.setDistanceToEntitiesKgi(arg);
            sendPlayerMessage(player,
                    line_start + "§3DistanceToEntitiesKgi option have been set to %s",
                    Formatting.GOLD + Integer.toString(arg),
                    "cyan.message.setDistanceToEntitiesKgi",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
        } else
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
     * <p>Called when a player execute the command "/allowBed"</p>
     *
     * <li>-> Gives the status of the options 'allowBed'</li>
     */
    public static int getAllowBed(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        boolean arg = CyanMidnightConfig.allowBed;

        if (arg)
        {
            color = green;
        } else
        {
            color = red;
        }

        sendPlayerMessage(player,
                line_start + "§3allowBed option is set to %s",
                color + Boolean.toString(arg),
                "cyan.message.getAllowBed",
                false,
                CyanMidnightConfig.useOneLanguage
        );


        return Command.SINGLE_SUCCESS;
    }

    // Get

    /**
     * <p>Called when a player execute the command "/allowKgi"</p>
     *
     * <li>-> Gives the status of the options 'allowKgi'</li>
     */
    public static int getAllowKgi(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        boolean arg = CyanMidnightConfig.allowKgi;

        if (arg)
        {
            color = green;
        } else
        {
            color = red;
        }

        sendPlayerMessage(player,
                line_start + "§3allowKgi option is set to %s",
                color + Boolean.toString(arg),
                "cyan.message.getAllowKgi",
                false,
                CyanMidnightConfig.useOneLanguage
        );


        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/allowSurface"</p>
     *
     * <li>-> Gives the status of the options 'allowSurface'</li>
     */
    public static int getAllowSurface(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        boolean arg = CyanMidnightConfig.allowSurface;

        if (arg)
        {
            color = green;
        } else
        {
            color = red;
        }

        sendPlayerMessage(player,
                line_start + "§3allowSurface option is set to %s",
                color + Boolean.toString(arg),
                "cyan.message.getAllowSurface",
                false,
                CyanMidnightConfig.useOneLanguage
        );


        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/useOneLanguage"</p>
     *
     * <li>-> Gives the status of the options 'useOneLanguage'</li>
     */
    public static int getUseOneLanguage(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        boolean arg = CyanMidnightConfig.useOneLanguage;

        if (arg)
        {
            color = green;
        } else
        {
            color = red;
        }

        sendPlayerMessage(player,
                line_start + "§3useOneLanguage option is set to %s",
                color + Boolean.toString(arg),
                "cyan.message.getUseOneLanguage",
                false,
                CyanMidnightConfig.useOneLanguage
        );


        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/distanceToEntitiesKgi"</p>
     *
     * <li>-> Gives the status of the options 'distanceToEntitiesKgi'</li>
     */
    public static int getDistanceToEntitiesKgi(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        int arg = CyanMidnightConfig.distanceToEntitiesKgi;

        sendPlayerMessage(player,
                line_start + "§3distanceToEntitiesKgi option is set to %s",
                Formatting.GOLD + Integer.toString(arg),
                "cyan.message.getDistanceToEntitiesKgi",
                false,
                CyanMidnightConfig.useOneLanguage
        );


        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/requiredOpLevelKgi"</p>
     *
     * <li>-> Gives the status of the options 'requiredOpLevelKgi'</li>
     */
    public static int getRequiredOpLevelKgi(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        int arg = CyanMidnightConfig.minOpLevelExeKgi;

        sendPlayerMessage(player,
                line_start + "§3requiredOpLevelKgi option is set to %s",
                Formatting.GOLD + Integer.toString(arg),
                "cyan.message.getRequiredOpLevelKgi",
                false,
                CyanMidnightConfig.useOneLanguage
        );


        return Command.SINGLE_SUCCESS;
    }

}
