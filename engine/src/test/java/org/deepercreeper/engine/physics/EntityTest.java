package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.junit.Assert;
import org.junit.Test;

public class EntityTest
{
    @Test
    public void testInfiniteMassScale()
    {
        Entity firstEntity = new Entity.EntityBuilder().build();
        Entity secondEntity = new Entity.EntityBuilder().build();

        firstEntity.setMass(Double.POSITIVE_INFINITY);
        secondEntity.setMass(1);

        Assert.assertEquals(1, firstEntity.getMassScaleTo(secondEntity), 0);
        Assert.assertEquals(0, secondEntity.getMassScaleTo(firstEntity), 0);

        secondEntity.setMass(Double.POSITIVE_INFINITY);

        Assert.assertEquals(.5, firstEntity.getMassScaleTo(secondEntity), 0);
    }

    @Test
    public void testFiniteMassScale()
    {
        Entity firstEntity = new Entity.EntityBuilder().build();
        Entity secondEntity = new Entity.EntityBuilder().build();

        firstEntity.setMass(100);
        secondEntity.setMass(1);

        Assert.assertTrue(firstEntity.getMassScaleTo(secondEntity) > .5);
        Assert.assertTrue(secondEntity.getMassScaleTo(firstEntity) < .5);

        secondEntity.setMass(100);

        Assert.assertEquals(.5, firstEntity.getMassScaleTo(secondEntity), 0);
    }

    @Test
    public void testDeltaBox()
    {
        Entity entity = new Entity.EntityBuilder().setXVelocity(Math.random()).setX(Math.random()).setXAcceleration(Math.random()).build();
        double delta = Math.random();
        Box deltaBox = entity.getDeltaBox(delta);

        entity.move(delta);

        Assert.assertEquals(entity.getX(), deltaBox.getX(), 0);
    }

    @Test
    public void testDeltaVelocity()
    {
        Entity entity = new Entity.EntityBuilder().setXVelocity(Math.random()).setX(Math.random()).setXAcceleration(Math.random()).build();
        double delta = Math.random();
        double deltaVelocity = entity.getDeltaVelocity(delta).getX();

        entity.accelerateX(delta);

        Assert.assertEquals(entity.getXVelocity(), deltaVelocity, 0);
    }
}
