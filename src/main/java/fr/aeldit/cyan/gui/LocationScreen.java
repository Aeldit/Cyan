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

package fr.aeldit.cyan.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import static fr.aeldit.cyan.util.Utils.LocationsObj;

public class LocationScreen extends Screen
{
    private final Screen parent;
    private final String locationName;

    protected LocationScreen(Screen parent, String locationName)
    {
        super(Text.translatable("cyan.screen.location.title"));
        this.parent = parent;
        this.locationName = locationName;
    }

    public static void open(String locationName)
    {
        MinecraftClient.getInstance().setScreen(new LocationScreen(new LocationsScreen(null), locationName));
    }

    @Override
    public void close()
    {
        client.setScreen(parent);
    }

    @Override
    protected void init()
    {
        ButtonWidget name = ButtonWidget.builder(Text.literal(this.locationName), button -> close())
                .dimensions(width / 2 - 75, 20, 150, 20)
                .build();

        ButtonWidget teleport = ButtonWidget.builder(Text.translatable("cyan.btn.locationScreen.teleport"), button -> {
                            LocationsObj.teleport(MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(selfUUID), this.locationName);
                            close();
                        }
                )
                .dimensions(width / 2 - 160, 60, 150, 20)
                .tooltip(Tooltip.of(Text.translatable("cyan.btn.locationScreen.teleport.tooltip")))
                .build();

        ButtonWidget remove = ButtonWidget.builder(Text.translatable("cyan.btn.locationScreen.remove"), button -> {
                    LocationsObj.remove(this.locationName);
                    MinecraftClient.getInstance().player.sendMessage(Text.of("test"));
                    close();
                })
                .dimensions(width / 2 + 10, 60, 150, 20)
                .tooltip(Tooltip.of(Text.translatable("cyan.btn.locationScreen.remove.tooltip")))
                .build();

        addDrawableChild(name);
        addDrawableChild(teleport);
        addDrawableChild(remove);
    }
}
