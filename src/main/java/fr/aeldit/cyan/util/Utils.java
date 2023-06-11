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

import fr.aeldit.cyan.config.CyanMidnightConfig;
import fr.aeldit.cyanlib.util.CyanLibUtils;
import fr.aeldit.cyanlib.util.LanguageUtils;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils
{
    public static final String MODID = "cyan";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    // Config Utils
    private static final List<String> optionsBool = new ArrayList<>();
    private static final List<String> optionsInt = new ArrayList<>();
    private static final Map<String, List<String>> options = new HashMap<>();

    // Language Utils
    public static final Path LANGUAGE_PATH = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/translations.json");
    public static LanguageUtils CyanLanguageUtils = new LanguageUtils(Utils.MODID);
    public static HashMap<String, String> defaultTranslations = new HashMap<>();

    // Utils
    public static CyanLibUtils CyanLibUtils = new CyanLibUtils(Utils.MODID, CyanLanguageUtils, CyanMidnightConfig.msgToActionBar, CyanMidnightConfig.useCustomTranslations);

    public static Map<String, List<String>> getOptionsList()
    {
        if (options.isEmpty())
        {
            optionsBool.add("allowBed");
            optionsBool.add("allowKgi");
            optionsBool.add("allowSurface");
            optionsBool.add("allowLocations");
            optionsBool.add("allowBackTp");
            optionsBool.add("allowConsoleEditConfig");
            optionsBool.add("useCustomTranslations");
            optionsBool.add("msgToActionBar");

            optionsInt.add("distanceToEntitiesKgi");
            optionsInt.add("minOpLevelExeKgi");
            optionsInt.add("minOpLevelExeEditConfig");
            optionsInt.add("minOpLevelExeEditLocation");

            options.put("booleans", optionsBool);
            options.put("integers", optionsInt);
        }
        return options;
    }

    // Locations Utils
    public static Locations LocationsObj = new Locations();

    // BackTp Utils
    public static boolean backTpExists(@NotNull List<BackTp> backTps, String playerUUID)
    {
        for (BackTp backTp : backTps)
        {
            if (backTp.playerUUID().equals(playerUUID))
            {
                return true;
            }
        }
        return false;
    }

    public static int getBackTpIndex(@NotNull List<BackTp> backTps, String playerUUID)
    {
        for (BackTp backTp : backTps)
        {
            if (backTp.playerUUID().equals(playerUUID))
            {
                return backTps.indexOf(backTp);
            }
        }
        return -1;
    }

    // Language Utils
    public static void generateDefaultTranslations()
    {
        defaultTranslations.put("desc.bed", "§3The §d/bed §3command teleports you to your bed or respawn anchor");
        defaultTranslations.put("desc.kgi",
                """
                        §3The §d/kgi §3command kills every item that is on the ground in a specific radius
                        §d - /kgi §3to kill items in the default radius (defined in the config, can be changed)
                        §d - /kgi [distance_in_chunks] §3to kill items in the specified radius"""
        );
        defaultTranslations.put("desc.surface", "§3The §d/surface §3command teleports you to the highest block located at your position");

        defaultTranslations.put("desc.allowBed", "§3The §eallowBed §3option toogles the use of the §d/bed §3command");
        defaultTranslations.put("desc.allowKgi", "§3The §eallowKgi §3option toogles the use of the §d/kgi §3command");
        defaultTranslations.put("desc.allowSurface", "§3The §eallowSurface §3option toogles the use of the §d/surface §3command");
        defaultTranslations.put("desc.allowLocations", "§3The §eallowLocations §3option toogles the use of the §dlocation §3commands");
        defaultTranslations.put("desc.allowBackTp", "§3The §eallowBackTp §3option toogles the use of the §d/back §3command");
        defaultTranslations.put("desc.useCustomTranslations", "§3The §euseTranslations §3option toogles the use of custom translations (server-side only)");
        defaultTranslations.put("desc.msgToActionBar", "§3The §emsgToActionBar §3option determines if messages are send to the chat or the player's action bar");
        defaultTranslations.put("desc.distanceToEntitiesKgi", "§3The §edistanceToEntitiesKgi §3option defines distance (in chunks) in which the ground items will be removed");
        defaultTranslations.put("desc.minOpLevelExeKgi", "§3The §eminOpLevelExeKgi §3option defines the required OP level to use the §d/kgi §3command");
        defaultTranslations.put("desc.minOpLevelExeEditConfig", "§3The §eminOpLevelExeEditConfig §3option defines the required OP level to edit config");
        defaultTranslations.put("desc.minOpLevelExeEditLocation", "§3The §eminOpLevelExeEditLocation §3option defines the required OP level to edit the locations");

        defaultTranslations.put("dashSeparation", "§6------------------------------------");
        defaultTranslations.put("listLocations", "§6Cyan - LOCATIONS :\n");
        defaultTranslations.put("headerDescCmd", "§6Cyan - DESCRIPTION (commands) :\n");
        defaultTranslations.put("headerDescOptions", "§6Cyan - DESCRIPTION (options) :\n");

        defaultTranslations.put("getCfg.header", "§6Cyan - OPTIONS :\n");
        defaultTranslations.put("getCfg.allowBed", "§6- §d/bed : %s");
        defaultTranslations.put("getCfg.allowKgi", "§6- §d/kgi : %s");
        defaultTranslations.put("getCfg.allowSurface", "§6- §d/surface : %s");
        defaultTranslations.put("getCfg.allowLocations", "§6- §3Location commands : %s");
        defaultTranslations.put("getCfg.allowBackTp", "§6- §d/back §3: %s");
        defaultTranslations.put("getCfg.useCustomTranslations", "§6- §3Custom translations : %s");
        defaultTranslations.put("getCfg.msgToActionBar", "§6- §3Messages to action bar : %s");
        defaultTranslations.put("getCfg.allowConsoleEditConfig", "§6- §3Edit config via console : %s");
        defaultTranslations.put("getCfg.distanceToEntitiesKgi", "§6- §d/kgi §3distance (in chunks) : %s");
        defaultTranslations.put("getCfg.minOpLevelExeKgi", "§6- §3Minimum OP level for §d/kgi §3: %s");
        defaultTranslations.put("getCfg.minOpLevelExeEditConfig", "§6- §3Minimum OP level to edit config : %s");
        defaultTranslations.put("getCfg.minOpLevelExeEditLocation", "§6- §3Minimum OP level to edit locations: %s");

        defaultTranslations.put("set.allowBed", "§3Toogled §d/bed §3command %s");
        defaultTranslations.put("set.allowKgi", "§3Toogled §d/kgi §3command %s");
        defaultTranslations.put("set.allowSurface", "§3Toogled §d/surface §3command %s");
        defaultTranslations.put("set.allowLocations", "§3Toogled §dlocation §3commands %s");
        defaultTranslations.put("set.allowBackTp", "§3Toogled §d/back §3command %s");
        defaultTranslations.put("set.useCustomTranslations", "§3Toogled custom translations %s");
        defaultTranslations.put("set.msgToActionBar", "§3Toogled messages to action bar %s");
        defaultTranslations.put("set.allowConsoleEditConfig", "§3Toogled config editing via console %s");
        defaultTranslations.put("set.distanceToEntitiesKgi", "§3The distance for §d/kgi §3is now %s");
        defaultTranslations.put("set.minOpLevelExeKgi", "§3The minimum OP level to execute §d/kgi §3is now %s");
        defaultTranslations.put("set.minOpLevelExeEditConfig", "§3The minimum OP level to edit the config is now %s");
        defaultTranslations.put("set.minOpLevelExeEditLocation", "§3The minimum OP level to edit locations is now %s");

        defaultTranslations.put("error.notOp", "§cYou don't have the required permission to do that");
        defaultTranslations.put("error.wrongOPLevel", "§cThe OP level must be in [0;4]");
        defaultTranslations.put("error.wrongDistanceKgi", "§cThe kgi distance must be in [1;128]");
        defaultTranslations.put("error.playerNotFound", "§cPlayer not found. The player must be online");
        defaultTranslations.put("error.incorrectIntOp", "§cThe OP level must be in [0;4]");
        defaultTranslations.put("error.incorrectIntKgi", "§cThe distance must be in [1;128]");
        defaultTranslations.put("error.bedDisabled", "§cThe /bed command is disabled");
        defaultTranslations.put("error.kgiDisabled", "§cThe /kgi command is disabled");
        defaultTranslations.put("error.surfaceDisabled", "§cThe /surface command is disabled");
        defaultTranslations.put("error.backTpDisabled", "§cThe /back command is disabled");
        defaultTranslations.put("error.servOnly", "§cThis command can only be used on servers");
        defaultTranslations.put("error.bedNotFound", "§cYou don't have an attributed bed or respawn anchor");
        defaultTranslations.put("error.playerOnlyCmd", "§cThis command can only be executed by a player");
        defaultTranslations.put("error.locationAlreadyExists", "§cA location with this name already exists");
        defaultTranslations.put("error.locationsDisabled", "§cThe locations commands are disabled");
        defaultTranslations.put("error.locationNotFound", "§cThe location %s §cdoesn't exist (check if you spelled it correctly)");
        defaultTranslations.put("error.noLocations", "§cThere is no saved locations");
        defaultTranslations.put("error.noLastPos", "§cYour last death location was not saved");
        defaultTranslations.put("error.optionNotFound", "§cThis option does not exist or you tried to set it to the wrong type (int or bool)");
        defaultTranslations.put("error.noPropertiesFiles", "§cNo properties files were found");

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

    public static Map<String, String> getDefaultTranslations()
    {
        if (defaultTranslations.isEmpty())
        {
            generateDefaultTranslations();
        }
        return defaultTranslations;
    }

    public static Map<String, String> getDefaultTranslations(boolean reloadAll)
    {
        if (defaultTranslations.isEmpty() || reloadAll)
        {
            generateDefaultTranslations();
        }
        return defaultTranslations;
    }
}
