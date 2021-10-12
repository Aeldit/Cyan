package fr.raphoulfifou.cyan.commands;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import org.jetbrains.annotations.NotNull;

import fr.raphoulfifou.cyan.commands.argumentTypes.ArgumentSuggestion;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

/**
 * @since 0.0.1
 * @author Raphoulfifou
 */
public class TeleportationCommands {

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("bed")
            .executes(TeleportationCommands::bed)
        );
        dispatcher.register(CommandManager.literal("b")
                .executes(TeleportationCommands::bed)
        );

        dispatcher.register(CommandManager.literal("bedof")
            .then(CommandManager.argument("playerName", GameProfileArgumentType.gameProfile())
                    .suggests(ArgumentSuggestion::getAllPlayerNames)
                    .executes(TeleportationCommands::playerBed)
            )
        );
        dispatcher.register(CommandManager.literal("bo")
            .then(CommandManager.argument("playerName", GameProfileArgumentType.gameProfile())
                    .suggests(ArgumentSuggestion::getAllPlayerNames)
                    .executes(TeleportationCommands::playerBed)
            )
        );

        dispatcher.register(CommandManager.literal("surface")
            .executes(TeleportationCommands::surface)
        );
        dispatcher.register(CommandManager.literal("s")
            .executes(TeleportationCommands::surface)
        );
    }

    /**
     * <p>Called when a player execute the commands "/bed" or "/b"</p>
     *
     * <ul>If the dimension of the player's spawnpoint is in the Overworld, get:
     *     <li>-> the x, y, z coordinates of the player's spawnpoint (x, y, z)</li>
     *     <li>-> the yaw and pitch of the player -> his eyes position (yaw, pitch)</li>
     *     <li>-> Teleport the player to the coordinates, yaw and pitch in the Overworld</li>
     * </ul>
     * <ul>If the dimension of the player's spawnpoint is in the Nether, get:
     *     <li>-> the x, y, z coordinates of the player's spawnpoint (x, y, z)</li>
     *     <li>-> the yaw and pitch of the player -> his eyes position (yaw, pitch)</li>
     *     <li>-> Teleport the player to the coordinates, yaw and pitch in the Nether</li>
     * </ul>
     * <ul>Else:
     *     <li>-> send a message to the player saying that no bed or respawn anchor was found</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int bed(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerCommandSource source = context.getSource();
        ServerWorld overworld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
        ServerWorld nether = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);

        if (player.getSpawnPointPosition() != null)
        {
            double x = player.getSpawnPointPosition().getX();
            double y = player.getSpawnPointPosition().getY();
            double z = player.getSpawnPointPosition().getZ();
            float yaw = player.getYaw();
            float pitch = player.getPitch();

            if(player.getSpawnPointDimension() == World.OVERWORLD)
            {

                if (player.getServerWorld().getRegistryKey() != World.OVERWORLD)
                {
                    player.teleport(overworld, x, y, z, yaw, pitch);
                    player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                    player.sendMessage(new TranslatableText("cyan.message.bed"), true);
                    // The add of 0 xp levels is here to update the levels, so that they appear when teleporting to the bed from an other dimension
                    source.getServer().getCommandManager().execute(source, "/xp add %s 0".formatted(player.getEntityName()));
                }
                else
                {
                    player.teleport(overworld, x, y, z, yaw, pitch);
                    player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                    player.sendMessage(new TranslatableText("cyan.message.bed"), true);
                }
            }

            if(player.getSpawnPointDimension() == World.NETHER)
            {
                if (player.getServerWorld().getRegistryKey() != World.NETHER)
                {
                    player.teleport(nether, x, y, z, yaw, pitch);
                    player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                    player.sendMessage(new TranslatableText("cyan.message.respawnanchor"), true);
                    // The add of 0 xp levels is here to update the levels, so that they appear when teleporting to the bed from an other dimension
                    source.getServer().getCommandManager().execute(source, "/xp add %s 0".formatted(player.getEntityName()));
                }
    
                else
                {
                    player.teleport(nether, x, y, z, yaw, pitch);
                    player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                    player.sendMessage(new TranslatableText("cyan.message.respawnanchor"), true);
                }
            }
        }

        else
        {
            player.sendMessage(new TranslatableText("cyan.message.bed.notfound"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the commands "/bedof playerName" or "/bo playerName"
     *      -> If the dimension of the target player's spawnpoint is in the Overworld, get:
     *          - the x, y, z coordinates of the player's spawnpoint (x, y, z)
     *          - the yaw and pitch of the player -> his eyes position (yaw, pitch)
     *         Teleport the player to the coordinates, yaw and pitch in the Overworld
     *
     *      -> If the dimension of the target player's spawnpoint is in the Nether, same as above but the player is
     *         teleported in the nether
     *
     *      -> Else, send a message to the player saying that no bed or respawn anchor was found
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int playerBed(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerWorld overworld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
        ServerWorld nether = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);

        Collection<GameProfile> argumentType = GameProfileArgumentType.getProfileArgument(context, "playerName");
        GameProfile target = argumentType.stream().findAny().orElseThrow(GameProfileArgumentType.UNKNOWN_PLAYER_EXCEPTION::create);
        ServerPlayerEntity targetPlayer = context.getSource().getServer().getPlayerManager().getPlayer(target.getId());

        List<ServerPlayerEntity> whitelistedPlayers = context.getSource().getServer().getPlayerManager().getPlayerList();
        int indexOfTargetName = whitelistedPlayers.indexOf(targetPlayer);
        ServerPlayerEntity targetName = whitelistedPlayers.get(indexOfTargetName);
        ServerPlayerEntity targetInWhitelist = whitelistedPlayers.get(indexOfTargetName);

        //if (context.getSource().getServer().getPlayerManager().)
        if (targetPlayer != null) {
            if(targetPlayer.getSpawnPointDimension() == World.OVERWORLD && targetPlayer.getSpawnPointPosition() != null) {
                double x = targetPlayer.getSpawnPointPosition().getX();
                double y = targetPlayer.getSpawnPointPosition().getY();
                double z = targetPlayer.getSpawnPointPosition().getZ();
                float yaw = player.getYaw();
                float pitch = player.getPitch();

                player.teleport(overworld, x, y, z, yaw, pitch);
                player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                player.sendMessage(new TranslatableText("cyan.message.bed"), true);
                return Command.SINGLE_SUCCESS;
            }

            if(targetPlayer.getSpawnPointDimension() == World.NETHER && targetPlayer.getSpawnPointPosition() != null) {
                double x = targetPlayer.getSpawnPointPosition().getX();
                double y = targetPlayer.getSpawnPointPosition().getY();
                double z = targetPlayer.getSpawnPointPosition().getZ();
                float yaw = player.getYaw();
                float pitch = player.getPitch();

                player.teleport(nether, x, y, z, yaw, pitch);
                player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                player.sendMessage(new TranslatableText("cyan.message.respawnanchor"), true);
                return Command.SINGLE_SUCCESS;
            }
            else {
                player.sendMessage(new TranslatableText("cyan.message.bed.notfound"), false);
            }
        }

        if (whitelistedPlayers.contains(targetName) && targetPlayer == null) {

            if(targetInWhitelist.getSpawnPointDimension() == World.OVERWORLD && targetInWhitelist.getSpawnPointPosition() != null) {
                double x = targetInWhitelist.getSpawnPointPosition().getX();
                double y = targetInWhitelist.getSpawnPointPosition().getY();
                double z = targetInWhitelist.getSpawnPointPosition().getZ();
                float yaw = player.getYaw();
                float pitch = player.getPitch();

                player.teleport(overworld, x, y, z, yaw, pitch);
                player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                player.sendMessage(new TranslatableText("cyan.message.bedOf"), true);
                return Command.SINGLE_SUCCESS;
            }

            if(targetInWhitelist.getSpawnPointDimension() == World.NETHER && targetInWhitelist.getSpawnPointPosition() != null) {
                double x = targetInWhitelist.getSpawnPointPosition().getX();
                double y = targetInWhitelist.getSpawnPointPosition().getY();
                double z = targetInWhitelist.getSpawnPointPosition().getZ();
                float yaw = player.getYaw();
                float pitch = player.getPitch();

                player.teleport(nether, x, y, z, yaw, pitch);
                player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
                player.sendMessage(new TranslatableText("cyan.message.respawnanchorOf"), true);
                return Command.SINGLE_SUCCESS;
            }
            else {
                player.sendMessage(new TranslatableText("cyan.message.bed.notfoundOf"), false);
            }
        }
        return Command.SINGLE_SUCCESS;
    }


    /**
     * <p>Called when a player execute the commands "/surface" or "/s"</p>
     *
     * <ul>Get:
     *     <li>-> the x and z coordinates of the player (x, z)</li>
     *     <li>-> the higher block at the y position of the player (y)</li>
     *     <li>-> the yaw and pitch of the player -> his eyes position (yaw, pitch)</li>
     * </ul>
     * <p>Teleport the player to the highest block that was found on the player's coordinates before being teleported</p>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int surface(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerWorld world = context.getSource().getWorld();

        int x = player.getBlockPos().getX();
        int z = player.getBlockPos().getZ();
        int y = player.world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);
        float yaw = player.getYaw();
        float pitch = player.getPitch();

        player.teleport(world, x, y, z, yaw, pitch);
        player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
        player.sendMessage(new TranslatableText("cyan.message.surface"), true);
        return Command.SINGLE_SUCCESS;
    }
}