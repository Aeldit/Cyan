package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import org.jetbrains.annotations.NotNull;

import fr.raphoulfifou.cyan.config.options.CyanOptions;
import fr.raphoulfifou.cyan.config.options.CyanOptions.GeneralSettings;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

/**
 * @since 0.2.6
 * @author Raphoulfifou
 */
public class SetCommands
{
    public static final CyanOptions CYAN_OPTIONS = new CyanOptions();
    public static final CyanOptions.GeneralSettings GENERAL_SETTINGS = new GeneralSettings();

    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("setAllowBed")
                .then(CommandManager.argument("true-false", BoolArgumentType.bool())
                    .executes(SetCommands::setAllowBed)
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
    public static int setAllowBed(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        boolean arg = BoolArgumentType.getBool(context, "true-false");

        GENERAL_SETTINGS.setAllowBed(arg);
        //CYAN_OPTIONS.writeChanges();
        player.sendMessage(new TranslatableText("cyan.message.setAllowBed", Boolean.toString(arg)), true);
        //player.sendMessage(new TranslatableText(Boolean.toString(arg)), true);

        // If OP with max level (4)
        /*if(player.hasPermissionLevel(4)) {
            generalSettings.setAllowBed(arg);
            source.getPlayer().sendMessage(new TranslatableText("cyan.message.setAllowBed", arg), true);
        }
        // If not OP or not OP with max level
        else {
            source.sendFeedback(new TranslatableText("cyan.message.notOp"), true);
        }*/
        return Command.SINGLE_SUCCESS;
    }
    
}
