package fr.raphoulfifou.cyan.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class CommandSuggestions {

    public static CompletableFuture<Suggestions> getOnlinePlayerNames(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        PlayerManager manager = context.getSource().getMinecraftServer().getPlayerManager();
        return CommandSource.suggestMatching(manager.getPlayerList().stream().map((player) -> player.getGameProfile().getName()), builder);
    }
}
