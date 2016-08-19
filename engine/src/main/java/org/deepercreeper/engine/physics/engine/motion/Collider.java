package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Box;

import java.util.*;

public class Collider
{
    private final Set<Collision> collisions = new HashSet<>();

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
                collisions.add(new Collision(entity, collisionEntity));
                hadCollisions = true;
            }
        }
    }

    private void collideInstantCollisions()
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

    private void collideMinCollisions()
    {
        double minDelta = computeMinDelta();
        moveEntities(minDelta);
        collideEntities(minDelta);
        delta -= minDelta;
    }

    private double computeMinDelta()
    {
        return collisions.stream().map(Collision::getDelta).min(Double::compare).orElse(delta);
    }

    private void moveEntities(double delta)
    {
        for (Entity entity : entities)
        {
            //TODO Maybe inside the collisions. That would do the splitting. Maybe with move back until no touching is performed
            entity.update(delta);
        }
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
