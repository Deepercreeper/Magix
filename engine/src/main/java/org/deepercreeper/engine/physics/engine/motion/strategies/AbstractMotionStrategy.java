package org.deepercreeper.engine.physics.engine.motion.strategies;

import org.deepercreeper.engine.physics.Entity;

import java.util.Set;

public abstract class AbstractMotionStrategy implements MotionStrategy
{
    private Set<Entity> entities;

    private double delta;

    @Override
    public final void init(Set<Entity> entities)
    {
        this.entities = entities;
        init();
    }

    public final Set<Entity> getEntities()
    {
        return entities;
    }

    public final double getDelta()
    {
        return delta;
    }

    @Override
    public final void update(double delta)
    {
        this.delta = delta;
        update();
    }

    protected abstract void update();

    protected void init()
    {
    }
}
