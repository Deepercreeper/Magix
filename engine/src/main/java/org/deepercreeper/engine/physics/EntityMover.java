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

    public void move(Set<Entity> entities, double delta)
    {
        this.entities.clear();
        this.entities.addAll(entities);
        this.delta = delta;
        move();
    }

    private void move()
    {
        if (entities.size() == 1)
        {
            moveSingleEntity();
            return;
        }
        splitter.split(entities);
        leftDelta = delta;
        initDelta();
        while (steps > 0)
        {
            collider.collide(entities, stepDelta);
            decreaseDelta();
            if (collider.hadCollisions())
            {
                initDelta();
            }
        }
    }

    private void decreaseDelta()
    {
        leftDelta -= stepDelta;
        if (leftDelta > 0)
        {
            steps--;
            if (leftDelta < stepDelta)
            {
                stepDelta = leftDelta;
            }
        }
        else
        {
            steps = 0;
        }
    }

    private void moveSingleEntity()
    {
        entities.forEach(entity -> entity.move(delta));
    }

    private void initDelta()
    {
        if (leftDelta <= 0)
        {
            return;
        }
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
