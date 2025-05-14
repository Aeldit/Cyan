package fr.aeldit.cyan.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class VersionUtils
{
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ClickEvent getClickEvent(String command)
    {
        //? if =1.21.5 {
        return new ClickEvent.RunCommand(command);
        //?} else {
        /*return new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                command
        );
        *///?}
    }

    public static void tp(@NotNull ServerPlayerEntity playerToTp, @NotNull ServerPlayerEntity player)
    {
        //? if >=1.21.2-1.21.3 {
        playerToTp.teleport(
                player.getServerWorld(), player.getX(), player.getY(), player.getZ(), new HashSet<>(), 0, 0, false
        );
        //?} elif >1.19.4 {
        /*playerToTp.teleport(player.getServerWorld(), player.getX(), player.getY(), player.getZ(), 0, 0);
         *///?} else {
        /*playerToTp.teleport(player.getWorld(), player.getX(), player.getY(), player.getZ(), 0, 0);
         *///?}
    }
}
