package org.deepercreeper.engine.physics.engine.motion.strategies;

import org.deepercreeper.engine.physics.engine.motion.components.motion.ComponentMotion;

public class BruteForceStrategy extends AbstractMotionStrategy
{
    private final ComponentMotion motion;

    public BruteForceStrategy(ComponentMotion motion)
    {
        this.motion = motion;
    }

    @Override
    protected void init()
    {
        motion.init(getEntities());
    }

    @Override
    protected void update()
    {
        motion.move(getDelta());
    }
}
