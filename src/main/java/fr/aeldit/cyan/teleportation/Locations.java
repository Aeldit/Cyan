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
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.aeldit.cyan.config.CyanConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static fr.aeldit.cyan.util.Utils.*;
import static fr.aeldit.cyanlib.lib.utils.TranslationsPrefixes.ERROR;

public class Locations
{
    public record Location(String name, String dimension, double x, double y, double z, float yaw, float pitch) {}

    private final List<Location> locations = Collections.synchronizedList(new ArrayList<>());
    private final TypeToken<List<Location>> locationsType = new TypeToken<>() {};
    private boolean isEditingFile = false;
    public static Path LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(CYAN_MODID + "/locations.json");

    public void add(Location location)
    {
        locations.add(location);
        write();
    }

    /**
     * Removes the location named {@code locationName}
     *
     * @implNote Can only be called if the result of {@link Locations#locationExists} is {@code true}
     */
    public void remove(String locationName)
    {
        locations.remove(getLocationIndex(locationName));
        write();
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
     * @implNote Can only be called if the result of {@link #locationExists} is {@code true}
     */
    public void rename(String locationName, String newLocationName)
    {
        Location tmpLocation = locations.get(getLocationIndex(locationName));
        locations.add(new Location(newLocationName,
                tmpLocation.dimension, tmpLocation.x, tmpLocation.y, tmpLocation.z, tmpLocation.yaw, tmpLocation.pitch
        ));
        locations.remove(tmpLocation);
        write();
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
        ArrayList<String> locationsNames = locations.stream().map(Location::name).collect(Collectors.toCollection(ArrayList::new));

        return CommandSource.suggestMatching(locationsNames, builder);
    }

    /**
     * @implNote Can only be called if the result of {@link #locationExists} is {@code true}
     */
    private Location getLocation(String locationName)
    {
        return locations.get(getLocationIndex(locationName));
    }

    /**
     * Returns the index of the location with the name {@code locationName} | 0 if the location doesn't exists
     */
    private int getLocationIndex(String locationName)
    {
        return locations.stream().filter(location -> location.name().equals(locationName))
                .findFirst().map(locations::indexOf).orElse(0);
    }

    public boolean locationExists(String locationName)
    {
        return locations.stream().anyMatch(location -> location.name().equals(locationName));
    }

    public void teleport(ServerPlayerEntity player, String locationName)
    {
        if (CYAN_LIB_UTILS.isOptionAllowed(player, CyanConfig.ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
        {
            if (LOCATIONS.locationExists(locationName))
            {
                Locations.Location loc = LOCATIONS.getLocation(locationName);

                switch (loc.dimension())
                {
                    case "overworld" ->
                            player.teleport(player.getServer().getWorld(World.OVERWORLD), loc.x(), loc.y(), loc.z(), loc.yaw(), loc.pitch());
                    case "nether" ->
                            player.teleport(player.getServer().getWorld(World.NETHER), loc.x(), loc.y(), loc.z(), loc.yaw(), loc.pitch());
                    case "end" ->
                            player.teleport(player.getServer().getWorld(World.END), loc.x(), loc.y(), loc.z(), loc.yaw(), loc.pitch());
                }

                CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                        CYAN_LANGUAGE_UTILS.getTranslation("goToLocation"),
                        "cyan.msg.goToLocation",
                        Formatting.YELLOW + locationName
                );
            }
            else
            {
                CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                        CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "locationNotFound"),
                        "cyan.msg.locationNotFound",
                        Formatting.YELLOW + locationName
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
        LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(CYAN_MODID + "/" + saveName + "/locations.json");
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
