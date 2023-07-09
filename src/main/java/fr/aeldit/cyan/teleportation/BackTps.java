/*
 * Copyright (c) 2023  -  Made by Aeldit
 *
 *              GNU LESSER GENERAL PUBLIC LICENSE
 *                  Version 3, 29 June 2007
 *
 *  Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *  Everyone is permitted to copy and distribute verbatim copies
 *  of this license document, but changing it is not allowed.
 *
 *
 * This version of the GNU Lesser General Public License incorporates
 * the terms and conditions of version 3 of the GNU General Public
 * License, supplemented by the additional permissions listed in the LICENSE.txt file
 * in the repo of this mod (https://github.com/Aeldit/Cyan)
 */

package fr.aeldit.cyan.teleportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

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

import static fr.aeldit.cyan.util.Utils.*;

public class BackTps
{
    public record BackTp(String playerUUID, String dimension, double x, double y, double z, String date) {}

    private final List<BackTp> backTps = Collections.synchronizedList(new ArrayList<>());
    private final TypeToken<List<BackTp>> BACK_TYPE = new TypeToken<>() {};
    private boolean isEditingFile = false;
    public static Path BACK_TP_PATH = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/back.json");

    public void add(BackTp backTp)
    {
        this.backTps.add(backTp);
        write();
    }

    public void remove(String playerUUID)
    {
        this.backTps.remove(getBackTpIndex(playerUUID));
        write();
    }

    public void removeAllOutdated()
    {
        try
        {
            ArrayList<BackTp> tmp = new ArrayList<>();

            for (BackTp backTp : this.backTps)
            {
                Date backTpDate = new SimpleDateFormat("dd/MM/yyyy").parse(backTp.date());
                long days = TimeUnit.DAYS.convert(Math.abs(new Date().getTime() - backTpDate.getTime()), TimeUnit.MILLISECONDS);

                if (days >= LibConfig.getIntOption("daysToRemoveBackTp"))
                {
                    tmp.add(backTp);
                }
            }

            this.backTps.removeAll(tmp);
            write();
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    public BackTp getBackTp(String playerUUID)
    {
        return this.backTps.get(getBackTpIndex(playerUUID));
    }

    public int getBackTpIndex(String playerUUID)
    {
        for (BackTp backTp : this.backTps)
        {
            if (backTp.playerUUID().equals(playerUUID))
            {
                return this.backTps.indexOf(backTp);
            }
        }
        return -1;
    }

    public boolean backTpExists(String playerUUID)
    {
        for (BackTp backTp : this.backTps)
        {
            if (backTp.playerUUID().equals(playerUUID))
            {
                return true;
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
                this.backTps.addAll(gsonReader.fromJson(reader, BACK_TYPE));
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
        BACK_TP_PATH = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/" + saveName + "/back.json");
        checkOrCreateModDir(false);

        if (Files.exists(BACK_TP_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(BACK_TP_PATH);
                this.backTps.addAll(gsonReader.fromJson(reader, BACK_TYPE));
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
            if (this.backTps.isEmpty())
            {
                if (Files.exists(BACK_TP_PATH))
                {
                    Files.delete(BACK_TP_PATH);
                    removeEmptyModDir(false);
                }
            }
            else
            {

                if (!this.isEditingFile)
                {
                    this.isEditingFile = true;

                    Gson gsonWriter = new GsonBuilder().create();
                    Writer writer = Files.newBufferedWriter(BACK_TP_PATH);
                    gsonWriter.toJson(this.backTps, writer);
                    writer.close();

                    this.isEditingFile = false;
                }
                else
                {
                    long end = System.currentTimeMillis() + 1000; // 1 s
                    boolean couldWrite = false;

                    while (System.currentTimeMillis() < end)
                    {
                        if (!this.isEditingFile)
                        {
                            this.isEditingFile = true;

                            Gson gsonWriter = new GsonBuilder().create();
                            Writer writer = Files.newBufferedWriter(BACK_TP_PATH);
                            gsonWriter.toJson(this.backTps, writer);
                            writer.close();

                            couldWrite = true;
                            this.isEditingFile = false;
                            break;
                        }
                    }

                    if (!couldWrite)
                    {
                        LOGGER.info("[Cyan] Could not write the backTps file because it is already being written (for more than 1 sec)");
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
