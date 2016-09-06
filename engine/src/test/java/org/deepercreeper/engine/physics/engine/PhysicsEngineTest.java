package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.physics.Force;
import org.deepercreeper.engine.util.Vector;
import org.junit.Assert;
import org.junit.Test;

public class PhysicsEngineTest
{
    @Test
    public void testForceAddition()
    {
        PhysicsEngine physicsEngine = new PhysicsEngine();
        physicsEngine.add(entity -> new Vector(0, 9.81 / entity.getMass()));

        Assert.assertTrue(physicsEngine.getForces().isEmpty());

        physicsEngine.update(1);

        Assert.assertFalse(physicsEngine.getForces().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddition()
    {
        PhysicsEngine physicsEngine = new PhysicsEngine();

        physicsEngine.getForces().add(entity -> new Vector(0, 9.81 / entity.getMass()));
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalRemoval()
    {
        PhysicsEngine physicsEngine = new PhysicsEngine();
        Force force = entity -> new Vector(0, 9.81 / entity.getMass());
        physicsEngine.add(force);

        physicsEngine.update(1);

        physicsEngine.getForces().remove(force);
    }
}
