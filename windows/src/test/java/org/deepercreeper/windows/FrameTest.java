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
                engine.add(new TestEntity(x - width / 2, y - height / 2, width, height, mass, Math.random())
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
                    public void update()
                    {
                        if (movable && isOnGround() && getEngine().getInputEngine().getInput().isActive(Key.JUMP))
                        {
                            getVelocity().add(0, -20);
                        }
                        if (movable)
                        {
                            if (getEngine().getInputEngine().getInput().isActive(Key.CROUCH) && getHeight() == height)
                            {
                                //                                getEngine().getDisplay().clear(asScaledRectangle(getEngine().getScale()));
                                moveBy(0, height / 2);
                            }
                            if (!getEngine().getInputEngine().getInput().isActive(Key.CROUCH) && getHeight() == height / 2)
                            {
                                moveBy(0, -height / 2);
                            }
                            setHeight(getEngine().getInputEngine().getInput().isActive(Key.CROUCH) ? height / 2 : height);
                        }
                        if (!getEngine().getRenderingEngine().isVisible(this))
                        {
                            remove();
                        }
                    }

                    @Override
                    public double getSpeed()
                    {
                        return e.getButton() == MouseEvent.BUTTON2 ? 2 : 1;
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
