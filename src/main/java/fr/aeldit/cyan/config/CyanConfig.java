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
    public Map<String, String> getDefaultTranslations() // TODO -> copy the latest translations in this function
    {
        Map<String, String> translations = new HashMap<>();

        translations.put("listLocations", "§6Cyan - LOCATIONS :\n");
        translations.put("headerDescCmd", "§6Cyan - DESCRIPTION (commands) :\n");
        translations.put("headerDescOptions", "§6Cyan - DESCRIPTION (options) :\n");

        translations.put("desc.allowBed", "§3The §eallowBed §3option toggles the use of the §d/bed §3command");
        translations.put("desc.allowKgi", "§3The §eallowKgi §3option toggles the use of the §d/kgi §3command");
        translations.put("desc.allowSurface", "§3The §eallowSurface §3option toggles the use of the §d/surface " +
                "§3command");
        translations.put("desc.allowLocations", "§3The §eallowLocations §3option toggles the use of the §dlocation " +
                "§3commands");
        translations.put("desc.allowBackTp", "§3The §eallowBackTp §3option toggles the use of the §d/back §3command");
        translations.put("desc.distanceToEntitiesKgi", "§3The §edistanceToEntitiesKgi §3option defines distance (in " +
                "chunks) in which the ground items will be removed");
        translations.put("desc.minOpLvlKgi", "§3The §eminOpLvlKgi §3option defines the required OP level to use the " +
                "§d/kgi §3command");
        translations.put("desc.minOpLvlEditLocation", "§3The §eminOpLvlEditLocation §3option defines the required OP " +
                "level to edit the locations");
        translations.put("desc.daysToRemoveBackTp", "§3The §edaysToRemoveBackTp §3option defines the number of days " +
                "the last death location of a player is kept");

        translations.put("getCfg.header", "§6Cyan - OPTIONS :\n");
        translations.put("getCfg.allowBed", "§6- §d/bed §3: %s");
        translations.put("getCfg.allowKgi", "§6- §d/kgi §3: %s");
        translations.put("getCfg.allowSurface", "§6- §d/surface §3: %s");
        translations.put("getCfg.allowLocations", "§6- §3Location commands : %s");
        translations.put("getCfg.allowBackTp", "§6- §d/back §3: %s");
        translations.put("getCfg.distanceToEntitiesKgi", "§6- §d/kgi §3distance (in chunks) : %s");
        translations.put("getCfg.minOpLvlKgi", "§6- §3Minimum OP level for §d/kgi §3: %s");
        translations.put("getCfg.minOpLvlEditLocation", "§6- §3Minimum OP level to edit locations: %s");
        translations.put("getCfg.daysToRemoveBackTp", "§6- §3Days to keep the death location: %s");

        translations.put("set.allowBed", "§3Toggled §d/bed §3command %s");
        translations.put("set.allowKgi", "§3Toggled §d/kgi §3command %s");
        translations.put("set.allowSurface", "§3Toggled §d/surface §3command %s");
        translations.put("set.allowLocations", "§3Toggled §dlocation §3commands %s");
        translations.put("set.allowBackTp", "§3Toggled §d/back §3command %s");
        translations.put("set.distanceToEntitiesKgi", "§3The distance for §d/kgi §3is now %s");
        translations.put("set.minOpLvlKgi", "§3The minimum OP level to execute §d/kgi §3is now %s");
        translations.put("set.minOpLvlEditLocation", "§3The minimum OP level to edit locations is now %s");
        translations.put("set.daysToRemoveBackTp", "§3The number of days to keep the last death locations is now %s");

        translations.put("error.bedDisabled", "§cThe /bed command is disabled");
        translations.put("error.kgiDisabled", "§cThe /kgi command is disabled");
        translations.put("error.surfaceDisabled", "§cThe /surface command is disabled");
        translations.put("error.locationsDisabled", "§cThe locations commands are disabled");
        translations.put("error.backTpDisabled", "§cThe /back command is disabled");
        translations.put("error.servOnly", "§cThis command can only be used on servers");
        translations.put("error.locationAlreadyExists", "§cA location with this name already exists");
        translations.put("error.locationNotFound", "§cThe location %s §cdoesn't exist (check if you spelled it " +
                "correctly)");
        translations.put("error.bedNotFound", "§cYou don't have an attributed bed or respawn anchor");
        translations.put("error.playerNotFound", "§cPlayer not found. The player must be online");
        translations.put("error.noLocations", "§cThere is no saved locations");
        translations.put("error.noLastPos", "§cYour last death location was not saved");
        translations.put("error.noPropertiesFiles", "§cNo properties files were found");
        translations.put("error.optionNotFound", "§cThis option does not exist");
        translations.put("error.wrongType", "§cThis option can only be set to the %s §ctype");

        translations.put("bed", "§3You have been teleported to your bed");
        translations.put("respawnAnchor", "§3You have been teleported to your respawn anchor");
        translations.put("kgi", "§3Ground items have been removed");
        translations.put("kgir", "§3Ground items have been removed in a radius of %s §3chunks");
        translations.put("surface", "§3You have been teleported to the surface");
        translations.put("setLocation", "§3The location %s §3have been saved");
        translations.put("goToLocation", "§3You have been teleported to %s");
        translations.put("removeLocation", "§3The location %s §3have been removed");
        translations.put("removedAllLocations", "§3All the locations have been removed");
        translations.put("renameLocation", "§3The location %s have been renamed to %s");
        translations.put("getLocation", "%s §3(%s§3)");
        translations.put("backTp", "§3You have been teleported to the place you died");

        return translations;
    }
}
