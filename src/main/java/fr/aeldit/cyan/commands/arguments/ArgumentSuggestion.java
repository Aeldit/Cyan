package fr.aeldit.cyan.commands.arguments;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.aeldit.cyan.teleportation.TPa;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ArgumentSuggestion
{
    public static CompletableFuture<Suggestions> getOnlinePlayersName(
            @NotNull SuggestionsBuilder builder, @NotNull ServerCommandSource source
    )
    {
        ArrayList<String> players = new ArrayList<>(source.getServer().getPlayerManager().getPlayerList().size() - 1);

        if (source.getPlayer() == null)
        {
            return new CompletableFuture<>();
        }

        String playerName = source.getPlayer().getName().getString();

        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList())
        {
            if (!playerName.equals(player.getName().getString()))
            {
                players.add(player.getName().getString());
            }
        }
        return CommandSource.suggestMatching(players, builder);
    }

    public static CompletableFuture<Suggestions> getRequestingPlayersNames(
            @NotNull SuggestionsBuilder builder, @NotNull ServerCommandSource source
    )
    {
        List<String> requestingPlayers = TPa.getRequestingPlayers(source.getName());
        if (requestingPlayers == null)
        {
            return new CompletableFuture<>();
        }
        return CommandSource.suggestMatching(requestingPlayers, builder);
    }
}
