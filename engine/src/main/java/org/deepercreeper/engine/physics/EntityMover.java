package org.deepercreeper.engine.physics;

import java.util.HashSet;
import java.util.Set;

public class EntityMover
{
    private static final double MAX_STEP_DISTANCE = 1;

    private final EntityCollider collider = new EntityCollider();

    private final EntitySplitter splitter = new EntitySplitter();

    private final Set<Entity> entities = new HashSet<>();

    private double delta;

    private double leftDelta;

    private double stepDelta;

    private int steps;

    public void setDelta(double delta)
    {
        this.delta = delta;
    }

    public void move(Set<Entity> entities)
    {
        this.entities.clear();
        this.entities.addAll(entities);
        move();
    }

    private void move()
    {
        if (entities.size() == 1)
        {
            moveSingleEntity();
            return;
        }
        leftDelta = delta;
        initDelta();
        while (steps > 0)
        {
            splitter.split(entities);
            collider.collide(entities, stepDelta);
            moveEntities();
            decreaseDelta();
            if (collider.hasCollisions())
            {
                initDelta();
            }
        }
    }

    private void decreaseDelta()
    {
        leftDelta -= stepDelta;
        steps--;
        if (leftDelta < stepDelta)
        {
            stepDelta = leftDelta;
            steps = 1;
        }
    }

    private void moveSingleEntity()
    {
        entities.forEach(entity -> entity.move(delta));
    }

    private void moveEntities()
    {
        entities.forEach(entity -> entity.move(stepDelta));
    }

    private void initDelta()
    {
        double maxDistance = entities.stream().map(entity -> entity.getVelocity().times(entity.getSpeed() * delta).norm()).max(Double::compare).orElse(.0);
        if (maxDistance < MAX_STEP_DISTANCE)
        {
            stepDelta = leftDelta;
            steps = 1;
            return;
        }
        stepDelta = leftDelta * MAX_STEP_DISTANCE / maxDistance;
        steps = (int) Math.ceil(leftDelta / stepDelta);
    }
}
