package fr.aeldit.cyan.config;

import fr.aeldit.cyanlib.lib.config.CyanLibOptionsStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class CyanLibConfigImplTest
{
    private final CyanLibConfigImpl cfg = new CyanLibConfigImpl();
    private final Map<String, String> translations = cfg.getDefaultTranslations();
    private final CyanLibOptionsStorage opts = new CyanLibOptionsStorage("cyan-test", cfg);

    @Test
    void checkSet()
    {
        Assertions.assertTrue(opts.getOptionsNames()
                                  .stream()
                                  .allMatch(s -> translations.containsKey("msg.set.%s".formatted(s))));
    }

    @Test
    void checkGetDesc()
    {
        Assertions.assertTrue(opts.getOptionsNames()
                                  .stream()
                                  .allMatch(s -> translations.containsKey("msg.getDesc.%s".formatted(s))));
    }

    @Test
    void checkGetCfg()
    {
        Assertions.assertTrue(opts.getOptionsNames()
                                  .stream()
                                  .allMatch(s -> translations.containsKey("msg.getCfg.%s".formatted(s))));
    }
}