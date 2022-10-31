package fr.aeldit.cyan.commands.argumentTypes;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import fr.aeldit.cyan.util.ChatConstants;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ArgumentSuggestion
{

    /**
     * @param builder the suggestion builder
     * @return a suggestion with all available boolean options
     */
    public static CompletableFuture<Suggestions> getOtherBoolOptions(@NotNull SuggestionsBuilder builder)
    {
        Map<String, Object> options = CyanMidnightConfig.generateOtherBoolOptionsMap();

        List<String> exeLevels = new ArrayList<>();
        for (Map.Entry<String, Object> entry : options.entrySet())
        {
            exeLevels.add(entry.getKey());
        }

        // Return the suggestion handler
        return CommandSource.suggestMatching(exeLevels, builder);
    }

    /**
     * @param builder the suggestion builder
     * @return a suggestion with all available integer options
     */
    public static CompletableFuture<Suggestions> getOtherIntOptions(@NotNull SuggestionsBuilder builder)
    {
        Map<String, Object> options = CyanMidnightConfig.generateOtherIntOptionsMap();

        List<String> exeLevels = new ArrayList<>();
        for (Map.Entry<String, Object> entry : options.entrySet())
        {
            exeLevels.add(entry.getKey());
        }

        // Return the suggestion handler
        return CommandSource.suggestMatching(exeLevels, builder);
    }

    /**
     * @param builder the suggestion builder
     * @return a suggestion with all available allow options
     */
    public static CompletableFuture<Suggestions> getCommands(@NotNull SuggestionsBuilder builder)
    {
        Map<String, Object> options = CyanMidnightConfig.generateAllowOptionsMap();

        List<String> exeLevels = new ArrayList<>();
        // Here we cut the 'allow' part of the String
        for (Map.Entry<String, Object> entry : options.entrySet())
        {
            String cutWord = entry.getKey().substring(5);
            String tmpOption = String.valueOf(cutWord.charAt(0)).toLowerCase();
            cutWord = cutWord.substring(1);
            tmpOption = tmpOption.concat(cutWord);
            exeLevels.add(tmpOption);
        }

        // Return the suggestion handler
        return CommandSource.suggestMatching(exeLevels, builder);
    }

    /**
     * @param builder the suggestion builder
     * @return a suggestion with all available allow options
     */
    public static CompletableFuture<Suggestions> getOptionsTypes(@NotNull SuggestionsBuilder builder)
    {
        List<String> options = ChatConstants.generateOptionsTypesMap();


        // Return the suggestion handler
        return CommandSource.suggestMatching(options, builder);
    }

    /**
     * @param builder the suggestion builder
     * @return a suggestion with all available allow options
     */
    public static CompletableFuture<Suggestions> getOptions(@NotNull SuggestionsBuilder builder)
    {
        Map<String, Object> options = CyanMidnightConfig.generateAllowOptionsMap();

        List<String> exeLevels = new ArrayList<>();
        exeLevels.add("all");
        // Here we cut the 'allow' part of the String
        for (Map.Entry<String, Object> entry : options.entrySet())
        {
            String cutWord = entry.getKey().substring(5);
            String tmpOption = String.valueOf(cutWord.charAt(0)).toLowerCase();
            cutWord = cutWord.substring(1);
            tmpOption = tmpOption.concat(cutWord);
            exeLevels.add(tmpOption);
        }

        // Return the suggestion handler
        return CommandSource.suggestMatching(exeLevels, builder);
    }

    /**
     * @param builder the suggestion builder
     * @return a suggestion with all available allow options + the bulk toogle + modifConfig
     */
    public static CompletableFuture<Suggestions> getOptionsGeneral(@NotNull SuggestionsBuilder builder)
    {
        Map<String, Object> options = CyanMidnightConfig.generateAllowOptionsMap();

        List<String> exeLevels = new ArrayList<>();
        exeLevels.add("all");
        exeLevels.add("modifConfig");
        // Here we cut the 'allow' part of the String
        for (Map.Entry<String, Object> entry : options.entrySet())
        {
            String cutWord = entry.getKey().substring(5);
            String tmpOption = String.valueOf(cutWord.charAt(0)).toLowerCase();
            cutWord = cutWord.substring(1);
            tmpOption = tmpOption.concat(cutWord);
            exeLevels.add(tmpOption);
        }

        // Return the suggestion handler
        return CommandSource.suggestMatching(exeLevels, builder);
    }

    /**
     * @param builder the suggestion builder
     * @return a suggestion with all OP levels
     */
    public static CompletableFuture<Suggestions> getOpLevels(@NotNull SuggestionsBuilder builder)
    {

        List<String> exeLevels = new ArrayList<>();
        exeLevels.add("0");
        exeLevels.add("1");
        exeLevels.add("2");
        exeLevels.add("3");
        exeLevels.add("4");

        // Return the suggestion handler
        return CommandSource.suggestMatching(exeLevels, builder);
    }

}
