package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Updatable;

public class MotionEngine extends AbstractEngine implements Updatable
{
    private boolean pause = false;

    public MotionEngine(Engine engine)
    {
        super(engine);
    }

    public void setPause(boolean pause)
    {
        this.pause = pause;
    }

    public void togglePause()
    {
        pause = !pause;
    }

    @Override
    public void update(double delta)
    {
        if (!pause)
        {
            updateMotion(delta);
        }
    }

    private void updateMotion(double delta)
    {
        for (Entity entity : getEngine().getEntityEngine().getEntities())
        {
            entity.update(delta);
            entity.move(delta);
        }
    }
}
