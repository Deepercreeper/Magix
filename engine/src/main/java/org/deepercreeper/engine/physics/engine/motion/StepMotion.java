package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;

import java.util.Set;

public class StepMotion
{
    private static final double MAX_STEP_DISTANCE = .1;

    private final Splitter splitter = new Splitter();

    private final Collider collider;

    private final Set<Entity> entities;

    private double delta;

    private double stepDelta;

    public StepMotion(Set<Entity> entities)
    {
        this.entities = entities;
        collider = new Collider(entities);
    }

    public void move(double delta)
    {
        this.delta = delta;
        computeStepDelta();
        while (this.delta > 0)
        {
            moveStep();
        }
        updateAccelerations();
    }

    private void moveStep()
    {
        collider.collide(stepDelta);
        splitter.split(entities);
        decreaseDelta();
        if (collider.hadCollisions())
        {
            computeStepDelta();
        }
    }

    private void decreaseDelta()
    {
        delta -= stepDelta;
    }

    private void computeStepDelta()
    {
        if (delta <= 0)
        {
            return;
        }
        double maxDistance = entities.stream().map(entity -> entity.getVelocity().times(entity.getSpeed() * delta)
                                                                   .plus(entity.getAcceleration().times(.5 * entity.getSpeed() * entity.getSpeed() * delta * delta)).norm())
                                     .max(Double::compare).orElse(.0);
        if (maxDistance < MAX_STEP_DISTANCE)
        {
            stepDelta = delta;
            return;
        }
        stepDelta = delta * MAX_STEP_DISTANCE / maxDistance;
    }

    private void updateAccelerations()
    {
        entities.forEach(Entity::updateProperties);
    }
}