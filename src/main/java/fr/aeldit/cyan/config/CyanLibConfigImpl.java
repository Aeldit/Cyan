package fr.aeldit.cyan.config;

import fr.aeldit.cyanlib.lib.config.BooleanOption;
import fr.aeldit.cyanlib.lib.config.ICyanLibConfig;
import fr.aeldit.cyanlib.lib.config.IntegerOption;
import fr.aeldit.cyanlib.lib.utils.RULES;

import java.util.Map;

import static java.util.Map.entry;

public class CyanLibConfigImpl implements ICyanLibConfig
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
        return Map.<String, String>ofEntries(
                // ERRORS
                entry("cyan.error.bedDisabled", "§cThe /bed command is disabled"),
                entry("cyan.error.kgiDisabled", "§cThe /kgi command is disabled"),
                entry("cyan.error.surfaceDisabled", "§cThe /surface command is disabled"),
                entry("cyan.error.backTpDisabled", "§cThe /back command is disabled"),
                entry("cyan.error.locationAlreadyExists", "§cA location with this name already exists"),
                entry("cyan.error.locationsDisabled", "§cThe locations commands are disabled"),
                entry("cyan.error.noLocations", "§cThere is no saved locations"),
                entry(
                        "cyan.error.locationNotFound",
                        "§cThe location %s §cdoesn't exist (check if you spelled it correctly)"
                ),
                entry("cyan.error.noLastPos", "§cYour last death location was not saved"),
                entry("cyan.error.bedNotFound", "§cYou don't have an attributed bed or respawn anchor"),
                entry("cyan.error.playerNotFound", "§cPlayer not found. The player must be online"),
                entry("cyan.error.noPropertiesFiles", "§cNo properties files were found"),
                entry("cyan.error.notEnoughXp", "§cYou don't have enough XP (%s §clevels are required)"),
                entry("cyan.error.notEnoughXpTpa", "You don't have enough XP to be teleported to %s"),
                entry("cyan.error.tpaAlreadyRequested", "§cYou already send a request to this player"),

                // MESSAGES
                entry("cyan.msg.bed", "§3You have been teleported to your bed"),
                entry("cyan.msg.respawnAnchor", "§3You have been teleported to your respawn anchor"),
                entry("cyan.msg.surface", "§3You have been teleported to the surface"),
                entry("cyan.msg.kgi", "§3Ground items have been removed"),
                entry("cyan.msg.kgir", "§3Ground items have been removed in a radius of %s §3chunks"),
                entry("cyan.msg.setLocation", "§3The location %s §3have been saved"),
                entry("cyan.msg.goToLocation", "§3You have been teleported to %s"),
                entry("cyan.msg.removeLocation", "§3The location %s §3have been removed"),
                entry("cyan.msg.listLocations", "§6Cyan - LOCATIONS :\n"),
                entry("cyan.msg.removedAllLocations", "§3All the locations have been removed"),
                entry("cyan.msg.renameLocation", "§3The location %s §3have been renamed to %s"),
                entry("cyan.msg.getLocation", "%s §3(%s§3)"),
                entry("cyan.msg.translationsReloaded", "§3The translations have been reloaded"),
                entry("cyan.msg.backTp", "§3You have been teleported to the place you died"),
                entry("cyan.msg.tpaRequestSend", "Your tpa request have been send"),
                entry("cyan.msg.listRequestingPlayers", "The players that requested to teleport to you are :"),
                entry("cyan.msg.noRequestingPlayers", "No player requested to teleport to you"),
                entry("cyan.msg.tpaSuccessful", "§6%s§r accepted your tpa request"),
                entry("cyan.msg.tpaRefused", "§6%s§c refused your teleportation request"),
                entry("cyan.msg.tpaAcceptedSelf", "You accepted §6%s§r's tpa request"),
                entry("cyan.msg.tpaRefusedSelf", "You refuse §6%s§r's tpa request"),
                entry("cyan.msg.tpaRequested", "%s§r wants to teleport to you"),

                // SETS
                entry("cyan.msg.set.allowBed", "§3Toggled §d/bed §3command %s"),
                entry("cyan.msg.set.allowKgi", "§3Toggled §d/kgi §3command %s"),
                entry("cyan.msg.set.allowSurface", "§3Toggled §d/surface §3command %s"),
                entry("cyan.msg.set.allowLocations", "§3Toggled §dlocation §3commands %s"),
                entry("cyan.msg.set.allowBackTp", "§3Toggled §d/back §3command %s"),
                entry("cyan.msg.set.allowTpa", "§3Toggled §d/tpa §3command %s"),
                entry("cyan.msg.set.distanceToEntitiesKgi", "§3The distance for §d/kgi §3is now %s"),
                entry("cyan.msg.set.minOpLvlKgi", "§3The minimum OP level to execute §d/kgi §3is now %s"),
                entry("cyan.msg.set.minOpLvlEditLocation", "§3The minimum OP level to edit locations is now %s"),
                entry(
                        "cyan.msg.set.daysToRemoveBackTp",
                        "§3The number of days to keep the last death locations is now %s"
                ),
                entry("cyan.msg.set.useXpToTeleport", "§3Toggled the use of XP to teleport %s"),
                entry(
                        "cyan.msg.set.blocksPerXpLevelBed",
                        "§3The number of blocks to consume 1 XP level for /bed is now %s"
                ),
                entry(
                        "cyan.msg.set.blocksPerXpLevelSurface",
                        "§3The number of blocks to consume 1 XP level for /surface is now %s"
                ),
                entry(
                        "cyan.msg.set.blocksPerXpLevelTpa",
                        "§3The number of blocks to consume 1 XP level for /tpa is now %s"
                ),

                // HEADERS
                entry("cyan.msg.headerDescCmd", "§6Cyan - DESCRIPTION (commands) :\n"),
                entry("cyan.msg.headerDescOptions", "§6Cyan - DESCRIPTION (options) :\n"),

                // CONFIG
                entry(
                        "cyan.msg.getDesc.allowBed",
                        "§3The §eallowBed §3option toggles the use of the §d/bed §3command"
                ),
                entry(
                        "cyan.msg.getDesc.allowKgi",
                        "§3The §eallowKgi §3option toggles the use of the §d/kgi §3command"
                ),
                entry(
                        "cyan.msg.getDesc.allowSurface",
                        "§3The §eallowSurface §3option toggles the use of the §d/surface §3command"
                ),
                entry(
                        "cyan.msg.getDesc.allowLocations",
                        "§3The §eallowLocations §3option toggles the use of the §dlocation §3commands"
                ),
                entry(
                        "cyan.msg.getDesc.allowBackTp",
                        "§3The §eallowBackTp §3option toggles the use of the §d/back §3command"
                ),
                entry(
                        "cyan.msg.getDesc.allowTpa",
                        "§3The §eallowTpa §3option toggles the use of the §d/tpa §3command"
                ),
                entry(
                        "cyan.msg.getDesc.distanceToEntitiesKgi",
                        "§3The §edistanceToEntitiesKgi §3option defines distance (in chunks) in which the ground " +
                                "items will be removed"
                ),
                entry(
                        "cyan.msg.getDesc.minOpLvlKgi",
                        "§3The §eminOpLvlKgi §3option defines the required OP level to use the §d/kgi §3command"
                ),
                entry(
                        "cyan.msg.getDesc.minOpLvlEditLocation",
                        "§3The §eminOpLvlEditLocation §3determines the " +
                                "minimum OP level required to edit locations"
                ),
                entry(
                        "cyan.msg.getDesc.daysToRemoveBackTp",
                        "§3The §edaysToRemoveBackTp §3option defines the " +
                                "number of days the last death location of a player is kept)"
                ),
                entry(
                        "cyan.msg.getDesc.useXpToTeleport",
                        "§3The §euseXpToTeleport §3option defines whether XP is " +
                                "required to use teleportation commands such as /bed, /surface or /tpa"
                ),
                entry(
                        "cyan.msg.getDesc.blocksPerXpLevelBed",
                        "§3The §eblocksPerXpLevelTpa §3option defines the how many blocks will consume 1 level when " +
                                "using the /bed command (iff the §euseXpToTeleport §3option is set to true)"
                ),
                entry(
                        "cyan.msg.getDesc.blocksPerXpLevelSurface",
                        "§3The §eblocksPerXpLevelTpa §3option defines the how many blocks will consume 1 level when " +
                                "using the /surface command (iff the §euseXpToTeleport §3option is set to true)"
                ),
                entry(
                        "cyan.msg.getDesc.blocksPerXpLevelTpa",
                        "§3The §eblocksPerXpLevelTpa §3option defines the how many blocks will consume 1 level when " +
                                "using the /tpa command (iff the §euseXpToTeleport §3options is set to true)"
                ),

                // GET_CFG
                entry("cyan.msg.getCfg.header", "§6Cyan - OPTIONS :\n"),
                entry("cyan.msg.getCfg.allowBed", "§6- §d/bed §3: %s"),
                entry("cyan.msg.getCfg.allowKgi", "§6- §d/kgi §3: %s"),
                entry("cyan.msg.getCfg.allowSurface", "§6- §d/surface §3: %s"),
                entry("cyan.msg.getCfg.allowLocations", "§6- §3Location commands : %s"),
                entry("cyan.msg.getCfg.allowBackTp", "§6- §d/back §3: %s"),
                entry("cyan.msg.getCfg.allowTpa", "§6- §d/tpa §3: %s"),
                entry("cyan.msg.getCfg.distanceToEntitiesKgi", "§6- §d/kgi §3distance (in chunks) : %s"),
                entry("cyan.msg.getCfg.minOpLvlKgi", "§6- §3Minimum OP level for §d/kgi §3: %s"),
                entry("cyan.msg.getCfg.minOpLvlEditLocation", "§6- §3Minimum OP level to edit locations: %s"),
                entry("cyan.msg.getCfg.daysToRemoveBackTp", "§6- §3Days to keep the death location: %s"),
                entry("cyan.msg.getCfg.useXpToTeleport", "§6- §3Use XP for teleportation commands: %s"),
                entry("cyan.msg.getCfg.blocksPerXpLevelBed", "§6- §3Blocks per 1 XP level for bed tp: %s"),
                entry("cyan.msg.getCfg.blocksPerXpLevelSurface", "§6- §3Blocks per 1 XP level for surface tp: %s"),
                entry("cyan.msg.getCfg.blocksPerXpLevelTpa", "§6- §3Blocks per 1 XP level for tpa: %s")
        );
    }
}
