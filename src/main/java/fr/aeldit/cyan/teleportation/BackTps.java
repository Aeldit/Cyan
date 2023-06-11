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
import fr.aeldit.cyan.config.CyanMidnightConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static fr.aeldit.cyan.util.Utils.MODID;
import static fr.aeldit.cyan.util.Utils.checkOrCreateModDir;

public class BackTps
{
    private ArrayList<BackTp> backTps;
    private final TypeToken<ArrayList<BackTp>> BACK_TYPE = new TypeToken<>() {};
    public static final Path BACK_TP_PATH = FabricLoader.getInstance().getConfigDir().resolve(MODID + "/back.json");


    public BackTps()
    {
        read();
    }

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

                if (days >= CyanMidnightConfig.daysToRemoveBackTp)
                {
                    tmp.add(backTp);
                }
            }

            this.backTps.removeAll(tmp);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }

        write();
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

    public void read()
    {
        if (Files.exists(BACK_TP_PATH))
        {
            try
            {
                Gson gsonReader = new Gson();
                Reader reader = Files.newBufferedReader(BACK_TP_PATH);
                this.backTps = gsonReader.fromJson(reader, BACK_TYPE);
                reader.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            this.backTps = new ArrayList<>();
        }
    }

    private void write()
    {
        checkOrCreateModDir();

        try
        {
            if (this.backTps.isEmpty() && Files.exists(BACK_TP_PATH))
            {
                Files.delete(BACK_TP_PATH);
            }
            else
            {
                Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
                Writer writer = Files.newBufferedWriter(BACK_TP_PATH);
                gsonWriter.toJson(this.backTps, writer);
                writer.close();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
