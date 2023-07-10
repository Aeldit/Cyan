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
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static fr.aeldit.cyan.util.Utils.MODID;

@Environment(EnvType.CLIENT)
public class CyanScreen extends Screen
{
    private final Screen parent;
    private static final ResourceTexture BACKGROUND = new ResourceTexture(new Identifier(MODID, "textures/gui/locations.png"));

    protected CyanScreen(Screen parent)
    {
        super(Text.translatable("cyan.screen.locations.title"));
        this.parent = parent;
    }

    public static void open()
    {
        MinecraftClient.getInstance().setScreen(new CyanScreen(null));
    }

    @Override
    public void close()
    {
        client.setScreen(parent);
    }

    @Override
    protected void init()
    {
        ButtonWidget locationsBtn = ButtonWidget.builder(Text.translatable("cyan.btn.mainScreen.locations"), button -> LocationsScreen.open())
                .dimensions(width / 2 - 100, height / 2 - 10, 200, 20)
                .tooltip(Tooltip.of(Text.translatable("cyan.btn.mainScreen.locations.tooltip")))
                .build();

        ButtonWidget configBtn = ButtonWidget.builder(Text.translatable("cyan.btn.mainScreen.config"), button -> ConfigScreen.open())
                .dimensions(width / 2 - 100, height / 2 - 40, 200, 20)
                .tooltip(Tooltip.of(Text.translatable("cyan.btn.mainScreen.config.tooltip")))
                .build();

        addDrawableChild(locationsBtn);
        addDrawableChild(configBtn);
    }
}
