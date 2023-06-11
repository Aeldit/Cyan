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

import fr.aeldit.cyan.teleportation.BackTp;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static fr.aeldit.cyan.util.Utils.*;

public class EventUtils
{
    public static void saveDeadPlayersPos(@NotNull LivingEntity entity)
    {
        if (entity.isPlayer())
        {
            String playerUUID = entity.getUuidAsString();

            if (BackTpsObj.backTpExists(playerUUID))
            {
                LOGGER.info("a");
                BackTpsObj.remove(playerUUID);
            }

            if (entity.getWorld() == entity.getServer().getWorld(World.OVERWORLD))
            {
                BackTpsObj.add(new BackTp(playerUUID, "overworld", entity.getX(), entity.getY(), entity.getZ(),
                        new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())));
            }
            else if (entity.getWorld() == entity.getServer().getWorld(World.NETHER))
            {
                BackTpsObj.add(new BackTp(playerUUID, "nether", entity.getX(), entity.getY(), entity.getZ(),
                        new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())));
            }
            else
            {
                BackTpsObj.add(new BackTp(playerUUID, "end", entity.getX(), entity.getY(), entity.getZ(),
                        new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())));
            }
        }
    }

    public static void onGameStop()
    {
        Path modPath = FabricLoader.getInstance().getConfigDir().resolve(MODID);

        if (Files.exists(modPath))
        {
            BackTpsObj.removeAllOutdated();

            if (new File(modPath.toUri()).listFiles().length == 0)
            {
                try
                {
                    Files.delete(modPath);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
