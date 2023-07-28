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
import fr.aeldit.cyan.config.Config;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.aeldit.cyan.util.Utils.*;
import static fr.aeldit.cyanlib.lib.utils.TranslationsPrefixes.ERROR;

public class Locations
{
    public static class Location
    {
        private String name;
        private final String dimension;
        private final double x;
        private final double y;
        private final double z;
        private final float yaw;
        private final float pitch;

        public Location(String name, String dimension, double x, double y, double z, float yaw, float pitch)
        {
            this.name = name;
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getDimension()
        {
            return dimension;
        }

        public double getX()
        {
            return x;
        }

        public double getY()
        {
            return y;
        }

        public double getZ()
        {
            return z;
        }

        public float getYaw()
        {
            return yaw;
        }

        public float getPitch()
        {
            return pitch;
        }
    }

    private final List<Location> locations = Collections.synchronizedList(new ArrayList<>());
    private final TypeToken<List<Location>> locationsType = new TypeToken<>() {};
    private boolean isEditingFile = false;
    public static Path LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(CYAN_MODID + "/locations.json");

    public void add(Location location)
    {
        this.locations.add(location);
        write();
    }

    /**
     * Can only be called if the result of {@link Locations#locationExists} is true
     */
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

    public void rename(String locationName, String newLocationName)
    {
        for (Location location : this.locations)
        {
            if (location.getName().equals(locationName))
            {
                location.setName(newLocationName);
                write();
                break;
            }
        }
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
        this.locations.forEach(location -> locationsNames.add(location.getName()));

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
            if (location.getName().equals(locationName))
            {
                return this.locations.indexOf(location);
            }
        }
        return 0;
    }

    public boolean locationExists(String locationName)
    {
        for (Location location : this.locations)
        {
            if (location.getName().equals(locationName))
            {
                return true;
            }
        }
        return false;
    }

    public void teleport(ServerPlayerEntity player, String location)
    {
        if (CYAN_LIB_UTILS.isOptionAllowed(player, Config.ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
        {
            if (LOCATIONS.locationExists(location))
            {
                Locations.Location loc = LOCATIONS.getLocation(location);

                switch (loc.getDimension())
                {
                    case "overworld" ->
                            player.teleport(player.getServer().getWorld(World.OVERWORLD), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                    case "nether" ->
                            player.teleport(player.getServer().getWorld(World.NETHER), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                    case "end" ->
                            player.teleport(player.getServer().getWorld(World.END), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                }

                CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                        CYAN_LANGUAGE_UTILS.getTranslation("goToLocation"),
                        "cyan.msg.goToLocation",
                        Formatting.YELLOW + location
                );
            }
            else
            {
                CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                        CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "locationNotFound"),
                        "cyan.msg.locationNotFound",
                        Formatting.YELLOW + location
                );
            }
        }
    }

    public void readServer()
    {
        if (Files.exists(LOCATIONS_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(LOCATIONS_PATH);
                this.locations.addAll(gsonReader.fromJson(reader, locationsType));
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
        LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(CYAN_MODID + "/" + saveName + "/locations.json");
        checkOrCreateModDir(true);

        if (Files.exists(LOCATIONS_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(LOCATIONS_PATH);
                this.locations.addAll(gsonReader.fromJson(reader, locationsType));
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
                        CYAN_LOGGER.info("[CyanSetHome] Could not write the locations file because it is already being written (for more than 1 sec)");
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
