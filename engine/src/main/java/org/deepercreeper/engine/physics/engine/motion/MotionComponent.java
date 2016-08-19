package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class MotionComponent
{
    private final Set<Entity> entities = new HashSet<>();

    private final Vector velocity = new Vector();

    private final double delta;

    public MotionComponent(double delta)
    {
        this.delta = delta;
    }

    public void add(Entity entity)
    {
        entities.add(entity);
        updateVelocity(entity.getVelocity());
    }

    private void updateVelocity(Vector velocity)
    {
        this.velocity.setX(Math.max(this.velocity.getX(), velocity.getAbsX()));
        this.velocity.setY(Math.max(this.velocity.getY(), velocity.getAbsY()));
    }

    public void consume(MotionComponent component)
    {
        entities.addAll(component.entities);
        updateVelocity(component.velocity);
    }

    public boolean isTouching(MotionComponent component)
    {
        for (Entity entity : entities)
        {
            for (Entity componentEntity : component.entities)
            {
                if (entity.isDistanceTouching(velocity, componentEntity, component.velocity, delta))
                {
                    return true;
                }
            }
        }
        return false;
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
        entity.update(delta);
        entity.updateProperties();
    }
}
