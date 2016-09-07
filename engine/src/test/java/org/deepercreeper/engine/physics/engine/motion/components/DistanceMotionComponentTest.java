package org.deepercreeper.engine.physics.engine.motion.components;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.engine.motion.components.motion.AbstractComponentMotion;
import org.deepercreeper.engine.util.Vector;
import org.junit.Assert;
import org.junit.Test;

public class DistanceMotionComponentTest
{
    @Test
    public void testFiniteAddition()
    {
        DistanceMotionComponent motionComponent = new DistanceMotionComponent(new AbstractComponentMotion()
        {
            @Override
            protected void move()
            {}
        }, 1);

        motionComponent.add(new Entity.EntityBuilder().setXVelocity(2).setMass(.5).build());

        Assert.assertEquals(new Vector(1, 0), motionComponent.getMomentum());
        Assert.assertEquals(new Vector(0, 0), motionComponent.getVelocity());
    }

    @Test
    public void testInfiniteAddition()
    {
        DistanceMotionComponent motionComponent = new DistanceMotionComponent(new AbstractComponentMotion()
        {
            @Override
            protected void move()
            {}
        }, 1);

        motionComponent.add(new Entity.EntityBuilder().setXVelocity(1).setMass(Double.POSITIVE_INFINITY).build());

        Assert.assertEquals(new Vector(0, 0), motionComponent.getMomentum());
        Assert.assertEquals(new Vector(1, 0), motionComponent.getVelocity());
    }

    @Test
    public void testMaxVelocity()
    {
        DistanceMotionComponent motionComponent = new DistanceMotionComponent(new AbstractComponentMotion()
        {
            @Override
            protected void move()
            {}
        }, 1);

        Entity finiteEntity = new Entity.EntityBuilder().setXVelocity(2).setMass(.5).build();
        Entity infiniteEntity = new Entity.EntityBuilder().setXVelocity(1).setMass(Double.POSITIVE_INFINITY).build();

        motionComponent.add(finiteEntity);
        motionComponent.add(infiniteEntity);

        Assert.assertEquals(new Vector(5, 0), motionComponent.getMaxVelocity(finiteEntity));
        Assert.assertEquals(new Vector(1, 0), motionComponent.getMaxVelocity(infiniteEntity));
    }

    @Test
    public void testConsumption()
    {
        DistanceMotionComponent finiteComponent = new DistanceMotionComponent(new AbstractComponentMotion()
        {
            @Override
            protected void move()
            {}
        }, 1);
        DistanceMotionComponent infiniteComponent = new DistanceMotionComponent(new AbstractComponentMotion()
        {
            @Override
            protected void move()
            {}
        }, 1);

        finiteComponent.add(new Entity.EntityBuilder().setXVelocity(2).setMass(.5).build());
        infiniteComponent.add(new Entity.EntityBuilder().setXVelocity(1).setMass(Double.POSITIVE_INFINITY).build());

        finiteComponent.consume(infiniteComponent);

        Assert.assertEquals(new Vector(1, 0), finiteComponent.getMomentum());
        Assert.assertEquals(new Vector(1, 0), finiteComponent.getVelocity());
    }

    @Test
    public void testSingleMove()
    {
        DistanceMotionComponent motionComponent = new DistanceMotionComponent(new AbstractComponentMotion()
        {
            @Override
            protected void move()
            {}
        }, 1);

        Entity entity = new Entity.EntityBuilder().setXVelocity(1).build();

        motionComponent.add(entity);
        boolean[] finished = new boolean[1];

        motionComponent.init(() -> finished[0] = true);
        motionComponent.run();

        Assert.assertEquals(1, entity.getX(), 0);
        Assert.assertTrue(finished[0]);
    }

    @Test
    public void testMultipleMove()
    {
        boolean[] moved = new boolean[2];

        DistanceMotionComponent motionComponent = new DistanceMotionComponent(new AbstractComponentMotion()
        {
            @Override
            protected void move()
            {
                moved[0] = true;
            }
        }, 1);

        motionComponent.add(new Entity.EntityBuilder().setXVelocity(1).setWidth(1).build());
        motionComponent.add(new Entity.EntityBuilder().setX(2).setWidth(1).setXVelocity(-1).build());


        motionComponent.init(() -> moved[1] = true);
        motionComponent.run();

        Assert.assertTrue(moved[0]);
        Assert.assertTrue(moved[1]);
    }
}
