package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;

import java.util.Objects;

public class BedCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
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
        return 1;
    }
}