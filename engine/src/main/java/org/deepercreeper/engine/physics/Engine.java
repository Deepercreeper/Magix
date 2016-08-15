package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.input.Input;
import org.deepercreeper.engine.input.Key;
import org.deepercreeper.engine.util.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Engine
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Engine.class);

    private final Updater updater;

    private final List<Entity> entities = new ArrayList<>();

    private final List<Entity> addedEntities = new ArrayList<>();

    private final EntityMover entityMover = new EntityMover();

    private final EntityDivider entityDivider = new EntityDivider();

    private final Display display;

    private final Input input;

    private final double scale;

    private boolean pause = false;

    public Engine(int fps, double scale, Display display, Input input)
    {
        updater = new Updater(fps);
        this.scale = scale;
        this.display = display;
        this.input = input;
        LOGGER.info("Engine created");
    }

    public void addEntity(Entity entity)
    {
        addedEntities.add(entity);
    }

    public void setFps(int fps)
    {
        updater.setFps(fps);
    }

    public void start()
    {
        updater.setUpdating(true);
        LOGGER.info("Engine started");
    }

    public void stop()
    {
        updater.setUpdating(false);
        LOGGER.info("Engine stopped");
    }

    public void shutDown()
    {
        updater.shutDown();
        LOGGER.info("Engine shut down");
    }

    public void setPause(boolean pause)
    {
        this.pause = pause;
    }

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

    public void setSpeed(double speed)
    {
        updater.setSpeed(speed);
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

    public boolean isVelocityFree(Box box, double delta)
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
        entities.forEach(Entity::saveBox);
    }

    private void renderEntities()
    {
        entities.forEach(Entity::clearLastBox);
        entities.forEach(Entity::render);
    }

    private void updateEntities(double delta)
    {
        entities.forEach((Entity entity) -> entity.update(delta));
    }

    private void moveEntities(double delta)
    {
        Set<Entity> solidEntities = entities.stream().filter(Entity::isSolid).collect(Collectors.toSet());
        Set<Set<Entity>> connectedEntities = entityDivider.divide(solidEntities, delta);

        entityMover.setDelta(delta);
        connectedEntities.forEach(entityMover::move);

        entities.stream().filter(entity -> !entity.isSolid()).forEach(entity -> entity.move(delta));
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
            Entity entity = iterator.next();
            if (entity.isRemoved())
            {
                entity.setEngine(null);
                iterator.remove();
            }
        }
    }

    private class Updater extends Thread
    {
        private double speed = 1;

        private boolean running = true;

        private boolean updating = false;

        private int fps;

        private Updater(int fps)
        {
            this.fps = fps;
            start();
        }

        @Override
        public void run()
        {
            long lastExecution = -1;
            long timeStamp;
            long difference;
            long timeout;
            while (running)
            {
                if (updating)
                {
                    if (lastExecution == -1)
                    {
                        lastExecution = System.currentTimeMillis();
                    }
                    timeStamp = System.currentTimeMillis();
                    difference = timeStamp - lastExecution;
                    timeout = Math.max(0, 1000 / fps - difference);
                    trySleep(timeout);
                    lastExecution = System.currentTimeMillis();
                    update(1000 / fps * speed / 1000);
                }
                else
                {
                    lastExecution = -1;
                    trySleep(1);
                }
            }
        }

        private void trySleep(long timeout)
        {
            if (timeout == 0)
            {
                return;
            }
            try
            {
                sleep(timeout);
            }
            catch (InterruptedException ignored) {}
        }

        public void setSpeed(double speed)
        {
            this.speed = speed;
        }

        public void setUpdating(boolean updating)
        {
            this.updating = updating;
        }

        public void setFps(int fps)
        {
            this.fps = fps;
        }

        public void shutDown()
        {
            running = false;
        }
    }
}
