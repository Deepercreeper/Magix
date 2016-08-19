package org.deepercreeper.windows;

import org.deepercreeper.engine.input.Key;
import org.deepercreeper.engine.physics.engine.Engine;
import org.deepercreeper.engine.util.Util;
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
        Engine engine = new Engine(frame.getInput(), frame.getDisplay());
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                final boolean movable = e.getButton() != MouseEvent.BUTTON3;
                double mass = movable ? 1 : Double.POSITIVE_INFINITY;
                double scale = engine.getRenderingEngine().getScale();
                double x = (e.getX() + engine.getRenderingEngine().getPosition().getX()) / scale;
                double y = (e.getY() + engine.getRenderingEngine().getPosition().getY()) / scale;
                double width = movable ? .5 : 30;
                double height = movable ? .5 : .5;
                engine.add(new TestEntity(x - width / 2, y - height / 2, width, height, mass, Math.random(), e.getButton() == MouseEvent.BUTTON2 ? 2 : 1)
                {
                    @Override
                    public Vector computeAcceleration()
                    {
                        if (!movable)
                        {
                            return new Vector();
                        }
                        double accelerationCoefficient = isOnGround() ? 20 : 12;
                        double rightAcceleration = getEngine().getInputEngine().getInput().isActive(Key.RIGHT) ? accelerationCoefficient : 0;
                        double leftAcceleration = getEngine().getInputEngine().getInput().isActive(Key.LEFT) ? -accelerationCoefficient : 0;
                        Vector friction = isOnGround() ? getVelocity().times(5) : getVelocity().times(3);
                        return new Vector(rightAcceleration + leftAcceleration, 40).minus(friction);
                    }

                    @Override
                    public Vector computeVelocity()
                    {
                        if (movable && isOnGround() && getEngine().getInputEngine().getInput().isActive(Key.JUMP))
                        {
                            return new Vector(getXVelocity(), getYVelocity() - 20);
                        }
                        return getVelocity();
                    }

                    @Override
                    public Vector computePosition()
                    {
                        if (!movable)
                        {
                            return getPosition();
                        }
                        if (!isCrouching() && getEngine().getInputEngine().getInput().isActive(Key.CROUCH))
                        {
                            return new Vector(getX(), getY() + getHeight() / 2);
                        }
                        else if (isCrouching() && !getEngine().getInputEngine().getInput().isActive(Key.CROUCH))
                        {
                            return new Vector(getX(), getY() - getHeight() / 2);
                        }
                        return getPosition();
                    }

                    @Override
                    public Vector computeSize()
                    {
                        if (!movable)
                        {
                            return getSize();
                        }
                        if (getEngine().getInputEngine().getInput().isActive(Key.CROUCH))
                        {
                            return new Vector(getWidth(), height / 2);
                        }
                        return new Vector(getWidth(), height);
                    }

                    @Override
                    public void updateInternal(double delta)
                    {
                        if (!getEngine().getRenderingEngine().isVisible(this))
                        {
                            remove();
                        }
                    }

                    private boolean isCrouching()
                    {
                        return getHeight() == height / 2;
                    }
                });
            }
        });

        Util.sleep(10);
        engine.start();
        frame.waitUntilClosed();
        engine.shutDown();
    }
}
