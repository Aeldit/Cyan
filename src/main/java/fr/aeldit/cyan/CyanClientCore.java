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

package fr.aeldit.cyan;

import fr.aeldit.cyan.commands.LocationCommands;
import fr.aeldit.cyan.commands.MiscellaneousCommands;
import fr.aeldit.cyan.commands.TeleportationCommands;
import fr.aeldit.cyan.gui.CyanScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import static fr.aeldit.cyan.util.EventUtils.removeOutdatedBackTps;
import static fr.aeldit.cyan.util.EventUtils.saveDeadPlayersPos;
import static fr.aeldit.cyan.util.GsonUtils.transferPropertiesToGson;
import static fr.aeldit.cyan.util.Utils.*;

public class CyanClientCore implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        if (LibConfig.getBoolOption("useCustomTranslations"))
        {
            LanguageUtils.loadLanguage(getDefaultTranslations());
        }

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> transferPropertiesToGson());
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> saveDeadPlayersPos(entity));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            LocationsObj.readClient(server.getIconFile().toString().replace("icon.png]", "")
                    .split("\\\\")[server.getIconFile().toString().split("\\\\").length - 2]
            );
            BackTpsObj.readClient(server.getIconFile().toString().replace("icon.png]", "")
                    .split("\\\\")[server.getIconFile().toString().split("\\\\").length - 2]
            );
            removeOutdatedBackTps();
        });

        KeyBinding mainScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "cyan.key.openScreen.locations",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C,
                "category.cyan"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mainScreenKey.wasPressed())
            {
                CyanScreen.open();
            }
        });

        // Register all the commands
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            TeleportationCommands.register(dispatcher);
            MiscellaneousCommands.register(dispatcher);
            LibConfigCommands.register(dispatcher);
            LocationCommands.register(dispatcher);
        });
        LOGGER.info("[Cyan] Successfully initialized");
    }
}
