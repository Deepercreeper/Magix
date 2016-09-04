package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.common.util.Util;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class UpdateEngineTest
{
    @Test
    public void testUpdate()
    {
        AtomicInteger updates = new AtomicInteger();
        UpdateEngine updateEngine = new UpdateEngine(delta -> updates.incrementAndGet());

        updateEngine.setFps(10);
        updateEngine.setUpdating(true);

        Util.sleep(1050);

        updateEngine.setUpdating(false);

        Assert.assertEquals(10, updates.get());
    }

    @Test
    public void testSum()
    {
        double[] deltaSum = new double[1];
        UpdateEngine updateEngine = new UpdateEngine(delta -> deltaSum[0] += delta);

        updateEngine.setFps(10);
        updateEngine.setUpdating(true);

        Util.sleep(1050);

        updateEngine.setUpdating(false);

        Assert.assertEquals(1, deltaSum[0], 10E-10);
    }

    @Test
    public void testSpeed()
    {
        double[] deltaSum = new double[1];
        UpdateEngine updateEngine = new UpdateEngine(delta -> deltaSum[0] += delta);

        updateEngine.setFps(10);
        updateEngine.setSpeed(2);
        updateEngine.setUpdating(true);

        Util.sleep(1050);

        updateEngine.setUpdating(false);

        Assert.assertEquals(2, deltaSum[0], 10E-10);
    }
}
