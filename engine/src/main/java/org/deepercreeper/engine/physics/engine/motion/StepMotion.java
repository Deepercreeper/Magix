package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;

import java.util.Set;

public class StepMotion
{
    private static final double MAX_STEP_DISTANCE = .1;

    private final Set<Entity> entities;

    private final Collider collider;

    private double delta;

    private double stepDelta;

    public StepMotion(Set<Entity> entities, double delta)
    {
        this.entities = entities;
        this.delta = delta;
        collider = new Collider(entities);
    }

    public void move()
    {
        computeStepDelta();
        while (delta > 0)
        {
            moveStep();
        }
        updateAccelerations();
    }

    private void moveStep()
    {
        collider.collide(stepDelta);
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
