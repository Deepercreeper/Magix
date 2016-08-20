package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Box;

import java.util.*;

public class Collider
{
    private final Set<Collision> collisions = new HashSet<>();

    private final Set<Collision> minCollisions = new HashSet<>();

    private final Set<Collision> instantCollisions = new HashSet<>();

    private final Splitter splitter = new Splitter();

    private final Set<Entity> entities;

    private boolean hadCollisions;

    private double delta;

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
            collideInstantCollisions();
            collideMinCollisions();
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
                Collision collision = new Collision(entity, collisionEntity);
                collision.computeDelta();
                collision.optimizeDelta();
                if (collision.isInstant())
                {
                    instantCollisions.add(collision);
                }
                else
                {
                    collisions.add(collision);
                }
                hadCollisions = true;
            }
        }
    }

    private void collideInstantCollisions()
    {
        instantCollisions.forEach(Collision::collide);
    }

    private void collideMinCollisions()
    {
        double minDelta = computeMinDelta();
        computeMinCollisions(minDelta);
        moveEntities(minDelta);
        collideEntities(minDelta);
        splitEntities();
        delta -= minDelta;
    }

    private double computeMinDelta()
    {
        return collisions.stream().map(Collision::getDelta).min(Double::compare).orElse(delta);
    }

    private void computeMinCollisions(double delta)
    {
        minCollisions.clear();
        for (Collision collision : collisions)
        {
            if (collision.getDelta() == delta)
            {
                minCollisions.add(collision);
            }
        }
    }

    private void moveEntities(double delta)
    {
        Entity lastEntity = null;
        for (Entity entity : entities)
        {
            //TODO Maybe inside the collisions. That would do the splitting. Maybe with move back until no touching is performed
            entity.update(delta);
            if (lastEntity != null && lastEntity.isTouching(entity))
            {
                System.out.println("Bla");
            }
            lastEntity = entity;
        }
    }

    private void splitEntities()
    {
        //        splitter.split(entities);
        instantCollisions.forEach(splitter::split);
        minCollisions.forEach(splitter::split);
    }

    private void collideEntities(double delta)
    {
        for (Collision collision : collisions)
        {
            if (collision.getDelta() == delta)
            {
                collision.collide();
            }
        }
    }

    public boolean hadCollisions()
    {
        return hadCollisions;
    }
}
