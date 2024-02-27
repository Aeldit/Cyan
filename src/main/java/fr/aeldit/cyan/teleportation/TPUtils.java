package fr.aeldit.cyan.teleportation;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import static fr.aeldit.cyan.config.CyanConfig.XP_REQUIRED_TO_TP_BASE_DISTANCE;

public class TPUtils
{
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
