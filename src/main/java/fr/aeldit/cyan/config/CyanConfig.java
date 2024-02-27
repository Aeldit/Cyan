/*
 * Copyright (c) 2023-2024  -  Made by Aeldit
 *
 *              GNU LESSER GENERAL PUBLIC LICENSE
 *                  Version 3, 29 June 2007
 *
 *  Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *  Everyone is permitted to copy and distribute verbatim copies
 *  of this license document, but changing it is not allowed.
 *
 *
 * This version of the GNU Lesser General Public License incorporates
 * the terms and conditions of version 3 of the GNU General Public
 * License, supplemented by the additional permissions listed in the LICENSE.txt file
 * in the repo of this mod (https://github.com/Aeldit/Cyan)
 */

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

    public static final IntegerOption MIN_OP_LVL_KGI = new IntegerOption("minOpLvlKgi", 4, RULES.OP_LEVELS);
    public static final IntegerOption MIN_OP_LVL_EDIT_LOCATIONS = new IntegerOption("minOpLvlEditLocation", 4, RULES.OP_LEVELS);
    public static final IntegerOption DISTANCE_TO_ENTITIES_KGI = new IntegerOption("distanceToEntitiesKgi", 12, RULES.POSITIVE_VALUE);
    public static final IntegerOption DAYS_TO_REMOVE_BACK_TP = new IntegerOption("daysToRemoveBackTp", 180, RULES.POSITIVE_VALUE);

    public static final BooleanOption USE_XP_TO_TELEPORT = new BooleanOption("useXpToTeleport", true);
    // Bse distance is 100 blocks
    public static final IntegerOption XP_REQUIRED_TO_TP_BASE_DISTANCE = new IntegerOption("xpRequiredToTpBaseDistance", 200, RULES.POSITIVE_VALUE);
    public static final IntegerOption XP_REQUIRED_TO_TP_BASE_DISTANCE_Y = new IntegerOption("xpRequiredToTpBaseDistanceY", 50, RULES.POSITIVE_VALUE);

    // CyanLib Required Options
    public static final BooleanOption USE_CUSTOM_TRANSLATIONS = new BooleanOption("useCustomTranslations", false, RULES.LOAD_CUSTOM_TRANSLATIONS);
}
