package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.teleportation.Location;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static fr.aeldit.cyan.CyanCore.*;
import static fr.aeldit.cyan.config.CyanLibConfigImpl.*;
import static fr.aeldit.cyanlib.lib.utils.TPUtils.getRequiredXpLevelsToTp;

public class LocationCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(
                CommandManager.literal("set-location")
                              .then(CommandManager.argument("name", StringArgumentType.string())
                                                  .executes(LocationCommands::setLocation)
                              )
        );
        dispatcher.register(
                CommandManager.literal("sl")
                              .then(CommandManager.argument("name", StringArgumentType.string())
                                                  .executes(LocationCommands::setLocation)
                              )
        );

        dispatcher.register(
                CommandManager.literal("remove-location")
                              .then(CommandManager.argument("name", StringArgumentType.string())
                                                  .suggests((context, builder) -> LOCATIONS.getLocationsNames(builder))
                                                  .executes(LocationCommands::removeLocation)
                              )
        );
        dispatcher.register(
                CommandManager.literal("rl")
                              .then(CommandManager.argument("name", StringArgumentType.string())
                                                  .suggests((context, builder) -> LOCATIONS.getLocationsNames(builder))
                                                  .executes(LocationCommands::removeLocation)
                              )
        );
        dispatcher.register(
                CommandManager.literal("remove-all-locations")
                              .executes(LocationCommands::removeAllLocations)
        );

        dispatcher.register(
                CommandManager.literal("rename-location").then(
                        CommandManager.argument("name", StringArgumentType.string())
                                      .suggests((context, builder) -> LOCATIONS.getLocationsNames(builder))
                                      .then(CommandManager.argument("new_name", StringArgumentType.string())
                                                          .executes(LocationCommands::renameLocation)
                                      )
                )
        );

        dispatcher.register(
                CommandManager.literal("location")
                              .then(CommandManager.argument("name", StringArgumentType.string())
                                                  .suggests((context, builder) -> LOCATIONS.getLocationsNames(builder))
                                                  .executes(LocationCommands::goToLocation)
                              )
                              .executes(LocationCommands::getLocationsList)
        );
        dispatcher.register(
                CommandManager.literal("l")
                              .then(CommandManager.argument("name", StringArgumentType.string())
                                                  .suggests((context, builder) -> LOCATIONS.getLocationsNames(builder))
                                                  .executes(LocationCommands::goToLocation)
                              )
                              .executes(LocationCommands::getLocationsList)
        );

        dispatcher.register(CommandManager.literal("get-locations").executes(LocationCommands::getLocationsList));
        dispatcher.register(CommandManager.literal("gl").executes(LocationCommands::getLocationsList));
    }

    /**
     * Called by the command {@code /set-location <location_name>} or {@code /sl <location_name>}
     * <p>
     * Saves the current player's location (dimension, x, y, z, yaw, pitch) + the player who created the location in
     * the locations file
     */
    public static int setLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue())
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled")
        )
        {
            return 0;
        }

        String locationName = StringArgumentType.getString(context, "name");

        if (!LOCATIONS.add(new Location(
                locationName,
                player.getWorld()
                      //? if <1.20.6 {
                      /*.getDimensionKey().getValue().toString()
                       *///?} else {
                      .getDimensionEntry().getIdAsString()
                      //?}
                      .replace("minecraft:", "")
                      .replace("the_", ""),
                player.getX(), player.getY(), player.getZ(),
                player.getYaw(), player.getPitch()
        )))
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.locationAlreadyExists");
            return 0;
        }

        CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.setLocation", Formatting.YELLOW + locationName);
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /remove-location <location_name>} or {@code /rl <location_name>}
     * <p>
     * Removes the given location
     */
    public static int removeLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue())
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled")
        )
        {
            return 0;
        }

        String locationName = StringArgumentType.getString(context, "name");

        if (!LOCATIONS.remove(locationName))
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.locationNotFound", Formatting.YELLOW + locationName);
            return 0;
        }

        CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.removeLocation", Formatting.YELLOW + locationName);
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /remove-all-locations}
     * <p>
     * Removes all the locations
     */
    public static int removeAllLocations(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue())
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled")
        )
        {
            return 0;
        }

        if (!LOCATIONS.removeAll())
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.noLocations");
            return 0;
        }

        CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.removedAllLocations");
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /rename-location <name> <new_name>}
     * <p>
     * Renames the location
     */
    public static int renameLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue())
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled")
        )
        {
            return 0;
        }

        String locationName = StringArgumentType.getString(context, "name");
        String newLocationName = StringArgumentType.getString(context, "new_name");

        if (!LOCATIONS.rename(locationName, newLocationName))
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.locationNotFound", locationName);
            return 0;
        }

        CYAN_LANG_UTILS.sendPlayerMessage(
                player,
                "msg.renameLocation",
                Formatting.YELLOW + locationName,
                Formatting.YELLOW + newLocationName
        );
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /location <location_name>} or {@code /l <location_name>}
     * <p>
     * Teleports the player to the given location
     */
    public static int goToLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled")
        )
        {
            return 0;
        }

        String locationName = StringArgumentType.getString(context, "name");
        Location loc = LOCATIONS.getLocation(locationName);
        MinecraftServer server = player.getServer();

        if (loc == null || server == null)
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.locationNotFound", Formatting.YELLOW + locationName);
            return 0;
        }

        int requiredXpLevel = 0;
        if (USE_XP_TO_TELEPORT.getValue() && !player.isCreative())
        {
            requiredXpLevel = XP_USE_FIXED_AMOUNT.getValue()
                              ? XP_AMOUNT.getValue()
                              : getRequiredXpLevelsToTp(
                                      player,
                                      new BlockPos((int) loc.x(), (int) loc.y(), (int) loc.z()),
                                      BLOCKS_PER_XP_LEVEL_LOCATION.getValue()
                              );

            if ((XP_USE_POINTS.getValue() ? player.totalExperience : player.experienceLevel) < requiredXpLevel)
            {
                CYAN_LANG_UTILS.sendPlayerMessage(
                        player,
                        "error.notEnoughXp",
                        Formatting.GOLD + String.valueOf(requiredXpLevel),
                        Formatting.RED + (XP_USE_POINTS.getValue() ? "points" : "levels")
                );
                return 0;
            }
        }

        loc.teleport(server, player);

        if (XP_USE_POINTS.getValue())
        {
            player.addExperience(-1 * requiredXpLevel);
        }
        else
        {
            player.addExperienceLevels(-1 * requiredXpLevel);
        }

        CYAN_LANG_UTILS.sendPlayerMessage(player, "msg.goToLocation", Formatting.YELLOW + locationName);
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /get-locations} or {@code /gl}
     * <p>
     * Lists all the locations in the player's chat
     */
    public static int getLocationsList(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null
            || !CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
        {
            return 0;
        }

        if (LOCATIONS.isEmpty())
        {
            CYAN_LANG_UTILS.sendPlayerMessage(player, "error.noLocations");
            return 0;
        }

        player.sendMessage(Text.of("§6------------------------------------"), false);
        CYAN_LANG_UTILS.sendPlayerMessageActionBar(player, "msg.listLocations", false);
        List<Location> locations = LOCATIONS.getLocations();

        if (locations != null)
        {
            for (Location location : locations)
            {
                CYAN_LANG_UTILS.sendPlayerMessageActionBar(
                        player,
                        "msg.getLocation",
                        false,
                        Formatting.YELLOW + location.name(),
                        Formatting.DARK_AQUA + location.dimension()
                );
            }
        }
        player.sendMessage(Text.of("§6------------------------------------"), false);
        return Command.SINGLE_SUCCESS;
    }
}
