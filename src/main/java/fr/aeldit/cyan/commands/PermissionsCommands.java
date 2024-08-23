package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
                                                      )).executes(PermissionsCommands::executeForSelector)
                                        )
                        )
                )
        );
    }

    public static int executeForSelector(@NotNull CommandContext<ServerCommandSource> context)
    {
        return Command.SINGLE_SUCCESS;
    }
}
