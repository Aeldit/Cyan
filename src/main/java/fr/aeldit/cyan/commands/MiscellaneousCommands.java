package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import static fr.aeldit.cyan.util.ChatConstants.*;
import static fr.aeldit.cyanlib.util.ChatUtil.sendPlayerMessage;

/**
 * @since 0.0.2
 */
public class MiscellaneousCommands
{

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("killgrounditems")
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
     * <p>Called when a player execute the command <code>/killgrounditems</code> or <code>/kgi</code></p>
     *
     * <ul>If the player has a permission level equal to the minimum required OP level
     *      <li>-> The ground items are removed and the player is notified by a message that the entities where removed</li>
     * </ul>
     * <ul>Else:
     *      <li>-> A message is send to the player saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int kgi(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        assert player != null;
        if (CyanMidnightConfig.allowKgi)
        {
            if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
            {
                // Default distance is 14 chunks, but can be changed in settings
                source.getServer().getCommandManager().executeWithPrefix(source, "/kill @e[type=minecraft:item,distance=..%d]".formatted(CyanMidnightConfig.distanceToEntitiesKgi * 16));
                sendPlayerMessage(player,
                        cyan + "Ground items have been removed",
                        null,
                        "cyan.message.kgi",
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
        } else
        {
            sendPlayerMessage(player,
                    red + "The /kgi command is disabled. To enable it, enter '/allowKgi true' in chat",
                    null,
                    "cyan.message.disabled.kgi",
                    CyanMidnightConfig.errorToActionBar,
                    CyanMidnightConfig.useTranslations
            );
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command <code>/killgrounditems [int]</code> or <code>/kgi [int]</code></p>
     *
     * <ul>If the player has a permission level equal to the minimum required OP level
     *      <li>-> The ground items are removed in the specified radius (in chunks) and the player is notified by a message that the entities where removed</li>
     * </ul>
     * <ul>Else:
     *      <li>-> A message is send to the player saying that it doesn't have the required permission</li>
     * </ul>
     */
    public static int kgir(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        int arg = IntegerArgumentType.getInteger(context, "radius_in_chunks");

        assert player != null;
        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            if (CyanMidnightConfig.allowKgi)
            {
                // The default distance is 14 chunks, but it can be changed in the config file or with commands
                source.getServer().getCommandManager().executeWithPrefix(source, "/kill @e[type=item,distance=..%d]".formatted(arg * 16));
                sendPlayerMessage(player,
                        cyan + "Ground items have been removed in a radius of %s §cchunks",
                        green + Integer.toString(arg),
                        "cyan.message.kgir",
                        CyanMidnightConfig.msgToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            } else
            {
                sendPlayerMessage(player,
                        red + "The /kgi command is disabled. To enable it, enter '/allowKgi true' in chat",
                        null,
                        "cyan.message.disabled.kgi",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
                return 0;
            }
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
