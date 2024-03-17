package fr.aeldit.cyan.config;

import fr.aeldit.cyanlib.lib.config.BooleanOption;
import fr.aeldit.cyanlib.lib.config.CyanLibConfig;
import fr.aeldit.cyanlib.lib.config.IntegerOption;
import fr.aeldit.cyanlib.lib.utils.RULES;

import java.util.HashMap;
import java.util.Map;

public class CyanConfig implements CyanLibConfig
{
    public static final BooleanOption ALLOW_BED = new BooleanOption("allowBed", true);
    public static final BooleanOption ALLOW_KGI = new BooleanOption("allowKgi", true);
    public static final BooleanOption ALLOW_SURFACE = new BooleanOption("allowSurface", true);
    public static final BooleanOption ALLOW_LOCATIONS = new BooleanOption("allowLocations", true);
    public static final BooleanOption ALLOW_BACK_TP = new BooleanOption("allowBackTp", true);
    public static final BooleanOption ALLOW_TPA = new BooleanOption("allowTpa", true);

    public static final IntegerOption MIN_OP_LVL_KGI = new IntegerOption("minOpLvlKgi", 4, RULES.OP_LEVELS);
    public static final IntegerOption MIN_OP_LVL_EDIT_LOCATIONS = new IntegerOption(
            "minOpLvlEditLocation", 4, RULES.OP_LEVELS);
    public static final IntegerOption DISTANCE_TO_ENTITIES_KGI = new IntegerOption(
            "distanceToEntitiesKgi", 12, RULES.POSITIVE_VALUE
    );
    public static final IntegerOption DAYS_TO_REMOVE_BACK_TP = new IntegerOption(
            "daysToRemoveBackTp", 180, RULES.POSITIVE_VALUE
    );

    public static final BooleanOption USE_XP_TO_TELEPORT = new BooleanOption("useXpToTeleport", true);
    public static final IntegerOption BLOCKS_PER_XP_LEVEL_BED = new IntegerOption("blocksPerXpLevelBed", 200,
            RULES.POSITIVE_VALUE
    );
    public static final IntegerOption BLOCKS_PER_XP_LEVEL_SURFACE = new IntegerOption("blocksPerXpLevelSurface", 50,
            RULES.POSITIVE_VALUE
    );
    public static final IntegerOption BLOCKS_PER_XP_LEVEL_TPA = new IntegerOption("blocksPerXpLevelTpa", 200,
            RULES.POSITIVE_VALUE
    );

    @Override
    public Map<String, String> getDefaultTranslations()
    {
        Map<String, String> translations = new HashMap<>();

        // ERRORS
        translations.put("cyan.error.bedDisabled", "§cThe /bed command is disabled");
        translations.put("cyan.error.kgiDisabled", "§cThe /kgi command is disabled");
        translations.put("cyan.error.surfaceDisabled", "§cThe /surface command is disabled");
        translations.put("cyan.error.backTpDisabled", "§cThe /back command is disabled");
        translations.put("cyan.error.locationAlreadyExists", "§cA location with this name already exists");
        translations.put("cyan.error.locationsDisabled", "§cThe locations commands are disabled");
        translations.put("cyan.error.noLocations", "§cThere is no saved locations");
        translations.put("cyan.error.locationNotFound", "§cThe location %s §cdoesn't exist (check if you spelled it " +
                "correctly)");
        translations.put("cyan.error.noLastPos", "§cYour last death location was not saved");
        translations.put("cyan.error.bedNotFound", "§cYou don't have an attributed bed or respawn anchor");
        translations.put("cyan.error.playerNotFound", "§cPlayer not found. The player must be online");
        translations.put("cyan.error.noPropertiesFiles", "§cNo properties files were found");
        translations.put("cyan.error.notEnoughXp", "§cYou don't have enough XP (%s §clevels are required)");

        // MESSAGES
        translations.put("cyan.msg.bed", "§3You have been teleported to your bed");
        translations.put("cyan.msg.respawnAnchor", "§3You have been teleported to your respawn anchor");
        translations.put("cyan.msg.surface", "§3You have been teleported to the surface");
        translations.put("cyan.msg.kgi", "§3Ground items have been removed");
        translations.put("cyan.msg.kgir", "§3Ground items have been removed in a radius of %s §3chunks");
        translations.put("cyan.msg.setLocation", "§3The location %s §3have been saved");
        translations.put("cyan.msg.goToLocation", "§3You have been teleported to %s");
        translations.put("cyan.msg.removeLocation", "§3The location %s §3have been removed");
        translations.put("cyan.msg.listLocations", "§6Cyan - LOCATIONS :\n");
        translations.put("cyan.msg.removedAllLocations", "§3All the locations have been removed");
        translations.put("cyan.msg.renameLocation", "§3The location %s §3have been renamed to %s");
        translations.put("cyan.msg.getLocation", "%s §3(%s§3)");
        translations.put("cyan.msg.translationsReloaded", "§3The translations have been reloaded");
        translations.put("cyan.msg.backTp", "§3You have been teleported to the place you died");
        translations.put("cyan.msg.tpaRequestSend", "Your tpa request have been send");
        translations.put("cyan.msg.listRequestingPlayers", "The players that requested to teleport to you are :");
        translations.put("cyan.msg.noRequestingPlayers", "No player requested to teleport to you");
        translations.put("cyan.msg.notEnoughXpTpa", "You don't have enough XP to be teleported to %s");
        translations.put("cyan.msg.tpaRequest", "§6%s§r has requested to teleport to you.\nEnter '§a/tpaAccept " +
                "<playerName>' §rto accept or '§a/tpaRefuse <playerName>' §rto refuse");
        translations.put("cyan.msg.tpaSuccessful", "§6%s§r accepted your tpa request");
        translations.put("cyan.msg.tpaRefused", "§6%s§c refused your teleportation request");
        translations.put("cyan.msg.tpaAlreadyRequested", "§cYou already send a request to this player");
        translations.put("cyan.msg.tpaAcceptedSelf", "You accepted §6%s§r's tpa request");
        translations.put("cyan.msg.tpaRefusedSelf", "You refuse §6%s§r's tpa request");

        // SETS
        translations.put("cyan.msg.set.allowBed", "§3Toggled §d/bed §3command %s");
        translations.put("cyan.msg.set.allowKgi", "§3Toggled §d/kgi §3command %s");
        translations.put("cyan.msg.set.allowSurface", "§3Toggled §d/surface §3command %s");
        translations.put("cyan.msg.set.allowLocations", "§3Toggled §dlocation §3commands %s");
        translations.put("cyan.msg.set.allowBackTp", "§3Toggled §d/back §3command %s");
        translations.put("cyan.msg.set.allowTpa", "§3Toggled §d/tpa §3command %s");
        translations.put("cyan.msg.set.distanceToEntitiesKgi", "§3The distance for §d/kgi §3is now %s");
        translations.put("cyan.msg.set.minOpLvlKgi", "§3The minimum OP level to execute §d/kgi §3is now %s");
        translations.put("cyan.msg.set.minOpLvlEditLocation", "§3The minimum OP level to edit locations is now %s");
        translations.put("cyan.msg.set.daysToRemoveBackTp", "§3The number of days to keep the last death locations is" +
                " now %s");
        translations.put("cyan.msg.set.useXpToTeleport", "§3Toggled the use of XP to teleport %s");
        translations.put("cyan.msg.set.blocksPerXpLevelBed", "§3The number of blocks to consume 1 XP level for /bed " +
                "is now %s");
        translations.put("cyan.msg.set.blocksPerXpLevelSurface", "§3The number of blocks to consume 1 XP level for " +
                "/surface is now %s");
        translations.put("cyan.msg.set.blocksPerXpLevelTpa", "§3The number of blocks to consume 1 XP level for /tpa " +
                "is now %s");

        // HEADERS
        translations.put("cyan.msg.headerDescCmd", "§6Cyan - DESCRIPTION (commands) :\n");
        translations.put("cyan.msg.headerDescOptions", "§6Cyan - DESCRIPTION (options) :\n");

        // CONFIG
        translations.put("cyan.msg.getDesc.allowBed", "§3The §eallowBed §3option toggles the use of the §d/bed " +
                "§3command");
        translations.put("cyan.msg.getDesc.allowKgi", "§3The §eallowKgi §3option toggles the use of the §d/kgi " +
                "§3command");
        translations.put("cyan.msg.getDesc.allowSurface", "§3The §eallowSurface §3option toggles the use of the " +
                "§d/surface §3command");
        translations.put("cyan.msg.getDesc.allowLocations", "§3The §eallowLocations §3option toggles the use of the " +
                "§dlocation §3commands");
        translations.put("cyan.msg.getDesc.allowBackTp", "§3The §eallowBackTp §3option toggles the use of the §d/back" +
                " §3command");
        translations.put("cyan.msg.getDesc.allowTpa", "§3The §eallowTpa §3option toggles the use of the §d/tpa " +
                "§3command");
        translations.put("cyan.msg.getDesc.distanceToEntitiesKgi", "§3The §edistanceToEntitiesKgi §3option defines " +
                "distance (in chunks) in which the ground items will be removed");
        translations.put("cyan.msg.getDesc.minOpLvlKgi", "§3The §eminOpLvlKgi §3option defines the required OP level " +
                "to use the §d/kgi §3command");
        translations.put("cyan.msg.getDesc.minOpLvlEditLocation", "§3The §eminOpLvlEditLocation §3determines the " +
                "minimum OP level required to edit locations");
        translations.put("cyan.msg.getDesc.daysToRemoveBackTp", "§3The §edaysToRemoveBackTp §3option defines the " +
                "number of days the last death location of a player is kept)");
        translations.put("cyan.msg.getDesc.useXpToTeleport", "§3The §euseXpToTeleport §3option defines whether XP is " +
                "required to use teleportation commands such as /bed, /surface or /tpa");
        translations.put("cyan.msg.getDesc.blocksPerXpLevelBed", "§3The §eblocksPerXpLevelTpa §3option defines the " +
                "how many blocks will consume 1 level when using the /bed command (iff the §euseXpToTeleport §3option" +
                " is set to true)");
        translations.put("cyan.msg.getDesc.blocksPerXpLevelSurface", "§3The §eblocksPerXpLevelTpa §3option defines " +
                "the how many blocks will consume 1 level when using the /surface command (iff the §euseXpToTeleport " +
                "§3option is set to true)");
        translations.put("cyan.msg.getDesc.blocksPerXpLevelTpa", "§3The §eblocksPerXpLevelTpa §3option defines the " +
                "how many blocks will consume 1 level when using the /tpa command (iff the §euseXpToTeleport " +
                "§3options is set to true)");

        // GET_CFG
        translations.put("cyan.msg.getCfg.header", "§6Cyan - OPTIONS :\n");
        translations.put("cyan.msg.getCfg.allowBed", "§6- §d/bed §3: %s");
        translations.put("cyan.msg.getCfg.allowKgi", "§6- §d/kgi §3: %s");
        translations.put("cyan.msg.getCfg.allowSurface", "§6- §d/surface §3: %s");
        translations.put("cyan.msg.getCfg.allowLocations", "§6- §3Location commands : %s");
        translations.put("cyan.msg.getCfg.allowBackTp", "§6- §d/back §3: %s");
        translations.put("cyan.msg.getCfg.allowTpa", "§6- §d/tpa §3: %s");
        translations.put("cyan.msg.getCfg.distanceToEntitiesKgi", "§6- §d/kgi §3distance (in chunks) : %s");
        translations.put("cyan.msg.getCfg.minOpLvlKgi", "§6- §3Minimum OP level for §d/kgi §3: %s");
        translations.put("cyan.msg.getCfg.minOpLvlEditLocation", "§6- §3Minimum OP level to edit locations: %s");
        translations.put("cyan.msg.getCfg.daysToRemoveBackTp", "§6- §3Days to keep the death location: %s");
        translations.put("cyan.msg.getCfg.useXpToTeleport", "§6- §3Use XP for teleportation commands: %s");
        translations.put("cyan.msg.getCfg.blocksPerXpLevelBed", "§6- §3Blocks per 1 XP level for bed tp: %s");
        translations.put("cyan.msg.getCfg.blocksPerXpLevelSurface", "§6- §3Blocks per 1 XP level for surface tp: %s");
        translations.put("cyan.msg.getCfg.blocksPerXpLevelTpa", "§6- §3Blocks per 1 XP level for tpa: %s");

        return translations;
    }
}
