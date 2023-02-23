package fr.aeldit.cyan;

import eu.midnightdust.lib.config.MidnightConfig;
import fr.aeldit.cyan.commands.CyanCommands;
import fr.aeldit.cyan.commands.LocationCommands;
import fr.aeldit.cyan.commands.MiscellaneousCommands;
import fr.aeldit.cyan.commands.TeleportationCommands;
import fr.aeldit.cyan.config.CyanMidnightConfig;
import fr.aeldit.cyan.util.LanguageUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;

import static fr.aeldit.cyan.config.CyanMidnightConfig.generateAllOptionsMap;
import static fr.aeldit.cyan.util.LanguageUtils.languagePath;
import static fr.aeldit.cyan.util.Utils.locationsPath;

public class CyanClientCore implements ClientModInitializer
{
    public static final String MODID = "cyan";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String MODNAME = "[Cyan]";

    @Override
    public void onInitializeClient()
    {
        MidnightConfig.init(MODID, CyanMidnightConfig.class);
        LOGGER.info("{} Successfully initialized config", MODNAME);

        try
        {
            if (Files.exists(locationsPath) && Files.readAllLines(locationsPath).size() <= 1)
            {
                Files.delete(locationsPath);
                LOGGER.info("{} Deleted the locations file because it was empty", MODNAME);
            }

            if (Files.exists(languagePath) && Files.readAllLines(languagePath).size() <= 1)
            {
                Files.delete(languagePath);
                LOGGER.info("{} Deleted the translation file because it was empty", MODNAME);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        if (!CyanMidnightConfig.useTranslations)
        {
            LanguageUtils.loadLanguage();
        }

        generateAllOptionsMap();

        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) ->
        {
            TeleportationCommands.register(dispatcher);
            MiscellaneousCommands.register(dispatcher);
            CyanCommands.register(dispatcher);
            LocationCommands.register(dispatcher);
        });
        LOGGER.info("{} Successfully initialized commands", MODNAME);

        LOGGER.info("{} Successfully completed initialization", MODNAME);
    }
}
