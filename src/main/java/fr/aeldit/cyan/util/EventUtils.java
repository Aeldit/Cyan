package fr.aeldit.cyan.util;

import fr.aeldit.cyan.teleportation.BackTp;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static fr.aeldit.cyan.CyanCore.BACK_TPS;

public class EventUtils
{
    public static void saveDeadPlayersPos(@NotNull LivingEntity entity)
    {
        String playerUUID = entity.getUuidAsString();

        if (BACK_TPS.backTpExists(playerUUID))
        {
            BACK_TPS.remove(playerUUID);
        }

        BACK_TPS.add(
                new BackTp(
                        playerUUID,
                        entity.getWorld()
                              //? if <1.20.6 {
                              /*.getDimensionKey().getValue().toString()
                               *///?} else {
                              .getDimensionEntry().getIdAsString()
                              //?}
                              .replace("minecraft:", "").replace("the_", ""),
                        entity.getX(), entity.getY(), entity.getZ(),
                        new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())
                )
        );
    }
}
