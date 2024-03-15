package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.config.CyanConfig;
import fr.aeldit.cyan.teleportation.Locations;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static fr.aeldit.cyan.config.CyanConfig.ALLOW_LOCATIONS;
import static fr.aeldit.cyan.config.CyanConfig.MIN_OP_LVL_EDIT_LOCATIONS;
import static fr.aeldit.cyan.util.Utils.*;
import static fr.aeldit.cyanlib.lib.utils.TranslationsPrefixes.ERROR;

public class LocationCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("set-location")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .executes(LocationCommands::setLocation)
                )
        );
        dispatcher.register(CommandManager.literal("sl")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .executes(LocationCommands::setLocation)
                )
        );

        dispatcher.register(CommandManager.literal("remove-location")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests((context, builder) -> LOCATIONS.getLocationsNames(builder))
                        .executes(LocationCommands::removeLocation)
                )
        );
        dispatcher.register(CommandManager.literal("rl")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests((context, builder) -> LOCATIONS.getLocationsNames(builder))
                        .executes(LocationCommands::removeLocation)
                )
        );
        dispatcher.register(CommandManager.literal("remove-all-locations")
                .executes(LocationCommands::removeAllLocations)
        );

        dispatcher.register(CommandManager.literal("rename-location")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests((context, builder) -> LOCATIONS.getLocationsNames(builder))
                        .then(CommandManager.argument("new_name", StringArgumentType.string())
                                .executes(LocationCommands::renameLocation)
                        )
                )
        );

        dispatcher.register(CommandManager.literal("location")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests((context, builder) -> LOCATIONS.getLocationsNames(builder))
                        .executes(LocationCommands::goToLocation)
                )
                .executes(LocationCommands::getLocationsList)
        );
        dispatcher.register(CommandManager.literal("l")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests((context, builder) -> LOCATIONS.getLocationsNames(builder))
                        .executes(LocationCommands::goToLocation)
                )
                .executes(LocationCommands::getLocationsList)
        );

        dispatcher.register(CommandManager.literal("get-locations")
                .executes(LocationCommands::getLocationsList)
        );
        dispatcher.register(CommandManager.literal("gl")
                .executes(LocationCommands::getLocationsList)
        );
    }

    /**
     * Called by the command {@code /set-location <location_name>} or {@code /sl <location_name>}
     * <p>
     * Saves the current player's location (dimension, x, y, z, yaw, pitch) + the player who created the location in
     * the locations file
     */
    public static int setLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue()))
                {
                    String locationName = StringArgumentType.getString(context, "name");

                    if (LOCATIONS.add(new Locations.Location(
                                    locationName,
                                    player.getWorld().getDimensionKey().getValue().toString()
                                            .replace("minecraft:", "").replace("the_", ""),
                                    player.getX(), player.getY(), player.getZ(),
                                    player.getYaw(), player.getPitch()
                            )
                    ))
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                CYAN_LANGUAGE_UTILS.getTranslation("setLocation"),
                                "cyan.msg.setLocation",
                                Formatting.YELLOW + locationName
                        );
                    }
                    else
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "locationAlreadyExists"),
                                "cyan.msg.locationAlreadyExists"
                        );
                    }
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /remove-location <location_name>} or {@code /rl <location_name>}
     * <p>
     * Removes the given location
     */
    public static int removeLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue()))
                {
                    String locationName = StringArgumentType.getString(context, "name");

                    if (LOCATIONS.remove(locationName))
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                CYAN_LANGUAGE_UTILS.getTranslation("removeLocation"),
                                "cyan.msg.removeLocation",
                                Formatting.YELLOW + locationName
                        );
                    }
                    else
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "locationNotFound"),
                                "cyan.msg.locationNotFound",
                                Formatting.YELLOW + locationName
                        );
                    }
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /remove-all-locations}
     * <p>
     * Removes all the locations
     */
    public static int removeAllLocations(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue()))
                {
                    if (LOCATIONS.removeAll())
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                CYAN_LANGUAGE_UTILS.getTranslation("removedAllLocations"),
                                "cyan.msg.removedAllLocations"
                        );
                    }
                    else
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "noLocations"),
                                "cyan.msg.noLocations"
                        );
                    }
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /rename-location <name> <new_name>}
     * <p>
     * Renames the location
     */
    public static int renameLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue()))
                {
                    String locationName = StringArgumentType.getString(context, "name");
                    String newLocationName = StringArgumentType.getString(context, "new_name");

                    if (LOCATIONS.rename(locationName, newLocationName))
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                CYAN_LANGUAGE_UTILS.getTranslation("renameLocation"),
                                "cyan.msg.renameLocation",
                                Formatting.YELLOW + locationName,
                                Formatting.YELLOW + newLocationName
                        );
                    }
                    else
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "locationNotFound"),
                                "cyan.msg.locationNotFound",
                                locationName
                        );
                    }
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /location <location_name>} or {@code /l <location_name>}
     * <p>
     * Teleports the player to the given location
     */
    public static int goToLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, CyanConfig.ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                String locationName = StringArgumentType.getString(context, "name");
                Locations.Location loc = LOCATIONS.getLocation(locationName);

                if (loc != null)
                {
                    MinecraftServer server = player.getServer();

                    if (server != null)
                    {
                        switch (loc.dimension())
                        {
                            case "overworld" -> player.teleport(
                                    server.getWorld(World.OVERWORLD), loc.x(), loc.y(), loc.z(), loc.yaw(),
                                    loc.pitch()
                            );
                            case "nether" -> player.teleport(
                                    server.getWorld(World.NETHER), loc.x(), loc.y(), loc.z(), loc.yaw(), loc.pitch()
                            );
                            case "end" -> player.teleport(
                                    server.getWorld(World.END), loc.x(), loc.y(), loc.z(),
                                    loc.yaw(), loc.pitch()
                            );
                        }

                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                                player,
                                CYAN_LANGUAGE_UTILS.getTranslation("goToLocation"),
                                "cyan.msg.goToLocation",
                                Formatting.YELLOW + locationName
                        );
                    }
                }
                else
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                            player,
                            CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "locationNotFound"),
                            "cyan.msg.locationNotFound",
                            Formatting.YELLOW + locationName
                    );
                }
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /get-locations} or {@code /gl}
     * <p>
     * Lists all the locations in the player's chat
     */
    public static int getLocationsList(@NotNull CommandContext<ServerCommandSource> context)
    {
        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            ServerPlayerEntity player = context.getSource().getPlayer();

            if (CYAN_LIB_UTILS.isOptionEnabled(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                if (!LOCATIONS.isEmpty())
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessageActionBar(
                            player,
                            CYAN_LANGUAGE_UTILS.getTranslation("dashSeparation"),
                            "cyan.msg.dashSeparation",
                            false
                    );
                    CYAN_LANGUAGE_UTILS.sendPlayerMessageActionBar(
                            player,
                            CYAN_LANGUAGE_UTILS.getTranslation("listLocations"),
                            "cyan.msg.listLocations",
                            false
                    );

                    LOCATIONS.getLocations().forEach(location -> CYAN_LANGUAGE_UTILS.sendPlayerMessageActionBar(
                            player,
                            CYAN_LANGUAGE_UTILS.getTranslation("getLocation"),
                            "cyan.msg.getLocation",
                            false,
                            Formatting.YELLOW + location.name(),
                            Formatting.DARK_AQUA + location.dimension()
                    ));

                    CYAN_LANGUAGE_UTILS.sendPlayerMessageActionBar(
                            player,
                            CYAN_LANGUAGE_UTILS.getTranslation("dashSeparation"),
                            "cyan.msg.dashSeparation",
                            false
                    );
                }
                else
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(
                            player,
                            CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "noLocations"),
                            "cyan.msg.noLocations"
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
