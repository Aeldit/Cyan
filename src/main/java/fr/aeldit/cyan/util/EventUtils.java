/*
 * Copyright (c) 2023  -  Made by Aeldit
 *
 *              GNU LESSER GENERAL PUBLIC LICENSE
 *                  Version 3, 29 June 2007
 *
 *  Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *  Everyone is permitted to copy and distribute verbatim copies
 *  of this license document, but changing it is not allowed.
 *
 *
 * This version of the GNU Lesser General Public License incorporates
 * the terms and conditions of version 3 of the GNU General Public
 * License, supplemented by the additional permissions listed in the LICENSE.txt file
 * in the repo of this mod (https://github.com/Aeldit/Cyan)
 */

package fr.aeldit.cyan.util;

import fr.aeldit.cyan.teleportation.BackTps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static fr.aeldit.cyan.teleportation.BackTps.BACK_TP_PATH;
import static fr.aeldit.cyan.util.Utils.BACK_TPS;
import static fr.aeldit.cyan.util.Utils.CYAN_LOGGER;

public class EventUtils
{
    public static void saveDeadPlayersPos(@NotNull LivingEntity entity)
    {
        if (entity.isPlayer())
        {
            String playerUUID = entity.getUuidAsString();

            if (BACK_TPS.backTpExists(playerUUID))
            {
                CYAN_LOGGER.info("a");
                BACK_TPS.remove(playerUUID);
            }

            if (entity.getWorld() == entity.getServer().getWorld(World.OVERWORLD))
            {
                BACK_TPS.add(new BackTps.BackTp(playerUUID, "overworld", entity.getX(), entity.getY(), entity.getZ(),
                        new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())));
            }
            else if (entity.getWorld() == entity.getServer().getWorld(World.NETHER))
            {
                BACK_TPS.add(new BackTps.BackTp(playerUUID, "nether", entity.getX(), entity.getY(), entity.getZ(),
                        new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())));
            }
            else
            {
                BACK_TPS.add(new BackTps.BackTp(playerUUID, "end", entity.getX(), entity.getY(), entity.getZ(),
                        new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())));
            }
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
