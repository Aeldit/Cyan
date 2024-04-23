package fr.aeldit.cyan.teleportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.aeldit.cyan.config.CyanLibConfigImpl;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static fr.aeldit.cyan.CyanCore.*;

public class BackTps
{
    public record BackTp(String playerUUID, String dimension, double x, double y, double z, String date)
    {
    }

    private final List<BackTp> backTps = Collections.synchronizedList(new ArrayList<>());
    private final TypeToken<List<BackTp>> backTpType = new TypeToken<>()
    {
    };
    private boolean isEditingFile = false;
    public static Path BACK_TP_PATH = FabricLoader.getInstance().getConfigDir().resolve(Path.of(CYAN_MODID + "/back" +
            ".json"));

    public void add(BackTp backTp)
    {
        backTps.add(backTp);
        write();
    }

    /**
     * @implNote Can only be called if the result of {@link BackTps#backTpExists} is {@code true}
     */
    public void remove(String playerUUID)
    {
        BackTp backTp = getBackTp(playerUUID);
        if (backTp != null)
        {
            backTps.remove(backTp);
            write();
        }
    }

    public void removeAllOutdated()
    {
        try
        {
            ArrayList<BackTp> tmp = new ArrayList<>();

            for (BackTp backTp : backTps)
            {
                Date backTpDate = new SimpleDateFormat("dd/MM/yyyy").parse(backTp.date());
                long days = TimeUnit.DAYS.convert(
                        Math.abs(new Date().getTime() - backTpDate.getTime()), TimeUnit.MILLISECONDS);

                if (days >= CyanLibConfigImpl.DAYS_TO_REMOVE_BACK_TP.getValue())
                {
                    tmp.add(backTp);
                }
            }

            backTps.removeAll(tmp);
            write();
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param playerUUID The UUID of the player
     * @return The index of the object if it exists | {@code -1} otherwise
     */
    private @Nullable BackTp getBackTp(String playerUUID)
    {
        for (BackTp backTp : backTps)
        {
            if (backTp.playerUUID().equals(playerUUID))
            {
                return backTp;
            }
        }
        return null;
    }

    public boolean backTpExists(String playerUUID)
    {
        return getBackTp(playerUUID) != null;
    }

    public void readServer()
    {
        if (Files.exists(BACK_TP_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(BACK_TP_PATH);
                backTps.addAll(gsonReader.fromJson(reader, backTpType));
                reader.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public void readClient(String saveName)
    {
        BACK_TP_PATH = FabricLoader.getInstance().getConfigDir().resolve(Path.of(CYAN_MODID + "/" + saveName + "/back" +
                ".json"));
        checkOrCreateModDir(false);

        if (Files.exists(BACK_TP_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(BACK_TP_PATH);
                backTps.addAll(gsonReader.fromJson(reader, backTpType));
                reader.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private void write()
    {
        checkOrCreateModDir(false);

        try
        {
            if (backTps.isEmpty())
            {
                if (Files.exists(BACK_TP_PATH))
                {
                    Files.delete(BACK_TP_PATH);
                    removeEmptyModDir(false);
                }
            }
            else
            {
                // Checks if the file is already being written, and waits 1 second before writing if so
                if (!isEditingFile)
                {
                    isEditingFile = true;

                    Gson gsonWriter = new GsonBuilder().create();
                    Writer writer = Files.newBufferedWriter(BACK_TP_PATH);
                    gsonWriter.toJson(backTps, writer);
                    writer.close();

                    isEditingFile = false;
                }
                else
                {
                    long end = System.currentTimeMillis() + 1000; // 1 s
                    boolean couldWrite = false;

                    while (System.currentTimeMillis() < end)
                    {
                        if (!isEditingFile)
                        {
                            isEditingFile = true;

                            Gson gsonWriter = new GsonBuilder().create();
                            Writer writer = Files.newBufferedWriter(BACK_TP_PATH);
                            gsonWriter.toJson(backTps, writer);
                            writer.close();

                            couldWrite = true;
                            isEditingFile = false;
                            break;
                        }
                    }

                    if (!couldWrite)
                    {
                        CYAN_LOGGER.info("[Cyan] Could not write the backTps file because it is already being written" +
                                " (for more than 1 sec)");
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
