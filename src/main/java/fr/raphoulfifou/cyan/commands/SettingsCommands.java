package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.raphoulfifou.cyan.commands.argumentTypes.ArgumentSuggestion;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import fr.raphoulfifou.cyan.util.CyanOption;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static fr.raphoulfifou.cyan.util.ChatConstants.*;
import static fr.raphoulfifou.cyanlib.util.ChatUtil.sendPlayerMessage;

/**
 * @since 0.2.6
 */
public class SettingsCommands
{

    public static Formatting color = Formatting.GREEN;

    private final Map<String, CyanOption> options = new HashMap<>();

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("cyan")
                .then(CommandManager.argument("text", StringArgumentType.word())
                        .suggests(SuggestionsBuilder::)
                        .then(CommandManager.argument("boolean", BoolArgumentType.bool())
                                .executes(SettingsCommands::setBooleanOption)
                        )
                        .then(CommandManager.argument("integer", IntegerArgumentType.integer())
                                .executes(SettingsCommands::setBooleanOption)
                        )
                )
        );
    }

    // Set

    public static int setBooleanOption(@NotNull CommandContext<ServerCommandSource> context)
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

        return Command.SINGLE_SUCCESS;
    }

}
