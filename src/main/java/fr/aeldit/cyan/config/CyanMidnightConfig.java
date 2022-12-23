package fr.aeldit.cyan.config;

import eu.midnightdust.lib.config.MidnightConfig;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CyanMidnightConfig extends MidnightConfig
{

    public static Map<String, Object> allowOptionsMap = new HashMap<>();
    public static Map<String, Object> exeLevelOptionsMap = new HashMap<>();
    public static Map<String, Object> otherOptionsBoolMap = new HashMap<>();
    public static Map<String, Object> otherOptionsIntMap = new HashMap<>();
    public static Map<String, Map<String, Object>> optionsMap = new HashMap<>();
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

    @Comment
    public static Comment intOptions;
    @Entry(min = 1, max = 64)
    public static int distanceToEntitiesKgi = 14;
    @Entry(min = 0, max = 4)
    public static int minOpLevelExeModifConfig = 4;
    @Entry(min = 0, max = 4)
    public static int minOpLevelExeBed = 0;
    @Entry(min = 0, max = 4)
    public static int minOpLevelExeKgi = 4;
    @Entry(min = 0, max = 4)
    public static int minOpLevelExeSurface = 0;

    @Comment
    public static Comment boolOptions;
    @Entry
    public static boolean useTranslations = true;
    @Entry
    public static boolean msgToActionBar = true;
    @Entry
    public static boolean errorToActionBar = true;

    public static Map<String, Object> generateBoolOptionsMap()
    {
        boolOptionsMap.put("allowBed", allowBed);
        boolOptionsMap.put("allowKgi", allowKgi);
        boolOptionsMap.put("allowSurface", allowSurface);
        boolOptionsMap.put("useTranslations", useTranslations);
        boolOptionsMap.put("msgToActionBar", msgToActionBar);
        boolOptionsMap.put("errorToActionBar", errorToActionBar);

        return boolOptionsMap;
    }

    public static Map<String, Object> generateIntegerOptionsMap()
    {
        integerOptionsMap.put("minOpLevelExeModifConfig", minOpLevelExeModifConfig);
        integerOptionsMap.put("minOpLevelExeBed", minOpLevelExeBed);
        integerOptionsMap.put("minOpLevelExeKgi", minOpLevelExeKgi);
        integerOptionsMap.put("minOpLevelExeSurface", minOpLevelExeSurface);

        integerOptionsMap.put("distanceToEntitiesKgi", distanceToEntitiesKgi);

        return integerOptionsMap;
    }

    public static Map<String, Object> generateAllOptionsMap()
    {
        allOptionsMap.putAll(generateBoolOptionsMap());
        allOptionsMap.putAll(generateIntegerOptionsMap());
        return allOptionsMap;
    }

    public static Map<String, Object> generateAllowOptionsMap()
    {
        allowOptionsMap.put("allowBed", allowBed);
        allowOptionsMap.put("allowKgi", allowKgi);
        allowOptionsMap.put("allowSurface", allowSurface);

        return allowOptionsMap;
    }

    public static Map<String, Object> generateExeLevelOptionsMap()
    {
        exeLevelOptionsMap.put("minOpLevelExeModifConfig", minOpLevelExeModifConfig);
        exeLevelOptionsMap.put("minOpLevelExeBed", minOpLevelExeBed);
        exeLevelOptionsMap.put("minOpLevelExeKgi", minOpLevelExeKgi);
        exeLevelOptionsMap.put("minOpLevelExeSurface", minOpLevelExeSurface);

        return exeLevelOptionsMap;
    }

    public static Map<String, Object> generateOtherBoolOptionsMap()
    {
        otherOptionsBoolMap.put("useOneLanguage", useTranslations);
        otherOptionsBoolMap.put("msgToActionBar", msgToActionBar);
        otherOptionsBoolMap.put("errorToActionBar", errorToActionBar);

        return otherOptionsBoolMap;
    }

    public static Map<String, Object> generateOtherIntOptionsMap()
    {
        otherOptionsIntMap.put("distanceToEntitiesKgi", distanceToEntitiesKgi);

        return otherOptionsIntMap;
    }

    /**
     * Generates the map that will contain all options and their value
     *
     * @return a <code>Map</code> that contains all the options
     */
    public static Map<String, Map<String, Object>> generateOptionsMap()
    {
        allowOptionsMap = generateAllowOptionsMap();
        exeLevelOptionsMap = generateExeLevelOptionsMap();
        otherOptionsBoolMap = generateOtherBoolOptionsMap();
        otherOptionsIntMap = generateOtherIntOptionsMap();
        optionsMap.put("allows", allowOptionsMap);
        optionsMap.put("minOpLevelExe", exeLevelOptionsMap);
        optionsMap.put("otherBool", otherOptionsBoolMap);
        optionsMap.put("otherInt", otherOptionsIntMap);

        return optionsMap;
    }

    public static void setBoolOption(@NotNull String optionName, boolean value)
    {
        switch (optionName)
        {
            case "allowBed" -> allowBed = value;
            case "allowKgi" -> allowKgi = value;
            case "allowSurface" -> allowSurface = value;
            case "useOneLanguage" -> useTranslations = value;
            case "msgToActionBar" -> msgToActionBar = value;
            case "errorToActionBar" -> errorToActionBar = value;
        }
        write("cyan");
    }

    public static void setIntOption(@NotNull String optionName, int value)
    {
        switch (optionName)
        {
            case "distanceToEntitiesKgi" -> distanceToEntitiesKgi = value;
            case "modifConfig" -> minOpLevelExeModifConfig = value;
            case "bed" -> minOpLevelExeBed = value;
            case "kgi" -> minOpLevelExeKgi = value;
            case "surface" -> minOpLevelExeSurface = value;
        }
        write("cyan");
    }

}
