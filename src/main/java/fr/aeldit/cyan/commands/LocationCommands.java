package fr.aeldit.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.aeldit.cyan.commands.argumentTypes.ArgumentSuggestion;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

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

        dispatcher.register(CommandManager.literal("removelocations")
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
        dispatcher.register(CommandManager.literal("removealllocation")
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

    public static int setLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            String locationName = StringArgumentType.getString(context, "name");
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            float yaw = player.getYaw();
            float pitch = player.getPitch();

            if (CyanMidnightConfig.allowLocations)
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditLocation))
                {
                    checkOrCreateFile(locationsPath);
                    try
                    {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(new File(locationsPath.toUri())));
                        ServerWorld overworld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
                        ServerWorld nether = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);
                        ServerWorld end = Objects.requireNonNull(player.getServer()).getWorld(World.END);

                        if (!properties.containsKey(locationName))
                        {
                            if (player.getWorld() == overworld)
                            {
                                properties.put(locationName, "%s %f %f %f %f %f %s".formatted("overworld", x, y, z, yaw, pitch, player.getName().getString()));
                            } else if (player.getWorld() == nether)
                            {
                                properties.put(locationName, "%s %f %f %f %f %f %s".formatted("nether", x, y, z, yaw, pitch, player.getName().getString()));
                            } else if (player.getWorld() == end)
                            {
                                properties.put(locationName, "%s %f %f %f %f %f %s".formatted("end", x, y, z, yaw, pitch, player.getName().getString()));
                            }

                            properties.store(new FileOutputStream(locationsPath.toFile()), null);

                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation("setLocation"),
                                    "cyan.message.setLocation",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations,
                                    Formatting.YELLOW + locationName
                            );
                        } else
                        {
                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation(ERROR + "locationAlreadyExists"),
                                    "cyan.message.locationAlreadyExists",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations
                            );
                        }
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                } else
                {
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation("notOp"),
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation(ERROR + "locationsDisabled"),
                        "cyan.message.disabled.locations",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int removeLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            if (CyanMidnightConfig.allowLocations)
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditLocation))
                {
                    String locationName = StringArgumentType.getString(context, "name");
                    checkOrCreateFile(locationsPath);
                    try
                    {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(new File(locationsPath.toUri())));

                        if (properties.containsKey(locationName))
                        {
                            properties.remove(locationName);
                            properties.store(new FileOutputStream(locationsPath.toFile()), null);

                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation("removeLocation"),
                                    "cyan.message.removeLocation",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations,
                                    Formatting.YELLOW + locationName
                            );
                        } else
                        {
                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation(ERROR + "locationNotFound"),
                                    "cyan.message.locationNotFound",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations,
                                    Formatting.YELLOW + locationName
                            );
                        }
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                } else
                {
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "notOp"),
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation(ERROR + "locationsDisabled"),
                        "cyan.message.disabled.locations",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int removeAllLocations(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            if (CyanMidnightConfig.allowLocations)
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditLocation))
                {
                    try
                    {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(locationsPath.toFile()));
                        properties.clear();
                        properties.store(new FileOutputStream(locationsPath.toFile()), null);
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("removedAllLocations"),
                                "cyan.message.removedAllLocations",
                                CyanMidnightConfig.msgToActionBar,
                                CyanMidnightConfig.useTranslations
                        );
                    } catch (IOException ex)
                    {
                        throw new RuntimeException(ex);
                    }
                } else
                {
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "notOp"),
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation(ERROR + "locationsDisabled"),
                        "cyan.message.disabled.locations",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int goToLocation(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            if (CyanMidnightConfig.allowLocations)
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeLocation))
                {
                    String locationName = StringArgumentType.getString(context, "name");
                    checkOrCreateFile(locationsPath);
                    try
                    {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(new File(locationsPath.toUri())));
                        ServerWorld overworld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
                        ServerWorld nether = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);
                        ServerWorld end = Objects.requireNonNull(player.getServer()).getWorld(World.END);

                        if (properties.containsKey(locationName))
                        {
                            String location = (String) properties.get(locationName);
                            String world = location.split(" ")[0];

                            if (Objects.equals(world, "overworld"))
                            {
                                player.teleport(
                                        overworld,
                                        Double.parseDouble(location.split(" ")[1]),
                                        Double.parseDouble(location.split(" ")[2]),
                                        Float.parseFloat(location.split(" ")[3]),
                                        Float.parseFloat(location.split(" ")[4]),
                                        Float.parseFloat(location.split(" ")[5])
                                );
                            } else if (Objects.equals(world, "nether"))
                            {
                                player.teleport(
                                        nether,
                                        Double.parseDouble(location.split(" ")[1]),
                                        Double.parseDouble(location.split(" ")[2]),
                                        Float.parseFloat(location.split(" ")[3]),
                                        Float.parseFloat(location.split(" ")[4]),
                                        Float.parseFloat(location.split(" ")[5])
                                );
                            } else if (Objects.equals(world, "end"))
                            {
                                player.teleport(
                                        end,
                                        Double.parseDouble(location.split(" ")[1]),
                                        Double.parseDouble(location.split(" ")[2]),
                                        Float.parseFloat(location.split(" ")[3]),
                                        Float.parseFloat(location.split(" ")[4]),
                                        Float.parseFloat(location.split(" ")[5])
                                );
                            }

                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation("goToLocation"),
                                    "cyan.message.goToLocation",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations,
                                    Formatting.YELLOW + locationName
                            );
                        } else
                        {
                            sendPlayerMessage(player,
                                    CyanLanguageUtils.getTranslation(ERROR + "locationNotFound"),
                                    "cyan.message.locationNotFound",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations,
                                    Formatting.YELLOW + locationName
                            );
                        }
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                } else
                {
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "notOp"),
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation(ERROR + "locationsDisabled"),
                        "cyan.message.disabled.locations",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int getLocationsList(@NotNull CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(CyanLanguageUtils.getTranslation(ERROR + "playerOnlyCmd")));
        } else
        {
            if (CyanMidnightConfig.allowLocations)
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeLocation))
                {
                    checkOrCreateFile(locationsPath);
                    try
                    {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(new File(locationsPath.toUri())));
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("dashSeparation"),
                                "cyan.message.getDescription.dashSeparation",
                                false,
                                CyanMidnightConfig.useTranslations
                        );
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("listLocations"),
                                "cyan.message.listLocations",
                                false,
                                CyanMidnightConfig.useTranslations
                        );

                        for (String key : properties.stringPropertyNames())
                        {
                            // Allows OP players to see which player created the location
                            if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditLocation) && properties.get(key).toString().split(" ").length == 7)
                            {
                                player.sendMessage(Text.of(Formatting.YELLOW + key + Formatting.DARK_AQUA + " (" + properties.get(key).toString().split(" ")[0].toUpperCase() + ", created by " + properties.get(key).toString().split(" ")[6] + ")"));
                            } else
                            {
                                player.sendMessage(Text.of(Formatting.YELLOW + key + Formatting.DARK_AQUA + " (" + properties.get(key).toString().split(" ")[0].toUpperCase() + ")"));
                            }
                        }
                        sendPlayerMessage(player,
                                CyanLanguageUtils.getTranslation("dashSeparation"),
                                "cyan.message.getDescription.dashSeparation",
                                false,
                                CyanMidnightConfig.useTranslations
                        );
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                } else
                {
                    sendPlayerMessage(player,
                            CyanLanguageUtils.getTranslation(ERROR + "notOp"),
                            "cyan.message.notOp",
                            false,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        CyanLanguageUtils.getTranslation(ERROR + "locationsDisabled"),
                        "cyan.message.disabled.locations",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
