package org.deepercreeper.common.util;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest
{
    @Test
    public void testSleep()
    {
        long timeStamp = System.currentTimeMillis();
        Util.sleep(500);
        Assert.assertTrue(System.currentTimeMillis() - timeStamp >= 500);
    }

    @Test
    public void testNegativeTimeout()
    {
        long timeStamp = System.currentTimeMillis();
        Util.sleep(-1);
        Assert.assertTrue(System.currentTimeMillis() - timeStamp <= 5);
    }
}
