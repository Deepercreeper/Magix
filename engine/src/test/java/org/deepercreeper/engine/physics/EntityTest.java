package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.junit.Assert;
import org.junit.Test;

public class EntityTest
{
    private static final double[][] TEST_DATA = new double[][]{
            {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, .5},
            {Double.POSITIVE_INFINITY, 1, 1},
            {1, Double.POSITIVE_INFINITY, 0},
            {1, 1000, 0},
            {1, 4, .2},
            {1, 2, .33},
            {1, 1.5, .4},
            {1, 1, .5},
            {1.5, 1, .6},
            {2, 1, .66},
            {4, 1, .8},
            {1000, 1, 1},
            };

    @Test
    public void testMassScale()
    {
        double[] massesAndScale = new double[3];
        Entity firstEntity = new Entity.EntityBuilder().build();
        Entity secondEntity = new Entity.EntityBuilder().build();
        for (double[] testData : TEST_DATA)
        {
            firstEntity.setMass(testData[0]);
            secondEntity.setMass(testData[1]);
            System.arraycopy(testData, 0, massesAndScale, 0, 3);
            Assert.assertEquals(massesAndScale[2], firstEntity.getMassScaleTo(secondEntity), .01);
        }
    }

    @Test
    public void testDeltaBox()
    {
        Entity entity = new Entity.EntityBuilder().setXVelocity(Math.random()).setX(Math.random()).setXAcceleration(Math.random()).build();
        double delta = Math.random();
        Box deltaBox = entity.getDeltaBox(delta);
        entity.update(delta);
        Assert.assertEquals(deltaBox.getX(), entity.getX(), 10E-20);
    }
}
