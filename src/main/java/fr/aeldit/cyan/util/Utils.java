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

import fr.aeldit.cyanlib.util.LanguageUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

public class Utils
{
    public static final String MODID = "cyan";
    public static String on = Formatting.GREEN + "ON";
    public static String off = Formatting.RED + "OFF";

    private static final List<String> commands = new ArrayList<>();
    private static final List<String> options = new ArrayList<>();

    private static void generateCommandsTraductionsList()
    {
        commands.add("bed");
        commands.add("kgi");
        commands.add("surface");
        commands.add("back");
        commands.add("location");
    }

    private static void generateOptionsTraductionsList()
    {
        options.add("allowBed");
        options.add("allowKgi");
        options.add("allowSurface");
        options.add("allowLocations");
        options.add("allowBackTp");

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

    // Files
    public static void checkOrCreateModDir()
    {
        if (!Files.exists(FabricLoader.getInstance().getConfigDir().resolve(MODID)))
        {
            try
            {
                Files.createDirectory(FabricLoader.getInstance().getConfigDir().resolve(MODID));
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static void checkOrCreateFile(Path path)
    {
        checkOrCreateModDir();
        if (!Files.exists(path))
        {
            try
            {
                Files.createFile(path);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setPropertiesKey(@NotNull Path filePath, String key, Object value)
    {
        try
        {
            Properties properties = new Properties();
            properties.load(new FileInputStream(filePath.toFile()));
            properties.put(key, value);
            properties.store(new FileOutputStream(filePath.toFile()), null);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    // Language Utils
    public static final Path languagePath = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/translations.properties");

    public static LanguageUtils CyanLanguageUtils = new LanguageUtils(Utils.MODID);
    public static LinkedHashMap<String, String> defaultTranslations = new LinkedHashMap<>();

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
        defaultTranslations.put("desc.useTranslations", "§3The §euseTranslations §3option toogles the use of translations (server-side only)");
        defaultTranslations.put("desc.msgToActionBar", "§3The §emsgToActionBar §3option determines if messages are send to the chat or the player's action bar");
        defaultTranslations.put("desc.errorToActionBar", "§3The §eerrorToActionBar §3option determines if error messages are send to the chat or the player's action bar");

        defaultTranslations.put("dashSeparation", "§6------------------------------------");
        defaultTranslations.put("listLocations", "§6Cyan - LOCATIONS :\n");
        defaultTranslations.put("headerDescCmd", "§6Cyan - DESCRIPTION (commands) :\n");
        defaultTranslations.put("headerDescOptions", "§6Cyan - DESCRIPTION (options) :\n");

        defaultTranslations.put("getCfg.header", "§6Cyan - OPTIONS :\n");
        defaultTranslations.put("getCfg.allowBed", "§6- §3/bed : %s");
        defaultTranslations.put("getCfg.allowKgi", "§6- §3/kgi : %s");
        defaultTranslations.put("getCfg.allowSurface", "§6- §3/surface : %s");
        defaultTranslations.put("getCfg.allowLocations", "§6- §3location commands : %s");
        defaultTranslations.put("getCfg.allowBackTp", "§6- §d/back %3: %s");
        defaultTranslations.put("getCfg.useTranslations", "§6- §3Use translations : %s");
        defaultTranslations.put("getCfg.msgToActionBar", "§6- §3Messages to action bar : %s");
        defaultTranslations.put("getCfg.errorToActionBar", "§6- §3Error messages to action bar : %s");
        defaultTranslations.put("getCfg.distanceToEntitiesKgi", "§6- §3kgi distance (in chunks) : %s");
        defaultTranslations.put("getCfg.minOpLevelExeEditConfig", "§6- §3Minimum OP level to edit config : %s");
        defaultTranslations.put("getCfg.minOpLevelExeBed", "§6- §3Minimum OP level for §d/bed §3: %s");
        defaultTranslations.put("getCfg.minOpLevelExeKgi", "§6- §3Minimum OP level for §d/kgi §3: %s");
        defaultTranslations.put("getCfg.minOpLevelExeSurface", "§6- §3Minimum OP level for §d/surface §3: %s");
        defaultTranslations.put("getCfg.minOpLevelExeLocation", "§6- §3Minimum OP level to see / teleport to locations §3: %s");
        defaultTranslations.put("getCfg.minOpLevelExeEditLocation", "§6- §3Minimum OP level to edit locations: %s");

        defaultTranslations.put("set.allowBed", "§3Toogled §d/bed §3command %s");
        defaultTranslations.put("set.allowKgi", "§3Toogled §d/kgi §3command %s");
        defaultTranslations.put("set.allowSurface", "§3Toogled §d/surface §3command %s");
        defaultTranslations.put("set.allowLocations", "§3Toogled §dlocation §3commands %s");
        defaultTranslations.put("set.allowBackTp", "§3Toogled §d/back §3command %s");
        defaultTranslations.put("set.useTranslations", "§3Toogled translations %s");
        defaultTranslations.put("set.msgToActionBar", "§3Toogled messages to action bar %s");
        defaultTranslations.put("set.errorToActionBar", "§3Toogled error messages to action bar %s");
        defaultTranslations.put("set.distanceToEntitiesKgi", "§3The distance for §d/kgi §3is now %s");
        defaultTranslations.put("set.minOpLevelExeModifConfig", "§3The minimum OP level to edit the config is now %s");
        defaultTranslations.put("set.minOpLevelExeBed", "§3The minimum OP level to execute §d/bed §3is now %s");
        defaultTranslations.put("set.minOpLevelExeKgi", "§3The minimum OP level to execute §d/kgi §3is now %s");
        defaultTranslations.put("set.minOpLevelExeSurface", "§3The minimum OP level to execute §d/surface §3is now %s");
        defaultTranslations.put("set.minOpLevelExeLocation", "§3The minimum OP level to see / teleport to locations is now %s");
        defaultTranslations.put("set.minOpLevelExeEditLocation", "§3The minimum OP level to edit locations is now %s");

        defaultTranslations.put("error.notOp", "§cYou don't have the required permission to do that");
        defaultTranslations.put("error.wrongOPLevel", "§cThe OP level must be in [0;4]");
        defaultTranslations.put("error.wrongDistanceKgi", "§cThe kgi distance must be in [1;128]");
        defaultTranslations.put("error.playerNotFound", "§cPlayer not found. The player must be online");
        defaultTranslations.put("error.incorrectIntOp", "§cThe OP level must be in [0;4]");
        defaultTranslations.put("error.incorrectIntKgi", "§cThe distance must be in [1;128]");
        defaultTranslations.put("error.bedDisabled", "§cThe /bed command is disabled. To enable it, enter '/cyan config booleanOptions allowBed true' in chat");
        defaultTranslations.put("error.kgiDisabled", "§cThe /kgi command is disabled. To enable it, enter '/cyan config booleanOptions allowKgi true' in chat");
        defaultTranslations.put("error.surfaceDisabled", "§cThe /surface command is disabled. To enable it, enter '/cyan config booleanOptions allowSurface true' in chat");
        defaultTranslations.put("error.servOnly", "§cThis command can only be used on servers");
        defaultTranslations.put("error.bed", "§cYou don't have an attributed bed or respawn anchor");
        defaultTranslations.put("error.playerOnlyCmd", "§cThis command can only be executed by a player");
        defaultTranslations.put("error.locationAlreadyExists", "§cA location with this name already exists");
        defaultTranslations.put("error.locationsDisabled", "§cThe locations commands are disabled. To enable them, enter '/cyan config booleanOptions allowLocations true' in chat");
        defaultTranslations.put("error.locationNotFound", "§cThe location %s §cdoesn't exist (check if you spelled it correctly)");
        defaultTranslations.put("error.fileNotRemoved", "§cAn error occured while trying to remove the locations file");
        defaultTranslations.put("error.noLastPos", "§cYour last death location was not saved");

        defaultTranslations.put("bed", "§3You have been teleported to your bed");
        defaultTranslations.put("respawnAnchor", "§3You have been teleported to your respawn anchor");
        defaultTranslations.put("kgi", "§3Ground items have been removed");
        defaultTranslations.put("kgir", "§3Ground items have been removed in a radius of %s §3chunks");
        defaultTranslations.put("surface", "§3You have been teleported to the surface");
        defaultTranslations.put("setLocation", "§3The location %s §3have been saved");
        defaultTranslations.put("goToLocation", "§3You have been teleported to %s");
        defaultTranslations.put("removeLocation", "§3The location %s §3have been removed");
        defaultTranslations.put("removedAllLocations", "§3All the locations have been removed");
        defaultTranslations.put("translationsReloaded", "§3The translations have been reloaded");
        defaultTranslations.put("backTp", "§3You have been teleported to the place you died");
    }

    public static LinkedHashMap<String, String> getDefaultTranslations()
    {
        if (defaultTranslations.isEmpty())
        {
            generateDefaultTranslations();
        }
        return defaultTranslations;
    }

    // Locations
    public static final Path locationsPath = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/locations.properties");
}
