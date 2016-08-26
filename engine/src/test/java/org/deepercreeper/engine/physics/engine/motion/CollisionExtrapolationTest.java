package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.engine.motion.collisions.CollisionExtrapolation;
import org.junit.Assert;
import org.junit.Test;

public class CollisionExtrapolationTest
{
    @Test
    public void testValidDelta()
    {
        Entity firstEntity = new Entity.EntityBuilder().setX(-1).setWidth(1).setHeight(1).setXVelocity(Math.random() * 5).setXAcceleration(Math.random() * 5).build();
        Entity secondEntity = new Entity.EntityBuilder().setX(Math.random() * 5 + 1).setWidth(Math.random() * 5 + 1).setHeight(1).setXVelocity(-Math.random())
                                                        .setXAcceleration(-Math.random()).build();
        CollisionExtrapolation collision = new CollisionExtrapolation(firstEntity, secondEntity);
        collision.computeDelta();
        double delta = collision.getDelta();
        assertDeltaValid(firstEntity, secondEntity, delta);
    }

    @Test
    public void testHorizontalDelta()
    {
        Entity firstEntity = new Entity.EntityBuilder().setX(-1).setWidth(1).setHeight(1).setXVelocity(Math.random() * 5).setXAcceleration(Math.random() * 5).build();
        Entity secondEntity = new Entity.EntityBuilder().setX(Math.random() * 5 + 1).setWidth(Math.random() * 5 + 1).setHeight(1).setXVelocity(-Math.random())
                                                        .setXAcceleration(-Math.random()).build();
        CollisionExtrapolation collision = new CollisionExtrapolation(firstEntity, secondEntity);
        collision.computeDelta();
        double delta = collision.getDelta();
        assertDeltaValid(firstEntity, secondEntity, delta);
        double firstX = firstEntity.getMaxX() + firstEntity.getXVelocity() * delta + .5 * firstEntity.getXAcceleration() * delta * delta;
        double secondX = secondEntity.getX() + secondEntity.getXVelocity() * delta + .5 * secondEntity.getXAcceleration() * delta * delta;

        Assert.assertEquals(firstX, secondX, 10E-5);
    }

    @Test
    public void testVerticalDelta()
    {
        Entity firstEntity = new Entity.EntityBuilder().setY(-1).setWidth(1).setHeight(1).setYVelocity(Math.random() * 5).setYAcceleration(Math.random() * 5).build();
        Entity secondEntity = new Entity.EntityBuilder().setY(Math.random() * 5 + 1).setWidth(Math.random() * 5 + 1).setHeight(1).setYVelocity(-Math.random())
                                                        .setYAcceleration(-Math.random()).build();
        CollisionExtrapolation collision = new CollisionExtrapolation(firstEntity, secondEntity);
        collision.computeDelta();
        double delta = collision.getDelta();
        assertDeltaValid(firstEntity, secondEntity, delta);
        double firstY = firstEntity.getMaxY() + firstEntity.getYVelocity() * delta + .5 * firstEntity.getYAcceleration() * delta * delta;
        double secondY = secondEntity.getY() + secondEntity.getYVelocity() * delta + .5 * secondEntity.getYAcceleration() * delta * delta;

        Assert.assertEquals(firstY, secondY, 10E-5);
    }

    @Test
    public void testOptimizedDelta()
    {
        Entity firstEntity = new Entity.EntityBuilder().setX(-1).setWidth(1).setHeight(1).setXVelocity(Math.random() * 5).setXAcceleration(Math.random() * 5).build();
        Entity secondEntity = new Entity.EntityBuilder().setX(Math.random() * 5 + 1).setWidth(Math.random() * 5 + 1).setHeight(1).setXVelocity(-Math.random())
                                                        .setXAcceleration(-Math.random()).build();
        CollisionExtrapolation collision = new CollisionExtrapolation(firstEntity, secondEntity);
        collision.computeDelta();
        collision.optimizeDelta();
        double delta = collision.getDelta();
        assertDeltaValid(firstEntity, secondEntity, delta);

        Assert.assertFalse(firstEntity.isDeltaTouching(secondEntity, delta));
    }

    @Test
    public void testHorizontalDetection()
    {
        directionDetection(-.1);
        directionDetection(.1);
    }

    private void directionDetection(double offset)
    {
        double v1 = Math.random();
        double a1 = Math.random();
        double v2 = -Math.random();
        double a2 = -Math.random();
        Entity firstEntity = new Entity.EntityBuilder().setX(offset).setY(-offset).setWidth(1).setHeight(1).setXVelocity(v1).setYVelocity(v1).setXAcceleration(a1)
                                                       .setYAcceleration(a1).build();
        Entity secondEntity = new Entity.EntityBuilder().setX(2).setY(2).setWidth(1).setHeight(1).setXVelocity(v2).setYVelocity(v2).setXAcceleration(a2).setYAcceleration(a2)
                                                        .build();
        CollisionExtrapolation collision = new CollisionExtrapolation(firstEntity, secondEntity);
        collision.computeDelta();
        assertDeltaValid(firstEntity, secondEntity, collision.getDelta());

        Assert.assertEquals(offset < 0, collision.isHorizontal());
    }

    private void assertDeltaValid(Entity firstEntity, Entity secondEntity, double delta)
    {
        Assert.assertTrue(Double.isFinite(delta) && delta >= 0);
        Assert.assertTrue(firstEntity.isDeltaTouching(secondEntity, delta + 10E-5));
    }
}
