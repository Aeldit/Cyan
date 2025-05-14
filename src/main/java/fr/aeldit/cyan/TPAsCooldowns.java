package fr.aeldit.cyan;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static fr.aeldit.cyan.CyanCore.TPAS;

public class TPAsCooldowns
{
    // Holds the cooldown start time for each player
    private static final ConcurrentHashMap<ServerPlayerEntity, Tuple> playersCooldowns = new ConcurrentHashMap<>();
    private static final List<ServerPlayerEntity> canceledCooldowns = Collections.synchronizedList(new ArrayList<>());

    public static void cancelCooldown(ServerPlayerEntity player)
    {
        canceledCooldowns.add(player);
        TPAS.removePlayerFromQueue(
                player.getName().getString(), playersCooldowns.get(player).requestedPlayer().getName().getString());
        playersCooldowns.remove(player);
    }

    public static List<ServerPlayerEntity> getCanceledCooldowns()
    {
        return canceledCooldowns;
    }

    public static void clearCanceledCooldowns()
    {
        canceledCooldowns.clear();
    }

    public static void addPlayerCooldown(
            ServerPlayerEntity requestingPlayer, long cooldown, long startTime, int requiredXpLevel,
            ServerPlayerEntity requestedPlayer
    )
    {
        playersCooldowns.put(
                requestingPlayer, new Tuple(cooldown, startTime, requiredXpLevel, requestedPlayer));
    }

    public static @NotNull HashMap<ServerPlayerEntity, Tuple> getPlayersCompletedCooldowns()
    {
        long currentTime = System.currentTimeMillis();
        HashMap<ServerPlayerEntity, Tuple> completedCooldowns = new HashMap<>();
        for (Map.Entry<ServerPlayerEntity, Tuple> entry : playersCooldowns.entrySet())
        {
            if (currentTime - entry.getValue().startTime() > entry.getValue().cooldown())
            {
                completedCooldowns.put(entry.getKey(), entry.getValue());
            }
        }
        completedCooldowns.forEach(playersCooldowns::remove);
        return completedCooldowns;
    }

    public record Tuple(long cooldown, long startTime, int requiredXpLevel, ServerPlayerEntity requestedPlayer)
    {
    }
}
