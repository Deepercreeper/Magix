package org.deepercreeper.engine.physics.engine.motion.components.motion;

import org.deepercreeper.engine.physics.Entity;

import java.util.Set;

public abstract class AbstractComponentMotion implements ComponentMotion
{
    private Set<Entity> entities;

    private double delta;

    @Override
    public final void init(Set<Entity> entities)
    {
        this.entities = entities;
        init();
    }

    @Override
    public final void move(double delta)
    {
        this.delta = delta;
        move();
    }

    protected final Set<Entity> getEntities()
    {
        return entities;
    }

    protected final double getDelta()
    {
        return delta;
    }

    protected final void decreaseDelta(double amount)
    {
        delta -= amount;
    }

    protected void init()
    {
    }

    protected abstract void move();
}
