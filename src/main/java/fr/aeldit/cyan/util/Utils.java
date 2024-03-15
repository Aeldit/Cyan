package fr.aeldit.cyan.util;

import fr.aeldit.cyan.config.CyanConfig;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static fr.aeldit.cyan.teleportation.BackTps.BACK_TP_PATH;
import static fr.aeldit.cyan.teleportation.Locations.LOCATIONS_PATH;

public class Utils
{
    public static final String CYAN_MODID = "cyan";
    public static final Logger CYAN_LOGGER = LoggerFactory.getLogger(CYAN_MODID);
    private static final Map<String, String> CYAN_DEFAULT_TRANSLATIONS = new HashMap<>();

    public static Locations LOCATIONS = new Locations();
    public static BackTps BACK_TPS = new BackTps();

    public static CyanLibOptionsStorage CYAN_OPTIONS_STORAGE = new CyanLibOptionsStorage(CYAN_MODID, CyanConfig.class);
    public static CyanLibLanguageUtils CYAN_LANGUAGE_UTILS = new CyanLibLanguageUtils(
            CYAN_MODID,
            getDefaultTranslations()
    );
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

    public static Map<String, String> getDefaultTranslations()
    {
        if (CYAN_DEFAULT_TRANSLATIONS.isEmpty())
        {
            CYAN_DEFAULT_TRANSLATIONS.put("dashSeparation", "§6------------------------------------");
            CYAN_DEFAULT_TRANSLATIONS.put("listLocations", "§6Cyan - LOCATIONS :\n");
            CYAN_DEFAULT_TRANSLATIONS.put("headerDescCmd", "§6Cyan - DESCRIPTION (commands) :\n");
            CYAN_DEFAULT_TRANSLATIONS.put("headerDescOptions", "§6Cyan - DESCRIPTION (options) :\n");

            CYAN_DEFAULT_TRANSLATIONS.put("desc.bed", "§3The §d/bed §3command teleports you to your bed or respawn " +
                    "anchor");
            CYAN_DEFAULT_TRANSLATIONS.put(
                    "desc.kgi",
                    """
                            §3The §d/kgi §3command kills every item that is on the ground in a specific radius
                            §d - /kgi §3to kill items in the default radius (defined in the config, can be changed)
                            §d - /kgi [distance_in_chunks] §3to kill items in the specified radius"""
            );
            CYAN_DEFAULT_TRANSLATIONS.put("desc.surface", "§3The §d/surface §3command teleports you to the highest " +
                    "block located at your position");

            CYAN_DEFAULT_TRANSLATIONS.put("desc.allowBed", "§3The §eallowBed §3option toggles the use of the §d/bed " +
                    "§3command");
            CYAN_DEFAULT_TRANSLATIONS.put("desc.allowKgi", "§3The §eallowKgi §3option toggles the use of the §d/kgi " +
                    "§3command");
            CYAN_DEFAULT_TRANSLATIONS.put("desc.allowSurface", "§3The §eallowSurface §3option toggles the use of the " +
                    "§d/surface §3command");
            CYAN_DEFAULT_TRANSLATIONS.put("desc.allowLocations", "§3The §eallowLocations §3option toggles the use of " +
                    "the §dlocation §3commands");
            CYAN_DEFAULT_TRANSLATIONS.put("desc.allowBackTp", "§3The §eallowBackTp §3option toggles the use of the " +
                    "§d/back §3command");
            CYAN_DEFAULT_TRANSLATIONS.put("desc.useCustomTranslations", "§3The §euseCustomTranslations §3option " +
                    "toggles the use of custom translations");
            CYAN_DEFAULT_TRANSLATIONS.put("desc.msgToActionBar", "§3The §emsgToActionBar §3option determines if " +
                    "messages are send to the chat or the player's action bar");
            CYAN_DEFAULT_TRANSLATIONS.put("desc.distanceToEntitiesKgi", "§3The §edistanceToEntitiesKgi §3option " +
                    "defines distance (in chunks) in which the ground items will be removed");
            CYAN_DEFAULT_TRANSLATIONS.put("desc.minOpLvlKgi", "§3The §eminOpLvlKgi §3option defines the required OP " +
                    "level to use the §d/kgi §3command");
            CYAN_DEFAULT_TRANSLATIONS.put("desc.minOpLvlEditLocation", "§3The §eminOpLvlEditLocation §3option defines" +
                    " the required OP level to edit the locations");
            CYAN_DEFAULT_TRANSLATIONS.put("desc.daysToRemoveBackTp", "§3The §edaysToRemoveBackTp §3option defines the" +
                    " number of days the last death location of a player is kept");

            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.header", "§6Cyan - OPTIONS :\n");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.allowBed", "§6- §d/bed §3: %s");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.allowKgi", "§6- §d/kgi §3: %s");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.allowSurface", "§6- §d/surface §3: %s");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.allowLocations", "§6- §3Location commands : %s");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.allowBackTp", "§6- §d/back §3: %s");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.useCustomTranslations", "§6- §3Custom translations : %s");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.msgToActionBar", "§6- §3Messages to action bar : %s");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.distanceToEntitiesKgi", "§6- §d/kgi §3distance (in chunks) : %s");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.minOpLvlKgi", "§6- §3Minimum OP level for §d/kgi §3: %s");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.minOpLvlEditLocation", "§6- §3Minimum OP level to edit locations: " +
                    "%s");
            CYAN_DEFAULT_TRANSLATIONS.put("getCfg.daysToRemoveBackTp", "§6- §3Days to keep the death location: %s");

            CYAN_DEFAULT_TRANSLATIONS.put("set.allowBed", "§3Toggled §d/bed §3command %s");
            CYAN_DEFAULT_TRANSLATIONS.put("set.allowKgi", "§3Toggled §d/kgi §3command %s");
            CYAN_DEFAULT_TRANSLATIONS.put("set.allowSurface", "§3Toggled §d/surface §3command %s");
            CYAN_DEFAULT_TRANSLATIONS.put("set.allowLocations", "§3Toggled §dlocation §3commands %s");
            CYAN_DEFAULT_TRANSLATIONS.put("set.allowBackTp", "§3Toggled §d/back §3command %s");
            CYAN_DEFAULT_TRANSLATIONS.put("set.useCustomTranslations", "§3Toggled custom translations %s");
            CYAN_DEFAULT_TRANSLATIONS.put("set.msgToActionBar", "§3Toggled messages to action bar %s");
            CYAN_DEFAULT_TRANSLATIONS.put("set.distanceToEntitiesKgi", "§3The distance for §d/kgi §3is now %s");
            CYAN_DEFAULT_TRANSLATIONS.put("set.minOpLvlKgi", "§3The minimum OP level to execute §d/kgi §3is now %s");
            CYAN_DEFAULT_TRANSLATIONS.put("set.minOpLvlEditLocation", "§3The minimum OP level to edit locations is " +
                    "now %s");
            CYAN_DEFAULT_TRANSLATIONS.put("set.daysToRemoveBackTp", "§3The number of days to keep the last death " +
                    "locations is now %s");

            CYAN_DEFAULT_TRANSLATIONS.put("error.notOp", "§cYou don't have the required permission to do that");
            CYAN_DEFAULT_TRANSLATIONS.put("error.bedDisabled", "§cThe /bed command is disabled");
            CYAN_DEFAULT_TRANSLATIONS.put("error.kgiDisabled", "§cThe /kgi command is disabled");
            CYAN_DEFAULT_TRANSLATIONS.put("error.surfaceDisabled", "§cThe /surface command is disabled");
            CYAN_DEFAULT_TRANSLATIONS.put("error.locationsDisabled", "§cThe locations commands are disabled");
            CYAN_DEFAULT_TRANSLATIONS.put("error.backTpDisabled", "§cThe /back command is disabled");
            CYAN_DEFAULT_TRANSLATIONS.put("error.servOnly", "§cThis command can only be used on servers");
            CYAN_DEFAULT_TRANSLATIONS.put("error.playerOnlyCmd", "§cThis command can only be executed by a player");
            CYAN_DEFAULT_TRANSLATIONS.put("error.locationAlreadyExists", "§cA location with this name already exists");
            CYAN_DEFAULT_TRANSLATIONS.put("error.locationNotFound", "§cThe location %s §cdoesn't exist (check if you " +
                    "spelled it correctly)");
            CYAN_DEFAULT_TRANSLATIONS.put("error.bedNotFound", "§cYou don't have an attributed bed or respawn anchor");
            CYAN_DEFAULT_TRANSLATIONS.put("error.playerNotFound", "§cPlayer not found. The player must be online");
            CYAN_DEFAULT_TRANSLATIONS.put("error.noLocations", "§cThere is no saved locations");
            CYAN_DEFAULT_TRANSLATIONS.put("error.noLastPos", "§cYour last death location was not saved");
            CYAN_DEFAULT_TRANSLATIONS.put("error.noPropertiesFiles", "§cNo properties files were found");
            CYAN_DEFAULT_TRANSLATIONS.put("error.optionNotFound", "§cThis option does not exist");
            CYAN_DEFAULT_TRANSLATIONS.put("error.wrongType", "§cThis option can only be set to the %s §ctype");
            CYAN_DEFAULT_TRANSLATIONS.put("error.rule.opLevels", "§cThe OP level must be between 0 and 4 (both " +
                    "included)");
            CYAN_DEFAULT_TRANSLATIONS.put("error.rule.positiveValue", "§cThe value must be positive");

            CYAN_DEFAULT_TRANSLATIONS.put("bed", "§3You have been teleported to your bed");
            CYAN_DEFAULT_TRANSLATIONS.put("respawnAnchor", "§3You have been teleported to your respawn anchor");
            CYAN_DEFAULT_TRANSLATIONS.put("kgi", "§3Ground items have been removed");
            CYAN_DEFAULT_TRANSLATIONS.put("kgir", "§3Ground items have been removed in a radius of %s §3chunks");
            CYAN_DEFAULT_TRANSLATIONS.put("surface", "§3You have been teleported to the surface");
            CYAN_DEFAULT_TRANSLATIONS.put("setLocation", "§3The location %s §3have been saved");
            CYAN_DEFAULT_TRANSLATIONS.put("goToLocation", "§3You have been teleported to %s");
            CYAN_DEFAULT_TRANSLATIONS.put("removeLocation", "§3The location %s §3have been removed");
            CYAN_DEFAULT_TRANSLATIONS.put("removedAllLocations", "§3All the locations have been removed");
            CYAN_DEFAULT_TRANSLATIONS.put("renameLocation", "§3The location %s have been renamed to %s");
            CYAN_DEFAULT_TRANSLATIONS.put("getLocation", "%s §3(%s§3)");
            CYAN_DEFAULT_TRANSLATIONS.put("translationsReloaded", "§3The translations have been reloaded");
            CYAN_DEFAULT_TRANSLATIONS.put("backTp", "§3You have been teleported to the place you died");
            CYAN_DEFAULT_TRANSLATIONS.put("currentValue", "§7Current value : %s");
            CYAN_DEFAULT_TRANSLATIONS.put("setValue", "§7Set value to : %s  %s  %s  %s %s");
            CYAN_DEFAULT_TRANSLATIONS.put("propertiesFilesDeleted", "§3Properties files were successfully removed");
        }
        return CYAN_DEFAULT_TRANSLATIONS;
    }
}
