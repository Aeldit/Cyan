package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import static fr.raphoulfifou.cyan.util.ChatConstants.green;
import static fr.raphoulfifou.cyan.util.ChatConstants.red;
import static fr.raphoulfifou.cyanlib.util.ChatUtil.sendPlayerMessage;

/**
 * @since 0.4.1
 */
public class GetCommand
{

    static Formatting a_c = Formatting.GREEN;
    static Formatting b_c = Formatting.GREEN;
    static Formatting c_c = Formatting.GREEN;
    static Formatting d_c = Formatting.GREEN;

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("getCyanConfigOptions")
                .executes(GetCommand::getConfigOptions)
        );
    }

    /**
     * <p>Called when a player execute the command "/getCyanConfigOptions"</p>
     * <p>Send a player in the player's chat with all options and their values</p>
     *
     * @throws CommandSyntaxException if the syntaxe of the command isn't correct
     */
    public static int getConfigOptions(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (! CyanMidnightConfig.allowBed)
        {
            a_c = red;
        } else
        {
            a_c = green;
        }
        if (! CyanMidnightConfig.allowKgi)
        {
            b_c = red;
        } else
        {
            b_c = green;
        }
        if (! CyanMidnightConfig.allowSurface)
        {
            c_c = red;
        } else
        {
            c_c = green;
        }
        if (CyanMidnightConfig.useOneLanguage)
        {
            d_c = red;
        } else
        {
            d_c = green;
        }

        sendPlayerMessage(player,
                "§6|--> §3Options defined for the Cyan mod :",
                null,
                "cyan.message.getCfgOptions.header",
                false,
                CyanMidnightConfig.useOneLanguage
        );

        sendPlayerMessage(player,
                "§6>> §3/bed allowed : %s",
                a_c + Boolean.toString(CyanMidnightConfig.allowBed),
                "cyan.message.getCfgOptions.allowBed",
                false,
                CyanMidnightConfig.useOneLanguage
        );
        sendPlayerMessage(player,
                "§6>> §3/kgi allowed : %s",
                b_c + Boolean.toString(CyanMidnightConfig.allowKgi),
                "cyan.message.getCfgOptions.allowKgi",
                false,
                CyanMidnightConfig.useOneLanguage
        );
        sendPlayerMessage(player,
                "§6>> §3/surface allowed : %s",
                c_c + Boolean.toString(CyanMidnightConfig.allowSurface),
                "cyan.message.getCfgOptions.allowSurface",
                false,
                CyanMidnightConfig.useOneLanguage
        );
        sendPlayerMessage(player,
                "§6>> §3Use of traduction files : %s",
                d_c + Boolean.toString(! CyanMidnightConfig.useOneLanguage),
                "cyan.message.getCfgOptions.getUseOneLanguage",
                false,
                CyanMidnightConfig.useOneLanguage
        );

        sendPlayerMessage(player,
                "§6>> §3Distance in which ground items will be removed (in chunks): %s",
                green + Integer.toString(CyanMidnightConfig.distanceToEntitiesKgi),
                "cyan.message.getCfgOptions.distanceToEntitiesKgi",
                false,
                CyanMidnightConfig.useOneLanguage
        );
        sendPlayerMessage(player,
                "§6>> §3Minimu OP level for /kgi : %s",
                green + Integer.toString(CyanMidnightConfig.minOpLevelExeKgi),
                "cyan.message.getCfgOptions.minOpLevelExeKgi",
                false,
                CyanMidnightConfig.useOneLanguage
        );

        return Command.SINGLE_SUCCESS;
    }

}
