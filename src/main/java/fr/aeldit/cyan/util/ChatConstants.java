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

    public static final Map<String, String> optionsTraductionsMap = new HashMap<>();
    public static final Map<String, String> commandsTraductionsMap = new HashMap<>();
    public static final Map<String, Map<String, String>> traductions = new HashMap<>();
    public static final List<String> commandsList = new ArrayList<>();
    public static final List<String> optionsTypesList = new ArrayList<>();

    public static void generateOptionsTraductionsMap()
    {
        optionsTraductionsMap.put("headerTop", "§e------------------------------------");
        optionsTraductionsMap.put("header", "\n§lDescription of the §e/%s option :");

        optionsTraductionsMap.put("allow", "§eAllow §foptions allows the player to enable or disable the use of a command");
        optionsTraductionsMap.put("minOpLevelExe", "§eMinOpLevelExe §fallows the player to define which OP level is required to execute the different commands");
        optionsTraductionsMap.put("other", "The other options have various other functionnalities, that are described by the name of the option itself");
    }

    public static void generateCommandsTraductionsMap()
    {
        commandsTraductionsMap.put("headerTop", "§e------------------------------------");
        commandsTraductionsMap.put("header", "\n§lDescription of the §e/%s command :");

        commandsTraductionsMap.put("bed", "The §e/bed §fcommand teleports you to your bed or respawn anchor");
        commandsTraductionsMap.put("kgi", "The §e/kgi §fcommand kills all item on the ground in a specific radius.\n§e/kgi §fto kill items in the default radius.\n§e/kgi [distance_in_chunks] §fto kill items in the specified radius");
        commandsTraductionsMap.put("surface", "The §e/surface §fcommand teleports you to the highest block located at your XY position");
    }

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
        optionsTypesList.add("other");


        return optionsTypesList;
    }

    public static Map<String, Map<String, String>> generateTraductionsMap()
    {
        generateOptionsTraductionsMap();
        generateCommandsTraductionsMap();

        traductions.put("options", optionsTraductionsMap);
        traductions.put("commands", commandsTraductionsMap);


        return traductions;
    }

    public static String getOptionTraduction(String optionName)
    {
        return optionsTraductionsMap.get(optionName);
    }

    public static String getAllOptionTraduction(String optionName)
    {
        return optionsTraductionsMap.get(optionName);
    }
}
