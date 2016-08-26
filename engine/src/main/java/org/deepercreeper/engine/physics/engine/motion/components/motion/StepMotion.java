package org.deepercreeper.engine.physics.engine.motion.components.motion;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.engine.motion.colliders.Collider;
import org.deepercreeper.engine.physics.engine.motion.splitters.Splitter;

import java.util.Set;

public class StepMotion implements ComponentMotion
{
    private static final double DEFAULT_MAX_STEP_DISTANCE = .1;

    private final double maxStepDistance;

    private final Collider collider;

    private final Splitter splitter;

    private Set<Entity> entities;

    private double delta;

    private double stepDelta;

    public StepMotion(double maxStepDistance, Splitter splitter, Collider collider)
    {
        this.maxStepDistance = maxStepDistance;
        this.splitter = splitter;
        this.collider = collider;
    }

    public StepMotion(Splitter splitter, Collider collider)
    {
        this(DEFAULT_MAX_STEP_DISTANCE, splitter, collider);
    }

    @Override
    public void init(Set<Entity> entities)
    {
        this.entities = entities;
        collider.init(entities);
    }

    @Override
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
        if (collider.changedVelocities())
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
        if (maxDistance < maxStepDistance)
        {
            stepDelta = delta;
            return;
        }
        stepDelta = delta * maxStepDistance / maxDistance;
    }

    private void updateAccelerations()
    {
        entities.forEach(Entity::updateProperties);
    }
}
