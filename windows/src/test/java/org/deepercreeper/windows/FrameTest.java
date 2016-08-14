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
        Engine engine = new Engine(100, 48, frame.getDisplay(), frame.getInput());
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                final boolean movable = e.getButton() != MouseEvent.BUTTON3;
                double mass = movable ? 1 : Double.POSITIVE_INFINITY;
                double x = e.getX() / engine.getScale();
                double y = e.getY() / engine.getScale();
                double width = movable ? .5 : 30;
                double height = movable ? .5 : .5;
                engine.addEntity(new TestEntity(x - width / 2, y - height / 2, width, height, mass)
                {
                    private final double elasticity = Math.random();

                    @Override
                    public void updateVelocity(double delta)
                    {
                        if (!movable)
                        {
                            return;
                        }
                        double accelerationCoefficient = isOnGround() ? 20 : 12;
                        double rightAcceleration = getEngine().getInput().isActive(Key.RIGHT) ? accelerationCoefficient : 0;
                        double leftAcceleration = getEngine().getInput().isActive(Key.LEFT) ? -accelerationCoefficient : 0;
                        Vector friction = isOnGround() ? getVelocity().times(5) : getVelocity().times(3);
                        Vector acceleration = new Vector(rightAcceleration + leftAcceleration, 40).minus(friction);
                        getVelocity().add(acceleration, delta);
                    }

                    @Override
                    public void update()
                    {
                        if (movable && isOnGround() && getEngine().getInput().isActive(Key.JUMP))
                        {
                            getVelocity().add(0, -20);
                        }
                        if (movable)
                        {
                            if (getEngine().getInput().isActive(Key.CROUCH) && getHeight() == height)
                            {
                                getEngine().getDisplay().clear(asScaledRectangle(getEngine().getScale()));
                                moveBy(0, height / 2);
                            }
                            if (!getEngine().getInput().isActive(Key.CROUCH) && getHeight() == height / 2)
                            {
                                moveBy(0, -height / 2);
                            }
                            setHeight(getEngine().getInput().isActive(Key.CROUCH) ? height / 2 : height);
                        }
                        if (asScaledRectangle(getEngine().getScale()).getCut(getEngine().getDisplay().getRectangle()).isEmpty())
                        {
                            remove();
                        }
                    }

                    @Override
                    public double getElasticity()
                    {
                        return elasticity;
                    }

                    @Override
                    public double getSpeed()
                    {
                        return e.getButton() == MouseEvent.BUTTON2 ? 2 : 1;
                    }
                });
            }
        });

        sleep(10);
        engine.start();
        frame.waitUntilClosed();
        engine.shutDown();
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
