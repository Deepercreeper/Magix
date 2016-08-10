package org.deepercreeper.engine;

import org.deepercreeper.engine.physics.PhysicsEntity;
import org.deepercreeper.engine.physics.PhysicsEntityUpdater;
import org.deepercreeper.engine.physics.Vector;

import java.util.*;

public class PhysicsEngine
{
    private final List<PhysicsEntity> entities = new ArrayList<>();

    private final List<PhysicsEntity> addedEntities = new ArrayList<>();

    private final PhysicsEntityUpdater entityUpdater = new PhysicsEntityUpdater();

    public PhysicsEngine()
    {
    }

    public void addEntity(PhysicsEntity entity)
    {
        addedEntities.add(entity);
    }

    public void update(double delta)
    {
        addNewEntities();
        removeRemovedEntities();

        accelerateEntities(delta);
        updateEntities(delta);
    }

    private void accelerateEntities(double delta)
    {
        entities.stream().filter(PhysicsEntity::isAccelerated).forEach(entity ->
        {
            Vector acceleration = entity.computeAcceleration();
            entity.accelerate(acceleration.times(delta));
        });
    }

    private void updateEntities(double delta)
    {
        Set<Set<PhysicsEntity>> connectedEntities = findConnectedEntities(delta);
        entityUpdater.setDelta(delta);
        connectedEntities.forEach(entityUpdater::update);
    }

    private Set<Set<PhysicsEntity>> findConnectedEntities(double delta)
    {
        Set<Set<PhysicsEntity>> connectedEntities = new HashSet<>();
        for (PhysicsEntity entity : entities)
        {
            boolean connected = false;
            for (Set<PhysicsEntity> connectionComponent : connectedEntities)
            {
                for (PhysicsEntity connectedEntity : connectionComponent)
                {
                    if (entity.isPossiblyTouching(connectedEntity, delta))
                    {
                        connectionComponent.add(entity);
                        connected = true;
                        break;
                    }
                }
                if (connected)
                {
                    break;
                }
            }
            if (!connected)
            {
                Set<PhysicsEntity> connectionComponent = new HashSet<>();
                connectionComponent.add(entity);
                connectedEntities.add(connectionComponent);
            }
        }
        return connectedEntities;
    }

    private void addNewEntities()
    {
        entities.addAll(addedEntities);
        addedEntities.clear();
    }

    private void removeRemovedEntities()
    {
        Iterator<PhysicsEntity> iterator = entities.iterator();
        while (iterator.hasNext())
        {
            if (iterator.next().isRemoved())
            {
                iterator.remove();
            }
        }
    }
}
