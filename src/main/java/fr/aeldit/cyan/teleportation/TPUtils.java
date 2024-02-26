package fr.aeldit.cyan.teleportation;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import static fr.aeldit.cyan.util.Utils.XP_TP_BASE_DISTANCE;

public class TPUtils
{
    public static int getRequiredXpLevelsToTp(@NotNull ServerPlayerEntity player)
    {
        double distanceX = player.getX() - player.getSpawnPointPosition().getX();
        double distanceZ = player.getZ() - player.getSpawnPointPosition().getZ();

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

        if (coordinatesDistance < XP_TP_BASE_DISTANCE)
        {
            return 1;
        }
        else
        {
            return 1 + coordinatesDistance / XP_TP_BASE_DISTANCE;
        }
    }
}
