package fr.aeldit.cyan.util;

import org.jetbrains.annotations.NotNull;

public class TypesValidation
{
    public static boolean isNumeric(@NotNull String string)
    {
        return string.matches("^[-+]?\\d+(\\.\\d+)?$");
    }

    public static boolean isBoolean(String string)
    {
        return "true".equals(string) || "false".equals(string);
    }
}
