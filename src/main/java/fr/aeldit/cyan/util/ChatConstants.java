package fr.aeldit.cyan.util;

import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatConstants
{
    public static String notOP = "§cYou don't have the required permission to do that";
    public static String playerOnlyCmd = "§cThis command can only be executed by a player";
    public static String wrongOPLevel = "§cThe OP level must be in [1, 4]";
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
        commandsTraductionsMap.put("headerTop", "§3------------------------------------");
        commandsTraductionsMap.put("header", "§3Description of the §6/%s §l§3command :");

        commandsTraductionsMap.put("bed", "§3The §6/bed §3command teleports you to your bed or respawn anchor");
        commandsTraductionsMap.put("kgi",
                """
                        §3The §6/kgi §3command kills every item that is on the ground in a specific radius.
                        §6/kgi §3to kill items in the default radius (defined in the config, can be changed).
                        §6/kgi [distance_in_chunks] §3to kill items in the specified radius
                        """);
        commandsTraductionsMap.put("surface", "§3The §6/surface §3command teleports you to the highest block located at your position");
    }

    public static void generateOptionsTraductionsMap()
    {
        optionsTraductionsMap.put("headerTop", "§3------------------------------------");
        optionsTraductionsMap.put("header", "§l§3Description of the §6/%s §l§3option :");

        optionsTraductionsMap.put("allowBed", "§eAllowBed §3option toogles the use of the §6/bed command");
        optionsTraductionsMap.put("allowKgi", "§eAllowKgi §3option toogles the use of the §6/kgi command");
        optionsTraductionsMap.put("allowSurface", "§eAllowSurface §3option toogles the use of the §6/surface command");

        optionsTraductionsMap.put("useTranslations", "§eUseTranslations §3option toogles the use of translations (server-side only)");
        optionsTraductionsMap.put("msgToActionBar", "§eMsgToActionBar §3option determines if messages are send to the chat or the player's action bar");
        optionsTraductionsMap.put("errorToActionBar", "§eErrorToActionBar §3option determines if error messages are send to the chat or the player's action bar");
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
    public static Map<String, Map<String, String>> generateDescriptionTraductionsMap()
    {
        generateOptionsTraductionsMap();
        generateCommandsTraductionsMap();

        traductions.put("options", optionsTraductionsMap);
        traductions.put("commands", commandsTraductionsMap);

        return traductions;
    }

    public static Map<String, String> generateConfigTraductionsMap()
    {
        configTraductionsMap.put("allowBed", "§3Toogled §d/bed §3command %s");
        configTraductionsMap.put("allowKgi", "§3Toogled §d/kgi §3command %s");
        configTraductionsMap.put("allowSurface", "§3Toogled §d/surface §3command %s");
        configTraductionsMap.put("useTranslations", "§3Toogled translations %s");
        configTraductionsMap.put("msgToActionBar", "§3Toogled messages to action bar %s");
        configTraductionsMap.put("errorToActionBar", "§3Toogled error messages to action bar %s");
        configTraductionsMap.put("distanceToEntitiesKgi", "§3The distance for §d/kgi §3is now %s");
        configTraductionsMap.put("minOpLevelExeModifConfig", "§3The minimum OP level to edit the config is now %s");
        configTraductionsMap.put("minOpLevelExeBed", "§3The minimum OP level to execute §d/bed §3is now %s");
        configTraductionsMap.put("minOpLevelExeKgi", "§3The minimum OP level to execute §d/kgi §3is now %s");
        configTraductionsMap.put("minOpLevelExeSurface", "§3The minimum OP level to execute §d/surface §3is now %s");

        return configTraductionsMap;
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
