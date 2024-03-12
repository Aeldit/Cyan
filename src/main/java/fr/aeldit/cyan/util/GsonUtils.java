/*
 * Copyright (c) 2023-2024  -  Made by Aeldit
 *
 *               GNU LESSER GENERAL PUBLIC LICENSE
 *                   Version 3, 29 June 2007
 *
 *   Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing it is not allowed.
 *
 *
 *  This version of the GNU Lesser General Public License incorporates
 *  the terms and conditions of version 3 of the GNU General Public
 *  License, supplemented by the additional permissions listed in the LICENSE.txt file
 *  in the repo of this mod (https://github.com/Aeldit/Cyan)
 */

package fr.aeldit.cyan.util;

import fr.aeldit.cyan.teleportation.BackTps;
import fr.aeldit.cyan.teleportation.Locations;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import static fr.aeldit.cyan.teleportation.BackTps.BACK_TP_PATH;
import static fr.aeldit.cyan.teleportation.Locations.LOCATIONS_PATH;
import static fr.aeldit.cyan.util.Utils.BACK_TPS;
import static fr.aeldit.cyan.util.Utils.LOCATIONS;

public class GsonUtils
{
    public static void transferPropertiesToGson()
    {
        Path path = Path.of(LOCATIONS_PATH.toString().replace("json", "properties"));

        if (Files.exists(path))
        {
            try
            {
                Properties properties = new Properties();
                FileInputStream fis = new FileInputStream(path.toFile());
                properties.load(fis);
                fis.close();

                for (String locationName : properties.stringPropertyNames())
                {
                    if (LOCATIONS.locationNotFound(locationName))
                    {
                        String[] splitProperty = properties.getProperty(locationName).split(" ");

                        LOCATIONS.add(new Locations.Location(
                                locationName,
                                splitProperty[0],
                                Double.parseDouble(splitProperty[1]),
                                Double.parseDouble(splitProperty[2]),
                                Double.parseDouble(splitProperty[3]),
                                Float.parseFloat(splitProperty[4]),
                                Float.parseFloat(splitProperty[5])
                        ));
                    }
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        path = Path.of(BACK_TP_PATH.toString().replace("json", "properties"));

        if (Files.exists(path))
        {
            try
            {
                Properties properties = new Properties();
                FileInputStream fis = new FileInputStream(path.toFile());
                properties.load(fis);
                fis.close();

                BACK_TPS.readServer();

                for (String playerUUID : properties.stringPropertyNames())
                {
                    if (!BACK_TPS.backTpExists(playerUUID))
                    {
                        String[] splitProperty = properties.getProperty(playerUUID).split(" ");

                        BACK_TPS.add(new BackTps.BackTp(
                                playerUUID,
                                splitProperty[0],
                                Double.parseDouble(splitProperty[1]),
                                Double.parseDouble(splitProperty[2]),
                                Double.parseDouble(splitProperty[3]),
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
}
