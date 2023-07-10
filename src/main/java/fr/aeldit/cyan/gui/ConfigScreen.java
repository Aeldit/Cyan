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
import net.minecraft.util.Formatting;

import static fr.aeldit.cyan.util.Utils.LibConfig;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen
{
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
        int x = width / 2 - 185;
        int y;
        int offset = 0;

        for (int i = 0; i < LibConfig.getOptions().size(); i++)
        {
            if (i == 10)
            {
                x = width / 2 + 10;
                offset = 0;
            }

            String optionName = LibConfig.getOptions().get(i);
            Object value = LibConfig.getOption(optionName);
            String option;

            ButtonWidget btn;

            if (offset >= 5)
            {
                y = 2 - 10 - 20 * (offset - 5);
            }
            else
            {
                y = height / 2 + 10 + 20 * offset;
            }

            if (value instanceof Boolean)
            {
                option = (Boolean) value ? optionName + " : " + Formatting.GREEN + true : optionName + " : " + Formatting.RED + false;

                btn = ButtonWidget.builder(Text.literal(option), button -> {
                            LibConfig.setOption(optionName, !(Boolean) value);
                            clearAndInit();
                        })
                        .dimensions(x, y, 175, 20)
                        .tooltip(Tooltip.of(Text.translatable("cyan.msg.getDesc.%s".formatted(optionName))))
                        .build();
            }
            else
            {
                option = optionName + " : " + Formatting.GOLD + value;

                btn = ButtonWidget.builder(Text.literal(option), button -> {
                            LibConfig.setOption(optionName, (Integer) value + 1);
                            clearAndInit();
                        })
                        .dimensions(x, y, 175, 20)
                        .tooltip(Tooltip.of(Text.translatable("cyan.msg.getDesc.%s".formatted(optionName))))
                        .build();
            }

            addDrawableChild(btn);
            offset++;
        }
    }
}
