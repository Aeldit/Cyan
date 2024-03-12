/*
 * Copyright (c) 2024  -  Made by Aeldit
 *
 *               GNU LESSER GENERAL PUBLIC LICENSE
 *                   Version 3, 29 June 2007
 *
 *   Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing it is not allowed.
 *
 *
 *  This version of the GNU Lesser General Public License incorporates
 *  the terms and conditions of version 3 of the GNU General Public
 *  License, supplemented by the additional permissions listed in the LICENSE.txt file
 *  in the repo of this mod (https://github.com/Aeldit/Cyan)
 */

package fr.aeldit.cyan.teleportation;

import fr.aeldit.cyanlib.lib.config.IntegerOption;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TPUtils
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

    public static List<String> getRequestingPlayers(String requestedPlayer)
    {
        return PLAYERS_TPA_QUEUES.getOrDefault(requestedPlayer, null);
    }

    public static int getRequiredXpLevelsToTp(
            @NotNull ServerPlayerEntity player, @NotNull BlockPos tpPos, IntegerOption opt
    )
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

        if (coordinatesDistance < opt.getValue())
        {
            return 1;
        }
        else
        {
            return 1 + coordinatesDistance / opt.getValue();
        }
    }

    public static void removePlayerOnQuit(String playerName)
    {
        for (List<String> queue : PLAYERS_TPA_QUEUES.values())
        {
            queue.remove(playerName);
        }
    }
}
