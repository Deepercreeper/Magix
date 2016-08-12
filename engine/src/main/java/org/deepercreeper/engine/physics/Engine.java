package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.input.Input;

import java.util.*;

public class Engine
{
    private final List<Entity> entities = new ArrayList<>();

    private final List<Entity> addedEntities = new ArrayList<>();

    private final EntityMover entityMover = new EntityMover();

    private final Display display;

    private final Input input;

    private final double scale;

    public Engine(double scale, Display display, Input input)
    {
        this.scale = scale;
        this.display = display;
        this.input = input;
    }

    public void addEntity(Entity entity)
    {
        addedEntities.add(entity);
    }

    public void update(double delta)
    {
        addNewEntities();
        removeRemovedEntities();

        updateEntities(delta);
        saveLastEntityBoxes();
        moveEntities(delta);
        renderEntities();
    }

    public Input getInput()
    {
        return input;
    }

    public Display getDisplay()
    {
        return display;
    }

    public double getScale()
    {
        return scale;
    }

    private void saveLastEntityBoxes()
    {
        entities.forEach(Entity::saveBox);
    }

    private void renderEntities()
    {
        for (Entity entity : entities)
        {
            display.clear(entity.getLastBox().asRectangle());
        }
        entities.forEach(Entity::render);
    }

    private void updateEntities(double delta)
    {
        entities.forEach((Entity entity) -> entity.update(delta));
    }

    private void moveEntities(double delta)
    {
        Set<Set<Entity>> connectedEntities = findConnectedEntities(delta);
        entityMover.setDelta(delta);
        connectedEntities.forEach(entityMover::move);
    }

    private Set<Set<Entity>> findConnectedEntities(double delta)
    {
        Set<Set<Entity>> connectionComponents = new HashSet<>();
        for (Entity entity : entities)
        {
            addToConnectionComponents(entity, connectionComponents, delta);
        }
        return connectionComponents;
    }

    private void addToConnectionComponents(Entity entity, Set<Set<Entity>> connectionComponents, double delta)
    {
        Set<Entity> connectedEntities = new HashSet<>();
        Iterator<Set<Entity>> iterator = connectionComponents.iterator();
        while (iterator.hasNext())
        {
            Set<Entity> connectionComponent = iterator.next();
            for (Entity connectedEntity : connectionComponent)
            {
                if (entity.isPossiblyTouching(connectedEntity, delta))
                {
                    connectedEntities.addAll(connectionComponent);
                    iterator.remove();
                    break;
                }
            }
        }
        connectedEntities.add(entity);
        connectionComponents.add(connectedEntities);
    }

    private void addNewEntities()
    {
        addedEntities.forEach((Entity entity) -> entity.setEngine(this));
        entities.addAll(addedEntities);
        addedEntities.clear();
    }

    private void removeRemovedEntities()
    {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext())
        {
            if (iterator.next().isRemoved())
            {
                iterator.remove();
            }
        }
    }
}
