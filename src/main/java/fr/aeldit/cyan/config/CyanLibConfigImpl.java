package fr.aeldit.cyan.config;

import fr.aeldit.cyanlib.lib.config.BooleanOption;
import fr.aeldit.cyanlib.lib.config.ICyanLibConfig;
import fr.aeldit.cyanlib.lib.config.IntegerOption;
import fr.aeldit.cyanlib.lib.utils.RULES;

import java.util.Map;

import static java.util.Map.entry;

public class CyanLibConfigImpl implements ICyanLibConfig
{
    // Allows
    public static final BooleanOption ALLOW_BED = new BooleanOption("allowBed", true);
    public static final BooleanOption ALLOW_KGI = new BooleanOption("allowKgi", true);
    public static final BooleanOption ALLOW_SURFACE = new BooleanOption("allowSurface", true);
    public static final BooleanOption ALLOW_LOCATIONS = new BooleanOption("allowLocations", true);
    public static final BooleanOption ALLOW_BACK_TP = new BooleanOption("allowBackTp", true);
    public static final BooleanOption ALLOW_TPA = new BooleanOption("allowTpa", true);

    // Min OP Level
    public static final IntegerOption MIN_OP_LVL_KGI = new IntegerOption("minOpLvlKgi", 4, RULES.OP_LEVELS);
    public static final IntegerOption MIN_OP_LVL_EDIT_LOCATIONS = new IntegerOption(
            "minOpLvlEditLocation", 4, RULES.OP_LEVELS
    );
    public static final IntegerOption MIN_OP_LVL_PERM_NODES = new IntegerOption(
            "minOpLvlPermNodes", 4, RULES.OP_LEVELS
    );

    // Other
    public static final IntegerOption DISTANCE_TO_ENTITIES_KGI = new IntegerOption(
            "distanceToEntitiesKgi", 12, RULES.POSITIVE_VALUE
    );
    public static final IntegerOption DAYS_TO_REMOVE_BACK_TP = new IntegerOption(
            "daysToRemoveBackTp", 180, RULES.POSITIVE_VALUE
    );

    // XP to teleport
    public static final BooleanOption USE_XP_TO_TELEPORT = new BooleanOption("useXpToTeleport", true);
    public static final BooleanOption XP_USE_POINTS = new BooleanOption("xpUsePoints", false);
    public static final BooleanOption XP_USE_FIXED_AMOUNT = new BooleanOption("xpUseFixedAmount", false);
    public static final IntegerOption XP_AMOUNT = new IntegerOption("xpAmount", 1, RULES.POSITIVE_VALUE);

    public static final IntegerOption BLOCKS_PER_XP_LEVEL_BED = new IntegerOption(
            "blocksPerXpLevelBed", 200, RULES.POSITIVE_VALUE
    );
    public static final IntegerOption BLOCKS_PER_XP_LEVEL_SURFACE = new IntegerOption(
            "blocksPerXpLevelSurface", 50, RULES.POSITIVE_VALUE
    );
    public static final IntegerOption BLOCKS_PER_XP_LEVEL_LOCATION = new IntegerOption(
            "blocksPerXpLevelLocation", 200, RULES.POSITIVE_VALUE
    );
    public static final IntegerOption BLOCKS_PER_XP_LEVEL_TPA = new IntegerOption(
            "blocksPerXpLevelTpa", 200, RULES.POSITIVE_VALUE
    );

    public static final BooleanOption TP_IN_COMBAT = new BooleanOption("tpInCombat", true);
    public static final IntegerOption COMBAT_TIMEOUT_SECONDS = new IntegerOption(
            "combatTimeoutSeconds", 30, RULES.POSITIVE_VALUE
    );

    @Override
    public Map<String, String> getDefaultTranslations()
    {
        return Map.<String, String>ofEntries(
                // ERRORS
                entry("error.bedDisabled", "§cThe /bed command is disabled"),
                entry("error.kgiDisabled", "§cThe /kgi command is disabled"),
                entry("error.surfaceDisabled", "§cThe /surface command is disabled"),
                entry("error.backTpDisabled", "§cThe /back command is disabled"),
                entry("error.locationAlreadyExists", "§cA location with this name already exists"),
                entry("error.locationsDisabled", "§cThe locations commands are disabled"),
                entry("error.noLocations", "§cThere is no saved locations"),
                entry(
                        "error.locationNotFound",
                        "§cThe location %s§c doesn't exist (check if you spelled it correctly)"
                ),
                entry("error.noLastPos", "§cYour last death location was not saved"),
                entry("error.bedNotFound", "§cYou don't have an attributed bed or respawn anchor"),
                entry("error.playerNotFound", "§cPlayer not found. The player must be online"),
                entry("error.noPropertiesFiles", "§cNo properties files were found"),
                entry("error.notEnoughXp", "§cYou don't have enough XP (%s§c %s§c are required)"),
                entry("error.notEnoughXpTpa", "You don't have enough XP to be teleported to %s"),
                entry("error.tpaAlreadyRequested", "§cYou already send a request to this player"),
                entry("error.noTpWhileInCombat", "§cYou cannot teleport while in combat"),
                entry("error.noRequestingPlayers", "No player requested to teleport to you"),

                // MESSAGES
                entry("msg.bed", "§3You have been teleported to your bed"),
                entry("msg.respawnAnchor", "§3You have been teleported to your respawn anchor"),
                entry("msg.surface", "§3You have been teleported to the surface"),
                entry("msg.kgi", "§3Ground items have been removed"),
                entry("msg.kgiR", "§3Ground items have been removed in a radius of %s §3chunks"),
                entry("msg.setLocation", "§3The location %s §3have been saved"),
                entry("msg.goToLocation", "§3You have been teleported to %s"),
                entry("msg.removeLocation", "§3The location %s §3have been removed"),
                entry("msg.listLocations", "§6Cyan - LOCATIONS :\n"),
                entry("msg.removedAllLocations", "§3All the locations have been removed"),
                entry("msg.renameLocation", "§3The location %s §3have been renamed to %s"),
                entry("msg.getLocation", "%s §3(%s§3)"),
                entry("msg.translationsReloaded", "§3The translations have been reloaded"),
                entry("msg.backTp", "§3You have been teleported to the place you died"),
                entry("msg.tpaRequestSend", "Your tpa request have been send"),
                entry("msg.listRequestingPlayers", "The players that requested to teleport to you are :"),
                entry("msg.tpaSuccessful", "§6%s§r accepted your tpa request"),
                entry("msg.tpaRefused", "§6%s§c refused your teleportation request"),
                entry("msg.tpaAcceptedSelf", "You accepted §6%s§r's tpa request"),
                entry("msg.tpaRefusedSelf", "You refuse §6%s§r's tpa request"),
                entry("msg.tpaRequested", "%s§r wants to teleport to you"),

                // SETS
                entry("msg.set.allowBed", "§3Toggled §d/bed §3command %s"),
                entry("msg.set.allowKgi", "§3Toggled §d/kgi §3command %s"),
                entry("msg.set.allowSurface", "§3Toggled §d/surface §3command %s"),
                entry("msg.set.allowLocations", "§3Toggled§d location §3commands %s"),
                entry("msg.set.allowBackTp", "§3Toggled §d/back §3command %s"),
                entry("msg.set.allowTpa", "§3Toggled §d/tpa §3command %s"),
                entry("msg.set.distanceToEntitiesKgi", "§3The distance for §d/kgi §3is now %s"),
                entry("msg.set.minOpLvlKgi", "§3The minimum OP level to execute §d/kgi §3is now %s"),
                entry("msg.set.minOpLvlEditLocation", "§3The minimum OP level to edit locations is now %s"),
                entry("msg.set.minOpLvlPermNodes", "§3The minimum OP level to use permission nodes is now %s"),
                entry("msg.set.daysToRemoveBackTp", "§3The number of days to keep the last death locations is now %s"),
                entry("msg.set.useXpToTeleport", "§3Toggled the use of XP to teleport %s"),
                entry(
                        "msg.set.blocksPerXpLevelBed",
                        "§3The number of blocks to consume 1 XP level for /bed is now %s"
                ),
                entry(
                        "msg.set.blocksPerXpLevelSurface",
                        "§3The number of blocks to consume 1 XP level for /surface is now %s"
                ),
                entry(
                        "msg.set.blocksPerXpLevelLocation",
                        "§3The number of blocks to consume 1 XP level for /location is now %s"
                ),
                entry(
                        "msg.set.blocksPerXpLevelTpa",
                        "§3The number of blocks to consume 1 XP level for /tpa is now %s"
                ),
                entry("msg.set.xpUsePoints", "§3Toggled the use of XP points instead of XP levels %s"),
                entry("msg.set.xpUseFixedAmount", "§3Toggled the use of a fixed XP amount %s"),
                entry("msg.set.xpAmount", "§3The fixed XP amount to use when teleporting is now %s"),
                entry("msg.set.tpInCombat", "§3Toggled teleportation while in combat %s"),
                entry("msg.set.combatTimeoutSeconds", "§3The combat timeout is now %s second(s)"),

                // HEADERS
                entry("msg.headerDescCmd", "§6Cyan - DESCRIPTION (commands) :\n"),
                entry("msg.headerDescOptions", "§6Cyan - DESCRIPTION (options) :\n"),

                // CONFIG
                entry("msg.getDesc.allowBed", "§3The§e allowBed §3option toggles the use of the §d/bed §3command"),
                entry("msg.getDesc.allowKgi", "§3The§e allowKgi §3option toggles the use of the §d/kgi §3command"),
                entry(
                        "msg.getDesc.allowSurface",
                        "§3The§e allowSurface §3option toggles the use of the §d/surface §3command"
                ),
                entry(
                        "msg.getDesc.allowLocations",
                        "§3The§e allowLocations §3option toggles the use of the§d location §3commands"
                ),
                entry(
                        "msg.getDesc.allowBackTp",
                        "§3The§e allowBackTp §3option toggles the use of the §d/back §3command"
                ),
                entry(
                        "msg.getDesc.allowTpa",
                        "§3The§e allowTpa §3option toggles the use of the §d/tpa §3command"
                ),
                entry(
                        "msg.getDesc.distanceToEntitiesKgi",
                        "§3The§e distanceToEntitiesKgi §3option defines distance (in chunks) in which the ground " +
                                "items will be removed"
                ),
                entry(
                        "msg.getDesc.minOpLvlKgi",
                        "§3The §eminOpLvlKgi §3option defines the required OP level to use the §d/kgi §3command"
                ),
                entry(
                        "msg.getDesc.minOpLvlEditLocation",
                        "§3The §eminOpLvlEditLocation §3determines the " +
                                "minimum OP level required to edit locations"
                ),
                entry(
                        "msg.getDesc.minOpLvlPermNodes",
                        "§3The §eminOpLvlPermNodes §3determines the " +
                                "minimum OP level required to use the permission nodes"
                ),
                entry(
                        "msg.getDesc.daysToRemoveBackTp",
                        "§3The§e daysToRemoveBackTp §3option defines the " +
                                "number of days the last death location of a player is kept)"
                ),
                entry(
                        "msg.getDesc.useXpToTeleport",
                        "§3The§e useXpToTeleport §3option defines whether XP is " +
                                "required to use teleportation commands such as /bed, /surface or /tpa"
                ),
                entry(
                        "msg.getDesc.blocksPerXpLevelBed",
                        "§3The§e blocksPerXpLevelTpa §3option defines how many blocks will consume 1 level when " +
                                "using the /bed command (iff the§e useXpToTeleport §3option is set to true)"
                ),
                entry(
                        "msg.getDesc.blocksPerXpLevelSurface",
                        "§3The§e blocksPerXpLevelTpa §3option defines how many blocks will consume 1 level when " +
                                "using the /surface command (iff the§e useXpToTeleport §3option is set to true)"
                ),
                entry(
                        "msg.getDesc.blocksPerXpLevelLocation",
                        "§3The§e blocksPerXpLevelLocation §3option defines how many blocks will consume 1 level when " +
                                "using the /location command (iff the§e useXpToTeleport §3options is set to true)"
                ),
                entry(
                        "msg.getDesc.blocksPerXpLevelTpa",
                        "§3The§e blocksPerXpLevelTpa §3option defines how many blocks will consume 1 level when " +
                                "using the /tpa command (iff the§e useXpToTeleport §3options is set to true)"
                ),
                entry(
                        "msg.getDesc.xpUsePoints",
                        "§3The§e xpUsePoints §3option defines the whether the necessary XP will be in points or in " +
                                "levels"
                ),
                entry(
                        "msg.getDesc.xpUseFixedAmount",
                        "§3The§e xpUseFixedAmount §3option defines the whether the necessary XP to teleport will be a"
                                + " fixed amount or will depend on the distance"
                ),
                entry(
                        "msg.getDesc.xpAmount",
                        "§3The§e xpAmount §3option defines the fixed amount of XP used when the xpUseFixedAmount "
                                + "option is ON"
                ),
                entry(
                        "msg.getDesc.tpInCombat",
                        "§3The§e tpInCombat §3option defines whether players can teleport to homes after taking "
                                + "damage by a mod or a player"
                ),
                entry(
                        "msg.getDesc.combatTimeoutSeconds",
                        "§3The§e combatTimeoutSeconds §3option defines the amount of time in seconds a player stays "
                                + "in combat mode after taking damage"
                ),

                // GET_CFG
                entry("msg.getCfg.header", "§6Cyan - OPTIONS:\n"),
                entry("msg.getCfg.allowBed", "§6- §d/bed §3: %s"),
                entry("msg.getCfg.allowKgi", "§6- §d/kgi §3: %s"),
                entry("msg.getCfg.allowSurface", "§6- §d/surface §3: %s"),
                entry("msg.getCfg.allowLocations", "§6- §3Location commands: %s"),
                entry("msg.getCfg.allowBackTp", "§6- §d/back §3: %s"),
                entry("msg.getCfg.allowTpa", "§6- §d/tpa §3: %s"),
                entry("msg.getCfg.distanceToEntitiesKgi", "§6- §d/kgi §3distance (in chunks): %s"),
                entry("msg.getCfg.minOpLvlKgi", "§6- §3Minimum OP level for §d/kgi §3: %s"),
                entry("msg.getCfg.minOpLvlEditLocation", "§6- §3Minimum OP level to edit locations: %s"),
                entry("msg.getCfg.minOpLvlPermNodes", "§6- §3Minimum OP level to use permission nodes: %s"),
                entry("msg.getCfg.daysToRemoveBackTp", "§6- §3Days to keep the death location: %s"),
                entry("msg.getCfg.useXpToTeleport", "§6- §3Use XP for teleportation commands: %s"),
                entry("msg.getCfg.xpUsePoints", "§6- §3Use XP points instead of XP levels: %s"),
                entry("msg.getCfg.blocksPerXpLevelBed", "§6- §3Blocks per 1 XP level for bed tp: %s"),
                entry("msg.getCfg.blocksPerXpLevelSurface", "§6- §3Blocks per 1 XP level for surface tp: %s"),
                entry("msg.getCfg.blocksPerXpLevelLocation", "§6- §3Blocks per 1 XP level for location: %s"),
                entry("msg.getCfg.blocksPerXpLevelTpa", "§6- §3Blocks per 1 XP level for tpa: %s"),
                entry("msg.getCfg.xpUseFixedAmount", "§6- §3Use fixed amount of XP for TPs: %s"),
                entry("msg.getCfg.xpAmount", "§6- §3Fixed XP amount: %s"),
                entry("msg.getCfg.tpInCombat", "§6- §3TP while in combat: %s"),
                entry("msg.getCfg.combatTimeoutSeconds", "§6- §3Combat timeout: %s §3second(s)")
        );
    }
}
