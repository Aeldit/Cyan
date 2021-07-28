package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.Objects;

public class TeleportationCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
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
     * Called when a player execute the commands "/bed" or "/b"
     *      -> If the dimension of the player's spawnpoint is in the Overworld, get:
     *          - the x, y, z coordinates of the player's spawnpoint (x, y, z)
     *          - the yaw and pitch of the player -> his eyes position (yaw, pitch)
     *         Teleport the player to the coordinates, yaw and pitch in the Overworld
     *
     *      -> If the dimension of the player's spawnpoint is in the Nether, same as above but the player is
     *         teleported in the nether
     *
     *      -> Else, send a message to the player saying the no bed or respawn anchor was found
     *
     * Call the "createHome" function located in 'SetHomeJSONConfig' and take as parameters the elements listed above
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct (ex: "/sethome ba se" will throw
     *                                an exception because there is two arguments instead of one)
     */
    public static int bed(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerWorld overworld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
        ServerWorld nether = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);

        if(player.getSpawnPointDimension() == World.OVERWORLD && player.getSpawnPointPosition() != null) {
            double x = player.getSpawnPointPosition().getX();
            double y = player.getSpawnPointPosition().getY();
            double z = player.getSpawnPointPosition().getZ();
            float yaw = player.getYaw();
            float pitch = player.getPitch();

            player.teleport(overworld, x, y, z, yaw, pitch);
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 10, 1);
            player.sendMessage(new TranslatableText("cyan.message.bed"), true);
            return Command.SINGLE_SUCCESS;
        }

        if(player.getSpawnPointDimension() == World.NETHER && player.getSpawnPointPosition() != null) {
            double x = player.getSpawnPointPosition().getX();
            double y = player.getSpawnPointPosition().getY();
            double z = player.getSpawnPointPosition().getZ();
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
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called when a player execute the command "/surface" or "/s"
     * Gets: - the x and z coordinates of the player (x, z)
     *       - the higher block at the y position of the player (y)
     *       - the yaw and pitch of the player -> his eyes position (yaw, pitch)
     *
     * Teleport the player to the highest block that was found on the player's coordinates before being teleported
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct (ex: "/sethome ba se" will throw
     *                                an exception because there is two arguments instead of one)
     */
    public static int surface(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
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