package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.util.Vector;

import java.util.*;

public class PhysicsEngine
{
    private final List<PhysicsEntity> entities = new ArrayList<>();

    private final List<PhysicsEntity> addedEntities = new ArrayList<>();

    private final PhysicsEntityUpdater entityUpdater = new PhysicsEntityUpdater();

    private final Display display;

    public PhysicsEngine(Display display)
    {
        this.display = display;
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
        saveLastEntityBoxes();
        updateEntities(delta);
        renderEntities();
    }

    private void saveLastEntityBoxes()
    {
        entities.forEach(PhysicsEntity::saveBox);
    }

    private void renderEntities()
    {
        for (PhysicsEntity entity : entities)
        {
            display.clear(entity.getLastBox().asRectangle());
        }
        for (PhysicsEntity entity : entities)
        {
            entity.render(display);
        }
    }

    private void accelerateEntities(double delta)
    {
        entities.stream().filter(PhysicsEntity::isAccelerated).forEach(entity ->
        {
            if (entity.isAccelerated())
            {
                Vector acceleration = entity.computeAcceleration();
                entity.accelerate(acceleration.times(delta));
            }
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
