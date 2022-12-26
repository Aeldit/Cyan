package fr.aeldit.cyan.commands.argumentTypes;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class ArgumentSuggestion
{
    /**
     * Called by the commands {@code /cyan config booleanOption} and {@code /cyan description options booleanOption}
     *
     * @param builder the suggestion builder
     * @return a suggestion with all the available commands
     */
    public static CompletableFuture<Suggestions> getBoolOptions(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(CyanMidnightConfig.generateBoolOptionsMap().keySet(), builder);
    }

    /**
     * Called by the commands {@code /cyan config integerOption} and {@code /cyan description options integerOption}
     *
     * @param builder the suggestion builder
     * @return a suggestion with all the available commands
     */
    public static CompletableFuture<Suggestions> getIntegerOptions(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(CyanMidnightConfig.generateIntegerOptionsMap().keySet(), builder);
    }

    /**
     * Called by the command {@code /cyan getConfig}
     *
     * @param builder the suggestion builder
     * @return a suggestion with all the available commands
     */
    public static CompletableFuture<Suggestions> getCommands(@NotNull SuggestionsBuilder builder)
    {
        return CommandSource.suggestMatching(CyanMidnightConfig.generateCommandsList(), builder);
    }
}
