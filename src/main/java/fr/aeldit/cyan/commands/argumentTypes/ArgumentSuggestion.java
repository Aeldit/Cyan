/*
 * Copyright (c) 2023  -  Made by Aeldit
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

package fr.aeldit.cyan.commands.argumentTypes;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.aeldit.cyan.util.Utils;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static fr.aeldit.cyan.util.Utils.LocationsObj;

public final class ArgumentSuggestion
{
    /**
     * Called for the location commands
     *
     * @return a suggestion with all the locations
     */
    public static CompletableFuture<Suggestions> getLocations(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(LocationsObj.getLocationsNames(), builder);
    }

    /**
     * Called for the command {@code /cyan optionName}
     *
     * @return a suggestion with all the available options
     */
    public static CompletableFuture<Suggestions> getOptions(@NotNull SuggestionsBuilder builder)
    {
        List<String> options = new ArrayList<>();
        options.addAll(Utils.getOptionsList().get("booleans"));
        options.addAll(Utils.getOptionsList().get("integers"));
        return CommandSource.suggestMatching(options, builder);
    }

    /**
     * Called for the command {@code /cyan optionName [integer]}
     *
     * @return a suggestion with all the available integers for the configurations
     */
    public static CompletableFuture<Suggestions> getInts(@NotNull SuggestionsBuilder builder)
    {
        List<String> ints = new ArrayList<>();
        ints.add("0");
        ints.add("1");
        ints.add("2");
        ints.add("3");
        ints.add("4");
        return CommandSource.suggestMatching(ints, builder);
    }
}
