package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class MotionComponent
{
    private final Set<Entity> entities = new HashSet<>();

    private final Vector momentum = new Vector();

    private final Vector velocity = new Vector();

    private final double delta;

    public MotionComponent(double delta)
    {
        this.delta = delta;
    }

    public void add(Entity entity)
    {
        entities.add(entity);
        update(entity);
    }

    private void update(Entity entity)
    {
        if (Double.isFinite(entity.getMass()))
        {
            this.momentum.add(entity.getVelocity(), entity.getMass());
        }
        else
        {
            velocity.add(entity.getVelocity().absolute());
        }
    }

    public void consume(MotionComponent component)
    {
        entities.addAll(component.entities);
        update(component);
    }

    private void update(MotionComponent component)
    {
        momentum.add(component.momentum);
        velocity.add(component.velocity);
    }

    public boolean isTouching(MotionComponent component)
    {
        for (Entity entity : entities)
        {
            for (Entity componentEntity : component.entities)
            {
                if (entity.isDistanceTouching(getMaxVelocity(entity), componentEntity, component.getMaxVelocity(componentEntity), delta))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private Vector getMaxVelocity(Entity entity)
    {
        if (Double.isFinite(entity.getMass()))
        {
            return momentum.times(2 / entity.getMass()).plus(velocity);
        }
        return entity.getVelocity();
    }

    public void move()
    {
        if (entities.size() == 1)
        {
            moveSingle();
            return;
        }
        new StepMotion(entities, delta).move();
    }

    private void moveSingle()
    {
        Entity entity = entities.iterator().next();
        entity.updateAll(delta);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof MotionComponent)
        {
            MotionComponent component = (MotionComponent) obj;
            return entities.equals(component.entities);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return entities.hashCode();
    }

    @Override
    public String toString()
    {
        return entities.toString();
    }
}
