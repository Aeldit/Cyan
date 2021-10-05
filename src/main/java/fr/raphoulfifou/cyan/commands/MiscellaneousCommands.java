package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.2
 * @author Raphoulfifou
 */
public class MiscellaneousCommands  {

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("killgrounditems")
                .executes(MiscellaneousCommands::kgi)
        );
        dispatcher.register(CommandManager.literal("kgi")
                .executes(MiscellaneousCommands::kgi)
        );

        dispatcher.register(CommandManager.literal("Chelp")
                .executes(MiscellaneousCommands::helpCyan)
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

        // If OP with max level (4)
        if(player.hasPermissionLevel(4)) {
            source.getServer().getCommandManager().execute(source,"/kill @e[type=item]");
            source.getPlayer().sendMessage(new TranslatableText("cyan.message.kgi"), true);
        }
        // If not OP or not OP with max level
        else {
            source.getServer().getCommandManager().execute(source,"/kill @e[type=item]");
            source.sendFeedback(new TranslatableText("cyan.message.kgi"), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/Chelp"</p>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int helpCyan(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        //String mod = StringArgumentType.getString(context, "mod");

        /*if (mod != null)
        {
            if (mod.equals("Cyan"))
            {
                player.sendMessage(new TranslatableText("cyan.message.help"), false);
            }
        }*/
        player.sendMessage(new TranslatableText("cyan.message.help.1"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.2"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.3"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.4"), false);

        return Command.SINGLE_SUCCESS;
    }
}