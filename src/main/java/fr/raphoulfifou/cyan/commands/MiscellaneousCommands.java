package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Properties;

import static fr.raphoulfifou.cyan.util.ChatConstants.*;
import static fr.raphoulfifou.cyanlib.util.ChatUtil.sendPlayerMessage;
import static fr.raphoulfifou.cyan.CyanServerCore.MODID;

/**
 * @since 0.0.2
 */
public class MiscellaneousCommands
{

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("killgrounditems")
                .then(CommandManager.argument("distance_in_chunks", IntegerArgumentType.integer())
                        .executes(MiscellaneousCommands::kgir)
                )
                .executes(MiscellaneousCommands::kgi)
        );
        dispatcher.register(CommandManager.literal("kgi")
                .then(CommandManager.argument("distance_in_chunks", IntegerArgumentType.integer())
                        .executes(MiscellaneousCommands::kgir)
                )
                .executes(MiscellaneousCommands::kgi)
        );

        dispatcher.register(CommandManager.literal("chelp")
                .executes(MiscellaneousCommands::helpCyan)
        );

        dispatcher.register(CommandManager.literal("ops")
                .executes(MiscellaneousCommands::ops)
        );

        dispatcher.register(CommandManager.literal("mods")
                .executes(MiscellaneousCommands::mods)
        );

        dispatcher.register(CommandManager.literal("addTranslation")
                .then(CommandManager.argument("lang_name", StringArgumentType.string())
                        .then(CommandManager.argument("translation_name", StringArgumentType.string())
                                .then(CommandManager.argument("translation", StringArgumentType.string())
                                        .executes(MiscellaneousCommands::addTranslation)
                                )
                        )
                )
        );
    }

    /**
     * <p>Called when a player execute the command "/killgrounditems" or "/kgi"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> The ground items are killed and the player is notified by a message that the entities where killed</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The ground items are killed and a message is send to the console and to the OPs</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int kgi(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (CyanMidnightConfig.allowKgi)
        {
            if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
            {
                // Default distance is 14 chunks, but can be changed in settings
                source.getServer().getCommandManager().execute(source, String.format("/kill @e[type=item,distance=..%d]", CyanMidnightConfig.distanceToEntitiesKgi * 16));
                sendPlayerMessage(player,
                        "§cGround items have been removed",
                        null,
                        "cyan.message.kgi",
                        true,
                        CyanMidnightConfig.useOneLanguage);
            } else
            {
                sendPlayerMessage(player,
                        notOP,
                        null,
                        "cyan.message.notOp",
                        true,
                        CyanMidnightConfig.useOneLanguage);
                return 0;
            }
        } else
        {
            sendPlayerMessage(player,
                    "§cThe /kgi command is disabled. To enable it, enter '/setAllowKgi true' in chat",
                    Arrays.toString(source.getServer().getPlayerManager().getOpNames()),
                    "cyan.message.disabled.kgi",
                    false,
                    CyanMidnightConfig.useOneLanguage);
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/killgrounditems (int)" or "/kgi (int)"</p>
     *
     * <ul>If the player has a permission level equal to 4
     *      <li>-> The ground items are killed in a range of the given arg and the player is notified by a message that the entities where killed</li>
     * </ul>
     * <ul>Else:
     *      <li>-> The ground items are killed and a message is send to the console and to the OPs</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int kgir(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        int arg = IntegerArgumentType.getInteger(context, "distance_in_chunks");

        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            if (CyanMidnightConfig.allowKgi)
            {
                // The default distance is 14 chunks, but it can be changed in the config file or with commands
                source.getServer().getCommandManager().execute(source, String.format("/kill @e[type=item,distance=..%d]", arg * 16));
                sendPlayerMessage(player,
                        "§cGround items have been removed in a radius of %s §cchunks",
                        green + Integer.toString(arg),
                        "cyan.message.kgir",
                        true,
                        CyanMidnightConfig.useOneLanguage);
            } else
            {
                sendPlayerMessage(player,
                        "§cThe /kgi command is disabled. To enable it, enter '/setAllowKgi true' in chat",
                        Arrays.toString(source.getServer().getPlayerManager().getOpNames()),
                        "cyan.message.disabled.kgi",
                        false,
                        CyanMidnightConfig.useOneLanguage);
                return 0;
            }
        } else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true,
                    CyanMidnightConfig.useOneLanguage);
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/Chelp"</p>
     *
     * <ul>Displays Help for the mod in the chat</ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int helpCyan(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerPlayerEntity player = context.getSource().getPlayer();

        player.sendMessage(new TranslatableText("cyan.message.help.1"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.2"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.3"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.4"), false);
        player.sendMessage(new TranslatableText("cyan.message.help.5"), false);

        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/ops"</p>
     *
     * <ul>If the player has a permission level equal to the level defined in the config (4 by default)
     *      <li>-> A list with all the op players is displayed</li>
     * </ul>
     * <ul>Else:
     *      <li>-> A message tells the player taht he/she don't have the required permission</li>
     * </ul>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int ops(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player.hasPermissionLevel(CyanMidnightConfig.minOpLevelExeKgi))
        {
            sendPlayerMessage(player,
                    line_start + "The op players are :\n%s",
                    Arrays.toString(source.getServer().getPlayerManager().getOpNames()),
                    "cyan.message.ops",
                    false,
                    CyanMidnightConfig.useOneLanguage);
        } else
        {
            sendPlayerMessage(player,
                    notOP,
                    null,
                    "cyan.message.notOp",
                    true,
                    CyanMidnightConfig.useOneLanguage);
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    /**
     * <p>Called when a player execute the command "/mods"</p>
     * <p>
     * A list of all mods installed on the server
     * TODO -> make work
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int mods(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();

        source.getPlayer().sendMessage(new TranslatableText("cyan.message.mods",
                Arrays.toString(FabricLoader.getInstance().getAllMods().toArray())), false);

        return Command.SINGLE_SUCCESS;
    }


    public static int addTranslation(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerPlayerEntity player = context.getSource().getPlayer();
        String langName = StringArgumentType.getString(context, "lang_name");
        String translationName = StringArgumentType.getString(context, "translation_name");
        String translation = StringArgumentType.getString(context, "translation");

        if (player.hasPermissionLevel(4))
        {
            setupTranslations(player, MODID, langName, CyanMidnightConfig.useOneLanguage, translationName, translation);
        } else
        {
            player.sendMessage(new TranslatableText(String.valueOf(getProperty("error.notOp", MODID, langName))), false);
            /*sendPlayerMessage(player,
                    getProperty("notOP", MODID, langName),
                    null,
                    "cyan.message.notOp",
                    true,
                    CyanMidnightConfig.useOneLanguage);*/
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void setupTranslations(@NotNull ServerPlayerEntity player, String folderName, String langName, boolean useOneLanguage, String translationName, String translation)
    {
        String dir = String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(folderName + "\\" + langName + ".properties"));

        if (player.hasPermissionLevel(4))
        {
            if (! Files.exists(FabricLoader.getInstance().getConfigDir().resolve(folderName + "\\" + langName + ".properties")))
            {
                try (OutputStream output = new FileOutputStream(dir))
                {
                    Properties prop = new Properties();

                    prop.setProperty(translationName, translation);
                    prop.store(output, null);

                    sendPlayerMessage(player,
                            "File created : %s",
                            langName + ".properties",
                            "cyan.message.fileCreated",
                            false,
                            useOneLanguage);
                } catch (IOException e)
                {
                    sendPlayerMessage(player,
                            "Could not create the file",
                            null,
                            "cyan.message.error.fileCreate",
                            false,
                            useOneLanguage);
                    e.printStackTrace();
                }
            } else
            {
                try (InputStream inputStream = new FileInputStream(dir))
                {
                    Properties prop = new Properties();
                    prop.load(inputStream);

                    prop.setProperty(translationName, translation);
                    prop.store(new FileOutputStream(dir), null);

                    inputStream.close();

                    sendPlayerMessage(player,
                            "Added translation : %s",
                            translationName,
                            "cyan.message.translationAdded",
                            false,
                            useOneLanguage);
                } catch (IOException e)
                {
                    sendPlayerMessage(player,
                            "Could not write to the file",
                            null,
                            "cyan.message.error.fileWrite",
                            false,
                            useOneLanguage);
                    e.printStackTrace();
                }
            }
        }
    }

}
