package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Box;

import java.util.*;

public class Collider
{
    private final Set<Collision> collisions = new HashSet<>();

    private final Set<Collision> minCollisions = new HashSet<>();

    private final Set<Collision> instantCollisions = new HashSet<>();

    private final Set<Entity> entities;

    private boolean hadCollisions;

    private double delta;

    private double minDelta;

    public Collider(Set<Entity> entities)
    {
        this.entities = entities;
    }

    public void collide(double delta)
    {
        this.delta = delta;
        hadCollisions = false;
        collide();
    }

    private void collide()
    {
        while (delta > 0)
        {
            computeCollisions();
            doInstantCollisions();
            computeMinDelta();
            computeMinCollisions();
            moveEntities();
            doMinCollisions();
            delta -= minDelta;
        }
    }

    private void computeCollisions()
    {
        collisions.clear();
        instantCollisions.clear();
        List<Entity> entities = new ArrayList<>(this.entities);
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext())
        {
            Entity entity = iterator.next();
            iterator.remove();
            if (iterator.hasNext())
            {
                computeCollisions(entity, entities);
            }
        }
    }

    private void computeCollisions(Entity entity, List<Entity> entities)
    {
        Box deltaBox = entity.getDeltaBox(delta);
        for (Entity collisionEntity : entities)
        {
            if (deltaBox.isTouching(collisionEntity.getDeltaBox(delta)))
            {
                addCollision(entity, collisionEntity);
            }
        }
    }

    private void addCollision(Entity firstEntity, Entity secondEntity)
    {
        Collision collision = new Collision(firstEntity, secondEntity);
        collision.computeDelta();
        collision.optimizeDelta();
        if (collision.isInstant())
        {
            collision.computeVelocity();
            instantCollisions.add(collision);
        }
        else
        {
            collisions.add(collision);
        }
        hadCollisions = true;
    }

    private void doInstantCollisions()
    {
        for (Collision collision : instantCollisions)
        {
            collision.collide();
        }
    }

    private void computeMinDelta()
    {
        minDelta = collisions.stream().map(Collision::getDelta).min(Double::compare).orElse(delta);
    }

    private void computeMinCollisions()
    {
        minCollisions.clear();
        for (Collision collision : collisions)
        {
            if (collision.getDelta() == minDelta)
            {
                minCollisions.add(collision);
            }
        }
    }

    private void moveEntities()
    {
        for (Entity entity : entities)
        {
            entity.update(minDelta);
        }
    }

    private void doMinCollisions()
    {
        for (Collision collision : minCollisions)
        {
            collision.computeVelocity();
            collision.collide();
        }
    }

    public boolean hadCollisions()
    {
        return hadCollisions;
    }
}
