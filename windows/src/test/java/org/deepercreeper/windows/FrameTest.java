package org.deepercreeper.windows;

import org.deepercreeper.engine.physics.PhysicsEngine;
import org.deepercreeper.engine.physics.PhysicsEntity;
import org.deepercreeper.engine.util.Vector;
import org.junit.Test;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameTest
{
    private Vector position = new Vector();

    @Test
    public void testPhysicsEntity()
    {
        Frame frame = new Frame();
        PhysicsEngine engine = new PhysicsEngine(frame);
        frame.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    engine.addEntity(new TestEntity(e.getX() - 5, e.getY() - 5));
                }
                else
                {
                    engine.addEntity(new TestEntity(e.getX() - 5, e.getY() - 5));
                }
            }
        });
        frame.addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                position.set(e.getX(), e.getY());
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
                    FrameTest.this.sleep(1);
                    long difference = System.currentTimeMillis() - stamp;
                    stamp += difference;
                    engine.update((double) difference / 200);
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

    private class TestEntity extends PhysicsEntity
    {
        public TestEntity(double x, double y)
        {
            super(x, y, 50, 50, 0, 0, 1);
        }

        @Override
        public boolean isAccelerated()
        {
            return true;
        }

        @Override
        public Vector computeAcceleration()
        {
            Vector distance = position.minus(getBox().getCenter());
            return distance.times(getMass() * 2000 / (distance.norm() * distance.norm() * distance.norm()));
        }
    }
}
