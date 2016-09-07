package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.engine.motion.strategies.AbstractMotionStrategy;
import org.deepercreeper.engine.physics.engine.motion.strategies.MotionStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class MotionEngineTest
{
    @Test
    public void testSolidUpdate()
    {
        boolean[] invocations = new boolean[2];

        EntityEngine entityEngine = new EntityEngine(new Engine());
        MotionEngine motionEngine = new MotionEngine(entityEngine);

        entityEngine.add(new Entity.EntityBuilder().build());
        entityEngine.add(new Entity.EntityBuilder().setSolid(false).build());
        entityEngine.update(1);

        motionEngine.setStrategy(new MotionStrategy()
        {
            @Override
            public void init(Set<Entity> entities)
            {
                invocations[0] = true;
                Assert.assertEquals(entityEngine.getSolidEntities(), entities);
            }

            @Override
            public void update(double delta)
            {
                invocations[1] = true;
            }
        });

        motionEngine.update(1);

        Assert.assertTrue(invocations[0]);
        Assert.assertTrue(invocations[1]);
    }

    @Test
    public void testNonSolidUpdate()
    {
        EntityEngine entityEngine = new EntityEngine(new Engine());
        MotionEngine motionEngine = new MotionEngine(entityEngine);

        Entity solidEntity = new Entity.EntityBuilder().setXVelocity(1).build();
        Entity nonSolidEntity = new Entity.EntityBuilder().setXVelocity(1).setSolid(false).build();

        entityEngine.add(solidEntity);
        entityEngine.add(nonSolidEntity);
        entityEngine.update(1);

        motionEngine.setStrategy(new AbstractMotionStrategy()
        {
            @Override
            protected void update()
            {
            }
        });
        motionEngine.update(1);

        Assert.assertEquals(1, nonSolidEntity.getX(), 0);
        Assert.assertEquals(0, solidEntity.getX(), 0);
    }

    @Test
    public void testPause()
    {
        boolean[] invocation = new boolean[1];

        EntityEngine entityEngine = new EntityEngine(new Engine());
        MotionEngine motionEngine = new MotionEngine(entityEngine);

        entityEngine.add(new Entity.EntityBuilder().build());
        entityEngine.add(new Entity.EntityBuilder().setSolid(false).build());
        entityEngine.update(1);

        motionEngine.setStrategy(new AbstractMotionStrategy()
        {
            @Override
            protected void update()
            {
                invocation[0] = true;
            }
        });

        motionEngine.setPause(true);
        motionEngine.update(1);

        Assert.assertFalse(invocation[0]);
    }
}
