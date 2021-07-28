package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class MiscellaneousCommands  {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("killgrounditems")
                .executes(MiscellaneousCommands::kgi)
        );
        dispatcher.register(CommandManager.literal("kgi")
                .executes(MiscellaneousCommands::kgi)
        );
    }

    /**
     * Called when a player execute the command "/killgrounditems" or "/kgi"
     *      -> If the player has a permission level equal to 4
     *          The ground items are killed and the player is notified by a message that the entities where killed
     *
     *      -> Else, the ground items are killed and a message is send to the console and to the OPs
     *
     * Teleport the player to the highest block that was found on the player's coordinates before being teleported
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct (ex: "/sethome ba se" will throw
     *                                an exception because there is two arguments instead of one)
     */
    public static int kgi(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        // If OP with max level (4)
        if(player.hasPermissionLevel(4)) {
            source.getServer().getCommandManager().execute(source,"/kill @e[type=item]");
            source.getPlayer().sendMessage(new TranslatableText("cyan.message.kgi"), true);
        }
        // If not OP
        else {
            source.sendFeedback(new TranslatableText("cyan.message.kgi"), true);
        }
        return Command.SINGLE_SUCCESS;
    }
}