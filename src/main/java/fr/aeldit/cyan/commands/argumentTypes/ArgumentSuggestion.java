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
import fr.aeldit.cyan.config.CyanMidnightConfig;
import fr.aeldit.cyan.util.Utils;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import static fr.aeldit.cyan.util.Utils.locationsPath;

public final class ArgumentSuggestion
{
    /**
     * Called for the commands {@code /cyan config booleanOption} and {@code /cyan description options booleanOption}
     *
     * @return a suggestion with all the available commands
     */
    public static CompletableFuture<Suggestions> getBoolOptions(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(CyanMidnightConfig.getBoolOptionsMap().keySet(), builder);
    }

    /**
     * Called for the commands {@code /cyan config integerOption} and {@code /cyan description options integerOption}
     *
     * @return a suggestion with all the available commands
     */
    public static CompletableFuture<Suggestions> getIntegerOptions(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(CyanMidnightConfig.getIntegerOptionsMap().keySet(), builder);
    }

    /**
     * Called for the command {@code /cyan description commands}
     *
     * @return a suggestion with all the available commands
     */
    public static CompletableFuture<Suggestions> getCommands(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(Utils.getCommandsList(), builder);
    }

    /**
     * Called for the location commands
     *
     * @return a suggestion with all the locations
     */
    public static CompletableFuture<Suggestions> getLocations(@NotNull SuggestionsBuilder builder)
    {
        List<String> locations = new ArrayList<>();
        try
        {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(locationsPath.toUri())));

            locations.addAll(properties.stringPropertyNames());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return CommandSource.suggestMatching(locations, builder);
    }
}
