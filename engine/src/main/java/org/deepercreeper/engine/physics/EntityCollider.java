package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.pairs.ImmutablePair;
import org.deepercreeper.engine.util.pairs.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityCollider
{
    private final Set<Entity> entities = new HashSet<>();

    private final Set<Pair<Entity, Entity>> collisions = new HashSet<>();

    private final Map<Entity, Set<Entity>> connectedEntities = new HashMap<>();

    private double delta;

    public void collide(Set<Entity> entities, double delta)
    {
        this.delta = delta;
        this.entities.clear();
        this.entities.addAll(entities);
        collisions.clear();
        computeConnectedEntities();
        checkCollisions();
        collide();
    }

    private void computeConnectedEntities()
    {
        connectedEntities.clear();
        for (Entity entity : entities)
        {
            Set<Entity> connectedEntities = entities.stream()
                                                    .filter(connectedEntity -> !entity.equals(connectedEntity) && entity
                                                            .isPossiblyTouching(connectedEntity, delta))
                                                    .collect(Collectors.toSet());
            this.connectedEntities.put(entity, connectedEntities);
        }
    }

    private void checkCollisions()
    {
        entities.forEach(this::checkCollisionsOf);
    }

    private void checkCollisionsOf(Entity entity)
    {
        Box box = entity.getBox().shift(entity.getVelocity().times(delta));
        for (Entity collisionEntity : connectedEntities.get(entity))
        {
            Box entityBox = collisionEntity.getBox().shift(collisionEntity.getVelocity().times(delta));
            if (box.isTouching(entityBox))
            {
                collisions.add(new ImmutablePair<>(entity, collisionEntity));
            }
        }
    }

    private void collide()
    {
        for (Pair<Entity, Entity> collision : collisions)
        {
            collision.getKey().collideWith(collision.getValue());
        }
    }

    public boolean hasCollisions()
    {
        return !collisions.isEmpty();
    }
}
