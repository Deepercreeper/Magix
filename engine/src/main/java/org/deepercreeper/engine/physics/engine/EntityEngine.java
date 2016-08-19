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

    private int idCounter = 0;

    public EntityEngine(Engine engine)
    {
        super(engine);
    }

    @Override
    public void update(double delta)
    {
        entitySet.setModifiable(true);
        removeEntities();
        addEntities();
        entitySet.setModifiable(false);
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
                idCounter = Math.min(idCounter, entity.getId());
                iterator.remove();
            }
        }
    }

    private void addEntities()
    {
        while (!addedEntities.isEmpty())
        {
            Entity entity = addedEntities.poll();
            entity.init(idCounter, getEngine());
            entities.put(idCounter, entity);
            entitySet.add(entity);
            updateIdCounter();
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
}
