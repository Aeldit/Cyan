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

package fr.aeldit.cyan.teleportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.aeldit.cyan.util.Utils.*;

public class Locations
{
    public record Location(String name, String dimension, double x, double y, double z, float yaw, float pitch) {}

    private final List<Location> locations;
    private final TypeToken<List<Location>> LOCATIONS_TYPE = new TypeToken<>() {};
    public static Path LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/locations.json");
    private boolean isEditingFile = false;

    public Locations()
    {
        this.locations = Collections.synchronizedList(new ArrayList<>());
    }

    public void add(Location location)
    {
        this.locations.add(location);
        write();
    }

    public void remove(String location)
    {
        this.locations.remove(getLocationIndex(location));
        write();
    }

    public boolean removeAll()
    {
        if (!this.locations.isEmpty())
        {
            this.locations.clear();
            write();

            return true;
        }
        return false;
    }

    public boolean isEmpty()
    {
        return this.locations.isEmpty();
    }

    public List<Location> getLocations()
    {
        return this.locations;
    }

    public List<String> getLocationsNames()
    {
        ArrayList<String> locationsNames = new ArrayList<>();
        this.locations.forEach(location -> locationsNames.add(location.name()));

        return locationsNames;
    }

    public Location getLocation(String locationName)
    {
        return this.locations.get(getLocationIndex(locationName));
    }

    public int getLocationIndex(String locationName)
    {
        for (Location location : this.locations)
        {
            if (location.name().equals(locationName))
            {
                return this.locations.indexOf(location);
            }
        }
        return -1;
    }

    public boolean locationExists(String locationName)
    {
        for (Location location : this.locations)
        {
            if (location.name().equals(locationName))
            {
                return true;
            }
        }
        return false;
    }

    public void readServer()
    {
        if (Files.exists(LOCATIONS_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(LOCATIONS_PATH);
                this.locations.addAll(gsonReader.fromJson(reader, LOCATIONS_TYPE));
                reader.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public void readClient(String saveName)
    {
        LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/" + saveName + "/locations.json");
        checkOrCreateModDir(true);

        if (Files.exists(LOCATIONS_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(LOCATIONS_PATH);
                this.locations.addAll(gsonReader.fromJson(reader, LOCATIONS_TYPE));
                reader.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private void write()
    {
        checkOrCreateModDir(true);

        try
        {
            if (this.locations.isEmpty())
            {
                if (Files.exists(LOCATIONS_PATH))
                {
                    Files.delete(LOCATIONS_PATH);
                    removeEmptyModDir(true);
                }
            }
            else
            {
                if (!this.isEditingFile)
                {
                    this.isEditingFile = true;

                    Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
                    Writer writer = Files.newBufferedWriter(LOCATIONS_PATH);
                    gsonWriter.toJson(this.locations, writer);
                    writer.close();

                    this.isEditingFile = false;
                }
                else
                {
                    long end = System.currentTimeMillis() + 1000; // 1 s
                    boolean couldWrite = false;

                    while (System.currentTimeMillis() < end)
                    {
                        if (!this.isEditingFile)
                        {
                            this.isEditingFile = true;

                            Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
                            Writer writer = Files.newBufferedWriter(LOCATIONS_PATH);
                            gsonWriter.toJson(this.locations, writer);
                            writer.close();

                            couldWrite = true;
                            this.isEditingFile = false;
                            break;
                        }
                    }

                    if (!couldWrite)
                    {
                        LOGGER.info("[CyanSetHome] Could not write the locations file because it is already being written (for more than 1 sec)");
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
