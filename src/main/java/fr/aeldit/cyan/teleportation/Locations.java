/*
 * Copyright (c) 2023-2024  -  Made by Aeldit
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
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static fr.aeldit.cyan.util.Utils.*;

public class Locations
{
    public record Location(String name, String dimension, double x, double y, double z, float yaw, float pitch)
    {
    }

    private final List<Location> locations = Collections.synchronizedList(new ArrayList<>());
    private final TypeToken<List<Location>> locationsType = new TypeToken<>()
    {
    };
    private boolean isEditingFile = false;
    public static Path LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(CYAN_MODID + "/locations" +
            ".json");

    public boolean add(@NotNull Location location)
    {
        if (!locationExists(location.name()))
        {
            locations.add(location);
            write();

            return true;
        }
        return false;
    }

    /**
     * Removes the location named {@code locationName}
     *
     * @return {@code true} on success |{@code false} on failure
     */
    public boolean remove(String locationName)
    {
        int idx = getLocationIndex(locationName);
        if (idx != -1)
        {
            locations.remove(idx);
            write();

            return true;
        }
        return false;
    }

    public boolean removeAll()
    {
        if (!locations.isEmpty())
        {
            locations.clear();
            write();

            return true;
        }
        return false;
    }

    /**
     * Renames the location named {@code locationName} to {@code newLocationName}
     *
     * @return {@code true} on success | {@code false} on failure
     */
    public boolean rename(String locationName, String newLocationName)
    {
        int idx = getLocationIndex(locationName);
        if (idx != -1)
        {
            Location tmpLocation = locations.get(idx);
            locations.add(new Location(newLocationName,
                    tmpLocation.dimension, tmpLocation.x, tmpLocation.y, tmpLocation.z, tmpLocation.yaw,
                    tmpLocation.pitch
            ));
            locations.remove(tmpLocation);
            write();

            return true;
        }
        return false;
    }

    public boolean isEmpty()
    {
        return locations.isEmpty();
    }

    public List<Location> getLocations()
    {
        return locations;
    }

    public @NotNull CompletableFuture<Suggestions> getLocationsNames(@NotNull SuggestionsBuilder builder)
    {
        ArrayList<String> locationsNames = new ArrayList<>();
        for (Location location : locations)
        {
            locationsNames.add(location.name);
        }

        return CommandSource.suggestMatching(locationsNames, builder);
    }

    /**
     * @param locationName The name of the location
     * @return The location with the given name if it exists | {@code null} otherwise
     */
    public @Nullable Location getLocation(String locationName)
    {
        int idx = getLocationIndex(locationName);

        return idx == -1 ? null : locations.get(idx);
    }

    /**
     * Returns the index of the location with the name {@code locationName} | {@code -1} if the location doesn't exist
     */
    private int getLocationIndex(String locationName)
    {
        for (Location location : locations)
        {
            if (location.name().equals(locationName))
            {
                return locations.indexOf(location);
            }
        }
        return -1;
    }

    public boolean locationExists(String locationName)
    {
        for (Location location : locations)
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
                locations.addAll(gsonReader.fromJson(reader, locationsType));
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
        LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(CYAN_MODID + "/" + saveName + "/locations" +
                ".json");
        checkOrCreateModDir(true);

        if (Files.exists(LOCATIONS_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(LOCATIONS_PATH);
                locations.addAll(gsonReader.fromJson(reader, locationsType));
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
            if (locations.isEmpty())
            {
                if (Files.exists(LOCATIONS_PATH))
                {
                    Files.delete(LOCATIONS_PATH);
                    removeEmptyModDir(true);
                }
            }
            else
            {
                if (!isEditingFile)
                {
                    isEditingFile = true;

                    Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
                    Writer writer = Files.newBufferedWriter(LOCATIONS_PATH);
                    gsonWriter.toJson(locations, writer);
                    writer.close();

                    isEditingFile = false;
                }
                else
                {
                    long end = System.currentTimeMillis() + 1000; // 1 s
                    boolean couldWrite = false;

                    while (System.currentTimeMillis() < end)
                    {
                        if (!isEditingFile)
                        {
                            isEditingFile = true;

                            Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
                            Writer writer = Files.newBufferedWriter(LOCATIONS_PATH);
                            gsonWriter.toJson(locations, writer);
                            writer.close();

                            couldWrite = true;
                            isEditingFile = false;
                            break;
                        }
                    }

                    if (!couldWrite)
                    {
                        CYAN_LOGGER.info("[CyanSetHome] Could not write the locations file because it is already " +
                                "being written (for more than 1 sec)");
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
