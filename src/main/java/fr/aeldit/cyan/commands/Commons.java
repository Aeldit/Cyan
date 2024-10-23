package fr.aeldit.cyan.commands;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

import static fr.aeldit.cyan.CyanCore.CYAN_LANG_UTILS;

public class Commons
{
    public static @Nullable BlockPos bed(@NotNull ServerPlayerEntity player)
    {
        BlockPos spawnPos = player.getSpawnPointPosition();
        MinecraftServer server = player.getServer();
        if (spawnPos == null || server == null)
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.bedNotFound");
            return null;
        }

        RegistryKey<World> spawnDim = player.getSpawnPointDimension();

        //? if >=1.21.2 {
        player.teleport(
                server.getWorld(spawnDim),
                spawnPos.getX(),
                spawnPos.getY(),
                spawnPos.getZ(),
                new HashSet<>(),
                player.getYaw(), player.getPitch(),
                false
        );
        //?} else {
        /*player.teleport(
                server.getWorld(spawnDim),
                spawnPos.getX(),
                spawnPos.getY(),
                spawnPos.getZ(),
                player.getYaw(), player.getPitch()
        );
        *///?}

        String key = spawnDim == World.OVERWORLD ? "bed" : "respawnAnchor";
        CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.%s".formatted(key));
        return spawnPos;
    }

    public static double surface(@NotNull ServerPlayerEntity player)
    {
        BlockPos blockPos = player.getBlockPos();
        double topY = player.getWorld().getTopY(Heightmap.Type.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());

        //? if >=1.21.2 {
        player.teleport(
                player.getServerWorld(),
                blockPos.getX(),
                topY,
                blockPos.getZ(),
                new HashSet<>(),
                player.getYaw(), player.getPitch(),
                false
        );
        //?} elif >1.19.4 {
        /*player.teleport(
                player.getServerWorld(),
                blockPos.getX(),
                topY,
                blockPos.getZ(),
                player.getYaw(), player.getPitch()
        );
        *///?} else {
        /*player.teleport(
                player.getWorld(),
                blockPos.getX(),
                topY,
                blockPos.getZ(),
                player.getYaw(), player.getPitch()
        );
        *///?}
        CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.surface");
        return topY;
    }
}
