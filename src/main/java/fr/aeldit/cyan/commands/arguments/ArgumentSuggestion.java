package fr.aeldit.cyan.commands.arguments;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.aeldit.cyan.teleportation.TPUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class ArgumentSuggestion
{
    public static CompletableFuture<Suggestions> getOnlinePlayersName(
            @NotNull SuggestionsBuilder builder, @NotNull ServerCommandSource source
    )
    {
        List<String> players = new ArrayList<>();
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList())
        {
            players.add(player.getName().getString());
        }
        players.remove(Objects.requireNonNull(source.getPlayer()).getName().getString());

        return CommandSource.suggestMatching(players, builder);
    }

    public static CompletableFuture<Suggestions> getRequestingPlayersNames(
            @NotNull SuggestionsBuilder builder, @NotNull ServerCommandSource source
    )
    {
        List<String> players = TPUtils.getRequestingPlayers(source.getName());

        return CommandSource.suggestMatching(players, builder);
    }
}
