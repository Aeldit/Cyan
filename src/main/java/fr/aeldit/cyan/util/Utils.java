package fr.aeldit.cyan.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Utils
{
    public static final Path locationsPath = FabricLoader.getInstance().getConfigDir().resolve("cyan/locations.properties");
    public static String on = Formatting.GREEN + "ON";
    public static String off = Formatting.RED + "OFF";

    private static final List<String> commands = new ArrayList<>();
    private static final List<String> options = new ArrayList<>();

    private static void generateCommandsTraductionsList()
    {
        commands.add("bed");
        commands.add("kgi");
        commands.add("surface");
    }

    private static void generateOptionsTraductionsList()
    {
        options.add("allowBed");
        options.add("allowKgi");
        options.add("allowSurface");
        options.add("allowLocations");

        options.add("useTranslations");
        options.add("msgToActionBar");
        options.add("errorToActionBar");
    }

    public static List<String> getCommandsList()
    {
        if (commands.isEmpty())
        {
            generateCommandsTraductionsList();
        }
        return commands;
    }

    public static List<String> getOptionsList()
    {
        if (options.isEmpty())
        {
            generateOptionsTraductionsList();
        }
        return options;
    }

    public static void checkOrCreateDirs()
    {
        if (!Files.exists(FabricLoader.getInstance().getConfigDir().resolve("cyan")))
        {
            try
            {
                Files.createDirectory(FabricLoader.getInstance().getConfigDir().resolve("cyan"));
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        if (!Files.exists(locationsPath))
        {
            try
            {
                Files.createFile(locationsPath);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
