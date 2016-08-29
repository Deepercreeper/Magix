package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.ModificationSet;
import org.deepercreeper.engine.util.Updatable;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class EntityEngine extends AbstractEngine implements Updatable
{
    private final Map<Integer, Entity> entities = new HashMap<>();

    private final Queue<Entity> addedEntities = new LinkedBlockingQueue<>();

    private final ModificationSet<Entity> entitySet = new ModificationSet<>();

    private final ModificationSet<Entity> solidEntities = new ModificationSet<>();

    private final ModificationSet<Entity> nonSolidEntities = new ModificationSet<>();

    private int idCounter = 0;

    public EntityEngine(Engine engine)
    {
        super(engine);
    }

    @Override
    public void update(double delta)
    {
        setSetsModifiable(true);
        removeEntities();
        addEntities();
        setSetsModifiable(false);
    }

    private void setSetsModifiable(boolean modifiable)
    {
        entitySet.setModifiable(modifiable);
        solidEntities.setModifiable(modifiable);
        nonSolidEntities.setModifiable(modifiable);
    }

    public void add(Entity entity)
    {
        addedEntities.add(entity);
    }

    private void removeEntities()
    {
        Iterator<Entity> iterator = entitySet.iterator();
        while (iterator.hasNext())
        {
            Entity entity = iterator.next();
            if (entity.isRemoved())
            {
                removeEntity(entity);
                idCounter = Math.min(idCounter, entity.getId());
                iterator.remove();
            }
        }
    }

    private void removeEntity(Entity entity)
    {
        if (entity.isSolid())
        {
            solidEntities.remove(entity);
        }
        else
        {
            nonSolidEntities.remove(entity);
        }
        entity.clear();
    }

    private void addEntities()
    {
        while (!addedEntities.isEmpty())
        {
            Entity entity = addedEntities.poll();
            entity.init(idCounter, getEngine());
            addEntity(entity);
            updateIdCounter();
        }
    }

    private void addEntity(Entity entity)
    {
        entities.put(entity.getId(), entity);
        entitySet.add(entity);
        if (entity.isSolid())
        {
            solidEntities.add(entity);
        }
        else
        {
            nonSolidEntities.add(entity);
        }
    }

    private void updateIdCounter()
    {
        while (entities.keySet().contains(idCounter))
        {
            idCounter++;
        }
    }

    public Set<Entity> getEntities()
    {
        return entitySet;
    }

    public Set<Entity> getNonSolidEntities()
    {
        return nonSolidEntities;
    }

    public Set<Entity> getSolidEntities()
    {
        return solidEntities;
    }
}