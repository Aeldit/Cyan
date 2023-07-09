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

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen
{
    public ButtonWidget exitBtn;
    private final Screen parent;

    protected ConfigScreen(Screen parent)
    {
        super(Text.translatable("cyan.screen.locations.title"));
        this.parent = parent;
    }

    public static void open()
    {
        MinecraftClient.getInstance().setScreen(new ConfigScreen(null));
    }

    @Override
    public void close()
    {
        client.setScreen(parent);
    }

    @Override
    protected void init()
    {
        exitBtn = ButtonWidget.builder(Text.translatable("cyan.btn.configScreen.exit"), button -> close())
                .dimensions(width / 2 - 205, 20, 200, 20)
                .tooltip(Tooltip.of(Text.translatable("cyan.btn.configScreen.exit.tooltip")))
                .build();

        addDrawableChild(exitBtn);
    }
}
