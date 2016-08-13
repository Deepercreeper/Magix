package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.input.Input;
import org.deepercreeper.engine.input.Key;
import org.deepercreeper.engine.util.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Engine
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Engine.class);

    private final Updater updater;

    private final List<Entity> entities = new ArrayList<>();

    private final List<Entity> addedEntities = new ArrayList<>();

    private final EntityMover entityMover = new EntityMover();

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
        LOGGER.debug("==> Starting update");
        long timeStamp = System.currentTimeMillis();
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
        LOGGER.debug("<== Update finished in {} ms", System.currentTimeMillis() - timeStamp);
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
            if (entity.getBox().isTouching(box))
            {
                return false;
            }
        }
        return true;
    }

    public boolean isFreeIn(Box box, double delta)
    {
        for (Entity entity : entities)
        {
            if (entity.getVelocityBox(delta).isTouching(box))
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
        LOGGER.debug("==> Starting save of last box");
        long timeStamp = System.currentTimeMillis();
        entities.forEach(Entity::saveBox);
        LOGGER.debug("<== Save of last box finished in {} ms", System.currentTimeMillis() - timeStamp);
    }

    private void renderEntities()
    {
        LOGGER.debug("==> Starting render");
        long timeStamp = System.currentTimeMillis();
        entities.forEach(Entity::clear);
        entities.forEach(Entity::render);
        LOGGER.debug("<== Render finished in {} ms", System.currentTimeMillis() - timeStamp);
    }

    private void updateEntities(double delta)
    {
        LOGGER.debug("==> Starting update");
        long timeStamp = System.currentTimeMillis();
        entities.forEach((Entity entity) -> entity.update(delta));
        LOGGER.debug("<== Update finished in {} ms", System.currentTimeMillis() - timeStamp);
    }

    private void moveEntities(double delta)
    {
        LOGGER.debug("==> Starting move");
        long timeStamp = System.currentTimeMillis();
        Set<Set<Entity>> connectedEntities = findConnectedEntities(delta);
        entityMover.setDelta(delta);
        connectedEntities.forEach(entityMover::move);
        entities.stream().filter(entity -> !entity.isSolid()).forEach(entity -> entity.move(delta));
        LOGGER.debug("<== Move finished in {} ms", System.currentTimeMillis() - timeStamp);
    }

    private Set<Set<Entity>> findConnectedEntities(double delta)
    {
        Set<Set<Entity>> connectionComponents = new HashSet<>();
        entities.stream().filter(Entity::isSolid).forEach(entity -> addToConnectionComponents(entity, connectionComponents, delta));
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
                if (entity.isVelocityTouching(connectedEntity, delta))
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
