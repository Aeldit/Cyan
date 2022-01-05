package fr.raphoulfifou.cyan.commands.argumentTypes;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class ArgumentSuggestion
{
    /**
     * @param context the command context
     * @param builder the suggestion builder
     * @return a suggestion with all players names (online <s>and whitelisted</s> player)
     */
    public static CompletableFuture<Suggestions> getAllPlayersNames(@NotNull CommandContext<ServerCommandSource> context, @NotNull SuggestionsBuilder builder)
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
     * @param context the command context
     * @param builder the suggestion builder
     * @return a suggestion with all players UUIDs (online and whitelisted player)
     */
    public static CompletableFuture<Suggestions> getAllPlayersUUID(@NotNull CommandContext<ServerCommandSource> context, @NotNull SuggestionsBuilder builder)
    {
        MinecraftServer server = context.getSource().getServer();

        Set<String> userNames = new HashSet<>(ArgumentSuggestion.getOnlinePlayersUUID(server));
        userNames.addAll(ArgumentSuggestion.getWhitelistedUUIDs(server));
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

    /**
     * @param server the Minecraft server
     * @return an array with the name of all online players UUIDs
     */
    public static @NotNull List<String> getOnlinePlayersUUID(final @NotNull MinecraftServer server)
    {
        PlayerManager playerManager = server.getPlayerManager();
        List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
        List<String> newL = new ArrayList<>();
        for (ServerPlayerEntity serverPlayerEntity : playerList)
        {
            UUID tempUUID = serverPlayerEntity.getUuid();
            newL.add(tempUUID.toString());
        }
        return newL;
    }

    /**
     * @param server server the Minecraft server
     * @return an array with the name of all whitelisted players names
     */
    public static @NotNull List<String> getWhitelistedUUIDs(final @NotNull MinecraftServer server)
    {
        PlayerManager playerManager = server.getPlayerManager();
        List<String> playerList = Arrays.stream(playerManager.getWhitelistedNames()).toList();
        List<String> newL = new ArrayList<>();
        for (String s : playerList)
        {
            UUID tempUUID = UUID.fromString(s);
            newL.add(tempUUID.toString());
        }
        return newL;
    }
}
