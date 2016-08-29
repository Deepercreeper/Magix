package org.deepercreeper.engine.util;

import org.junit.Assert;
import org.junit.Test;

public class VectorTest
{
    @Test
    public void testModify()
    {
        int[] modifications = new int[1];
        Vector vector = new Vector(0, 0, () -> modifications[0]++);

        Assert.assertEquals(0, modifications[0]);

        vector.add(1, 1);

        Assert.assertEquals(1, modifications[0]);

        vector.setX(2);

        Assert.assertEquals(2, modifications[0]);

        vector.set(2, 2);

        Assert.assertEquals(3, modifications[0]);

        vector.setX(2);

        Assert.assertEquals(3, modifications[0]);
    }

    @Test
    public void testAsPoint()
    {
        Assert.assertEquals(0, new Vector(0, .49).asPoint().getY());

        Assert.assertEquals(1, new Vector(0, .5).asPoint().getY());
    }
}
