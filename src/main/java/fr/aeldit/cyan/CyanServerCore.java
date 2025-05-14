package fr.aeldit.cyan;

import net.fabricmc.api.DedicatedServerModInitializer;

import static fr.aeldit.cyan.CyanCore.*;

public class CyanServerCore implements DedicatedServerModInitializer
{
    @Override
    public void onInitializeServer()
    {
        LOCATIONS.readServer();
        BACK_TPS.readServer();
    }
}
