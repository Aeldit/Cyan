package fr.aeldit.cyan.teleportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.aeldit.cyan.config.CyanLibConfigImpl;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
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
    private List<BackTp> backTps = null;
    private final TypeToken<List<BackTp>> backTpType = new TypeToken<>()
    {
    };
    private boolean isEditingFile = false;
    public static Path BACK_TP_PATH =
            FabricLoader.getInstance().getConfigDir().resolve(Path.of("%s/back.json".formatted(MODID)));

    public void add(@NotNull BackTp backTp)
    {
        if (backTps == null)
        {
            backTps = Collections.synchronizedList(new ArrayList<>());
        }
        else
        {
            backTps.remove(backTp); // Makes sure there is only one backTp at a time per player
        }
        backTps.add(backTp);
        write();
    }

    public void remove(String playerUUID)
    {
        BackTp backTp = getBackTp(playerUUID);
        if (backTp != null && backTps != null)
        {
            backTps.remove(backTp);
            write();
        }
    }

    public void removeAllOutdated()
    {
        if (backTps != null)
        {
            try
            {
                ArrayList<BackTp> tmp = new ArrayList<>();
                int maxTime = CyanLibConfigImpl.DAYS_TO_REMOVE_BACK_TP.getValue();

                for (BackTp backTp : backTps)
                {
                    long days = TimeUnit.DAYS.convert(
                            Math.abs(new Date().getTime()
                                     - new SimpleDateFormat("dd/MM/yyyy").parse(backTp.date()).getTime()),
                            TimeUnit.MILLISECONDS
                    );

                    if (days >= maxTime)
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
    }

    public @Nullable BackTp getBackTp(String playerUUID)
    {
        if (backTps != null)
        {
            for (BackTp backTp : backTps)
            {
                if (backTp.playerUUID().equals(playerUUID))
                {
                    return backTp;
                }
            }
        }
        return null;
    }

    public boolean backTpExists(String playerUUID)
    {
        if (backTps != null)
        {
            for (BackTp backTp : backTps)
            {
                if (backTp.playerUUID().equals(playerUUID))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void readServer()
    {
        if (Files.exists(BACK_TP_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(BACK_TP_PATH);
                if (backTps == null)
                {
                    backTps = Collections.synchronizedList(new ArrayList<>());
                }
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
        BACK_TP_PATH = FabricLoader.getInstance().getConfigDir().resolve(
                Path.of("%s/%s/back.json".formatted(MODID, saveName))
        );

        if (Files.exists(BACK_TP_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(BACK_TP_PATH);
                if (backTps == null)
                {
                    backTps = Collections.synchronizedList(new ArrayList<>());
                }
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

        if (backTps.isEmpty())
        {
            if (Files.exists(BACK_TP_PATH))
            {
                try
                {
                    Files.delete(BACK_TP_PATH);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                removeEmptyModDir(false);
            }
        }
        else
        {
            // Checks if the file is already being written, and waits 1 second before writing if so
            if (!isEditingFile)
            {
                try
                {
                    isEditingFile = true;

                    Gson gsonWriter = new GsonBuilder().create();
                    Writer writer = Files.newBufferedWriter(BACK_TP_PATH);
                    gsonWriter.toJson(backTps, writer);
                    writer.close();

                    isEditingFile = false;
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                long end = System.currentTimeMillis() + 1000; // 1 s
                boolean couldWrite = false;

                while (System.currentTimeMillis() < end)
                {
                    if (!isEditingFile)
                    {
                        try
                        {
                            isEditingFile = true;

                            Gson gsonWriter = new GsonBuilder().create();
                            Writer writer = Files.newBufferedWriter(BACK_TP_PATH);
                            gsonWriter.toJson(backTps, writer);
                            writer.close();

                            couldWrite    = true;
                            isEditingFile = false;
                        }
                        catch (IOException e)
                        {
                            throw new RuntimeException(e);
                        }
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
}
