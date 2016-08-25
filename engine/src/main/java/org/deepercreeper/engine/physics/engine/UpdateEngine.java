package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.util.Util;

public class UpdateEngine extends AbstractEngine implements Runnable
{
    private boolean running = true;

    private boolean updating = false;

    private double speed = 1;

    private long difference;

    private int fps = 60;

    public UpdateEngine(Engine engine)
    {
        super(engine);
        new Thread(this, "Updater").start();
    }

    @Override
    public void run()
    {
        long lastExecution = -1;
        long timeout;
        while (running)
        {
            if (updating)
            {
                if (lastExecution == -1)
                {
                    lastExecution = System.currentTimeMillis();
                }
                difference = System.currentTimeMillis() - lastExecution;
                timeout = Math.max(0, 1000 / fps - difference);
                Util.sleep(timeout);
                lastExecution = System.currentTimeMillis();
                getEngine().update(speed / fps);
            }
            else
            {
                lastExecution = -1;
                Util.sleep(1);
            }
        }
    }

    public long getDifference()
    {
        return difference;
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
