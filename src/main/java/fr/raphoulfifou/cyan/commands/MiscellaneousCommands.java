package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @since 0.0.2
 * @author Raphoulfifou
 */
public class MiscellaneousCommands
{

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("killgrounditems")
                .then(CommandManager.argument("distance_in_chunks", IntegerArgumentType.integer())
                        .executes(MiscellaneousCommands::kgir)
                )
            .executes(MiscellaneousCommands::kgi)
        );
        dispatcher.register(CommandManager.literal("kgi")
                .then(CommandManager.argument("distance_in_chunks", IntegerArgumentType.integer())
                        .executes(MiscellaneousCommands::kgir)
                )
            .executes(MiscellaneousCommands::kgi)
        );

        dispatcher.register(CommandManager.literal("Chelp")
            .executes(MiscellaneousCommands::helpCyan)
        );

        dispatcher.register(CommandManager.literal("ops")
            .executes(MiscellaneousCommands::ops)
        );

        dispatcher.register(CommandManager.literal("mods")
            .executes(MiscellaneousCommands::mods)
        );
    }

    /**
     * <p>Called when a player execute the command "/killgrounditems" or "/kgi"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> The ground items are killed and the player is notified by a message that the entities where killed</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The ground items are killed and a message is send to the console and to the OPs</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int kgi(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (CyanMidnightConfig.allowKgi)
        {
            // If OP with defined level (4 by default)
            if(player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi)) {
                source.getServer().getCommandManager().execute(source,String.format("/kill @e[type=item,distance=..%d]",
                        CyanMidnightConfig.distanceToEntitiesKgi*16)); // Default distance is 14 chunks, but can be changed in settings
                source.getPlayer().sendMessage(new TranslatableText("cyan.message.kgi"), true);
            }
            // If not OP or not OP with max level
            else {
                source.getServer().getCommandManager().execute(source,String.format("/kill @e[type=item,distance=..%d]",
                        CyanMidnightConfig.distanceToEntitiesKgi*16));
                source.sendFeedback(new TranslatableText("cyan.message.kgi"), true);
            }
        }
        else
        {
            player.sendMessage(new TranslatableText("cyan.message.disabled.kgi"), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/killgrounditems (int)" or "/kgi (int)"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> The ground items are killed in a range of the given arg and the player is notified by a message that the entities where killed</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The ground items are killed and a message is send to the console and to the OPs</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int kgir(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        int arg = IntegerArgumentType.getInteger(context, "distance_in_chunks");

        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            // If OP with defined level (4 by default)
            if(player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi)) {
                source.getServer().getCommandManager().execute(source,String.format("/kill @e[type=item,distance=..%d]",
                        arg*16));   // Default distance is 14 chunks, but can be changed in settings or with commands
                source.getPlayer().sendMessage(new TranslatableText("cyan.message.kgir",arg), true);
            }
            // If not OP or not OP with max level
            else {
                source.getServer().getCommandManager().execute(source,String.format("/kill @e[type=item,distance=..%d]",
                        arg*16));
                source.sendFeedback(new TranslatableText("cyan.message.kgir",arg), true);
            }
        }
        else
        {
            player.sendMessage(new TranslatableText("cyan.message.disabled.kgi"), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/Chelp"</p>
     *
     * <ul>Displays Help for the mod in the chat</ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int helpCyan(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();

        player.sendMessage(new TranslatableText("cyan.message.help.1"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.2"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.3"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.4"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.5"), false);

        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/ops"</p>
     *
     * <ul>If the player has a permission level equal to the level defined in the config (4 by default)
     *      <li>-> A list with all the op players is displayed</li>
     * </ul>
     * <ul>Else:
     *      <li>-> A message tells the player taht he/she don't have the required permission</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int ops(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        // If OP with max level (4)
        if(player.hasPermissionLevel(4)) {
            source.getPlayer().sendMessage(new TranslatableText("cyan.message.ops", Arrays.toString(source.getServer().getPlayerManager().getOpNames())), false);
        }
        // If not OP or not OP with max level
        else {
            player.sendMessage(new TranslatableText("cyan.message.notOp"), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/mods"</p>
     *
     * A list of all mods installed on the server
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int mods(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();

        source.getPlayer().sendMessage(new TranslatableText("cyan.message.mods",
                Arrays.toString(FabricLoader.getInstance().getAllMods().toArray())), false);
        
        return Command.SINGLE_SUCCESS;
    }
}