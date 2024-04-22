package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.commands.arguments.ArgumentSuggestion;
import fr.aeldit.cyan.teleportation.BackTps;
import fr.aeldit.cyan.teleportation.TPUtils;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static fr.aeldit.cyan.CyanCore.*;
import static fr.aeldit.cyan.config.CyanLibConfigImpl.*;
import static fr.aeldit.cyan.teleportation.TPUtils.*;

public class TeleportationCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("back")
                .executes(TeleportationCommands::back)
        );

        dispatcher.register(CommandManager.literal("bed")
                .executes(TeleportationCommands::bed)
        );
        dispatcher.register(CommandManager.literal("b")
                .executes(TeleportationCommands::bed)
        );

        dispatcher.register(CommandManager.literal("surface")
                .executes(TeleportationCommands::surface)
        );
        dispatcher.register(CommandManager.literal("s")
                .executes(TeleportationCommands::surface)
        );

        dispatcher.register(CommandManager.literal("tpa")
                .then(CommandManager.argument("player_name", StringArgumentType.string())
                        .suggests(
                                (context, builder) -> ArgumentSuggestion.getOnlinePlayersName(
                                        builder, context.getSource()))
                        .executes(TeleportationCommands::tpa)
                )
        );
        dispatcher.register(CommandManager.literal("tpaAccept")
                .then(CommandManager.argument("player_name", StringArgumentType.string())
                        .suggests(
                                (context, builder) -> ArgumentSuggestion.getRequestingPlayersNames(
                                        builder, context.getSource()))
                        .executes(TeleportationCommands::acceptTpa)
                )
        );
        dispatcher.register(CommandManager.literal("tpaRefuse")
                .then(CommandManager.argument("player_name", StringArgumentType.string())
                        .suggests(
                                (context, builder) -> ArgumentSuggestion.getRequestingPlayersNames(
                                        builder, context.getSource()))
                        .executes(TeleportationCommands::refuseTpa)
                )
        );
    }

    /**
     * Called by the command {@code /back}
     * <p>
     * Teleports the player to its last death position
     */
    public static int back(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_BACK_TP.getValue(), "backTpDisabled"))
            {
                BackTps.BackTp backTp = BACK_TPS.getBackTp(player.getUuidAsString());

                if (backTp != null)
                {
                    MinecraftServer server = player.getServer();

                    if (server != null)
                    {
                        switch (backTp.dimension())
                        {
                            case "overworld" -> player.teleport(
                                    server.getWorld(World.OVERWORLD), backTp.x(), backTp.y(), backTp.z(), 0, 0
                            );
                            case "nether" -> player.teleport(
                                    server.getWorld(World.NETHER), backTp.x(), backTp.y(), backTp.z(), 0, 0
                            );
                            case "end" -> player.teleport(
                                    server.getWorld(World.END), backTp.x(), backTp.y(), backTp.z(), 0, 0
                            );
                        }

                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player, "cyan.msg.backTp");
                    }
                }
                else
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player, "cyan.error.noLastPos");
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the commands {@code /bed} or {@code /b}
     * <p>
     * Teleports the player to its bed
     */
    public static int bed(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_BED.getValue(), "bedDisabled"))
            {
                int requiredXpLevel = 0;
                BlockPos spawnPos = player.getSpawnPointPosition();

                if (spawnPos != null)
                {
                    if (USE_XP_TO_TELEPORT.getValue())
                    {
                        requiredXpLevel = getRequiredXpLevelsToTp(player, spawnPos, BLOCKS_PER_XP_LEVEL_BED);

                        if (player.experienceLevel < requiredXpLevel)
                        {
                            CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                    player,
                                    "cyan.msg.notEnoughXp",
                                    Formatting.GOLD + String.valueOf(requiredXpLevel)
                            );
                            return 0;
                        }
                    }

                    MinecraftServer server = player.getServer();

                    if (server != null)
                    {
                        RegistryKey<World> spawnDim = player.getSpawnPointDimension();

                        player.teleport(
                                server.getWorld(spawnDim),
                                spawnPos.getX(),
                                spawnPos.getY(),
                                spawnPos.getZ(),
                                player.getYaw(), player.getPitch()
                        );

                        String key = spawnDim == World.OVERWORLD ? "bed" : "respawnAnchor";
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player, "cyan.msg.%s".formatted(key));

                        player.addExperienceLevels(-1 * requiredXpLevel);
                    }
                    else
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player, "cyan.error.bedNotFound");
                    }
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the commands {@code /surface} or {@code /s}
     * <p>
     * Teleport the player to the highest block that was found on the player's coordinates
     */
    public static int surface(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_SURFACE.getValue(), "surfaceDisabled"))
            {
                int requiredXpLevel = 0;
                BlockPos blockPos = player.getBlockPos();
                double topY = player.getWorld().getTopY(Heightmap.Type.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());

                if (USE_XP_TO_TELEPORT.getValue())
                {
                    int distanceY = (int) player.getY() - (int) topY;

                    // Converts to a positive distance
                    if (distanceY < 0)
                    {
                        distanceY *= -1;
                    }
                    // Minecraft doesn't center the position to the middle of the block but in 1 corner,
                    // so this allows for a better centering
                    distanceY += 1;

                    int coordinatesDistance = distanceY;

                    if (coordinatesDistance < BLOCKS_PER_XP_LEVEL_SURFACE.getValue())
                    {
                        requiredXpLevel = 1;
                    }
                    else
                    {
                        requiredXpLevel = 1 + coordinatesDistance / BLOCKS_PER_XP_LEVEL_SURFACE.getValue();
                    }

                    if (player.experienceLevel < requiredXpLevel)
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                "cyan.msg.notEnoughXp",
                                Formatting.GOLD + String.valueOf(requiredXpLevel)
                        );
                        return 0;
                    }
                }

                player.teleport(
                        context.getSource().getWorld(),
                        blockPos.getX(),
                        topY,
                        blockPos.getZ(),
                        player.getYaw(), player.getPitch()
                );
                CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                        player,
                        "cyan.msg.surface"
                );

                player.addExperienceLevels(-1 * requiredXpLevel);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int tpa(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_TPA.getValue(), "tpaDisabled"))
            {
                String playerName = StringArgumentType.getString(context, "player_name");

                if (!TPUtils.isPlayerRequesting(player.getName().getString(), playerName))
                {
                    addPlayerToQueue(player.getName().getString(), playerName);

                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                            player,
                            "cyan.msg.tpaRequestSend",
                            player.getName().getString()
                    );

                    Objects.requireNonNull(context.getSource().getServer().getPlayerManager().getPlayer(playerName))
                            .sendMessage(Text.literal(Formatting.GREEN + "[Accept]")
                                    .setStyle(Style.EMPTY.withClickEvent(
                                            new ClickEvent(
                                                    ClickEvent.Action.RUN_COMMAND,
                                                    "/tpaAccept %s".formatted(
                                                            player.getName().getString())
                                            )))
                                    .append(Text.literal(Formatting.RED + "    [Refuse]")
                                            .setStyle(Style.EMPTY.withClickEvent(
                                                    new ClickEvent(
                                                            ClickEvent.Action.RUN_COMMAND,
                                                            "/tpaRefuse %s".formatted(
                                                                    player.getName()
                                                                            .getString())
                                                    ))
                                            )
                                    )
                            );

                    CYAN_LANGUAGE_UTILS.sendPlayerMessageActionBar(
                            Objects.requireNonNull(
                                    context.getSource().getServer().getPlayerManager().getPlayer(playerName)),
                            "cyan.msg.tpaRequest",
                            false,
                            player.getName().getString()
                    );
                }
                else
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player, "cyan.error.tpaAlreadyRequested");
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int acceptTpa(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_TPA.getValue(), "tpaDisabled"))
            {
                String requestingPlayerName = StringArgumentType.getString(context, "player_name");
                ServerPlayerEntity requestingPlayer = context.getSource().getServer().getPlayerManager().getPlayer(
                        requestingPlayerName);

                // If the player is online
                // &&
                // If the player has requested a teleportation to the player running the command
                if (requestingPlayer != null && TPUtils.isPlayerRequesting(
                        requestingPlayerName, player.getName().getString()))
                {
                    int requiredXpLevel = getRequiredXpLevelsToTp(requestingPlayer, player.getBlockPos(),
                            BLOCKS_PER_XP_LEVEL_TPA
                    );

                    if (requestingPlayer.experienceLevel < requiredXpLevel)
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                requestingPlayer,
                                "cyan.msg.notEnoughXpTpa",
                                player.getName().getString()
                        );
                    }
                    else
                    {
                        requestingPlayer.addExperienceLevels(-1 * requiredXpLevel);
                        requestingPlayer.teleport(
                                player.getServerWorld(), player.getX(), player.getY(), player.getZ(), 0, 0);
                        removePlayerFromQueue(requestingPlayerName, player.getName().getString());

                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                requestingPlayer,
                                "cyan.msg.tpaSuccessful",
                                player.getName().getString()
                        );
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                "cyan.msg.tpaAcceptedSelf",
                                requestingPlayer.getName().getString()
                        );
                    }
                }
                else
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player, "cyan.error.noRequestingPlayers");
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int refuseTpa(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_TPA.getValue(), "tpaDisabled"))
            {
                String requestingPlayerName = StringArgumentType.getString(context, "player_name");
                ServerPlayerEntity requestingPlayer = context.getSource().getServer().getPlayerManager().getPlayer(
                        requestingPlayerName);

                // If the player is online
                // &&
                // If the player has requested a teleportation to the player running the command
                if (requestingPlayer != null && TPUtils.isPlayerRequesting(
                        requestingPlayerName, player.getName().getString()))
                {
                    removePlayerFromQueue(requestingPlayerName, player.getName().getString());

                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                            requestingPlayer,
                            "cyan.msg.tpaRefused",
                            player.getName().getString()
                    );
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                            player,
                            "cyan.msg.tpaRefusedSelf",
                            requestingPlayer.getName().getString()
                    );
                }
                else
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player, "cyan.error.noRequestingPlayers");
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
