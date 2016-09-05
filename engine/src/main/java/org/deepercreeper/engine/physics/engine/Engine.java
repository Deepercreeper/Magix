package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.input.Input;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.Force;
import org.deepercreeper.engine.util.Updatable;

public class Engine implements Updatable
{
    private final InputEngine inputEngine = new InputEngine(this);

    private final PhysicsEngine physicsEngine = new PhysicsEngine(this);

    private final EntityEngine entityEngine = new EntityEngine(this);

    private final MotionEngine motionEngine = new MotionEngine(this);

    private final UpdateEngine updateEngine = new UpdateEngine(this);

    private final RenderingEngine renderingEngine = new RenderingEngine(entityEngine, updateEngine);

    public Engine(Input input, Display display)
    {
        inputEngine.setInput(input);
        renderingEngine.setDisplay(display);
    }

    public Engine()
    {
        this(null, null);
    }

    @Override
    public void update(double delta)
    {
        inputEngine.update(delta);
        physicsEngine.update(delta);
        entityEngine.update(delta);
        motionEngine.update(delta);
        renderingEngine.update(delta);
    }

    public PhysicsEngine getPhysicsEngine()
    {
        return physicsEngine;
    }

    public EntityEngine getEntityEngine()
    {
        return entityEngine;
    }

    public RenderingEngine getRenderingEngine()
    {
        return renderingEngine;
    }

    public InputEngine getInputEngine()
    {
        return inputEngine;
    }

    public MotionEngine getMotionEngine()
    {
        return motionEngine;
    }

    public UpdateEngine getUpdateEngine()
    {
        return updateEngine;
    }

    public void add(Force force)
    {
        physicsEngine.add(force);
    }

    public void add(Entity entity)
    {
        entityEngine.add(entity);
    }

    public void setFps(int fps)
    {
        updateEngine.setFps(fps);
    }

    public void setScale(double scale)
    {
        renderingEngine.setScale(scale);
    }

    public void start()
    {
        updateEngine.setUpdating(true);
    }

    public void stop()
    {
        updateEngine.setUpdating(false);
    }

    public void shutDown()
    {
        updateEngine.shutDown();
    }
}
