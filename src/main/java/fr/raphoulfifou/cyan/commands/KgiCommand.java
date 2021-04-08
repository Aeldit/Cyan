package fr.raphoulfifou.cyan.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class KgiCommand implements Command<ServerCommandSource> {

    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();

        //source.getPlayer().sendMessage(new TranslatableText("cyan.message.kgi"), true);
        source.getPlayer().sendMessage(Text.of("The command have not been implemented yet"), false);
        return 0;
    }
}
