package fr.aeldit.cyan.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fr.aeldit.cyanlib.lib.gui.CyanLibConfigScreen;

import static fr.aeldit.cyan.CyanCore.MODID;

public class ModMenuApiImpl implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> new CyanLibConfigScreen(null, parent, MODID);
    }
}
