package fr.raphoulfifou.cyan.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import fr.raphoulfifou.cyan.CyanClientCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public class ModMenuApiImpl implements ModMenuApi {
    
    @Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
			return new ClothConfigScreenFactory(CyanClientCore.getOptions());
		}
		return (screen) -> null;
	}

}
