package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Vector;
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
        Entity firstEntity = new Entity(0, 0)
        {
            @Override
            public double getMass()
            {
                return massesAndScale[0];
            }
        };
        Entity secondEntity = new Entity(0, 0)
        {
            @Override
            public double getMass()
            {
                return massesAndScale[1];
            }
        };
        for (double[] testData : TEST_DATA)
        {
            System.arraycopy(testData, 0, massesAndScale, 0, 3);
            Assert.assertEquals(massesAndScale[2], firstEntity.getMassScaleTo(secondEntity), .01);
        }
    }
}
