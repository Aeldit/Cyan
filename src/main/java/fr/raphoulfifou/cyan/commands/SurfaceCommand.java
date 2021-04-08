package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.Heightmap;

public class SurfaceCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();

        int x = source.getPlayer().getBlockPos().getX();
        int z = source.getPlayer().getBlockPos().getZ();
        int y = source.getPlayer().world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);
        source.getPlayer().teleport(world, x, y, z, 0, 0);
        source.getPlayer().sendMessage(new TranslatableText("cyan.message.surface"), true);
        return 1;
    }
}