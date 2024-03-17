package fr.aeldit.cyan.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.aeldit.cyanlib.lib.gui.CyanLibConfigScreen;

import static fr.aeldit.cyan.CyanCore.CYAN_MODID;
import static fr.aeldit.cyanlib.lib.CyanLib.CONFIG_CLASS_INSTANCES;

public class ModMenuApiImpl implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> new CyanLibConfigScreen(
                CONFIG_CLASS_INSTANCES.get(CYAN_MODID).getOptionsStorage(),
                parent,
                CONFIG_CLASS_INSTANCES.get(CYAN_MODID).getOptionsStorage().getConfigClass()
        );
    }
}
