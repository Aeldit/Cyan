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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import static fr.aeldit.cyan.util.Utils.LocationsObj;

@Environment(EnvType.CLIENT)
public class LocationsScreen extends Screen
{
    private final Screen parent;

    protected LocationsScreen(Screen parent)
    {
        super(Text.translatable("cyan.screen.locations.title"));
        this.parent = parent;
    }

    public static void open()
    {
        MinecraftClient.getInstance().setScreen(new LocationsScreen(null));
    }

    @Override
    public void close()
    {
        client.setScreen(parent);
    }

    @Override
    protected void init()
    {
        int x = width / 2 - 185;
        int y;
        int offset = 0;

        for (int i = 0; i < LocationsObj.getLocations().size(); i++)
        {
            if (i == 10)
            {
                x = width / 2 + 10;
                offset = 0;
            }

            if (offset >= 5)
            {
                y = 2 - 10 - 20 * (offset - 5);
            }
            else
            {
                y = height / 2 + 10 + 20 * offset;
            }

            int finalI = i;
            ButtonWidget btn = ButtonWidget.builder(Text.literal(LocationsObj.getLocation(i).name()), button -> LocationScreen.open(LocationsObj.getLocation(finalI).name()))
                    .dimensions(x, y, 150, 20)
                    .tooltip(Tooltip.of(Text.translatable("cyan.btn.locationsScreen.location.tooltip")))
                    .build();

            addDrawableChild(btn);
            offset++;
        }
    }
}
