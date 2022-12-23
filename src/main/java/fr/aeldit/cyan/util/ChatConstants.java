package fr.aeldit.cyan.util;

import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatConstants
{
    public static String notOP = "§cYou don't have the required permission to do that";
    public static String on = Formatting.GREEN + "ON";
    public static String off = Formatting.RED + "OFF";

    public static Formatting green = Formatting.GREEN;
    public static Formatting red = Formatting.RED;
    public static Formatting gold = Formatting.GOLD;
    public static Formatting cyan = Formatting.DARK_AQUA;

    public static final Map<String, String> commandsTraductionsMap = new HashMap<>();
    public static final Map<String, Map<String, String>> traductions = new HashMap<>();
    public static final List<String> commandsList = new ArrayList<>();
    public static final List<String> optionsTypesList = new ArrayList<>();

    public static final Map<String, String> optionsTraductionsMap = new HashMap<>();
    public static final Map<String, String> configTraductionsMap = new HashMap<>();

    public static void generateCommandsTraductionsMap()
    {
        commandsTraductionsMap.put("headerTop", "§e------------------------------------");
        commandsTraductionsMap.put("header", "\n§lDescription of the §e/%s command :");

        commandsTraductionsMap.put("bed", "The §e/bed §fcommand teleports you to your bed or respawn anchor");
        commandsTraductionsMap.put("kgi", "The §e/kgi §fcommand kills every item that is on the ground in a specific radius.\n§e/kgi §fto kill items in the default radius (defined in the config, can be changed).\n§e/kgi [distance_in_chunks] §fto kill items in the specified radius");
        commandsTraductionsMap.put("surface", "The §e/surface §fcommand teleports you to the highest block located at your position");
    }

    public static void generateOptionsTraductionsMap()
    {
        optionsTraductionsMap.put("headerTop", "§l§e------------------------------------");
        optionsTraductionsMap.put("header", "\n§lDescription of the §e/%s option :");

        optionsTraductionsMap.put("allowBed", "§eAllowBed §foption toogles the use of the /bed command");
        optionsTraductionsMap.put("allowKgi", "§eAllowKgi §foption toogles the use of the /kgi command");
        optionsTraductionsMap.put("allowSurface", "§eAllowSurface §foption toogles the use of the /surface command");

        optionsTraductionsMap.put("useTranslations", "§eUseTranslations §foption toogles the use of translations (server-side only)");
    }

    // For the ArgumentSuggestions
    public static List<String> generateCommandsMap()
    {
        commandsList.add("bed");
        commandsList.add("kgi");
        commandsList.add("surface");

        return commandsList;
    }

    public static List<String> generateOptionsTypesMap()
    {
        optionsTypesList.add("allow");
        optionsTypesList.add("minOpLevelExe");

        return optionsTypesList;
    }

    // Generates the final map
    public static Map<String, Map<String, String>> generateConfigTraductionsMap()
    {
        generateOptionsTraductionsMap();
        generateCommandsTraductionsMap();

        traductions.put("options", optionsTraductionsMap);
        traductions.put("commands", commandsTraductionsMap);

        return traductions;
    }

    public static Map<String, Map<String, String>> generateDescriptionTraductionsMap()
    {
        generateOptionsTraductionsMap();
        generateCommandsTraductionsMap();

        traductions.put("options", optionsTraductionsMap);
        traductions.put("commands", commandsTraductionsMap);

        return traductions;
    }

    public static String getConfigTraduction(String optionName)
    {
        return configTraductionsMap.get(optionName);
    }

    public static String getOptionTraduction(String optionName)
    {
        return optionsTraductionsMap.get(optionName);
    }
}
