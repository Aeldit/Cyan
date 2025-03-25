package fr.aeldit.cyan.util;

import net.minecraft.text.ClickEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class VersionUtils
{
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ClickEvent getClickEvent(String command)
    {
        //? if =1.21.5 {
        return new ClickEvent.RunCommand(command);
        //?} else {
        /*return new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                command);
        *///?}
    }
}
