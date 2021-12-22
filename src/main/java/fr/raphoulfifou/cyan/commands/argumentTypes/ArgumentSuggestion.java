package fr.raphoulfifou.cyan.commands.argumentTypes;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ArgumentSuggestion
{
    /**
     * @param context the command context
     * @param builder the suggestion builder
     * @return a suggestion with all players names (online <s>and whitelisted</s> player)
     */
    public static CompletableFuture<Suggestions> getAllPlayerNames(@NotNull CommandContext<ServerCommandSource> context, @NotNull SuggestionsBuilder builder)
    {
        MinecraftServer server = context.getSource().getServer();

        Set<String> userNames = new HashSet<>(ArgumentSuggestion.getOnlinePlayerNames(server));
        //userNames.addAll(ArgumentSuggestion.getWhitelistedNames(server));
        /*if (!builder.getRemaining().isEmpty())
        {
            
        }*/

        // Return the suggestion handler
        return CommandSource.suggestMatching(userNames, builder);
    }

    /**
     * @param server the Minecraft server
     * @return an array with the name of all online players names
     */
    public static @NotNull List<String> getOnlinePlayerNames(final @NotNull MinecraftServer server)
    {
        PlayerManager playerManager = server.getPlayerManager();
        return Arrays.asList(playerManager.getPlayerNames());
    }

    /**
     * @param server server the Minecraft server
     * @return an array with the name of all whitelisted players names
     */
    public static @NotNull List<String> getWhitelistedNames(final @NotNull MinecraftServer server)
    {
        PlayerManager playerManager = server.getPlayerManager();
        return Arrays.asList(playerManager.getWhitelistedNames());
    }
}
