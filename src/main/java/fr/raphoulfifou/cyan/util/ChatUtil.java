package fr.raphoulfifou.cyan.util;

import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

public class ChatUtil
{

    public static void sendPlayerMessage(@NotNull ServerPlayerEntity player, String msg, Object args, String trad, boolean actionbar)
    {
        if (CyanMidnightConfig.useOneLanguage)
        {
            player.sendMessage(new TranslatableText(msg, args), actionbar);
        } else
        {
            player.sendMessage(new TranslatableText(trad, args), actionbar);
        }
    }

}
