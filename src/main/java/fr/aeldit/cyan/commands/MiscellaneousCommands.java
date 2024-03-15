package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.config.CyanConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import static fr.aeldit.cyan.config.CyanConfig.*;
import static fr.aeldit.cyan.util.Utils.CYAN_LANGUAGE_UTILS;
import static fr.aeldit.cyan.util.Utils.CYAN_LIB_UTILS;

public class MiscellaneousCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("kill-ground-items")
                .then(CommandManager.argument("radius_in_chunks", IntegerArgumentType.integer())
                        .executes(MiscellaneousCommands::kgir)
                )
                .executes(MiscellaneousCommands::kgi)
        );
        dispatcher.register(CommandManager.literal("kgi")
                .then(CommandManager.argument("radius_in_chunks", IntegerArgumentType.integer())
                        .executes(MiscellaneousCommands::kgir)
                )
                .executes(MiscellaneousCommands::kgi)
        );
    }

    /**
     * Called when a player execute the command {@code /kill-ground-items} or {@code /kgi}
     * <p>
     * Kills all the items on the ground in the default radius (defined if {@link CyanConfig})
     */
    public static int kgi(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();

        if (CYAN_LIB_UTILS.isPlayer(source))
        {
            ServerPlayerEntity player = source.getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_KGI.getValue(), "kgiDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_KGI.getValue()))
                {
                    source.getServer().getCommandManager().executeWithPrefix(
                            source, "/kill @e[type=minecraft:item,distance=..%d]"
                                    .formatted(DISTANCE_TO_ENTITIES_KGI.getValue() * 16)
                    );

                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                            player,
                            CYAN_LANGUAGE_UTILS.getTranslation("kgi"),
                            "cyan.msg.kgi"
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the command {@code /kill-ground-items [int]} or {@code /kgi [int]}
     * <p>
     * Kills all the items on the ground in the specified radius
     */
    public static int kgir(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();

        if (CYAN_LIB_UTILS.isPlayer(source))
        {
            ServerPlayerEntity player = source.getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_KGI.getValue(), "kgiDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_KGI.getValue()))
                {
                    int arg = IntegerArgumentType.getInteger(context, "radius_in_chunks");

                    source.getServer().getCommandManager().executeWithPrefix(
                            source, "/kill @e[type=item,distance=..%d]".formatted(arg * 16)
                    );

                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                            player,
                            CYAN_LANGUAGE_UTILS.getTranslation("kgir")
                                    .formatted(Formatting.GOLD + Integer.toString(arg)),
                            "cyan.msg.kgir",
                            Formatting.GOLD + Integer.toString(arg)
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
