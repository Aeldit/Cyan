package fr.raphoulfifou.cyan.config.options;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.raphoulfifou.cyan.util.OpLevels;
import net.fabricmc.loader.api.FabricLoader;

public class CyanOptions
{
    public static final File DEFAULT_FILE_NAME = FabricLoader.getInstance().getConfigDir().resolve("cyan-options.json").toFile();

    public final GeneralSettings generalSettings = new GeneralSettings();

    private File file;
    private Options options;

    public static class GeneralSettings
    {

        public boolean allowBed;
        public boolean allowKgi;
        public boolean allowSurface;

        public int requiredOpLevelKgi;

        CyanOptions options;

        public GeneralSettings()
        {
            this.allowBed = true;
            this.allowKgi = true;
            this.allowSurface = true;

            this.requiredOpLevelKgi = 4;
        }

        public boolean setAllowBed(boolean value)
        {
            return this.allowBed = value;
        }

        public boolean setAllowKgi(boolean value)
        {
            return this.allowKgi = value;
        }

        public boolean setAllowSurface(boolean value)
        {
            return this.allowSurface = value;
        }

        public int setRequiredOpLevelKgi(int value)
        {
            return this.requiredOpLevelKgi = value;
        }
    }

    public Options getOptions()
    {
        return options;
    }

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    public static CyanOptions load(File file)
    {
        CyanOptions config;

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                config = GSON.fromJson(reader, CyanOptions.class);
            } catch (IOException e) {
                throw new RuntimeException("Could not parse config", e);
            }
            if (config.options != null) {
				if (config.options.replaceInvalidOptions(Options.DEFAULT)) {
					config.writeChanges();
				}
			}
        } else {
            config = new CyanOptions();
        }

        config.file = file;
        config.writeChanges();

        return config;
    }

    public void writeChanges()
    {
        File dir = this.file.getParentFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("Could not create parent directories");
            }
        } else if (!dir.isDirectory()) {
            throw new RuntimeException("The parent file is not a directory");
        }

        try (FileWriter writer = new FileWriter(this.file)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            throw new RuntimeException("Could not save configuration file", e);
        }
    }

    public static class Options {
		public static final Options DEFAULT = new Options();

		public OpLevels opLevels = OpLevels.OPMAX;

		public boolean replaceInvalidOptions(Options options) {
			boolean invalid = false;
			if (opLevels == null) {
				opLevels = options.opLevels;
				invalid = true;
			}
			return invalid;
		}
	}
}
