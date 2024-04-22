package fr.aeldit.cyan;

import fr.aeldit.cyan.config.CyanLibConfigImpl;
import fr.aeldit.cyan.teleportation.BackTps;
import fr.aeldit.cyan.teleportation.Locations;
import fr.aeldit.cyanlib.lib.CyanLib;
import fr.aeldit.cyanlib.lib.CyanLibLanguageUtils;
import fr.aeldit.cyanlib.lib.config.CyanLibOptionsStorage;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static fr.aeldit.cyan.teleportation.BackTps.BACK_TP_PATH;
import static fr.aeldit.cyan.teleportation.Locations.LOCATIONS_PATH;

public class CyanCore
{
    public static final String CYAN_MODID = "cyan";
    public static final Logger CYAN_LOGGER = LoggerFactory.getLogger(CYAN_MODID);

    public static Locations LOCATIONS = new Locations();
    public static BackTps BACK_TPS = new BackTps();

    public static CyanLibOptionsStorage CYAN_OPTIONS_STORAGE = new CyanLibOptionsStorage(CYAN_MODID,
            new CyanLibConfigImpl());
    public static CyanLibLanguageUtils CYAN_LANGUAGE_UTILS = new CyanLibLanguageUtils(CYAN_MODID);
    public static CyanLib CYAN_LIB_UTILS = new CyanLib(CYAN_MODID, CYAN_OPTIONS_STORAGE, CYAN_LANGUAGE_UTILS);

    public static void checkOrCreateModDir(boolean locations)
    {
        Path dir = FabricLoader.getInstance().getConfigDir().resolve(CYAN_MODID);

        if (!Files.exists(dir))
        {
            try
            {
                Files.createDirectory(dir);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        if (locations)
        {
            dir = Path.of(LOCATIONS_PATH.toString().replace("locations.json", ""));
        }
        else
        {
            dir = Path.of(BACK_TP_PATH.toString().replace("back.json", ""));
        }

        if (!Files.exists(dir))
        {
            try
            {
                Files.createDirectory(dir);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static void removeEmptyModDir(boolean locations)
    {
        Path dir;

        if (locations)
        {
            dir = Path.of(LOCATIONS_PATH.toString().replace("locations.json", ""));
        }
        else
        {
            dir = Path.of(BACK_TP_PATH.toString().replace("back.json", ""));
        }

        if (Files.exists(dir))
        {
            File[] listOfFiles = new File(dir.toUri()).listFiles();

            if (Objects.requireNonNull(listOfFiles).length == 0)
            {
                try
                {
                    Files.delete(dir);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }

        dir = FabricLoader.getInstance().getConfigDir().resolve(CYAN_MODID);

        if (Files.exists(dir))
        {
            File[] listOfFiles = new File(dir.toUri()).listFiles();

            if (Objects.requireNonNull(listOfFiles).length == 0)
            {
                try
                {
                    Files.delete(dir);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
