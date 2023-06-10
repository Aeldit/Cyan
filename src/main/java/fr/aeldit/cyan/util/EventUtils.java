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

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static fr.aeldit.cyan.util.GsonUtils.*;
import static fr.aeldit.cyan.util.Utils.backTpExists;
import static fr.aeldit.cyan.util.Utils.getBackTpIndex;

public class EventUtils
{
    public static void saveDeadPlayersPos(@NotNull LivingEntity entity)
    {
        if (entity.isPlayer())
        {
            try
            {
                ArrayList<BackTp> backTps = new ArrayList<>();

                if (!Files.exists(BACK_TP_PATH))
                {
                    Files.createFile(BACK_TP_PATH);
                }

                if (!Files.readAllLines(BACK_TP_PATH).isEmpty())
                {
                    backTps = readBackTpFile();
                }

                if (backTpExists(backTps, entity.getUuidAsString()))
                {
                    backTps.remove(getBackTpIndex(backTps, entity.getUuidAsString()));
                }

                if (entity.getWorld() == entity.getServer().getWorld(World.OVERWORLD))
                {
                    backTps.add(new BackTp(entity.getUuidAsString(), "overworld", entity.getX(), entity.getY(), entity.getZ()));
                }
                else if (entity.getWorld() == entity.getServer().getWorld(World.NETHER))
                {
                    backTps.add(new BackTp(entity.getUuidAsString(), "nether", entity.getX(), entity.getY(), entity.getZ()));
                }
                else
                {
                    backTps.add(new BackTp(entity.getUuidAsString(), "end", entity.getX(), entity.getY(), entity.getZ()));
                }

                writeBackTp(backTps);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
