package fr.aeldit.cyan.config;

import eu.midnightdust.lib.config.MidnightConfig;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CyanMidnightConfig extends MidnightConfig
{
    public static final List<String> commandsList = new ArrayList<>();
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
    @Entry(isSlider = true, min = 1, max = 128)
    public static int distanceToEntitiesKgi = 14;
    @Entry(isSlider = true, min = 0, max = 4)
    public static int minOpLevelExeModifConfig = 4;
    @Entry(isSlider = true, min = 0, max = 4)
    public static int minOpLevelExeBed = 0;
    @Entry(isSlider = true, min = 0, max = 4)
    public static int minOpLevelExeKgi = 4;
    @Entry(isSlider = true, min = 0, max = 4)
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

    // For the ArgumentSuggestions
    public static List<String> generateCommandsList()
    {
        commandsList.add("bed");
        commandsList.add("kgi");
        commandsList.add("surface");

        return commandsList;
    }

    public static void setBoolOption(@NotNull String optionName, boolean value)
    {
        switch (optionName)
        {
            case "allowBed" -> allowBed = value;
            case "allowKgi" -> allowKgi = value;
            case "allowSurface" -> allowSurface = value;
            case "useTranslations" -> useTranslations = value;
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
            case "minOpLevelExeModifConfig" -> minOpLevelExeModifConfig = value;
            case "minOpLevelExeBed" -> minOpLevelExeBed = value;
            case "minOpLevelExeKgi" -> minOpLevelExeKgi = value;
            case "minOpLevelExeSurface" -> minOpLevelExeSurface = value;
        }
        write("cyan");
    }
}
