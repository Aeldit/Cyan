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
    private final List<Location> locations = Collections.synchronizedList(new ArrayList<>());
    private final TypeToken<List<Location>> locationsType = new TypeToken<>()
    {
    };
    public static Path LOCATIONS_PATH =
            FabricLoader.getInstance().getConfigDir().resolve(Path.of("%s/locations.json".formatted(MODID)));

    public boolean add(@NotNull Location location)
    {
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
        return locations.isEmpty();
    }

    public @Nullable List<Location> getLocations()
    {
        return locations;
    }

    public CompletableFuture<Suggestions> getLocationsNames(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(locations.stream().map(Location::name).toList(), builder);
    }

    public @Nullable Location getLocation(String locationName)
    {
        return locations.stream()
                        .filter(location -> location.name().equals(locationName))
                        .findFirst()
                        .orElse(null);
    }

    public void readServer()
    {
        if (Files.exists(LOCATIONS_PATH))
        {
            try (Reader reader = Files.newBufferedReader(LOCATIONS_PATH))
            {
                locations.addAll(new Gson().fromJson(reader, locationsType));
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

        if (Files.exists(LOCATIONS_PATH))
        {
            try (Reader reader = Files.newBufferedReader(LOCATIONS_PATH))
            {
                locations.addAll(new Gson().fromJson(reader, locationsType));
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

        if (locations.isEmpty())
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
            try (Writer writer = Files.newBufferedWriter(LOCATIONS_PATH))
            {
                new GsonBuilder().setPrettyPrinting().create().toJson(locations, writer);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
