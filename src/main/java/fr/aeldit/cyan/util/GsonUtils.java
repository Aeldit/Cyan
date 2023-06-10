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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Properties;

import static fr.aeldit.cyan.util.Utils.*;

public class GsonUtils
{
    public static final Path LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/locations.json");
    public static final Path BACK_TP_PATH = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/back.json");

    public static ArrayList<Location> readLocationsFile()
    {
        try
        {
            Gson gsonReader = new Gson();
            Reader reader = Files.newBufferedReader(LOCATIONS_PATH);
            TypeToken<ArrayList<Location>> locationsType = new TypeToken<>() {};
            ArrayList<Location> locations = gsonReader.fromJson(reader, locationsType);
            reader.close();

            return locations;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<BackTp> readBackTpFile()
    {
        try
        {
            Gson gsonReader = new Gson();
            Reader reader = Files.newBufferedReader(BACK_TP_PATH);
            TypeToken<ArrayList<BackTp>> backTpType = new TypeToken<>() {};
            ArrayList<BackTp> locations = gsonReader.fromJson(reader, backTpType);
            reader.close();

            return locations;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void writeLocations(@NotNull ArrayList<Location> locations)
    {
        try
        {
            if (locations.isEmpty())
            {
                Files.delete(LOCATIONS_PATH);
            }
            else
            {
                Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
                Writer writer = Files.newBufferedWriter(LOCATIONS_PATH);
                gsonWriter.toJson(locations, writer);
                writer.close();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void writeBackTp(ArrayList<BackTp> backTps)
    {
        try
        {
            Gson gsonWriter = new GsonBuilder().create();
            Writer writer = Files.newBufferedWriter(BACK_TP_PATH);
            gsonWriter.toJson(backTps, writer);
            writer.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

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

                ArrayList<Location> locations = new ArrayList<>();

                if (!Files.exists(LOCATIONS_PATH))
                {
                    for (String key : properties.stringPropertyNames())
                    {
                        locations.add(new Location(
                                key,
                                properties.getProperty(key).split(" ")[0],
                                Double.parseDouble(properties.getProperty(key).split(" ")[1]),
                                Double.parseDouble(properties.getProperty(key).split(" ")[2]),
                                Double.parseDouble(properties.getProperty(key).split(" ")[3]),
                                Float.parseFloat(properties.getProperty(key).split(" ")[4]),
                                Float.parseFloat(properties.getProperty(key).split(" ")[5])
                        ));
                    }
                }
                else
                {
                    locations = readLocationsFile();
                    ArrayList<String> existantLocations = new ArrayList<>();
                    locations.forEach(location -> existantLocations.add(location.name()));

                    for (String key : properties.stringPropertyNames())
                    {
                        if (!existantLocations.contains(key))
                        {
                            locations.add(new Location(
                                    key,
                                    properties.getProperty(key).split(" ")[0],
                                    Double.parseDouble(properties.getProperty(key).split(" ")[1]),
                                    Double.parseDouble(properties.getProperty(key).split(" ")[2]),
                                    Double.parseDouble(properties.getProperty(key).split(" ")[3]),
                                    Float.parseFloat(properties.getProperty(key).split(" ")[4]),
                                    Float.parseFloat(properties.getProperty(key).split(" ")[5])
                            ));
                        }
                    }
                }

                writeLocations(locations);
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

                ArrayList<BackTp> backTps = new ArrayList<>();

                if (!Files.exists(BACK_TP_PATH))
                {
                    for (String key : properties.stringPropertyNames())
                    {
                        backTps.add(new BackTp(
                                key,
                                properties.getProperty(key).split(" ")[0],
                                Double.parseDouble(properties.getProperty(key).split(" ")[1]),
                                Double.parseDouble(properties.getProperty(key).split(" ")[2]),
                                Double.parseDouble(properties.getProperty(key).split(" ")[3])
                        ));
                    }
                }
                else
                {
                    backTps = readBackTpFile();
                    ArrayList<String> existantbackTps = new ArrayList<>();
                    backTps.forEach(location -> existantbackTps.add(location.playerUUID()));

                    for (String key : properties.stringPropertyNames())
                    {
                        if (!existantbackTps.contains(key))
                        {
                            backTps.add(new BackTp(
                                    key,
                                    properties.getProperty(key).split(" ")[0],
                                    Double.parseDouble(properties.getProperty(key).split(" ")[1]),
                                    Double.parseDouble(properties.getProperty(key).split(" ")[2]),
                                    Double.parseDouble(properties.getProperty(key).split(" ")[3])
                            ));
                        }
                    }
                }

                writeBackTp(backTps);
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
