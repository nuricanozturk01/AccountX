package com.example.accountx.util;
public final class StringControl
{
    public static boolean isValid(String...args)
    {
        for (String arg : args)
            if (arg.isEmpty() || arg.isBlank())
                return false;
        return true;
    }
}
