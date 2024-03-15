package fr.aeldit.cyan.util;

import fr.aeldit.cyan.teleportation.BackTps;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static fr.aeldit.cyan.teleportation.BackTps.BACK_TP_PATH;
import static fr.aeldit.cyan.CyanCore.BACK_TPS;

public class EventUtils
{
    public static void saveDeadPlayersPos(@NotNull LivingEntity entity)
    {
        if (entity.isPlayer())
        {
            String playerUUID = entity.getUuidAsString();

            if (BACK_TPS.backTpExists(playerUUID))
            {
                BACK_TPS.remove(playerUUID);
            }

            BACK_TPS.add(new BackTps.BackTp(playerUUID,
                            entity.getWorld()
                                    .getDimensionKey()
                                    .getValue()
                                    .toString()
                                    .replace("minecraft:", "")
                                    .replace("the_", ""),
                            entity.getX(), entity.getY(), entity.getZ(),
                            new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())
                    )
            );
        }
    }

    public static void removeOutdatedBackTps()
    {
        if (Files.exists(BACK_TP_PATH))
        {
            BACK_TPS.removeAllOutdated();
        }
    }
}
