package org.deepercreeper.windows;

import org.deepercreeper.engine.input.Key;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.engine.Engine;
import org.deepercreeper.engine.physics.engine.motion.colliders.MinimumCollider;
import org.deepercreeper.engine.physics.engine.motion.components.motion.StepMotion;
import org.deepercreeper.engine.physics.engine.motion.splitters.AxialSplitter;
import org.deepercreeper.engine.physics.engine.motion.strategies.BruteForceStrategy;
import org.deepercreeper.engine.util.Vector;
import org.junit.Test;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameTest
{
    @Test
    public void testPhysicsEntity()
    {
        Frame frame = new Frame();
        Engine engine = new Engine(frame.getInput(), frame.getDisplay());
        engine.getMotionEngine().setStrategy(new BruteForceStrategy(new StepMotion(1, new AxialSplitter(10E-5), new MinimumCollider())));
        engine.add((entity) -> new Vector(0, 9.81 / entity.getMass()));
        engine.add((entity) ->
        {
            double accelerationCoefficient = entity.isOnGround() ? 20 : 12;
            double rightAcceleration = entity.getEngine().getInputEngine().getInput().isActive(Key.RIGHT) ? accelerationCoefficient : 0;
            double leftAcceleration = entity.getEngine().getInputEngine().getInput().isActive(Key.LEFT) ? -accelerationCoefficient : 0;
            double friction = entity.isOnGround() ? entity.getXVelocity() * 5 : 0;
            return new Vector(rightAcceleration + leftAcceleration - friction, 0);
        });

        frame.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                engine.getMotionEngine().setPause(false);
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                engine.getMotionEngine().setPause(true);
            }
        });
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                final boolean movable = e.getButton() != MouseEvent.BUTTON3;
                double scale = engine.getRenderingEngine().getScale();
                double x = (e.getX() + engine.getRenderingEngine().getPosition().getX()) / scale;
                double y = (e.getY() + engine.getRenderingEngine().getPosition().getY()) / scale;

                Entity entity = getEntityAt(x, y);
                if (entity != null)
                {
                    entity.setMass(entity.getMass() * 2);
                    return;
                }

                double mass = movable ? 1 : Double.POSITIVE_INFINITY;
                double width = movable ? .5 : 30;
                double height = movable ? .5 : .5;
                engine.add(new TestEntity(x - width / 2, y - height / 2, width, height, mass, .75, e.getButton() == MouseEvent.BUTTON2 ? 2 : 1)
                {
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
                            //                            remove();
                        }
                    }

                    private boolean isCrouching()
                    {
                        return getHeight() == height / 2;
                    }
                });
            }

            private Entity getEntityAt(double x, double y)
            {
                for (Entity entity : engine.getEntityEngine().getEntities())
                {
                    if (entity.getX() <= x && x <= entity.getMaxX() && entity.getY() <= y && y <= entity.getMaxY())
                    {
                        return entity;
                    }
                }
                return null;
            }
        });

        engine.start();
        frame.waitUntilClosed();
        engine.shutDown();
    }
}