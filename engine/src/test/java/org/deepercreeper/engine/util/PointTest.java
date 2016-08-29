package org.deepercreeper.engine.util;

import org.junit.Assert;
import org.junit.Test;

public class PointTest
{
    @Test
    public void testModify()
    {
        int[] modifications = new int[1];
        Point point = new Point(0, 0, () -> modifications[0]++);

        Assert.assertEquals(0, modifications[0]);

        point.add(1, 1);

        Assert.assertEquals(1, modifications[0]);

        point.setX(2);

        Assert.assertEquals(2, modifications[0]);

        point.set(2, 2);

        Assert.assertEquals(3, modifications[0]);

        point.setX(2);

        Assert.assertEquals(3, modifications[0]);
    }
}
