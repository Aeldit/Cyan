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
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import static fr.aeldit.cyan.util.Utils.*;
import static fr.aeldit.cyanlib.util.ChatUtil.sendPlayerMessage;

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

        dispatcher.register(CommandManager.literal("location")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests((context4, builder4) -> ArgumentSuggestion.getLocations(builder4))
                        .executes(LocationCommands::goToLocation)
                )
        );
        dispatcher.register(CommandManager.literal("l")
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .suggests((context4, builder4) -> ArgumentSuggestion.getLocations(builder4))
                        .executes(LocationCommands::goToLocation)
                )
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

        String locationName = StringArgumentType.getString(context, "name");

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getErrorTraduction("playerOnlyCmd")));
        } else
        {
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            float yaw = player.getYaw();
            float pitch = player.getPitch();

            if (CyanMidnightConfig.allowLocations)
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditLocation))
                {
                    try
                    {
                        Properties properties = new Properties();
                        ServerWorld overworld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
                        ServerWorld nether = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);
                        ServerWorld end = Objects.requireNonNull(player.getServer()).getWorld(World.END);
                        properties.load(new FileInputStream(new File(locationsPath.toUri())));

                        if (!properties.containsKey(locationName))
                        {
                            if (player.getWorld() == overworld)
                            {
                                properties.put(locationName, "%s %f %f %f %f %f".formatted("overworld", x, y, z, yaw, pitch));
                            } else if (player.getWorld() == nether)
                            {
                                properties.put(locationName, "%s %f %f %f %f %f".formatted("nether", x, y, z, yaw, pitch));
                            } else if (player.getWorld() == end)
                            {
                                properties.put(locationName, "%s %f %f %f %f %f".formatted("end", x, y, z, yaw, pitch));
                            }

                            properties.store(new FileOutputStream(locationsPath.toFile()), null);

                            sendPlayerMessage(player,
                                    getCmdFeedbackTraduction("setLocation"),
                                    yellow + locationName,
                                    "cyan.message.setLocation",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations
                            );
                        } else
                        {
                            sendPlayerMessage(player,
                                    getCmdFeedbackTraduction("locationAlreadyExists"),
                                    null,
                                    "cyan.message.locationAlreadyExists",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations
                            );
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    sendPlayerMessage(player,
                            getErrorTraduction("notOp"),
                            null,
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        getErrorTraduction("disabled.locations"),
                        null,
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

        String locationName = StringArgumentType.getString(context, "name");

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getErrorTraduction("playerOnlyCmd")));
        } else
        {
            if (CyanMidnightConfig.allowLocations)
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeEditLocation))
                {
                    try
                    {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(new File(locationsPath.toUri())));

                        if (properties.containsKey(locationName))
                        {
                            properties.remove(locationName);
                            properties.store(new FileOutputStream(locationsPath.toFile()), null);

                            sendPlayerMessage(player,
                                    getCmdFeedbackTraduction("removeLocation"),
                                    yellow + locationName,
                                    "cyan.message.removeLocation",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations
                            );
                        } else
                        {
                            sendPlayerMessage(player,
                                    getCmdFeedbackTraduction("locationNotFound"),
                                    yellow + locationName,
                                    "cyan.message.locationNotFound",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations
                            );
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    sendPlayerMessage(player,
                            getErrorTraduction("notOp"),
                            null,
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        getErrorTraduction("disabled.locations"),
                        null,
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

        String locationName = StringArgumentType.getString(context, "name");

        if (player == null)
        {
            source.getServer().sendMessage(Text.of(getErrorTraduction("playerOnlyCmd")));
        } else
        {
            if (CyanMidnightConfig.allowLocations)
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeLocation))
                {
                    ServerWorld overworld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
                    ServerWorld nether = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);
                    ServerWorld end = Objects.requireNonNull(player.getServer()).getWorld(World.END);

                    try
                    {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(new File(locationsPath.toUri())));
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
                                    getCmdFeedbackTraduction("goToLocation"),
                                    yellow + locationName,
                                    "cyan.message.goToLocation",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations
                            );
                        } else
                        {
                            sendPlayerMessage(player,
                                    getCmdFeedbackTraduction("locationNotFound"),
                                    yellow + locationName,
                                    "cyan.message.locationNotFound",
                                    CyanMidnightConfig.msgToActionBar,
                                    CyanMidnightConfig.useTranslations
                            );
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    sendPlayerMessage(player,
                            getErrorTraduction("notOp"),
                            null,
                            "cyan.message.notOp",
                            CyanMidnightConfig.errorToActionBar,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        getErrorTraduction("disabled.locations"),
                        null,
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
            source.getServer().sendMessage(Text.of(getErrorTraduction("playerOnlyCmd")));
        } else
        {
            if (CyanMidnightConfig.allowLocations)
            {
                if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeLocation))
                {
                    try
                    {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(new File(locationsPath.toUri())));
                        sendPlayerMessage(player,
                                getMiscTraduction("listLocations"),
                                null,
                                "cyan.message.listLocations",
                                false,
                                CyanMidnightConfig.useTranslations
                        );

                        for (String key : properties.stringPropertyNames())
                        {
                            player.sendMessage(Text.of(yellow + key + gold + " (" + properties.get(key).toString().split(" ")[0] + ")"));
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    sendPlayerMessage(player,
                            getErrorTraduction("notOp"),
                            null,
                            "cyan.message.notOp",
                            false,
                            CyanMidnightConfig.useTranslations
                    );
                }
            } else
            {
                sendPlayerMessage(player,
                        getErrorTraduction("disabled.locations"),
                        null,
                        "cyan.message.disabled.locations",
                        CyanMidnightConfig.errorToActionBar,
                        CyanMidnightConfig.useTranslations
                );
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
