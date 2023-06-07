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
import fr.aeldit.cyan.config.CyanMidnightConfig;
import fr.aeldit.cyan.util.Location;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static fr.aeldit.cyan.util.GsonUtils.*;
import static fr.aeldit.cyan.util.Utils.*;
import static fr.aeldit.cyanlib.util.ChatUtils.sendPlayerMessage;
import static fr.aeldit.cyanlib.util.Constants.ERROR;

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

        if (CyanLibUtils.isPlayer(context.getSource()))
        {
            String locationName = StringArgumentType.getString(context, "name");

            if (CyanLibUtils.isOptionAllowed(player, CyanMidnightConfig.allowLocations, "locationsDisabled"))
            {
                if (CyanLibUtils.hasPermission(player, CyanMidnightConfig.minOpLevelExeEditLocation))
                {
                    try
                    {
                        ArrayList<Location> locations = new ArrayList<>();

                        if (Files.exists(LOCATIONS_PATH) && !Files.readAllLines(LOCATIONS_PATH).isEmpty())
                        {
                            locations = readLocationsFile();
                        }

                        boolean locationExists = false;

                        for (Location location : locations)
                        {
                            if (location.name().equals(locationName))
                            {
                                locationExists = true;
                                break;
                            }
                        }

                        if (!locationExists)
                        {
                            if (player.getWorld() == player.getServer().getWorld(World.OVERWORLD))
                            {
                                locations.add(new Location(locationName, "overworld", player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch()));
                            }
                            else if (player.getWorld() == player.getServer().getWorld(World.NETHER))
                            {
                                locations.add(new Location(locationName, "nether", player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch()));
                            }
                            else
                            {
                                locations.add(new Location(locationName, "end", player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch()));
                            }

                            writeLocations(locations);

                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation("setLocation"),
                                    "cyan.message.setLocation",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useCustomTranslations,
                                    Formatting.YELLOW + locationName
                            );
                        }
                        else
                        {
                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation(ERROR + "locationAlreadyExists"),
                                    "cyan.message.locationAlreadyExists",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useCustomTranslations
                            );
                        }
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
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

        if (CyanLibUtils.isPlayer(context.getSource()))
        {
            if (CyanLibUtils.isOptionAllowed(player, CyanMidnightConfig.allowLocations, "locationsDisabled"))
            {
                if (CyanLibUtils.hasPermission(player, CyanMidnightConfig.minOpLevelExeEditLocation))
                {
                    String locationName = StringArgumentType.getString(context, "name");

                    try
                    {
                        if (Files.exists(LOCATIONS_PATH) && !Files.readAllLines(LOCATIONS_PATH).isEmpty())
                        {
                            ArrayList<Location> locations = readLocationsFile();

                            if (locationExists(locations, locationName))
                            {
                                locations.remove(getLocationIndex(locations, locationName));
                                writeLocations(locations);
                                sendPlayerMessage(player,
                                        CyanLanguageUtils.getTranslation("removeLocation"),
                                        "cyan.message.removeLocation",
                                        CyanMidnightConfig.msgToActionBar,
                                        CyanMidnightConfig.useCustomTranslations,
                                        Formatting.YELLOW + locationName
                                );
                            }
                            else
                            {
                                sendPlayerMessage(player,
                                        CyanLanguageUtils.getTranslation(ERROR + "locationNotFound"),
                                        "cyan.message.locationNotFound",
                                        CyanMidnightConfig.msgToActionBar,
                                        CyanMidnightConfig.useCustomTranslations,
                                        Formatting.YELLOW + locationName
                                );
                            }
                        }
                        else
                        {
                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation(ERROR + "locationNotFound"),
                                    "cyan.message.locationNotFound",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useCustomTranslations,
                                    Formatting.YELLOW + locationName
                            );
                        }
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
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

        if (CyanLibUtils.isPlayer(context.getSource()))
        {
            if (CyanLibUtils.isOptionAllowed(player, CyanMidnightConfig.allowLocations, "locationsDisabled"))
            {
                if (CyanLibUtils.hasPermission(player, CyanMidnightConfig.minOpLevelExeEditLocation))
                {
                    if (Files.exists(LOCATIONS_PATH))
                    {
                        try
                        {
                            Files.delete(LOCATIONS_PATH);
                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation("removedAllLocations"),
                                    "cyan.message.removedAllLocations",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useCustomTranslations
                            );
                        }
                        catch (IOException e)
                        {
                            throw new RuntimeException(e);
                        }
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

        if (CyanLibUtils.isPlayer(context.getSource()))
        {
            if (CyanLibUtils.isOptionAllowed(player, CyanMidnightConfig.allowLocations, "locationsDisabled"))
            {
                String locationName = StringArgumentType.getString(context, "name");

                try
                {
                    if (Files.exists(LOCATIONS_PATH) && !Files.readAllLines(LOCATIONS_PATH).isEmpty())
                    {
                        ArrayList<Location> locations = readLocationsFile();

                        if (locationExists(locations, locationName))
                        {
                            Location loc = locations.get(getLocationIndex(locations, locationName));

                            switch (loc.dimension())
                            {
                                case "overworld" ->
                                        player.teleport(player.getServer().getWorld(World.OVERWORLD), loc.x(), loc.y(), loc.z(), loc.yaw(), loc.pitch());
                                case "nether" ->
                                        player.teleport(player.getServer().getWorld(World.NETHER), loc.x(), loc.y(), loc.z(), loc.yaw(), loc.pitch());
                                case "end" ->
                                        player.teleport(player.getServer().getWorld(World.END), loc.x(), loc.y(), loc.z(), loc.yaw(), loc.pitch());
                            }

                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation("goToLocation"),
                                    "cyan.message.goToLocation",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useCustomTranslations,
                                    Formatting.YELLOW + locationName
                            );
                        }
                        else
                        {
                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation(ERROR + "locationNotFound"),
                                    "cyan.message.locationNotFound",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useCustomTranslations,
                                    Formatting.YELLOW + locationName
                            );
                        }
                    }
                    else
                    {
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation(ERROR + "locationNotFound"),
                                "cyan.message.locationNotFound",
                                CyanMidnightConfig.msgToActionBar,
                                CyanMidnightConfig.useCustomTranslations,
                                Formatting.YELLOW + locationName
                        );
                    }
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
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

        if (CyanLibUtils.isPlayer(context.getSource()))
        {
            try
            {
                if (Files.exists(LOCATIONS_PATH) && !Files.readAllLines(LOCATIONS_PATH).isEmpty())
                {
                    ArrayList<Location> locations = readLocationsFile();

                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation("dashSeparation"),
                            "cyan.message.getDescription.dashSeparation",
                            false,
                            CyanMidnightConfig.useCustomTranslations
                    );
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation("listLocations"),
                            "cyan.message.listLocations",
                            false,
                            CyanMidnightConfig.useCustomTranslations
                    );

                    locations.forEach(location -> sendPlayerMessage(
                            player,
                            CyanLanguageUtils.getTranslation("getLocation"),
                            "cyansh.message.getLocation",
                            false,
                            CyanMidnightConfig.useCustomTranslations,
                            Formatting.YELLOW + location.name(),
                            Formatting.DARK_AQUA + location.dimension()
                    ));

                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation("dashSeparation"),
                            "cyan.message.getDescription.dashSeparation",
                            false,
                            CyanMidnightConfig.useCustomTranslations
                    );
                }
                else
                {
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "noLocations"),
                            "cyan.message.noLocations",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useCustomTranslations
                    );
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
