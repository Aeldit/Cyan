package fr.aeldit.cyan.config;

import eu.midnightdust.lib.config.MidnightConfig;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CyanMidnightConfig extends MidnightConfig
{
    public static Map<String, Object> boolOptionsMap = new HashMap<>();
    public static Map<String, Object> integerOptionsMap = new HashMap<>();
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
    public static int minOpLevelExeBed = 0;
    @Entry(isSlider = true, min = 0, max = 4)
    public static int minOpLevelExeKgi = 4;
    @Entry(isSlider = true, min = 0, max = 4)
    public static int minOpLevelExeSurface = 0;
    @Entry(isSlider = true, min = 0, max = 4)
    public static int minOpLevelExeLocation = 0;
    @Entry(isSlider = true, min = 0, max = 4)
    public static int minOpLevelExeEditLocation = 4;

    @Comment
    public static Comment boolOptions;
    @Entry
    public static boolean msgToActionBar = true;
    @Entry
    public static boolean errorToActionBar = true;
    @Entry
    public static boolean useTranslations = true;

    private static void generateBoolOptionsMap()
    {
        boolOptionsMap.put("allowBed", allowBed);
        boolOptionsMap.put("allowKgi", allowKgi);
        boolOptionsMap.put("allowSurface", allowSurface);
        boolOptionsMap.put("allowLocations", allowLocations);
        boolOptionsMap.put("allowBackTp", allowBackTp);
        boolOptionsMap.put("allowConsoleEditConfig", allowConsoleEditConfig);
        boolOptionsMap.put("useTranslations", useTranslations);
        boolOptionsMap.put("msgToActionBar", msgToActionBar);
        boolOptionsMap.put("errorToActionBar", errorToActionBar);
    }

    private static void generateIntegerOptionsMap()
    {
        integerOptionsMap.put("minOpLevelExeEditConfig", minOpLevelExeEditConfig);
        integerOptionsMap.put("minOpLevelExeBed", minOpLevelExeBed);
        integerOptionsMap.put("minOpLevelExeKgi", minOpLevelExeKgi);
        integerOptionsMap.put("minOpLevelExeSurface", minOpLevelExeSurface);
        integerOptionsMap.put("minOpLevelExeLocation", minOpLevelExeLocation);
        integerOptionsMap.put("minOpLevelExeEditLocation", minOpLevelExeEditLocation);

        integerOptionsMap.put("distanceToEntitiesKgi", distanceToEntitiesKgi);
    }

    public static void generateAllOptionsMap()
    {
        generateBoolOptionsMap();
        generateIntegerOptionsMap();
        allOptionsMap.putAll(getBoolOptionsMap());
        allOptionsMap.putAll(getIntegerOptionsMap());
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
            case "useTranslations" -> useTranslations = value;
            case "msgToActionBar" -> msgToActionBar = value;
            case "errorToActionBar" -> errorToActionBar = value;
        }
        write("cyan");
        generateAllOptionsMap();
    }

    public static void setIntOption(@NotNull String optionName, int value)
    {
        switch (optionName)
        {
            case "distanceToEntitiesKgi" -> distanceToEntitiesKgi = value;
            case "minOpLevelExeEditConfig" -> minOpLevelExeEditConfig = value;
            case "minOpLevelExeBed" -> minOpLevelExeBed = value;
            case "minOpLevelExeKgi" -> minOpLevelExeKgi = value;
            case "minOpLevelExeSurface" -> minOpLevelExeSurface = value;
            case "minOpLevelExeLocation" -> minOpLevelExeLocation = value;
            case "minOpLevelExeEditLocation" -> minOpLevelExeEditLocation = value;
        }
        write("cyan");
        generateAllOptionsMap();
    }

    public static Map<String, Object> getBoolOptionsMap()
    {
        return boolOptionsMap;
    }

    public static Map<String, Object> getIntegerOptionsMap()
    {
        return integerOptionsMap;
    }

    public static Map<String, Object> getAllOptionsMap()
    {
        return allOptionsMap;
    }
}
