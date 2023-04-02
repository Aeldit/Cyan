/*
 * Copyright (c) 2023
 *
 *              GNU LESSER GENERAL PUBLIC LICENSE
 *                        Version 3, 29 June 2007
 *
 *  Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *  Everyone is permitted to copy and distribute verbatim copies
 *  of this license document, but changing it is not allowed.
 *
 *
 *   This version of the GNU Lesser General Public License incorporates
 * the terms and conditions of version 3 of the GNU General Public
 * License, supplemented by the additional permissions listed in the LICENSE.txt file
 * in the repo of this mod (https://github.com/Aeldit/Cyan)
 */

package fr.aeldit.cyan.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

import static fr.aeldit.cyan.util.Utils.*;

public class EventUtils
{
    // Death Event
    public static final Path backTpPath = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/back.properties");

    public static void saveDeadPlayersPos(@NotNull LivingEntity entity)
    {
        if (entity.isPlayer())
        {
            ServerWorld overworld = Objects.requireNonNull(entity.getServer()).getWorld(World.OVERWORLD);
            ServerWorld nether = Objects.requireNonNull(entity.getServer()).getWorld(World.NETHER);
            ServerWorld end = Objects.requireNonNull(entity.getServer()).getWorld(World.END);
            checkOrCreateFile(backTpPath);
            String pos = null;
            if (entity.getWorld() == overworld)
            {
                pos = "overworld" + " " + entity.getX() + " " + entity.getY() + " " + entity.getZ();
            } else if (entity.getWorld() == nether)
            {
                pos = "nether" + " " + entity.getX() + " " + entity.getY() + " " + entity.getZ();
            } else if (entity.getWorld() == end)
            {
                pos = "end" + " " + entity.getX() + " " + entity.getY() + " " + entity.getZ();
            }
            setPropertiesKey(backTpPath, entity.getUuidAsString(), pos);
        }
    }
}
