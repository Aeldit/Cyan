package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static fr.aeldit.cyan.util.LanguageUtils.ERROR;
import static fr.aeldit.cyan.util.LanguageUtils.getTranslation;
import static fr.aeldit.cyanlib.util.ChatUtil.sendPlayerMessage;

public class TeleportationCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
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
    }

    /**
     * <p>Called when a player execute the commands <code>/bed</code> or <code>/b</code></p>
     *
     * <ul>If the dimension of the player's spawnpoint is in the Overworld, get :
     *     <li>- Teleport the player to the coordinates, yaw and pitch in the Overworld</li>
     * </ul>
     * <ul>If the dimension of the player's spawnpoint is in the Nether, get :
     *     <li>- Teleport the player to the coordinates, yaw and pitch in the Nether</li>
     * </ul>
     * <ul>Else:
     *     <li>- send a message to the player saying that no bed or respawn anchor was found</li>
     * </ul>
     */
    public static int bed(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            ServerWorld overworld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
            ServerWorld nether = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);

            if (CyanMidnightConfig.allowBed)
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeBed))
                {
                    if (player.getSpawnPointPosition() != null)
                    {
                        double x = player.getSpawnPointPosition().getX();
                        double y = player.getSpawnPointPosition().getY();
                        double z = player.getSpawnPointPosition().getZ();
                        float yaw = player.getYaw();
                        float pitch = player.getPitch();

                        if (player.getSpawnPointDimension() == World.OVERWORLD)
                        {
                            player.teleport(overworld, x, y, z, yaw, pitch);
                            sendPlayerMessage(player,
                                    getTranslation("bed"),
                                    "cyan.message.bed",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations
                            );
                        } else if (player.getSpawnPointDimension() == World.NETHER)
                        {
                            player.teleport(nether, x, y, z, yaw, pitch);
                            sendPlayerMessage(player,
                                    getTranslation("respawnAnchor"),
                                    "cyan.message.respawnAnchor",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations
                            );
                        }
                    } else
                    {
                        sendPlayerMessage(player,
                                getTranslation(ERROR + "bed"),
                                "cyan.message.bed.error",
                                CyanMidnightConfig.errorToActionBar,
                                CyanMidnightConfig.useTranslations
                        );
                    }
                } else
                {
                    sendPlayerMessage(player,
                            getTranslation(ERROR + "notOp"),
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        getTranslation(ERROR + "bedDisabled"),
                        "cyan.message.disabled.bed",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the commands <code>/surface</code> or <code>/s</code></p>
     * <p>Teleport the player to the highest block that was found on the player's coordinates</p>
     */
    public static int surface(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            if (CyanMidnightConfig.allowSurface)
            {
                ServerWorld world = context.getSource().getWorld();
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeSurface))
                {
                    int x = player.getBlockPos().getX();
                    int z = player.getBlockPos().getZ();
                    int y = player.world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);
                    float yaw = player.getYaw();
                    float pitch = player.getPitch();

                    player.teleport(world, x, y, z, yaw, pitch);
                    sendPlayerMessage(player,
                            getTranslation("surface"),
                            "cyan.message.surface",
                            CyanMidnightConfig.msgToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                } else
                {
                    sendPlayerMessage(player,
                            getTranslation(ERROR + "notOp"),
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        getTranslation(ERROR + "surfaceDisabled"),
                        "cyan.message.disabled.surface",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
