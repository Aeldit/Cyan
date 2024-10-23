package fr.aeldit.cyan.teleportation;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public record Location(String name, String dimension, double x, double y, double z, float yaw, float pitch)
{
    @Contract("_ -> new")
    public @NotNull Location getRenamed(String newName)
    {
        return new Location(newName, dimension, x, y, z, yaw, pitch);
    }

    public void teleport(@NotNull MinecraftServer server, ServerPlayerEntity player)
    {
        if (player != null)
        {
            //? if >=1.21.2 {
            switch (dimension)
            {
                case "overworld" ->
                        player.teleport(server.getWorld(World.OVERWORLD), x(), y(), z(), new HashSet<>(), 0, 0, false);
                case "nether" ->
                        player.teleport(server.getWorld(World.NETHER), x(), y(), z(), new HashSet<>(), 0, 0, false);
                case "end" -> player.teleport(server.getWorld(World.END), x(), y(), z(), new HashSet<>(), 0, 0, false);
            }
            //?} else {
            /*switch (dimension)
            {
                case "overworld" -> player.teleport(server.getWorld(World.OVERWORLD), x(), y(), z(), 0, 0);
                case "nether" -> player.teleport(server.getWorld(World.NETHER), x(), y(), z(), 0, 0);
                case "end" -> player.teleport(server.getWorld(World.END), x(), y(), z(), 0, 0);
            }
            *///?}
        }
    }
}
