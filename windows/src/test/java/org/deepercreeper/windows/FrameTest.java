package org.deepercreeper.windows;

import org.deepercreeper.engine.input.Key;
import org.deepercreeper.engine.physics.Engine;
import org.deepercreeper.engine.util.Vector;
import org.junit.Test;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameTest
{
    @Test
    public void testPhysicsEntity()
    {
        Frame frame = new Frame();
        Engine engine = new Engine(16, frame, frame);
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                final boolean movable = e.getButton() == MouseEvent.BUTTON1;
                double mass = movable ? 1 : Double.POSITIVE_INFINITY;
                double size = movable ? 20 : 25;
                engine.addEntity(new TestEntity(e.getX() - size / 2, e.getY() - size / 2, size, size, mass)
                {
                    @Override
                    public double getElasticity()
                    {
                        return 0;
                    }

                    @Override
                    public void accelerate(double delta)
                    {
                        Vector acceleration = getAcceleration();
                        accelerate(acceleration.times(delta));
                    }

                    private Vector getAcceleration()
                    {
                        if (!movable)
                        {
                            return new Vector();
                        }
                        double accelerationCoefficient = 1024;
                        double rightAcceleration = getEngine().getInput().isActive(Key.RIGHT) ? accelerationCoefficient : 0;
                        double leftAcceleration = getEngine().getInput().isActive(Key.LEFT) ? -accelerationCoefficient : 0;
                        double friction = -getVelocity().getX() * 2;
                        return new Vector(rightAcceleration + leftAcceleration + friction, 16 * 16 * 9.81);
                    }

                    @Override
                    public void update()
                    {
                        if (movable && isOnGround() && getEngine().getInput().isActive(Key.JUMP))
                        {
                            setVelocity(getVelocity().plus(new Vector(0, -16 * 16 * 3)));
                        }
                        if (getBox().asRectangle().getCut(getEngine().getDisplay().getRectangle()).isEmpty())
                        {
                            remove();
                        }
                    }
                });
            }
        });
        boolean[] closed = new boolean[1];

        sleep(10);
        new Thread()
        {
            @Override
            public void run()
            {
                long stamp = System.currentTimeMillis();
                while (!closed[0])
                {
                    FrameTest.this.sleep(1000 / 60);
                    long difference = System.currentTimeMillis() - stamp;
                    stamp += difference;
                    engine.update((double) difference / 1000);
                }
            }
        }.start();
        frame.waitUntilClosed();
        closed[0] = true;
    }

    private void sleep(long timeout)
    {
        try
        {
            Thread.sleep(timeout);
        }
        catch (InterruptedException ignored)
        {
        }
    }
}
