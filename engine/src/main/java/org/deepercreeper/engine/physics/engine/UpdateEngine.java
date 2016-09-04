package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.common.util.Util;
import org.deepercreeper.engine.util.Updatable;

public class UpdateEngine implements Runnable
{
    private final Updatable updatable;

    private boolean running = true;

    private boolean updating = false;

    private double speed = 1;

    private long difference;

    private int fps = 60;

    public UpdateEngine(Updatable updatable)
    {
        this.updatable = updatable;
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
                updatable.update(speed / fps);
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
