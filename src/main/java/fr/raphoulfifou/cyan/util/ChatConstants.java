package fr.raphoulfifou.cyan.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Formatting;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ChatConstants
{

    public static String line_start = "\n§d>> ";
    public static String line_start_error = "\n§c>> ";

    public static String getProperty(String propertyName, String folderName, String langName)
    {
        String dir = String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(folderName + "\\" + langName + ".properties"));

        String property = "";
        try (InputStream inputStream = new FileInputStream(dir))
        {
            Properties prop = new Properties();
            prop.load(inputStream);

            property = prop.getProperty(propertyName);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        /*if (propertyName.startsWith("error."))
        {
            property += "§c";
        }*/
        return property;
    }

    public static String notOP = "§cYou don't have the required permission to do that";

    public static Formatting green = Formatting.GREEN;
    public static Formatting red = Formatting.RED;

}
