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
import fr.aeldit.cyan.teleportation.Location;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import static fr.aeldit.cyan.util.Utils.*;

public class GsonUtils
{
    public static void transferPropertiesToGson()
    {
        if (Files.exists(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/locations.properties")))
        {
            try
            {
                Properties properties = new Properties();
                FileInputStream fis = new FileInputStream(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/locations.properties").toFile());
                properties.load(fis);
                fis.close();

                LocationsObj.read();

                for (String locationName : properties.stringPropertyNames())
                {
                    if (!LocationsObj.locationExists(locationName))
                    {
                        LocationsObj.add(new Location(
                                locationName,
                                properties.getProperty(locationName).split(" ")[0],
                                Double.parseDouble(properties.getProperty(locationName).split(" ")[1]),
                                Double.parseDouble(properties.getProperty(locationName).split(" ")[2]),
                                Double.parseDouble(properties.getProperty(locationName).split(" ")[3]),
                                Float.parseFloat(properties.getProperty(locationName).split(" ")[4]),
                                Float.parseFloat(properties.getProperty(locationName).split(" ")[5])
                        ));
                    }
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        if (Files.exists(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/back.properties")))
        {
            try
            {
                Properties properties = new Properties();
                FileInputStream fis = new FileInputStream(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/back.properties").toFile());
                properties.load(fis);
                fis.close();

                BackTpsObj.read();

                for (String playerUUID : properties.stringPropertyNames())
                {
                    if (!BackTpsObj.backTpExists(playerUUID))
                    {
                        BackTpsObj.add(new BackTp(
                                playerUUID,
                                properties.getProperty(playerUUID).split(" ")[0],
                                Double.parseDouble(properties.getProperty(playerUUID).split(" ")[1]),
                                Double.parseDouble(properties.getProperty(playerUUID).split(" ")[2]),
                                Double.parseDouble(properties.getProperty(playerUUID).split(" ")[3]),
                                new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())
                        ));
                    }
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static void removePropertiesFiles(ServerPlayerEntity player)
    {
        boolean fileDeleted = false;

        try
        {
            if (Files.exists(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/back.properties")))
            {
                Files.delete(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/back.properties"));
                fileDeleted = true;
            }

            if (Files.exists(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/locations.properties")))
            {
                Files.delete(FabricLoader.getInstance().getConfigDir().resolve(MODID + "/locations.properties"));
                fileDeleted = true;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        if (fileDeleted)
        {
            CyanLibUtils.sendPlayerMessage(player,
                    CyanLanguageUtils.getTranslation("propertiesFilesDeleted"),
                    "cyan.message.propertiesFilesDeleted"
            );
        }
        else
        {
            CyanLibUtils.sendPlayerMessage(player,
                    CyanLanguageUtils.getTranslation("noPropertiesFiles"),
                    "cyan.message.noPropertiesFiles"
            );
        }
    }
}
