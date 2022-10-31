package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static fr.aeldit.cyan.util.ChatConstants.line_start;
import static fr.aeldit.cyan.util.ChatConstants.line_start_error;
import static fr.aeldit.cyanlib.util.ChatUtil.sendPlayerMessage;

/**
 * @since 0.0.1
 */
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
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        ServerWorld overworld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
        ServerWorld nether = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);

        if (CyanMidnightConfig.allowBed)
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
                    if (player.getWorld().getRegistryKey() != World.OVERWORLD)
                    {
                        player.teleport(overworld, x, y, z, yaw, pitch);
                        player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                        sendPlayerMessage(player,
                                line_start + "§6You have been teleported to your bed",
                                null,
                                "cyan.message.bed",
                                true,
                                CyanMidnightConfig.useOneLanguage
                        );
                    } else
                    {
                        player.teleport(overworld, x, y, z, yaw, pitch);
                        player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                        sendPlayerMessage(player,
                                line_start + "§6You have been teleported to your bed",
                                null,
                                "cyan.message.bed",
                                true,
                                CyanMidnightConfig.useOneLanguage
                        );
                    }
                } else if (player.getSpawnPointDimension() == World.NETHER)
                {
                    if (player.getWorld().getRegistryKey() != World.NETHER)
                    {
                        player.teleport(nether, x, y, z, yaw, pitch);
                        player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                        sendPlayerMessage(player,
                                line_start + "§6You have been teleported to your respawn anchor",
                                null,
                                "cyan.message.respawnanchor",
                                true,
                                CyanMidnightConfig.useOneLanguage
                        );
                    } else
                    {
                        player.teleport(nether, x, y, z, yaw, pitch);
                        player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                        sendPlayerMessage(player,
                                line_start + "§6You have been teleported to your respawn anchor",
                                null,
                                "cyan.message.respawnanchor",
                                true,
                                CyanMidnightConfig.useOneLanguage
                        );
                    }
                }
            } else
            {
                sendPlayerMessage(player,
                        line_start_error + "§cYou don't have an attributed bed or respawn anchor",
                        null,
                        "cyan.message.bed.notfound",
                        false,
                        CyanMidnightConfig.useOneLanguage
                );
                return 0;
            }
        } else
        {
            sendPlayerMessage(player,
                    line_start_error + "The /bed command is disabled. To enable it, enter '/allowBed true' in chat",
                    null,
                    "cyan.message.disabled.bed",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }


    /**
     * <p>Called when a player execute the commands <code>/surface</code> or <code>/s</code></p>
     * <p>Teleport the player to the highest block that was found on the player's coordinates</p>
     */
    public static int surface(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerWorld world = context.getSource().getWorld();

        assert player != null;

        int x = player.getBlockPos().getX();
        int z = player.getBlockPos().getZ();
        int y = player.world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);
        float yaw = player.getYaw();
        float pitch = player.getPitch();

        if (CyanMidnightConfig.allowSurface)
        {
            player.teleport(world, x, y, z, yaw, pitch);
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
            sendPlayerMessage(player,
                    line_start + "§6You have been teleported to the surface",
                    null,
                    "cyan.message.surface",
                    true,
                    CyanMidnightConfig.useOneLanguage
            );
        } else
        {
            sendPlayerMessage(player,
                    line_start_error + "The /surface command is disabled. To enable it, enter '/allowSurface true' in chat",
                    null,
                    "cyan.message.disabled.surface",
                    false,
                    CyanMidnightConfig.useOneLanguage
            );
            return 0;
        }

        return Command.SINGLE_SUCCESS;
    }

}
