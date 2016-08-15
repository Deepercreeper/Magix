package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Vector;

import java.util.*;

public class EntityDivider
{
    private final Set<Entity> entities = new HashSet<>();

    private final Map<Set<Entity>, Vector> dividedEntities = new HashMap<>();

    private double delta;

    public Set<Set<Entity>> divide(Set<Entity> entities, double delta)
    {
        this.entities.clear();
        dividedEntities.clear();
        this.entities.addAll(entities);
        this.delta = delta;

        divide();

        return dividedEntities.keySet();
    }

    private void divide()
    {
        entities.forEach(this::divide);
    }

    private void divide(Entity entity)
    {
        Set<Entity> component = new HashSet<>();
        Vector velocity = entity.getVelocity().absolute();
        component.add(entity);
        boolean componentAdded = true;
        while (componentAdded)
        {
            componentAdded = addConnectionComponents(component, velocity);
        }
        component.add(entity);
        dividedEntities.put(component, velocity);
    }

    private boolean addConnectionComponents(Set<Entity> component, Vector velocity)
    {
        boolean addedComponent = false;
        Iterator<Map.Entry<Set<Entity>, Vector>> componentIterator = dividedEntities.entrySet().iterator();
        while (componentIterator.hasNext())
        {
            Map.Entry<Set<Entity>, Vector> entry = componentIterator.next();
            Set<Entity> connectionComponent = entry.getKey();
            Vector componentVelocity = entry.getValue();

            if (isComponentsTouching(component, velocity, connectionComponent, componentVelocity))
            {
                component.addAll(connectionComponent);
                velocity.setX(Math.max(velocity.getX(), componentVelocity.getAbsX()));
                velocity.setY(Math.max(velocity.getY(), componentVelocity.getAbsY()));
                componentIterator.remove();
                addedComponent = true;
            }
        }
        return addedComponent;
    }

    private boolean isComponentsTouching(Set<Entity> firstComponent, Vector firstVelocity, Set<Entity> secondComponent, Vector secondVelocity)
    {
        for (Entity firstEntity : firstComponent)
        {
            for (Entity secondEntity : secondComponent)
            {
                if (firstEntity.isDistanceTouching(firstVelocity, secondEntity, secondVelocity, delta))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
