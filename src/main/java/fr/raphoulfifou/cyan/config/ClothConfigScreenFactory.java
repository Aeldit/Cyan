package fr.raphoulfifou.cyan.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;

import fr.raphoulfifou.cyan.config.options.CyanOptions;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class ClothConfigScreenFactory implements ConfigScreenFactory<Screen>
{
	private CyanOptions config;
	private CyanOptions.GeneralSettings generalSettings;

	public ClothConfigScreenFactory(CyanOptions config)
	{
		this.config = config;
	}

	@Override
	public Screen create(Screen parent)
	{
		SavingRunnable savingRunnable = new SavingRunnable();

		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(new TranslatableText("screen.cyan.config.title"))
				.setSavingRunnable(savingRunnable);
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.cyan.general"));
		general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("options.cyan.allowBed"), generalSettings.getAllowBed())
				.setTooltip(new TranslatableText("mm.msg.allowBed"))
				.setDefaultValue(generalSettings.getAllowBed())
				.setSaveConsumer((value) -> {
					if (generalSettings.getAllowBed() != value) {
						savingRunnable.reloadResources = true;
					}
					generalSettings.setAllowBed(value);
				})
				.build());

		general.addEntry(entryBuilder.startIntSlider(new TranslatableText("options.cyan.required_op_level_kgi"), 0, 4, generalSettings.getRequiredOpLevelKgi())
				.setTooltip(new TranslatableText("mm.msg.required_op_level_kgi"))
				.setDefaultValue(generalSettings.getRequiredOpLevelKgi())
				.setSaveConsumer((value) -> {
					if (generalSettings.getRequiredOpLevelKgi() != value) {
						savingRunnable.reloadResources = true;
					}
					generalSettings.setRequiredOpLevelKgi(value);
				})
				.build());

		return builder.build();
	}

	private class SavingRunnable implements Runnable
	{
		public boolean reloadResources = false;

		@Override
		public void run() {
			config.writeChanges();
			if (reloadResources) {
				MinecraftClient.getInstance().reloadResources();
			}
			reloadResources = false;
		}
	}
}
