package fr.aeldit.cyan;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import static fr.aeldit.cyan.CyanCore.*;

public class CyanClientCore implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ServerPlayConnectionEvents.JOIN.register(
                (handler, sender, server) -> {
                    LOCATIONS.readClient(server.getSaveProperties().getLevelName());
                    BACK_TPS.readClient(server.getSaveProperties().getLevelName());
                }
        );
    }
}
