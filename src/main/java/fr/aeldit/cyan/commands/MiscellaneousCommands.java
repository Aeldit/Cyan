package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import static fr.aeldit.cyan.util.Constants.*;
import static fr.aeldit.cyanlib.util.ChatUtil.sendPlayerMessage;

/**
 * @since 0.0.2
 */
public class MiscellaneousCommands {
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher) {
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
    public static int kgi(@NotNull CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            source.getServer().sendMessage(Text.of(getErrorTraduction("playerOnlyCmd")));
            return 0;
        } else {
            if (CyanMidnightConfig.allowKgi) {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi)) {
                    source.getServer().getCommandManager().executeWithPrefix(source, "/kill @e[type=minecraft:item,distance=..%d]".formatted(CyanMidnightConfig.distanceToEntitiesKgi * 16));
                    sendPlayerMessage(player,
                            getCmdFeedbackTraduction("kgi"),
                            null,
                            "cyan.message.kgi",
                            CyanMidnightConfig.msgToActionBar,
                            CyanMidnightConfig.useTranslations
                    );


                    return Command.SINGLE_SUCCESS;
                } else {
                    sendPlayerMessage(player,
                            getErrorTraduction("notOp"),
                            null,
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                    return 0;
                }
            } else {
                sendPlayerMessage(player,
                        getErrorTraduction("disabled.kgi"),
                        null,
                        "cyan.message.disabled.kgi",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
                return 0;
            }
        }
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
    public static int kgir(@NotNull CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        int arg = IntegerArgumentType.getInteger(context, "radius_in_chunks");

        if (player == null) {
            source.getServer().sendMessage(Text.of(getErrorTraduction("playerOnlyCmd")));
            return 0;
        } else {
            if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi)) {
                if (CyanMidnightConfig.allowKgi) {
                    source.getServer().getCommandManager().executeWithPrefix(source, "/kill @e[type=item,distance=..%d]".formatted(arg * 16));
                    sendPlayerMessage(player,
                            getCmdFeedbackTraduction("kgir").formatted(gold + Integer.toString(arg)),
                            gold + Integer.toString(arg),
                            "cyan.message.kgir",
                            CyanMidnightConfig.msgToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                    return Command.SINGLE_SUCCESS;
                } else {
                    sendPlayerMessage(player,
                            getErrorTraduction("disabled.kgi"),
                            null,
                            "cyan.message.disabled.kgi",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                    return 0;
                }
            } else {
                sendPlayerMessage(player,
                        getErrorTraduction("notOp"),
                        null,
                        "cyan.message.notOp",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
                return 0;
            }
        }
    }
}
