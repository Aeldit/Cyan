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

import static fr.aeldit.cyan.CyanCore.*;

public class Locations
{
    private List<Location> locations = null;
    private final TypeToken<List<Location>> locationsType = new TypeToken<>()
    {
    };
    private boolean isEditingFile = false;
    public static Path LOCATIONS_PATH =
            FabricLoader.getInstance().getConfigDir().resolve(Path.of("%s/locations.json".formatted(MODID)));

    public boolean add(@NotNull Location location)
    {
        if (locations == null)
        {
            locations = Collections.synchronizedList(new ArrayList<>());
            locations.add(location);
            write();
            return true;
        }

        if (getLocation(location.name()) == null)
        {
            locations.add(location);
            write();
            return true;
        }
        return false;
    }

    public boolean remove(String locationName)
    {
        Location location = getLocation(locationName);
        if (location != null && locations != null)
        {
            locations.remove(location);
            if (locations.isEmpty())
            {
                locations = null;
            }
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
            locations = null;
            write();

            return true;
        }
        return false;
    }

    public boolean rename(String locationName, String newLocationName)
    {
        Location location = getLocation(locationName);
        if (location != null)
        {
            locations.remove(location);
            locations.add(location.getRenamed(newLocationName));
            write();

            return true;
        }
        return false;
    }

    public boolean isEmpty()
    {
        return locations == null || locations.isEmpty();
    }

    public @Nullable List<Location> getLocations()
    {
        return locations;
    }

    public CompletableFuture<Suggestions> getLocationsNames(@NotNull SuggestionsBuilder builder)
    {
        if (locations != null)
        {
            ArrayList<String> locationsNames = new ArrayList<>(locations.size());
            for (Location location : locations)
            {
                locationsNames.add(location.name());
            }
            return CommandSource.suggestMatching(locationsNames, builder);
        }
        return new CompletableFuture<>();
    }

    public @Nullable Location getLocation(String locationName)
    {
        if (locations != null)
        {
            for (Location location : locations)
            {
                if (location.name().equals(locationName))
                {
                    return location;
                }
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
                if (locations == null)
                {
                    locations = Collections.synchronizedList(new ArrayList<>());
                }
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
        LOCATIONS_PATH = FabricLoader.getInstance().getConfigDir().resolve(
                Path.of("%s/%s/locations.json".formatted(MODID, saveName))
        );
        checkOrCreateModDir(true);

        if (Files.exists(LOCATIONS_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(LOCATIONS_PATH);
                if (locations == null)
                {
                    locations = Collections.synchronizedList(new ArrayList<>());
                }
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

        if (locations == null || locations.isEmpty())
        {
            if (Files.exists(LOCATIONS_PATH))
            {
                try
                {
                    Files.delete(LOCATIONS_PATH);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                removeEmptyModDir(true);
            }
        }
        else
        {
            if (!isEditingFile)
            {
                try
                {
                    isEditingFile = true;

                    Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
                    Writer writer = Files.newBufferedWriter(LOCATIONS_PATH);
                    gsonWriter.toJson(locations, writer);
                    writer.close();

                    isEditingFile = false;
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                long end = System.currentTimeMillis() + 1000; // 1 s
                boolean couldWrite = false;

                try
                {
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
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }

                if (!couldWrite)
                {
                    CYAN_LOGGER.info("[CyanSetHome] Could not write the locations file because it is already " +
                                     "being written (for more than 1 sec)");
                }
            }
        }
    }
}
