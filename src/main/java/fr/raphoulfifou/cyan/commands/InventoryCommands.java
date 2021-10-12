package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.CommandDispatcher;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class InventoryCommands {

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("craft")
                        //.then(CommandManager.argument("playerName", GameProfileArgumentType.gameProfile())
                                //.suggests(ArgumentSuggestion::getAllPlayerNames)
                                //.executes(TeleportationCommands::playerBed)
                        //)
                //.executes(InventoryCommands::craft)
        );
    }

    /**
     * <p>Called when a player execute the commands "/craft" or "/c"</p>
     *
     * <ul>Get:
     *     <li>-> the x and z coordinates of the player (x, z)</li>
     *     <li>-> the higher block at the y position of the player (y)</li>
     *     <li>-> the yaw and pitch of the player -> his eyes position (yaw, pitch)</li>
     * </ul>
     * <p>Teleport the player to the highest block that was found on the player's coordinates before being teleported</p>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    /*public static int craft(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerWorld world = context.getSource().getWorld();

        int x = player.getBlockPos().getX();
        int y = player.getBlockPos().getY();
        int z = player.getBlockPos().getZ();

        //player.openHandledScreen(CraftingInventory.class);
        return Command.SINGLE_SUCCESS;
    }*/
    
}
