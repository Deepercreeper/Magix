package org.deepercreeper.engine.physics.engine.motion.colliders;

import org.deepercreeper.engine.physics.Entity;

import java.util.Set;

public abstract class AbstractCollider implements Collider
{
    private Set<Entity> entities;

    private double delta;

    private boolean changedVelocities;

    @Override
    public final void init(Set<Entity> entities)
    {
        this.entities = entities;
    }

    @Override
    public final boolean changedVelocities()
    {
        return changedVelocities;
    }

    @Override
    public final void collide(double delta)
    {
        this.delta = delta;
        changedVelocities = false;
        collide();
    }

    public final Set<Entity> getEntities()
    {
        return entities;
    }

    public final double getDelta()
    {
        return delta;
    }

    protected final void velocitiesChanged()
    {
        changedVelocities = true;
    }

    protected final void decreaseDeltaBy(double delta)
    {
        this.delta -= delta;
    }

    protected abstract void collide();
}
