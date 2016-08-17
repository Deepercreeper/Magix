package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;

import java.util.*;

public class EntityCollider
{
    private final EntitySplitter entitySplitter = new EntitySplitter();

    private final Set<Entity> entities = new HashSet<>();

    private final Set<Collision> collisions = new HashSet<>();

    private double delta;

    private boolean hadCollisions;

    public void collide(Set<Entity> entities, double delta)
    {
        this.delta = delta;
        this.entities.clear();
        this.entities.addAll(entities);
        hadCollisions = false;
        collide();
    }

    private void collide()
    {
        while (delta > 0)
        {
            checkCollisions();
            removeZeroDeltas();
            double minDelta = computeMinDelta();
            Set<Collision> minCollisions = getDeltaCollisions(minDelta);
            moveEntities(minDelta);
            collideCollisions(minCollisions);
            delta -= minDelta;
        }
    }

    private void moveEntities(double delta)
    {
        for (Entity entity : entities)
        {
            entity.move(delta);
            entitySplitter.split(entities);
        }
    }

    private void removeZeroDeltas()
    {
        Iterator<Collision> iterator = collisions.iterator();
        while (iterator.hasNext())
        {
            Collision collision = iterator.next();
            if (collision.getDelta() == 0)
            {
                collision.collide();
                iterator.remove();
            }
        }
    }

    private void collideCollisions(Set<Collision> collisions)
    {
        collisions.forEach(Collision::collide);
    }

    private double computeMinDelta()
    {
        return collisions.stream().map(Collision::getDelta).min(Double::compare).orElse(delta);
    }

    private Set<Collision> getDeltaCollisions(double delta)
    {
        Set<Collision> minCollisions = new HashSet<>(collisions);
        Iterator<Collision> iterator = minCollisions.iterator();
        while (iterator.hasNext())
        {
            if (iterator.next().getDelta() != delta)
            {
                iterator.remove();
            }
        }
        return minCollisions;
    }

    private void checkCollisions()
    {
        collisions.clear();
        Set<Entity> entities = new HashSet<>(this.entities);
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext())
        {
            Entity entity = iterator.next();
            iterator.remove();
            if (iterator.hasNext())
            {
                checkCollisions(entity, entities);
            }
        }
    }

    private void checkCollisions(Entity entity, Set<Entity> entities)
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
            hadCollisions = true;
        }
    }

    public boolean hadCollisions()
    {
        return hadCollisions;
    }
}
