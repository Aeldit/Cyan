package fr.aeldit.cyan.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Utils
{
    public static final Path locationsPath = FabricLoader.getInstance().getConfigDir().resolve("cyan/locations.properties");
    public static String on = Formatting.GREEN + "ON";
    public static String off = Formatting.RED + "OFF";

    private static final Map<String, String> commandsTraductionsMap = new HashMap<>();
    private static final Map<String, String> optionsTraductionsMap = new HashMap<>();
    private static final Map<String, String> configTraductionsMap = new HashMap<>();
    private static final Map<String, String> configSetTraductionsMap = new HashMap<>();
    private static final Map<String, String> miscTraductionsMap = new HashMap<>();
    private static final Map<String, String> errorsTraductionsMap = new HashMap<>();
    private static final Map<String, String> cmdFeedbackTraductionsMap = new HashMap<>();

    private static void generateCommandsTraductionsMap()
    {
        commandsTraductionsMap.put("bed", "§3The §d/bed §3command teleports you to your bed or respawn anchor");
        commandsTraductionsMap.put("kgi",
                """
                        §3The §d/kgi §3command kills every item that is on the ground in a specific radius.
                        §d - /kgi §3to kill items in the default radius (defined in the config, can be changed).
                        §d - /kgi [distance_in_chunks] §3to kill items in the specified radius
                        """
        );
        commandsTraductionsMap.put("surface", "§3The §d/surface §3command teleports you to the highest block located at your position");
    }

    private static void generateOptionsTraductionsMap()
    {
        optionsTraductionsMap.put("allowBed", "§3The §eallowBed §3option toogles the use of the §d/bed §3command");
        optionsTraductionsMap.put("allowKgi", "§3The §eallowKgi §3option toogles the use of the §d/kgi §3command");
        optionsTraductionsMap.put("allowSurface", "§3The §eallowSurface §3option toogles the use of the §d/surface §3command");
        optionsTraductionsMap.put("allowLocations", "§3The §eallowLocations §3option toogles the use of the §dlocation §3commands");

        optionsTraductionsMap.put("useTranslations", "§3The §euseTranslations §3option toogles the use of translations (server-side only)");
        optionsTraductionsMap.put("msgToActionBar", "§3The §emsgToActionBar §3option determines if messages are send to the chat or the player's action bar");
        optionsTraductionsMap.put("errorToActionBar", "§3The §eerrorToActionBar §3option determines if error messages are send to the chat or the player's action bar");
    }

    private static void generateMiscTraductionsMap()
    {
        miscTraductionsMap.put("dashSeparation", "§6------------------------------------");
        miscTraductionsMap.put("listLocations", "§6Cyan - LOCATIONS :\n");
        miscTraductionsMap.put("headerDescCmd", "§6Cyan - DESCRIPTION (commands) :\n");
        miscTraductionsMap.put("headerDescOptions", "§6Cyan - DESCRIPTION (options) :\n");
    }

    private static void generateConfigTraductionsMap()
    {
        configTraductionsMap.put("header", "§6Cyan - OPTIONS :\n");
        configTraductionsMap.put("allowBed", "§6- §3/bed : %s");
        configTraductionsMap.put("allowKgi", "§6- §3/kgi : %s");
        configTraductionsMap.put("allowSurface", "§6- §3/surface : %s");
        configTraductionsMap.put("allowLocations", "§6- §3location commands : %s");
        configTraductionsMap.put("useTranslations", "§6- §3Use translations : %s");
        configTraductionsMap.put("msgToActionBar", "§6- §3Messages to action bar : %s");
        configTraductionsMap.put("errorToActionBar", "§6- §3Error messages to action bar : %s");

        configTraductionsMap.put("distanceToEntitiesKgi", "§6- §3kgi distance (in chunks) : %s");
        configTraductionsMap.put("minOpLevelExeModifConfig", "§6- §3Minimum OP level to edit config : %s");
        configTraductionsMap.put("minOpLevelExeBed", "§6- §3Minimum OP level for §d/bed §3: %s");
        configTraductionsMap.put("minOpLevelExeKgi", "§6- §3Minimum OP level for §d/kgi §3: %s");
        configTraductionsMap.put("minOpLevelExeSurface", "§6- §3Minimum OP level for §d/surface §3: %s");
        configTraductionsMap.put("minOpLevelExeLocation", "§6- §3Minimum OP level to see / teleport to locations §3: %s");
        configTraductionsMap.put("minOpLevelExeEditLocation", "§6- §3Minimum OP level to edit locations: %s");
    }

    private static void generateConfigSetTraductionsMap()
    {
        configSetTraductionsMap.put("allowBed", "§3Toogled §d/bed §3command %s");
        configSetTraductionsMap.put("allowKgi", "§3Toogled §d/kgi §3command %s");
        configSetTraductionsMap.put("allowSurface", "§3Toogled §d/surface §3command %s");
        configSetTraductionsMap.put("allowLocations", "§3Toogled §dlocation §3commands %s");
        configSetTraductionsMap.put("useTranslations", "§3Toogled translations %s");
        configSetTraductionsMap.put("msgToActionBar", "§3Toogled messages to action bar %s");
        configSetTraductionsMap.put("errorToActionBar", "§3Toogled error messages to action bar %s");
        configSetTraductionsMap.put("distanceToEntitiesKgi", "§3The distance for §d/kgi §3is now %s");
        configSetTraductionsMap.put("minOpLevelExeModifConfig", "§3The minimum OP level to edit the config is now %s");
        configSetTraductionsMap.put("minOpLevelExeBed", "§3The minimum OP level to execute §d/bed §3is now %s");
        configSetTraductionsMap.put("minOpLevelExeKgi", "§3The minimum OP level to execute §d/kgi §3is now %s");
        configSetTraductionsMap.put("minOpLevelExeSurface", "§3The minimum OP level to execute §d/surface §3is now %s");
        configSetTraductionsMap.put("minOpLevelExeLocation", "§3The minimum OP level to see / teleport to locations is now %s");
        configSetTraductionsMap.put("minOpLevelExeEditLocation", "§3The minimum OP level to edit locations is now %s");
    }

    public static void generateErrorsTraductionsMap()
    {
        errorsTraductionsMap.put("notOp", "§cYou don't have the required permission to do that");
        errorsTraductionsMap.put("wrongOPLevel", "§cThe OP level must be in [0;4]");
        errorsTraductionsMap.put("wrongDistanceKgi", "§cThe kgi distance must be in [1;128]");
        errorsTraductionsMap.put("playerNotFound", "§cPlayer not found. The player must be online");
        errorsTraductionsMap.put("incorrectIntOp", "§cThe OP level must be in [0;4]");
        errorsTraductionsMap.put("incorrectIntKgi", "§cThe distance must be in [1;128]");
        errorsTraductionsMap.put("disabled.bed", "§cThe /bed command is disabled. To enable it, enter '/cyan config booleanOptions allowBed true' in chat");
        errorsTraductionsMap.put("disabled.kgi", "§cThe /kgi command is disabled. To enable it, enter '/cyan config booleanOptions allowKgi true' in chat");
        errorsTraductionsMap.put("disabled.surface", "§cThe /surface command is disabled. To enable it, enter '/cyan config booleanOptions allowSurface true' in chat");
        errorsTraductionsMap.put("servOnly", "§cThis command can only be used on servers");
        errorsTraductionsMap.put("bed.error", "§cYou don't have an attributed bed or respawn anchor");
        errorsTraductionsMap.put("playerOnlyCmd", "§cThis command can only be executed by a player");
        errorsTraductionsMap.put("locationAlreadyExists", "§cA location with this name already exists");
        errorsTraductionsMap.put("disabled.locations", "§cThe locations commands are disabled. To enable them, enter '/cyan config booleanOptions allowLocations true' in chat");
        errorsTraductionsMap.put("locationNotFound", "§cThe location %s §cdoesn't exist (check if you spelled it correctly)");
        errorsTraductionsMap.put("fileNotRemoved", "§cAn error occured while trying to remove the locations file");
    }

    private static void generateCmdFeedbackTraductionsMap()
    {
        cmdFeedbackTraductionsMap.put("bed", "§3You have been teleported to your bed");
        cmdFeedbackTraductionsMap.put("respawnAnchor", "§3You have been teleported to your respawn anchor");
        cmdFeedbackTraductionsMap.put("kgi", "§3Ground items have been removed");
        cmdFeedbackTraductionsMap.put("kgir", "§3Ground items have been removed in a radius of %s §3chunks");
        cmdFeedbackTraductionsMap.put("surface", "§3You have been teleported to the surface");
        cmdFeedbackTraductionsMap.put("setLocation", "§3The location %s §3have been saved");
        cmdFeedbackTraductionsMap.put("goToLocation", "§3You have been teleported to %s");
        cmdFeedbackTraductionsMap.put("removeLocation", "§3The location %s §3have been removed");
        cmdFeedbackTraductionsMap.put("listLocations", "§3Locations :");
        cmdFeedbackTraductionsMap.put("removedAllLocations", "§3All the locations have been removed");
    }

    public static void generateAllMaps()
    {
        generateCommandsTraductionsMap();
        generateOptionsTraductionsMap();
        generateMiscTraductionsMap();
        generateConfigTraductionsMap();
        generateConfigSetTraductionsMap();
        generateErrorsTraductionsMap();
        generateCmdFeedbackTraductionsMap();
    }

    public static String getCommandTraduction(String command)
    {
        return commandsTraductionsMap.get(command) != null ? commandsTraductionsMap.get(command) : "null";
    }

    public static String getOptionTraduction(String option)
    {
        return optionsTraductionsMap.get(option) != null ? optionsTraductionsMap.get(option) : "null";
    }

    public static String getMiscTraduction(String option)
    {
        return miscTraductionsMap.get(option) != null ? miscTraductionsMap.get(option) : "null";
    }

    public static String getConfigTraduction(String option)
    {
        return configTraductionsMap.get(option) != null ? configTraductionsMap.get(option) : "null";
    }

    public static String getConfigSetTraduction(String option)
    {
        return configSetTraductionsMap.get(option) != null ? configSetTraductionsMap.get(option) : "null";
    }

    public static String getErrorTraduction(String option)
    {
        return errorsTraductionsMap.get(option) != null ? errorsTraductionsMap.get(option) : "null";
    }

    public static String getCmdFeedbackTraduction(String option)
    {
        return cmdFeedbackTraductionsMap.get(option) != null ? cmdFeedbackTraductionsMap.get(option) : "null";
    }

    public static Map<String, String> getCommandsTraductionsMap()
    {
        return commandsTraductionsMap;
    }

    public static Map<String, String> getOptionsTraductionsMap()
    {
        return optionsTraductionsMap;
    }

    public static void checkOrCreateDirs()
    {
        if (!Files.exists(FabricLoader.getInstance().getConfigDir().resolve("cyan")))
        {
            try
            {
                Files.createDirectory(FabricLoader.getInstance().getConfigDir().resolve("cyan"));
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        if (!Files.exists(locationsPath))
        {
            try
            {
                Files.createFile(locationsPath);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
