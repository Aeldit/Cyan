package fr.aeldit.cyan.teleportation;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TPa
{
    private static final ConcurrentHashMap<String, List<String>> PLAYERS_TPA_QUEUES = new ConcurrentHashMap<>();

    /**
     * Adds {@code playerToAdd} to the list of players that requested to tp to {@code destinationPlayerName}
     *
     * @param playerToAdd           The player that requested the teleportation
     * @param destinationPlayerName The destination player
     */
    public static void addPlayerToQueue(String playerToAdd, String destinationPlayerName)
    {
        if (!PLAYERS_TPA_QUEUES.containsKey(destinationPlayerName))
        {
            PLAYERS_TPA_QUEUES.put(destinationPlayerName, Collections.synchronizedList(new ArrayList<>()));
        }

        if (!PLAYERS_TPA_QUEUES.get(destinationPlayerName).contains(playerToAdd))
        {
            PLAYERS_TPA_QUEUES.get(destinationPlayerName).add(playerToAdd);
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

    public static @Nullable List<String> getRequestingPlayers(String requestedPlayer)
    {
        // TODO -> Remove manual check as the get function already does it
        if (PLAYERS_TPA_QUEUES.containsKey(requestedPlayer))
        {
            return PLAYERS_TPA_QUEUES.get(requestedPlayer);
        }
        return null;
    }

    public static void removePlayerOnQuit(String playerName)
    {
        for (List<String> queue : PLAYERS_TPA_QUEUES.values())
        {
            queue.remove(playerName);
        }
    }
}
