package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;


/**
 * @author Raphoulfifou
 * @since 0.4.1
 */
public class GetCommand
{

    static Formatting a_c = Formatting.GREEN;
    static Formatting b_c = Formatting.GREEN;
    static Formatting c_c = Formatting.GREEN;

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
            a_c = Formatting.RED;
        }
        if (! CyanMidnightConfig.allowKgi)
        {
            b_c = Formatting.RED;
        }
        if (! CyanMidnightConfig.allowSurface)
        {
            c_c = Formatting.RED;
        }

        player.sendMessage(new TranslatableText("cyan.message.getCfgOptions.header"), false);

        player.sendMessage(new TranslatableText("cyan.message.getCfgOptions.allowBed", a_c + Boolean.toString(CyanMidnightConfig.allowBed)), false);
        player.sendMessage(new TranslatableText("cyan.message.getCfgOptions.allowKgi", b_c + Boolean.toString(CyanMidnightConfig.allowKgi)), false);
        player.sendMessage(new TranslatableText("cyan.message.getCfgOptions.allowSurface", c_c + Boolean.toString(CyanMidnightConfig.allowSurface)), false);

        player.sendMessage(new TranslatableText("cyan.message.getCfgOptions.distanceToEntitiesKgi", Formatting.GREEN + Integer.toString(CyanMidnightConfig.distanceToEntitiesKgi)), false);
        player.sendMessage(new TranslatableText("cyan.message.getCfgOptions.minOpLevelExeKgi", Formatting.GREEN + Integer.toString(CyanMidnightConfig.minOpLevelExeKgi)), false);

        return Command.SINGLE_SUCCESS;
    }

}
