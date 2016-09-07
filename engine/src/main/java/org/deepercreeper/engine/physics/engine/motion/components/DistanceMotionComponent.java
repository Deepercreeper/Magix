package org.deepercreeper.engine.physics.engine.motion.components;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.engine.motion.components.motion.ComponentMotion;
import org.deepercreeper.engine.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class DistanceMotionComponent implements MotionComponent<DistanceMotionComponent>
{
    private final Set<Entity> entities = new HashSet<>();

    private final ComponentMotion motion;

    private final Vector momentum = new Vector();

    private final Vector velocity = new Vector();

    private final double delta;

    private Runnable finishAction;

    public DistanceMotionComponent(ComponentMotion motion, double delta)
    {
        this.motion = motion;
        this.delta = delta;
        motion.init(entities);
    }

    @Override
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

    @Override
    public void consume(DistanceMotionComponent component)
    {
        entities.addAll(component.entities);
        update(component);
    }

    private void update(DistanceMotionComponent component)
    {
        momentum.add(component.momentum);
        velocity.add(component.velocity);
    }

    @Override
    public boolean isTouching(DistanceMotionComponent component)
    {
        for (Entity entity : entities)
        {
            for (Entity componentEntity : component.entities)
            {
                boolean canTouch = entity.canTouch(componentEntity) || componentEntity.canTouch(entity);
                boolean isTouching = entity.isDistanceTouching(getMaxVelocity(entity), componentEntity, component.getMaxVelocity(componentEntity), delta);
                if (canTouch && isTouching)
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void init(Runnable finishAction)
    {
        this.finishAction = finishAction;
    }

    @Override
    public void run()
    {
        if (entities.size() == 1)
        {
            moveSingle();
        }
        else
        {
            motion.move(delta);
        }
        finishAction.run();
        finishAction = null;
    }

    private void moveSingle()
    {
        Entity entity = entities.iterator().next();
        entity.updateAll(delta);
    }

    Vector getMaxVelocity(Entity entity)
    {
        if (Double.isFinite(entity.getMass()))
        {
            return momentum.times(2 / entity.getMass()).plus(velocity);
        }
        return entity.getVelocity();
    }

    Vector getVelocity()
    {
        return velocity;
    }

    Vector getMomentum()
    {
        return momentum;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof DistanceMotionComponent)
        {
            DistanceMotionComponent component = (DistanceMotionComponent) obj;
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
