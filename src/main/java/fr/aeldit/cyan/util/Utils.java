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

package fr.aeldit.cyan.util;

import fr.aeldit.cyan.teleportation.BackTps;
import fr.aeldit.cyan.teleportation.Locations;
import fr.aeldit.cyanlib.lib.CyanLib;
import fr.aeldit.cyanlib.lib.CyanLibConfig;
import fr.aeldit.cyanlib.lib.CyanLibLanguageUtils;
import fr.aeldit.cyanlib.lib.commands.CyanLibConfigCommands;
import fr.aeldit.cyanlib.lib.utils.RULES;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static fr.aeldit.cyan.teleportation.BackTps.BACK_TP_PATH;
import static fr.aeldit.cyan.teleportation.Locations.LOCATIONS_PATH;

public class Utils
{
    public static final String MODID = "cyan";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static Locations LocationsObj = new Locations();
    public static BackTps BackTpsObj = new BackTps();

    public static CyanLibConfig LibConfig = new CyanLibConfig(MODID, getDefaultOptions(), getRules());
    public static CyanLibLanguageUtils LanguageUtils = new CyanLibLanguageUtils(Utils.MODID, LibConfig);
    public static CyanLib LibUtils = new CyanLib(Utils.MODID, LibConfig, LanguageUtils);
    public static CyanLibConfigCommands LibConfigCommands = new CyanLibConfigCommands(MODID, LibUtils, getDefaultTranslations());

    private static HashMap<String, String> defaultTranslations;

    public static @NotNull Map<String, Object> getDefaultOptions()
    {
        Map<String, Object> options = new HashMap<>();

        options.put("allowBed", true);
        options.put("allowKgi", true);
        options.put("allowSurface", true);
        options.put("allowLocations", true);
        options.put("allowBackTp", true);
        options.put("useCustomTranslations", false);
        options.put("msgToActionBar", true);

        options.put("minOpLevelExeKgi", 4);
        options.put("minOpLevelExeEditConfig", 4);
        options.put("minOpLevelExeEditLocation", 4);
        options.put("distanceToEntitiesKgi", 14);
        options.put("daysToRemoveBackTp", 180);

        return options;
    }

    public static @NotNull Map<String, Object> getRules()
    {
        Map<String, Object> rules = new HashMap<>();

        rules.put("useCustomTranslations", RULES.LOAD_CUSTOM_TRANSLATIONS);

        rules.put("minOpLevelExeKgi", RULES.OP_LEVELS);
        rules.put("minOpLevelExeEditConfig", RULES.OP_LEVELS);
        rules.put("minOpLevelExeEditLocation", RULES.OP_LEVELS);
        rules.put("distanceToEntitiesKgi", RULES.POSITIVE_VALUE);
        rules.put("daysToRemoveBackTp", RULES.POSITIVE_VALUE);

        return rules;
    }

    public static void checkOrCreateModDir(boolean locations)
    {
        Path dir = FabricLoader.getInstance().getConfigDir().resolve(MODID);

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

            if (listOfFiles.length == 0)
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

        dir = FabricLoader.getInstance().getConfigDir().resolve(MODID);

        if (Files.exists(dir))
        {
            File[] listOfFiles = new File(dir.toUri()).listFiles();

            if (listOfFiles.length == 0)
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
        if (defaultTranslations == null)
        {
            defaultTranslations = new HashMap<>();

            defaultTranslations.put("dashSeparation", "§6------------------------------------");
            defaultTranslations.put("listLocations", "§6Cyan - LOCATIONS :\n");
            defaultTranslations.put("headerDescCmd", "§6Cyan - DESCRIPTION (commands) :\n");
            defaultTranslations.put("headerDescOptions", "§6Cyan - DESCRIPTION (options) :\n");

            defaultTranslations.put("desc.bed", "§3The §d/bed §3command teleports you to your bed or respawn anchor");
            defaultTranslations.put("desc.kgi",
                    """
                            §3The §d/kgi §3command kills every item that is on the ground in a specific radius
                            §d - /kgi §3to kill items in the default radius (defined in the config, can be changed)
                            §d - /kgi [distance_in_chunks] §3to kill items in the specified radius"""
            );
            defaultTranslations.put("desc.surface", "§3The §d/surface §3command teleports you to the highest block located at your position");

            defaultTranslations.put("desc.allowBed", "§3The §eallowBed §3option toggles the use of the §d/bed §3command");
            defaultTranslations.put("desc.allowKgi", "§3The §eallowKgi §3option toggles the use of the §d/kgi §3command");
            defaultTranslations.put("desc.allowSurface", "§3The §eallowSurface §3option toggles the use of the §d/surface §3command");
            defaultTranslations.put("desc.allowLocations", "§3The §eallowLocations §3option toggles the use of the §dlocation §3commands");
            defaultTranslations.put("desc.allowBackTp", "§3The §eallowBackTp §3option toggles the use of the §d/back §3command");
            defaultTranslations.put("desc.useCustomTranslations", "§3The §euseCustomTranslations §3option toggles the use of custom translations");
            defaultTranslations.put("desc.msgToActionBar", "§3The §emsgToActionBar §3option determines if messages are send to the chat or the player's action bar");
            defaultTranslations.put("desc.distanceToEntitiesKgi", "§3The §edistanceToEntitiesKgi §3option defines distance (in chunks) in which the ground items will be removed");
            defaultTranslations.put("desc.minOpLevelExeKgi", "§3The §eminOpLevelExeKgi §3option defines the required OP level to use the §d/kgi §3command");
            defaultTranslations.put("desc.minOpLevelExeEditConfig", "§3The §eminOpLevelExeEditConfig §3option defines the required OP level to edit config");
            defaultTranslations.put("desc.minOpLevelExeEditLocation", "§3The §eminOpLevelExeEditLocation §3option defines the required OP level to edit the locations");
            defaultTranslations.put("desc.daysToRemoveBackTp", "§3The §edaysToRemoveBackTp §3option defines the number of days the last death location of a player is kept");

            defaultTranslations.put("getCfg.header", "§6Cyan - OPTIONS :\n");
            defaultTranslations.put("getCfg.allowBed", "§6- §d/bed §3: %s");
            defaultTranslations.put("getCfg.allowKgi", "§6- §d/kgi §3: %s");
            defaultTranslations.put("getCfg.allowSurface", "§6- §d/surface §3: %s");
            defaultTranslations.put("getCfg.allowLocations", "§6- §3Location commands : %s");
            defaultTranslations.put("getCfg.allowBackTp", "§6- §d/back §3: %s");
            defaultTranslations.put("getCfg.useCustomTranslations", "§6- §3Custom translations : %s");
            defaultTranslations.put("getCfg.msgToActionBar", "§6- §3Messages to action bar : %s");
            defaultTranslations.put("getCfg.distanceToEntitiesKgi", "§6- §d/kgi §3distance (in chunks) : %s");
            defaultTranslations.put("getCfg.minOpLevelExeKgi", "§6- §3Minimum OP level for §d/kgi §3: %s");
            defaultTranslations.put("getCfg.minOpLevelExeEditConfig", "§6- §3Minimum OP level to edit config : %s");
            defaultTranslations.put("getCfg.minOpLevelExeEditLocation", "§6- §3Minimum OP level to edit locations: %s");
            defaultTranslations.put("getCfg.daysToRemoveBackTp", "§6- §3Days to keep the death location: %s");

            defaultTranslations.put("set.allowBed", "§3Toggled §d/bed §3command %s");
            defaultTranslations.put("set.allowKgi", "§3Toggled §d/kgi §3command %s");
            defaultTranslations.put("set.allowSurface", "§3Toggled §d/surface §3command %s");
            defaultTranslations.put("set.allowLocations", "§3Toggled §dlocation §3commands %s");
            defaultTranslations.put("set.allowBackTp", "§3Toggled §d/back §3command %s");
            defaultTranslations.put("set.useCustomTranslations", "§3Toggled custom translations %s");
            defaultTranslations.put("set.msgToActionBar", "§3Toggled messages to action bar %s");
            defaultTranslations.put("set.distanceToEntitiesKgi", "§3The distance for §d/kgi §3is now %s");
            defaultTranslations.put("set.minOpLevelExeKgi", "§3The minimum OP level to execute §d/kgi §3is now %s");
            defaultTranslations.put("set.minOpLevelExeEditConfig", "§3The minimum OP level to edit the config is now %s");
            defaultTranslations.put("set.minOpLevelExeEditLocation", "§3The minimum OP level to edit locations is now %s");
            defaultTranslations.put("set.daysToRemoveBackTp", "§3The number of days to keep the last death locations is now %s");

            defaultTranslations.put("error.notOp", "§cYou don't have the required permission to do that");
            defaultTranslations.put("error.bedDisabled", "§cThe /bed command is disabled");
            defaultTranslations.put("error.kgiDisabled", "§cThe /kgi command is disabled");
            defaultTranslations.put("error.surfaceDisabled", "§cThe /surface command is disabled");
            defaultTranslations.put("error.locationsDisabled", "§cThe locations commands are disabled");
            defaultTranslations.put("error.backTpDisabled", "§cThe /back command is disabled");
            defaultTranslations.put("error.servOnly", "§cThis command can only be used on servers");
            defaultTranslations.put("error.playerOnlyCmd", "§cThis command can only be executed by a player");
            defaultTranslations.put("error.locationAlreadyExists", "§cA location with this name already exists");
            defaultTranslations.put("error.locationNotFound", "§cThe location %s §cdoesn't exist (check if you spelled it correctly)");
            defaultTranslations.put("error.bedNotFound", "§cYou don't have an attributed bed or respawn anchor");
            defaultTranslations.put("error.playerNotFound", "§cPlayer not found. The player must be online");
            defaultTranslations.put("error.noLocations", "§cThere is no saved locations");
            defaultTranslations.put("error.noLastPos", "§cYour last death location was not saved");
            defaultTranslations.put("error.noPropertiesFiles", "§cNo properties files were found");
            defaultTranslations.put("error.optionNotFound", "§cThis option does not exist");
            defaultTranslations.put("error.wrongType", "§cThis option can only be set to the %s §ctype");
            defaultTranslations.put("error.rule.opLevels", "§cThe OP level must be between 0 and 4 (both included)");
            defaultTranslations.put("error.rule.positiveValue", "§cThe value must be positive");

            defaultTranslations.put("bed", "§3You have been teleported to your bed");
            defaultTranslations.put("respawnAnchor", "§3You have been teleported to your respawn anchor");
            defaultTranslations.put("kgi", "§3Ground items have been removed");
            defaultTranslations.put("kgir", "§3Ground items have been removed in a radius of %s §3chunks");
            defaultTranslations.put("surface", "§3You have been teleported to the surface");
            defaultTranslations.put("setLocation", "§3The location %s §3have been saved");
            defaultTranslations.put("goToLocation", "§3You have been teleported to %s");
            defaultTranslations.put("removeLocation", "§3The location %s §3have been removed");
            defaultTranslations.put("removedAllLocations", "§3All the locations have been removed");
            defaultTranslations.put("getLocation", "%s §3(%s§3)");
            defaultTranslations.put("translationsReloaded", "§3The translations have been reloaded");
            defaultTranslations.put("backTp", "§3You have been teleported to the place you died");
            defaultTranslations.put("currentValue", "§7Current value : %s");
            defaultTranslations.put("setValue", "§7Set value to : %s  %s  %s  %s %s");
            defaultTranslations.put("propertiesFilesDeleted", "§3Properties files were successfully removed");
        }
        return defaultTranslations;
    }
}
