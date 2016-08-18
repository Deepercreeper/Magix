package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.input.Input;
import org.deepercreeper.engine.input.Key;
import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Updatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Deprecated
public class Engine implements Updatable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Engine.class);

    //    private final UpdateEngine updateEngine;

    private final List<Entity> entities = new ArrayList<>();

    private final List<Entity> addedEntities = new ArrayList<>();

    private final EntityMover entityMover = new EntityMover();

    private final EntityDivider entityDivider = new EntityDivider();

    private final Display display;

    private final Input input;

    private final double scale;

    private boolean pause = false;

    public Engine(double scale, Display display, Input input)
    {
        //        updateEngine = new UpdateEngine(this);
        this.scale = scale;
        this.display = display;
        this.input = input;
        LOGGER.info("Engine created");
    }

    public void setPause(boolean pause)
    {
        this.pause = pause;
    }

    @Override
    public void update(double delta)
    {
        addNewEntities();
        removeRemovedEntities();

        updateInput();
        if (!pause)
        {
            updateEntities(delta);
            saveLastEntityBoxes();
            moveEntities(delta);
        }
        renderEntities();
    }

    //    public void setSpeed(double speed)
    //    {
    //        updateEngine.setSpeed(speed);
    //    }

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

    public boolean isFree(Box box)
    {
        for (Entity entity : entities)
        {
            if (entity.isTouching(box))
            {
                return false;
            }
        }
        return true;
    }

    public boolean isDeltaFree(Box box, double delta)
    {
        for (Entity entity : entities)
        {
            if (entity.getDeltaBox(delta).isTouching(box))
            {
                return false;
            }
        }
        return true;
    }

    private void updateInput()
    {
        if (getInput().checkHit(Key.PAUSE))
        {
            setPause(!pause);
        }
    }

    private void saveLastEntityBoxes()
    {
        //        entities.forEach(Entity::saveBox);
    }

    private void renderEntities()
    {
        //        entities.forEach(Entity::clearLastBox);
        //        entities.forEach(Entity::render);
    }

    private void updateEntities(double delta)
    {
        entities.forEach((Entity entity) -> entity.update(delta));
    }

    private void moveEntities(double delta)
    {
        Set<Entity> solidEntities = entities.stream().filter(Entity::isSolid).collect(Collectors.toSet());
        Set<Set<Entity>> connectedEntities = entityDivider.divide(solidEntities, delta);

        connectedEntities.forEach(entity -> entityMover.move(entity, delta));

        entities.stream().filter(entity -> !entity.isSolid()).forEach(entity -> entity.move(delta));
    }

    private void addNewEntities()
    {
        //        addedEntities.forEach((Entity entity) -> entity.init(this));
        entities.addAll(addedEntities);
        addedEntities.clear();
    }

    private void removeRemovedEntities()
    {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext())
        {
            Entity entity = iterator.next();
            if (entity.isRemoved())
            {
                //                entity.init(null);
                iterator.remove();
            }
        }
    }
}
