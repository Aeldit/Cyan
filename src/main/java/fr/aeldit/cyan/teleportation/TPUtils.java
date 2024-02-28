package fr.aeldit.cyan.teleportation;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static fr.aeldit.cyan.config.CyanConfig.XP_REQUIRED_TO_TP_BASE_DISTANCE;

public class TPUtils
{
    private static final ConcurrentHashMap<String, List<String>> PLAYERS_TPA_QUEUES = new ConcurrentHashMap<>();

    public static void addPlayerToQueue(String playerToAdd, String destinationPlayerQueue)
    {
        if (!PLAYERS_TPA_QUEUES.containsKey(destinationPlayerQueue))
        {
            PLAYERS_TPA_QUEUES.put(destinationPlayerQueue, Collections.synchronizedList(new ArrayList<>()));
        }

        if (!PLAYERS_TPA_QUEUES.get(destinationPlayerQueue).contains(playerToAdd))
        {
            PLAYERS_TPA_QUEUES.get(destinationPlayerQueue).add(playerToAdd);
        }
    }

    public static void removePlayerFromQueue(String playerToRemove, String destinationPlayerQueue)
    {
        if (PLAYERS_TPA_QUEUES.containsKey(destinationPlayerQueue))
        {
            PLAYERS_TPA_QUEUES.get(destinationPlayerQueue).remove(playerToRemove);
        }
    }

    public static boolean isPlayerRequesting(String requestingPlayerName, String requestedPlayerName)
    {
        if (PLAYERS_TPA_QUEUES.containsKey(requestedPlayerName))
        {
            return PLAYERS_TPA_QUEUES.get(requestedPlayerName).contains(requestingPlayerName);
        }
        return false;
    }

    public static List<String> getRequestingPlayers(String requestedPlayer)
    {
        return PLAYERS_TPA_QUEUES.getOrDefault(requestedPlayer, null);
    }

    public static int getRequiredXpLevelsToTp(@NotNull ServerPlayerEntity player, @NotNull BlockPos tpPos)
    {
        double distanceX = player.getX() - tpPos.getX();
        double distanceZ = player.getZ() - tpPos.getZ();

        // Converts to a positive distance
        if (distanceX < 0)
        {
            distanceX *= -1;
        }
        if (distanceZ < 0)
        {
            distanceZ *= -1;
        }
        // Minecraft doesn't center the position to the middle of the block but in 1 corner,
        // so this allows for a better centering
        distanceX += 1;
        distanceZ += 1;

        int coordinatesDistance = (int) (distanceX + distanceZ) / 2;

        if (coordinatesDistance < XP_REQUIRED_TO_TP_BASE_DISTANCE.getValue())
        {
            return 1;
        }
        else
        {
            return 1 + coordinatesDistance / XP_REQUIRED_TO_TP_BASE_DISTANCE.getValue();
        }
    }
}
