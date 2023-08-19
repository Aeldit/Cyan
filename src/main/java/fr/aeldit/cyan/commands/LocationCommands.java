/*
 * Copyright (c) 2023  -  Made by Aeldit
 *
 *              GNU LESSER GENERAL PUBLIC LICENSE
 *                  Version 3, 29 June 2007
 *
 *  Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *  Everyone is permitted to copy and distribute verbatim copies
 *  of this license document, but changing it is not allowed.
 *
 *
 * This version of the GNU Lesser General Public License incorporates
 * the terms and conditions of version 3 of the GNU General Public
 * License, supplemented by the additional permissions listed in the LICENSE.txt file
 * in the repo of this mod (https://github.com/Aeldit/Cyan)
 */

package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.commands.argumentTypes.ArgumentSuggestion;
import fr.aeldit.cyan.teleportation.Locations;
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
                        .suggests((context4, builder4) -> ArgumentSuggestion.getLocations(builder4))
                        .executes(LocationCommands::removeLocation)
                )
        );
        dispatcher.register(CommandManager.literal("rl")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests((context4, builder4) -> ArgumentSuggestion.getLocations(builder4))
                        .executes(LocationCommands::removeLocation)
                )
        );
        dispatcher.register(CommandManager.literal("remove-all-locations")
                .executes(LocationCommands::removeAllLocations)
        );

        dispatcher.register(CommandManager.literal("rename-location")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .then(CommandManager.argument("new_name", StringArgumentType.string())
                                .executes(LocationCommands::renameLocation)
                        )
                )
        );

        dispatcher.register(CommandManager.literal("location")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests((context4, builder4) -> ArgumentSuggestion.getLocations(builder4))
                        .executes(LocationCommands::goToLocation)
                )
                .executes(LocationCommands::getLocationsList)
        );
        dispatcher.register(CommandManager.literal("l")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests((context4, builder4) -> ArgumentSuggestion.getLocations(builder4))
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
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            String locationName = StringArgumentType.getString(context, "name");

            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue()))
                {
                    if (!LOCATIONS.locationExists(locationName))
                    {
                        if (player.getWorld() == player.getServer().getWorld(World.OVERWORLD))
                        {
                            LOCATIONS.add(new Locations.Location(locationName, "overworld", player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch()));
                        }
                        else if (player.getWorld() == player.getServer().getWorld(World.NETHER))
                        {
                            LOCATIONS.add(new Locations.Location(locationName, "nether", player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch()));
                        }
                        else
                        {
                            LOCATIONS.add(new Locations.Location(locationName, "end", player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch()));
                        }

                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                                CYAN_LANGUAGE_UTILS.getTranslation("setLocation"),
                                "cyan.msg.setLocation",
                                Formatting.YELLOW + locationName
                        );
                    }
                    else
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
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
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue()))
                {
                    String locationName = StringArgumentType.getString(context, "name");

                    if (LOCATIONS.locationExists(locationName))
                    {
                        LOCATIONS.remove(locationName);

                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                                CYAN_LANGUAGE_UTILS.getTranslation("removeLocation"),
                                "cyan.msg.removeLocation",
                                Formatting.YELLOW + locationName
                        );
                    }
                    else
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
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
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue()))
                {
                    if (LOCATIONS.removeAll())
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                                CYAN_LANGUAGE_UTILS.getTranslation("removedAllLocations"),
                                "cyan.msg.removedAllLocations"
                        );
                    }
                    else
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
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
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                if (CYAN_LIB_UTILS.hasPermission(player, MIN_OP_LVL_EDIT_LOCATIONS.getValue()))
                {
                    String locationName = StringArgumentType.getString(context, "name");
                    String newLocationName = StringArgumentType.getString(context, "new_name");

                    if (LOCATIONS.locationExists(locationName))
                    {
                        LOCATIONS.rename(locationName, newLocationName);

                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                                CYAN_LANGUAGE_UTILS.getTranslation("renameLocation"),
                                "cyan.msg.renameLocation",
                                Formatting.YELLOW + locationName,
                                Formatting.YELLOW + newLocationName
                        );
                    }
                    else
                    {
                        CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
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
        LOCATIONS.teleport(context.getSource().getPlayer(), StringArgumentType.getString(context, "name"));

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

        if (CYAN_LIB_UTILS.isPlayer(context.getSource()))
        {
            if (CYAN_LIB_UTILS.isOptionAllowed(player, ALLOW_LOCATIONS.getValue(), "locationsDisabled"))
            {
                if (!LOCATIONS.isEmpty())
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessageActionBar(player,
                            CYAN_LANGUAGE_UTILS.getTranslation("dashSeparation"),
                            "cyan.msg.dashSeparation",
                            false
                    );
                    CYAN_LANGUAGE_UTILS.sendPlayerMessageActionBar(player,
                            CYAN_LANGUAGE_UTILS.getTranslation("listLocations"),
                            "cyan.msg.listLocations",
                            false
                    );

                    LOCATIONS.getLocations().forEach(location -> CYAN_LANGUAGE_UTILS.sendPlayerMessageActionBar(
                            player,
                            CYAN_LANGUAGE_UTILS.getTranslation("getLocation"),
                            "cyan.msg.getLocation",
                            false,
                            Formatting.YELLOW + location.getName(),
                            Formatting.DARK_AQUA + location.getDimension()
                    ));

                    CYAN_LANGUAGE_UTILS.sendPlayerMessageActionBar(player,
                            CYAN_LANGUAGE_UTILS.getTranslation("dashSeparation"),
                            "cyan.msg.dashSeparation",
                            false
                    );
                }
                else
                {
                    CYAN_LANGUAGE_UTILS.sendPlayerMessage(player,
                            CYAN_LANGUAGE_UTILS.getTranslation(ERROR + "noLocations"),
                            "cyan.msg.noLocations"
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
