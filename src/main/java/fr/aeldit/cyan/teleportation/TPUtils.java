package fr.aeldit.cyan.teleportation;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class TPUtils
{
    public static int XP_TP_BASE_DISTANCE = 200; // 1 level for every 200 blocks (2 levels for 400 blocks, ...)
    public static int XP_TP_BASE_DISTANCE_Y = 50; // 1 level for every 50 blocks vertically (2 levels for 100 blocks, ...)

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
