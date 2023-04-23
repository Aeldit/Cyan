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

package fr.aeldit.cyan.config;

import eu.midnightdust.lib.config.MidnightConfig;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static fr.aeldit.cyan.util.Utils.*;

public class CyanMidnightConfig extends MidnightConfig
{
    public static Map<String, Object> allOptionsMap = new HashMap<>();

    @Comment
    public static Comment allowOptions;
    @Entry
    public static boolean allowBed = true;
    @Entry
    public static boolean allowKgi = true;
    @Entry
    public static boolean allowSurface = true;
    @Entry
    public static boolean allowLocations = true;
    @Entry
    public static boolean allowBackTp = true;
    @Entry
    public static boolean allowConsoleEditConfig = false;

    @Comment
    public static Comment intOptions;
    @Entry(isSlider = true, min = 1, max = 128)
    public static int distanceToEntitiesKgi = 14;
    @Entry(isSlider = true, min = 0, max = 4)
    public static int minOpLevelExeEditConfig = 4;
    @Entry(isSlider = true, min = 0, max = 4)
    public static int minOpLevelExeKgi = 4;
    @Entry(isSlider = true, min = 0, max = 4)
    public static int minOpLevelExeEditLocation = 4;

    @Comment
    public static Comment boolOptions;
    @Entry
    public static boolean msgToActionBar = true;
    @Entry
    public static boolean errorToActionBar = true;
    @Entry
    public static boolean useCustomTranslations = false;

    public static void generateAllOptionsMap()
    {
        allOptionsMap.put("allowBed", allowBed);
        allOptionsMap.put("allowKgi", allowKgi);
        allOptionsMap.put("allowSurface", allowSurface);
        allOptionsMap.put("allowLocations", allowLocations);
        allOptionsMap.put("allowBackTp", allowBackTp);
        allOptionsMap.put("allowConsoleEditConfig", allowConsoleEditConfig);
        allOptionsMap.put("useCustomTranslations", useCustomTranslations);
        allOptionsMap.put("msgToActionBar", msgToActionBar);
        allOptionsMap.put("errorToActionBar", errorToActionBar);

        allOptionsMap.put("minOpLevelExeEditConfig", minOpLevelExeEditConfig);
        allOptionsMap.put("minOpLevelExeKgi", minOpLevelExeKgi);
        allOptionsMap.put("minOpLevelExeEditLocation", minOpLevelExeEditLocation);

        allOptionsMap.put("distanceToEntitiesKgi", distanceToEntitiesKgi);
    }

    public static void setBoolOption(@NotNull String optionName, boolean value)
    {
        switch (optionName)
        {
            case "allowBed" -> allowBed = value;
            case "allowKgi" -> allowKgi = value;
            case "allowSurface" -> allowSurface = value;
            case "allowLocations" -> allowLocations = value;
            case "allowBackTp" -> allowBackTp = value;
            case "allowConsoleEditConfig" -> allowConsoleEditConfig = value;
            case "useCustomTranslations" -> useCustomTranslations = value;
            case "msgToActionBar" -> msgToActionBar = value;
            case "errorToActionBar" -> errorToActionBar = value;
        }
        write("cyan");
        generateAllOptionsMap();
        if (useCustomTranslations)
        {
            CyanLanguageUtils.loadLanguage(getDefaultTranslations());
        }
        if (optionName.equals("errorToActionBar"))
        {
            CyanLibUtils.setErrorToActionBar(value);
        }
        else if (optionName.equals("useCustomTranslations"))
        {
            CyanLibUtils.setUseCustomTranslations(value);
        }
    }

    public static void setIntOption(@NotNull String optionName, int value)
    {
        switch (optionName)
        {
            case "distanceToEntitiesKgi" -> distanceToEntitiesKgi = value;
            case "minOpLevelExeEditConfig" -> minOpLevelExeEditConfig = value;
            case "minOpLevelExeKgi" -> minOpLevelExeKgi = value;
            case "minOpLevelExeEditLocation" -> minOpLevelExeEditLocation = value;
        }
        write("cyan");
        generateAllOptionsMap();
    }

    public static Map<String, Object> getAllOptionsMap()
    {
        return allOptionsMap;
    }
}
