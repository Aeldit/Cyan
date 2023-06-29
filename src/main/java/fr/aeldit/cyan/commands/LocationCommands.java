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
import fr.aeldit.cyanlib.lib.CyanLibLanguageUtils;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static fr.aeldit.cyan.util.Utils.*;
import static fr.aeldit.cyanlib.lib.TranslationsPrefixes.ERROR;

public class LocationCommands
{
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("setlocation")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .executes(LocationCommands::setLocation)
                )
        );
        dispatcher.register(CommandManager.literal("sl")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .executes(LocationCommands::setLocation)
                )
        );

        dispatcher.register(CommandManager.literal("removelocation")
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
        dispatcher.register(CommandManager.literal("removealllocations")
                .executes(LocationCommands::removeAllLocations)
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

        dispatcher.register(CommandManager.literal("getlocations")
                .executes(LocationCommands::getLocationsList)
        );
        dispatcher.register(CommandManager.literal("gl")
                .executes(LocationCommands::getLocationsList)
        );
    }

    /**
     * Called by the command {@code /setlocation <location_name>} or {@code /sl <location_name>}
     * <p>
     * Saves the current player's location (dimension, x, y, z, yaw, pitch) + the player who created the location in
     * the locations file
     */
    public static int setLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (LibUtils.isPlayer(context.getSource()))
        {
            String locationName = StringArgumentType.getString(context, "name");

            if (LibUtils.isOptionAllowed(player, LibConfig.getBoolOption("allowLocations"), "locationsDisabled"))
            {
                if (LibUtils.hasPermission(player, LibConfig.getIntOption("minOpLevelExeEditLocation")))
                {
                    if (!LocationsObj.locationExists(locationName))
                    {
                        if (player.getWorld() == player.getServer().getWorld(World.OVERWORLD))
                        {
                            LocationsObj.add(new Locations.Location(locationName, "overworld", player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch()));
                        }
                        else if (player.getWorld() == player.getServer().getWorld(World.NETHER))
                        {
                            LocationsObj.add(new Locations.Location(locationName, "nether", player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch()));
                        }
                        else
                        {
                            LocationsObj.add(new Locations.Location(locationName, "end", player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch()));
                        }

                        CyanLibLanguageUtils.sendPlayerMessage(player,
                                LanguageUtils.getTranslation("setLocation"),
                                "cyan.message.setLocation",
                                Formatting.YELLOW + locationName
                        );
                    }
                    else
                    {
                        CyanLibLanguageUtils.sendPlayerMessage(player,
                                LanguageUtils.getTranslation(ERROR + "locationAlreadyExists"),
                                "cyan.message.locationAlreadyExists"
                        );
                    }
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /removelocation <location_name>} or {@code /rl <location_name>}
     * <p>
     * Removes the given location
     */
    public static int removeLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (LibUtils.isPlayer(context.getSource()))
        {
            if (LibUtils.isOptionAllowed(player, LibConfig.getBoolOption("allowLocations"), "locationsDisabled"))
            {
                if (LibUtils.hasPermission(player, LibConfig.getIntOption("minOpLevelExeEditLocation")))
                {
                    String locationName = StringArgumentType.getString(context, "name");

                    if (LocationsObj.locationExists(locationName))
                    {
                        LocationsObj.remove(locationName);
                        CyanLibLanguageUtils.sendPlayerMessage(player,
                                LanguageUtils.getTranslation("removeLocation"),
                                "cyan.message.removeLocation",
                                Formatting.YELLOW + locationName
                        );
                    }
                    else
                    {
                        CyanLibLanguageUtils.sendPlayerMessage(player,
                                LanguageUtils.getTranslation(ERROR + "locationNotFound"),
                                "cyan.message.locationNotFound",
                                Formatting.YELLOW + locationName
                        );
                    }
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /removealllocations}
     * <p>
     * Removes all the locations
     */
    public static int removeAllLocations(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (LibUtils.isPlayer(context.getSource()))
        {
            if (LibUtils.isOptionAllowed(player, LibConfig.getBoolOption("allowLocations"), "locationsDisabled"))
            {
                if (LibUtils.hasPermission(player, LibConfig.getIntOption("minOpLevelExeEditLocation")))
                {
                    if (LocationsObj.removeAll())
                    {
                        CyanLibLanguageUtils.sendPlayerMessage(player,
                                LanguageUtils.getTranslation("removedAllLocations"),
                                "cyan.message.removedAllLocations"
                        );
                    }
                    else
                    {
                        CyanLibLanguageUtils.sendPlayerMessage(player,
                                LanguageUtils.getTranslation(ERROR + "noLocations"),
                                "cyan.message.noLocations"
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
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (LibUtils.isPlayer(context.getSource()))
        {
            if (LibUtils.isOptionAllowed(player, LibConfig.getBoolOption("allowLocations"), "locationsDisabled"))
            {
                String locationName = StringArgumentType.getString(context, "name");

                if (LocationsObj.locationExists(locationName))
                {
                    Locations.Location loc = LocationsObj.getLocation(locationName);

                    switch (loc.dimension())
                    {
                        case "overworld" ->
                                player.teleport(player.getServer().getWorld(World.OVERWORLD), loc.x(), loc.y(), loc.z(), loc.yaw(), loc.pitch());
                        case "nether" ->
                                player.teleport(player.getServer().getWorld(World.NETHER), loc.x(), loc.y(), loc.z(), loc.yaw(), loc.pitch());
                        case "end" ->
                                player.teleport(player.getServer().getWorld(World.END), loc.x(), loc.y(), loc.z(), loc.yaw(), loc.pitch());
                    }

                    CyanLibLanguageUtils.sendPlayerMessage(player,
                            LanguageUtils.getTranslation("goToLocation"),
                            "cyan.message.goToLocation",
                            Formatting.YELLOW + locationName
                    );
                }
                else
                {
                    CyanLibLanguageUtils.sendPlayerMessage(player,
                            LanguageUtils.getTranslation(ERROR + "locationNotFound"),
                            "cyan.message.locationNotFound",
                            Formatting.YELLOW + locationName
                    );
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Called by the command {@code /getlocations} or {@code /gl}
     * <p>
     * Lists all the locations in the player's chat
     */
    public static int getLocationsList(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (LibUtils.isPlayer(context.getSource()))
        {
            if (!LocationsObj.isEmpty())
            {
                CyanLibLanguageUtils.sendPlayerMessageActionBar(player,
                        LanguageUtils.getTranslation("dashSeparation"),
                        "cyan.message.dashSeparation",
                        false
                );
                CyanLibLanguageUtils.sendPlayerMessageActionBar(player,
                        LanguageUtils.getTranslation("listLocations"),
                        "cyan.message.listLocations",
                        false
                );

                LocationsObj.getLocations().forEach(location -> CyanLibLanguageUtils.sendPlayerMessageActionBar(
                        player,
                        LanguageUtils.getTranslation("getLocation"),
                        "cyan.message.getLocation",
                        false,
                        Formatting.YELLOW + location.name(),
                        Formatting.DARK_AQUA + location.dimension()
                ));

                CyanLibLanguageUtils.sendPlayerMessageActionBar(player,
                        LanguageUtils.getTranslation("dashSeparation"),
                        "cyan.message.dashSeparation",
                        false
                );
            }
            else
            {
                CyanLibLanguageUtils.sendPlayerMessage(player,
                        LanguageUtils.getTranslation(ERROR + "noLocations"),
                        "cyan.message.noLocations"
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
