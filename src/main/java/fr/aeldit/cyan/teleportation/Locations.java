package fr.aeldit.cyan.teleportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.Contract;
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

import static fr.aeldit.cyan.CyanCore.*;

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

        @Contract(pure = true)
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

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
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
    private final TypeToken<List<Location>> locationsType = new TypeToken<>()
    {
    };
    private boolean isEditingFile = false;
    public static Path LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(Path.of(CYAN_MODID +
            "/locations.json"));

    public boolean add(@NotNull Location location)
    {
        if (locationNotFound(location.getName()))
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
        Location location = getLocation(locationName);
        if (location != null)
        {
            locations.remove(location);
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
        Location location = getLocation(locationName);
        if (location != null)
        {
            location.setName(newLocationName);
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
        ArrayList<String> locationsNames = new ArrayList<>(locations.size());
        for (Location location : locations)
        {
            locationsNames.add(location.getName());
        }
        return CommandSource.suggestMatching(locationsNames, builder);
    }

    public boolean locationNotFound(String locationName)
    {
        return getLocation(locationName) == null;
    }

    public @Nullable Location getLocation(String locationName)
    {
        for (Location location : locations)
        {
            if (location.getName().equals(locationName))
            {
                return location;
            }
        }
        return null;
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
        LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(Path.of(CYAN_MODID + "/" + saveName +
                "/locations.json"));
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
