package fr.raphoulfifou.cyan.config;

import java.util.Locale;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;

import fr.raphoulfifou.cyan.config.options.CyanOptions;
import fr.raphoulfifou.cyan.util.OpLevels;
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
	//private CyanOptions.GeneralSettings generalSettings;

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
		/*general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("options.cyan.allowBed"), generalSettings.allowBed)
				.setTooltip(new TranslatableText("mm.msg.allowBed"))
				.setDefaultValue(generalSettings.allowBed)
				.setSaveConsumer((value) -> {
					if (generalSettings.allowBed != value) {
						savingRunnable.reloadResources = true;
					}
					generalSettings.allowBed = value;
				})
				.build());*/

		general.addEntry(entryBuilder.startEnumSelector(new TranslatableText("options.cyan.required_op_level_kgi"), OpLevels.class, config.getOptions().opLevels)
				//.setTooltip(new TranslatableText("mm.msg.required_op_level_kgi"))
				.setSaveConsumer((value) -> {
					if (config.getOptions().opLevels != value) {
						savingRunnable.reloadResources = true;
					}
					config.getOptions().opLevels = value;
				})
				.setEnumNameProvider((value) -> {
					return new TranslatableText("option.cyan.opLevels." + value.name().toLowerCase(Locale.ROOT));
				})
				.setDefaultValue(CyanOptions.Options.DEFAULT.opLevels)
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
