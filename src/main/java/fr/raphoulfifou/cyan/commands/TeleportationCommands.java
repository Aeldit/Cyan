package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.Heightmap;

import java.util.Objects;

public class TeleportationCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("bed")
                .executes(TeleportationCommands::bed)
        );

        dispatcher.register(CommandManager.literal("surface")
                .executes(TeleportationCommands::surface)
        );
    }

    public static int bed(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = Objects.requireNonNull(source.getPlayer().getServer()).getOverworld();

        if(source.getPlayer().getSpawnPointPosition() != null) {
            int x = source.getPlayer().getSpawnPointPosition().getX();
            int y = source.getPlayer().getSpawnPointPosition().getY();
            int z = source.getPlayer().getSpawnPointPosition().getZ();
            source.getPlayer().teleport(world, x, y, z, 0, 0);
            source.getPlayer().sendMessage(new TranslatableText("cyan.message.bed"), true);
        }
        else {
            source.getPlayer().sendMessage(new TranslatableText("cyan.message.bed.notfound"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int surface(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();

        int x = source.getPlayer().getBlockPos().getX();
        int z = source.getPlayer().getBlockPos().getZ();
        int y = source.getPlayer().world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);
        source.getPlayer().teleport(world, x, y, z, 0, 0);
        source.getPlayer().sendMessage(new TranslatableText("cyan.message.surface"), true);
        return Command.SINGLE_SUCCESS;
    }
}