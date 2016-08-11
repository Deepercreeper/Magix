package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PhysicsEntityUpdater
{
    private static final double MAX_STEP_VELOCITY = 1;

    private final Set<PhysicsEntity> entities = new HashSet<>();

    private Set<PhysicsEntity> collidedEntities = new HashSet<>();

    private double delta;

    private double leftDelta;

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
        if (entities.size() == 1)
        {
            updateSingleEntity();
            return;
        }
        leftDelta = delta;
        initDelta();
        while (steps > 0)
        {
            collidedEntities.clear();
            //TODO Tear apart
            collideEntities();
            moveEntities();
            leftDelta -= stepDelta;
            steps--;
            if (!collidedEntities.isEmpty())
            {
                initDelta();
            }
        }
    }

    private void updateSingleEntity()
    {
        PhysicsEntity entity = entities.iterator().next();
        entity.move(delta);
    }

    private void collideEntities()
    {
        Set<PhysicsEntity> collisionEntities = new HashSet<>(entities);
        Iterator<PhysicsEntity> iterator = collisionEntities.iterator();
        while (iterator.hasNext())
        {
            PhysicsEntity entity = iterator.next();
            iterator.remove();
            if (iterator.hasNext())
            {
                collideEntityWith(entity, collisionEntities);
            }
        }
    }

    private void collideEntityWith(PhysicsEntity entity, Set<PhysicsEntity> collisionEntities)
    {
        Box box = entity.getBox().shift(entity.getVelocity().times(stepDelta));
        for (PhysicsEntity collisionEntity : collisionEntities)
        {
            Box entityBox = collisionEntity.getBox().shift(collisionEntity.getVelocity().times(stepDelta));
            if (box.isTouching(entityBox))
            {
                entity.collideWith(collisionEntity);
                collidedEntities.add(entity);
                collidedEntities.add(collisionEntity);
            }
        }
    }

    private void moveEntities()
    {
        entities.stream().filter(entity -> !collidedEntities.contains(entity)).forEach(entity ->
                entity.move(stepDelta));
    }

    private void initDelta()
    {
        double maxVelocity = entities.stream().map(entity -> entity.getVelocity().norm())
                                     .max(Double::compare).orElse(.0);
        if (maxVelocity < MAX_STEP_VELOCITY)
        {
            stepDelta = leftDelta;
            steps = 1;
            return;
        }
        stepDelta = leftDelta * MAX_STEP_VELOCITY / maxVelocity;
        steps = (int) Math.ceil(leftDelta / stepDelta);
    }
}
