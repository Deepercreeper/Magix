package org.deepercreeper.engine.physics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PhysicsEntityUpdater
{
    private static final double MAX_STEP_VELOCITY = .1;

    private final Set<PhysicsEntity> entities = new HashSet<>();

    private boolean collided = false;

    private double delta;

    private double stepDelta;

    private int steps;

    public void setDelta(double delta)
    {
        this.delta = delta;
    }

    public void update(Set<PhysicsEntity> entities)
    {
        this.entities.clear();
        this.entities.addAll(entities);
        update();
    }

    private void update()
    {
        initDelta();
        while (steps > 0)
        {
            moveEntities();
            collideEntities();
            delta -= stepDelta;
            steps--;
            if (collided)
            {
                initDelta();
            }
        }
    }

    private void collideEntities()
    {
        Set<PhysicsEntity> collisionEntities = new HashSet<>(entities);
        Iterator<PhysicsEntity> iterator = collisionEntities.iterator();
        while (iterator.hasNext())
        {
            PhysicsEntity entity = iterator.next();
            iterator.remove();
            collideEntityWith(entity, collisionEntities);
        }
    }

    private void collideEntityWith(PhysicsEntity entity, Set<PhysicsEntity> collisionEntities)
    {
        collisionEntities.stream().filter(entity::isTouching).forEach(collisionEntity ->
        {
            entity.collideWith(collisionEntity);
            collided = true;
        });
    }

    private void moveEntities()
    {
        for (PhysicsEntity entity : entities)
        {
            entity.move(stepDelta);
        }
    }

    private void initDelta()
    {
        double maxVelocity = entities.stream().map(entity -> entity.getVelocity().norm())
                                     .max(Double::compare).orElse(.0);
        if (maxVelocity < MAX_STEP_VELOCITY)
        {
            stepDelta = delta;
            steps = 1;
            return;
        }
        stepDelta = delta * MAX_STEP_VELOCITY / maxVelocity;
        steps = (int) Math.round(delta / stepDelta);
    }
}
