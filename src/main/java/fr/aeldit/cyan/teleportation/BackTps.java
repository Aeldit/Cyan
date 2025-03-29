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
    private final List<BackTp> backTps = Collections.synchronizedList(new ArrayList<>());
    private final TypeToken<List<BackTp>> backTpType = new TypeToken<>()
    {
    };
    public static Path BACK_TP_PATH =
            FabricLoader.getInstance().getConfigDir().resolve(Path.of("%s/back.json".formatted(MODID)));

    public void add(@NotNull BackTp backTp)
    {
        backTps.remove(backTp); // Makes sure there is only one backTp at a time per player
        backTps.add(backTp);
        write();
    }

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

    public @Nullable BackTp getBackTp(String playerUUID)
    {
        return backTps.stream().filter(backTp -> backTp.playerUUID().equals(playerUUID)).findFirst().orElse(null);
    }

    public boolean backTpExists(String playerUUID)
    {
        return backTps.stream().anyMatch(backTp -> backTp.playerUUID().equals(playerUUID));
    }

    public void readServer()
    {
        if (Files.exists(BACK_TP_PATH))
        {
            try (Reader reader = Files.newBufferedReader(BACK_TP_PATH))
            {
                backTps.addAll(new Gson().fromJson(reader, backTpType));
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
            try (Reader reader = Files.newBufferedReader(BACK_TP_PATH))
            {
                backTps.addAll(new Gson().fromJson(reader, backTpType));
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
            try (Writer writer = Files.newBufferedWriter(BACK_TP_PATH))
            {
                new GsonBuilder().create().toJson(backTps, writer);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
