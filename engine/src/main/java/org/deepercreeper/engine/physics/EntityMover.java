package org.deepercreeper.engine.physics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class EntityMover
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityMover.class);

    private static final double MAX_STEP_VELOCITY = 1;

    private final EntityCollider entityCollider = new EntityCollider();

    private final EntitySplitter entitySplitter = new EntitySplitter();

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
        long timeStamp = System.currentTimeMillis();
        if (entities.size() == 1)
        {
            moveSingleEntity();
            LOGGER.debug("Moved single entity in {} ms", System.currentTimeMillis() - timeStamp);
            return;
        }
        int stepCounter = 0;
        leftDelta = delta;
        initDelta();
        while (steps > 0)
        {
            stepCounter++;
            entitySplitter.split(entities);
            entityCollider.collide(entities, stepDelta);
            moveEntities();
            decreaseDelta();
            if (entityCollider.hasCollisions())
            {
                initDelta();
            }
        }
        LOGGER.debug("Moved {} entities in {} steps and {} ms", entities.size(), stepCounter, System.currentTimeMillis() - timeStamp);
    }

    private void decreaseDelta()
    {
        leftDelta -= stepDelta;
        stepDelta = Math.min(leftDelta, stepDelta);
        steps--;
    }

    private void moveSingleEntity()
    {
        Entity entity = entities.iterator().next();
        entity.move(delta);
    }

    private void moveEntities()
    {
        entities.forEach(entity -> entity.move(stepDelta));
    }

    private void initDelta()
    {
        double maxVelocity = entities.stream().map(entity -> entity.getVelocity().times(entity.getSpeed() * delta).norm()).max(Double::compare).orElse(.0);
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
