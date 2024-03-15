package fr.aeldit.cyan.config;

import fr.aeldit.cyanlib.lib.config.BooleanOption;
import fr.aeldit.cyanlib.lib.config.IntegerOption;
import fr.aeldit.cyanlib.lib.utils.RULES;

public class CyanConfig
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
    public static final IntegerOption XP_TO_TP_BED = new IntegerOption("xpToTpBed", 200, RULES.POSITIVE_VALUE);
    public static final IntegerOption XP_TO_TP_SURFACE = new IntegerOption("xpToTpSurface", 50, RULES.POSITIVE_VALUE);
    public static final IntegerOption XP_TO_TP_TPA = new IntegerOption("xpToTpTpa", 200, RULES.POSITIVE_VALUE);
}
