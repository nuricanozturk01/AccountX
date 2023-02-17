package com.example.accountx.util;

import java.util.concurrent.TimeUnit;
public final class UtilConverter
{
    public static long start()
    {
        return System.nanoTime();
    }
    public static long stop()
    {
        return start();
    }
    public static long convert(TimeUnit from, TimeUnit to, long t1, long t2)
    {
        return to.convert((t2 - t1), from);
    }
    public static long convert(TimeUnit from, TimeUnit to, long t)
    {
        return to.convert(t, from);
    }
}
