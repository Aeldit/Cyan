package fr.raphoulfifou.cyan.util;

import fr.raphoulfifou.cyan.config.CyanMidnightConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class ChatUtil
{

    public static Formatting color = Formatting.GREEN;
    public static String line_start = "\n§d>> ";
    public static String line_start_error = "\n§c>> ";
    public static String notOP = "§cYou don't have the required permission to do that";

    public static Formatting green = Formatting.GREEN;
    public static Formatting red = Formatting.RED;

    public static void sendPlayerMessage(@NotNull ServerPlayerEntity player, String msg, Object args, String trad_path, boolean actionbar)
    {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
        {
            if (CyanMidnightConfig.useOneLanguage)
            {
                player.sendMessage(new TranslatableText(msg, args), actionbar);
            } else
            {
                player.sendMessage(new TranslatableText(trad_path, args), actionbar);
            }
        } else
        {
            player.sendMessage(new TranslatableText(trad_path, args), actionbar);
        }
    }

}
