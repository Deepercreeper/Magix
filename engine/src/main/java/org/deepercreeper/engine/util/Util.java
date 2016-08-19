package org.deepercreeper.engine.util;

public class Util
{
    private Util() {}

    public static void sleep(long timeout)
    {
        if (timeout <= 0)
        {
            return;
        }
        try
        {
            Thread.sleep(timeout);
        }
        catch (InterruptedException ignored)
        {
        }
    }
}
