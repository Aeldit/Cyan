package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import static fr.raphoulfifou.cyan.util.ChatUtil.*;

/**
 * @author Raphoulfifou
 * @since 0.2.6
 */
public class SetCommands
{

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("setAllowBed")
                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                        .executes(SetCommands::setAllowBed)
                )
        );
        dispatcher.register(CommandManager.literal("setAllowKgi")
                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                        .executes(SetCommands::setAllowKgi)
                )
        );
        dispatcher.register(CommandManager.literal("setAllowSurface")
                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                        .executes(SetCommands::setAllowSurface)
                )
        );
        dispatcher.register(CommandManager.literal("setUseOneLanguage")
                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                        .executes(SetCommands::setUseOneLanguage)
                )
        );

        dispatcher.register(CommandManager.literal("setDistanceToEntitiesKgi")
                .then(CommandManager.argument("int", IntegerArgumentType.integer())
                        .executes(SetCommands::setDistanceToEntitiesKgi)
                )
        );
        dispatcher.register(CommandManager.literal("setRequiredOpLevelKgi")
                .then(CommandManager.argument("int", IntegerArgumentType.integer())
                        .executes(SetCommands::setRequiredOpLevelKgi)
                )
        );
    }

    /**
     * <p>Called when a player execute the command "/setAllowBed [true | false]"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Enables/disables the use of the /bed command</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int setAllowBed(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        boolean arg = BoolArgumentType.getBool(context, "bool");

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
                    false);
        }
        // If not OP or not OP with max level
        else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true);
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/setAllowKgi [true | false]"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Enables/disables the use of the /kgi command</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int setAllowKgi(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        boolean arg = BoolArgumentType.getBool(context, "bool");

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
                    false);
        } else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true);
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/setAllowSurface [true | false]"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Enables/disables the use of the /surface command</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int setAllowSurface(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        boolean arg = BoolArgumentType.getBool(context, "bool");

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
                    false);
        } else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true);
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/setDistanceToEntitiesKgi (int)"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Set the distance (in chunks) in which the ground items will be removed</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int setDistanceToEntitiesKgi(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        int arg = IntegerArgumentType.getInteger(context, "int");

        if (arg < 1 || arg > 64)
        {
            sendPlayerMessage(player,
                    line_start_error + "The distance must be in [1;64]",
                    null,
                    "cyan.message.incorrectIntKgi",
                    false);
            return 0;
        }

        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            CyanMidnightConfig.setDistanceToEntitiesKgi(arg);
            sendPlayerMessage(player,
                    line_start + "§3DistanceToEntitiesKgi option have been set to %s",
                    green + Integer.toString(arg),
                    "cyan.message.setDistanceToEntitiesKgi",
                    false);
        } else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true);
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/setRequiredOpLevelKgi (int)"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Set the minimum OP level required to execute the /kgi command</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int setRequiredOpLevelKgi(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        int arg = IntegerArgumentType.getInteger(context, "int");

        // If the argument passed to the command isn't in [0;4], the config file will not be modified and the function
        // stops here
        if (arg < 0 || arg > 4)
        {
            sendPlayerMessage(player,
                    line_start_error + "The OP level must be [0;1;2;3 or 4]",
                    null,
                    "cyan.message.incorrectIntOp",
                    false);
            return 0;
        }

        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            CyanMidnightConfig.setMinOpLevelExeKgi(arg);
            sendPlayerMessage(player,
                    line_start + "§3RequiredOpLevelKgi option have been set to %s",
                    green + Integer.toString(arg),
                    "cyan.message.setRequiredOpLevelKgi",
                    false);
        } else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true);
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/setUseOneLanguage [true|false]"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> Set the option to use only one language to the value</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The player receive a message saying that it doesn't have the required permission</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int setUseOneLanguage(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        boolean arg = BoolArgumentType.getBool(context, "bool");

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
                        false);
            } else
            {
                sendPlayerMessage(player,
                        notOP,
                        null,
                        "cyan.message.notOp",
                        true);
                return 0;
            }
        } else
        {
            sendPlayerMessage(player,
                    "§cThis command can only be used on servers",
                    null,
                    "cyan.message.only_serv",
                    true);
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

}
