package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class InventoryCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("craft")
                .executes(InventoryCommands::craft)
        );

        dispatcher.register(CommandManager.literal("ec")
                .executes(InventoryCommands::enderchest)
        );
    }

    public static int craft(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        NamedScreenHandlerFactory inv = source.getPlayer().getBlockState().createScreenHandlerFactory(source.getWorld(), player.getBlockPos());

        source.getPlayer().openHandledScreen(inv);
        return Command.SINGLE_SUCCESS;
    }

    public static int enderchest(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        EnderChestInventory inv = player.getEnderChestInventory();

        source.getPlayer().sendMessage(Text.of("In implementation"), true);
        return Command.SINGLE_SUCCESS;
    }
}