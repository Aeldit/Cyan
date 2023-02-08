package fr.aeldit.cyan.commands.argumentTypes;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.aeldit.cyan.config.CyanMidnightConfig;
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
     * @param builder the suggestion builder
     * @return a suggestion with all the available commands
     */
    public static CompletableFuture<Suggestions> getBoolOptions(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(CyanMidnightConfig.generateBoolOptionsMap().keySet(), builder);
    }

    /**
     * Called for the commands {@code /cyan config integerOption} and {@code /cyan description options integerOption}
     *
     * @param builder the suggestion builder
     * @return a suggestion with all the available commands
     */
    public static CompletableFuture<Suggestions> getIntegerOptions(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(CyanMidnightConfig.generateIntegerOptionsMap().keySet(), builder);
    }

    /**
     * Called for the command {@code /cyan description commands}
     *
     * @param builder the suggestion builder
     * @return a suggestion with all the available commands
     */
    public static CompletableFuture<Suggestions> getCommands(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(CyanMidnightConfig.generateCommandsList(), builder);
    }

    /**
     * Called for the location commands
     *
     * @param builder the suggestion builder
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
