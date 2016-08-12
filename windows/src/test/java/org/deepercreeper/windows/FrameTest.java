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
                double width = movable ? 20 : 1000;
                double height = movable ? 20 : 25;
                engine.addEntity(new TestEntity(e.getX() - width / 2, e.getY() - height / 2, width, height, mass)
                {
                    @Override
                    public double getElasticity()
                    {
                        return 0;
                    }

                    @Override
                    public void accelerate(double delta)
                    {
                        if (!movable)
                        {
                            return;
                        }
                        double accelerationCoefficient = isOnGround() ? 2000 : 800;
                        double rightAcceleration = getEngine().getInput().isActive(Key.RIGHT) ? accelerationCoefficient : 0;
                        double leftAcceleration = getEngine().getInput().isActive(Key.LEFT) ? -accelerationCoefficient : 0;
                        Vector friction = isOnGround() ? getVelocity().times(-6) : getVelocity().times(-.5);
                        Vector acceleration = new Vector(rightAcceleration + leftAcceleration, 16 * 16 * 9.81).plus(friction);
                        accelerate(acceleration.times(delta));
                    }

                    @Override
                    public void update()
                    {
                        if (movable && isOnGround() && getEngine().getInput().isActive(Key.JUMP))
                        {
                            setVelocity(getVelocity().plus(new Vector(0, -16 * 16 * 3)));
                        }
                        if (movable)
                        {
                            if (getEngine().getInput().isActive(Key.CROUCH) && getBox().getHeight() == height)
                            {
                                getEngine().getDisplay().clear(getBox().asRectangle());
                                getBox().move(new Vector(0, height / 2));
                            }
                            if (!getEngine().getInput().isActive(Key.CROUCH) && getBox().getHeight() == height / 2)
                            {
                                getBox().move(new Vector(0, -height / 2));
                            }
                            getBox().setSize(getBox().getWidth(), getEngine().getInput().isActive(Key.CROUCH) ? height / 2 : height);
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
