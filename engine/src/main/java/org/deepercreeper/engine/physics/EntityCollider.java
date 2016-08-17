package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EntityCollider
{
    private final Set<Entity> entities = new HashSet<>();

    private final Set<Collision> collisions = new HashSet<>();

    private double delta;

    public void collide(Set<Entity> entities, double delta)
    {
        this.delta = delta;
        this.entities.clear();
        this.entities.addAll(entities);
        collisions.clear();
        checkCollisions();
        collide();
    }

    private void checkCollisions()
    {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext())
        {
            Entity entity = iterator.next();
            iterator.remove();
            if (iterator.hasNext())
            {
                checkCollisions(entity);
            }
        }
    }

    private void checkCollisions(Entity entity)
    {
        Box box = entity.getDeltaBox(delta);
        for (Entity collisionEntity : entities)
        {
            checkCollision(entity, box, collisionEntity);
        }
    }

    private void checkCollision(Entity firstEntity, Box box, Entity secondEntity)
    {
        if (box.isTouching(secondEntity.getDeltaBox(delta)))
        {
            collisions.add(new Collision(firstEntity, secondEntity));
        }
    }

    private void collide()
    {
        collisions.forEach(Collision::collide);
    }

    public boolean hasCollisions()
    {
        return !collisions.isEmpty();
    }
}
