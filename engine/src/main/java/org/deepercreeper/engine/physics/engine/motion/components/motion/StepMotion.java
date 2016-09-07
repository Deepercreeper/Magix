package org.deepercreeper.engine.physics.engine.motion.components.motion;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.engine.motion.colliders.Collider;
import org.deepercreeper.engine.physics.engine.motion.splitters.Splitter;

public class StepMotion extends AbstractComponentMotion
{
    private static final double DEFAULT_MAX_STEP_DISTANCE = .1;

    private final double maxStepDistance;

    private final Collider collider;

    private final Splitter splitter;

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
    public void init()
    {
        collider.init(getEntities());
    }

    @Override
    public void move()
    {
        computeStepDelta();
        while (getDelta() > 0)
        {
            moveStep();
        }
        updateAccelerations();
    }

    private void moveStep()
    {
        collider.collide(stepDelta);
        splitter.split(getEntities());
        decreaseDelta(stepDelta);
        if (collider.changedVelocities())
        {
            computeStepDelta();
        }
    }

    private void computeStepDelta()
    {
        if (getDelta() <= 0)
        {
            return;
        }
        double maxDistance = getEntities().stream().map(entity -> entity.getVelocity().times(entity.getSpeed() * getDelta())
                                                                        .plus(entity.getAcceleration().times(.5 * entity.getSpeed() * entity.getSpeed() * getDelta() * getDelta()))
                                                                        .norm()).max(Double::compare).orElse(.0);
        if (maxDistance < maxStepDistance)
        {
            stepDelta = getDelta();
            return;
        }
        stepDelta = getDelta() * maxStepDistance / maxDistance;
    }

    private void updateAccelerations()
    {
        getEntities().forEach(Entity::updateProperties);
    }
}
