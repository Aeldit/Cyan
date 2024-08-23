package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static fr.aeldit.cyan.CyanCore.CYAN_LIB_UTILS;
import static fr.aeldit.cyan.config.CyanLibConfigImpl.MIN_OP_LVL_PERM_NODES;

public class PermissionsCommands
{
    private static final List<String> COMMANDS = List.of("bed", "surface");

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(
                CommandManager.literal("cyan").then(
                        CommandManager.literal("execute").then(
                                CommandManager.argument("command", StringArgumentType.string())
                                        .suggests((context, builder) -> CommandSource.suggestMatching(
                                                COMMANDS, builder
                                        ))
                                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                                      .suggests((context, builder) -> CommandSource.suggestMatching(
                                                              context.getSource().getPlayerNames(), builder
                                                      )).executes(PermissionsCommands::executeForTargets)
                                        )
                        )
                )
        );
    }

    public static int executeForTargets(
            @NotNull CommandContext<ServerCommandSource> context
    ) throws CommandSyntaxException
    {
        ServerPlayerEntity playerSource = context.getSource().getPlayer();
        // If the command source is the not server's console (playerSource == null)
        // and the player doesn't have the required permission
        if (playerSource != null && !CYAN_LIB_UTILS.hasPermission(playerSource, MIN_OP_LVL_PERM_NODES.getValue()))
        {
            return 0;
        }

        String command = StringArgumentType.getString(context, "command");
        if (!COMMANDS.contains(command))
        {
            return 0;
        }

        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
        if (players.isEmpty())
        {
            return 0;
        }

        switch (command)
        {
            case "bed" -> TeleportationCommands.bedForTargets(players);
            case "surface" -> TeleportationCommands.surfaceForTargets(players);
        }

        return Command.SINGLE_SUCCESS;
    }
}
