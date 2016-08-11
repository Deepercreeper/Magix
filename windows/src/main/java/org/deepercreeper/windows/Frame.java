package org.deepercreeper.windows;

import org.deepercreeper.engine.display.AbstractDisplay;
import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.util.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Frame extends JFrame implements Display
{
    private final FrameDisplay frameDisplay = new FrameDisplay();

    private BufferedImage image;

    private boolean closed = false;

    public Frame()
    {
        super("Display");
        setSize(800, 600);
        setLocation();
        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                resized();
            }
        });
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closed = true;
            }
        });
        setVisible(true);
    }

    @Override
    public void paint(Graphics g)
    {
        g.drawImage(image, 0, 0, null);
    }

    private void resized()
    {
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        frameDisplay.clear(frameDisplay.getRectangle());
    }

    private void setLocation()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
    }

    @Override
    public void render(Rectangle rectangle, int[] image)
    {
        frameDisplay.render(rectangle, image);
    }

    @Override
    public void render(int x, int y, int width, int height, int[] image)
    {
        frameDisplay.render(x, y, width, height, image);
    }

    @Override
    public void clear(Rectangle rectangle)
    {
        frameDisplay.clear(rectangle);
    }

    @Override
    public void clear(int x, int y, int width, int height)
    {
        frameDisplay.clear(x, y, width, height);
    }

    @Override
    public Rectangle getRectangle()
    {
        return frameDisplay.getRectangle();
    }

    public void waitUntilClosed()
    {
        while (!closed)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException ignored)
            {
            }
        }
    }

    private class FrameDisplay extends AbstractDisplay
    {
        @Override
        protected void renderInternal(int x, int y, int width, int height, int[] image)
        {
            try
            {
                Frame.this.image.setRGB(x, y, width, height, image, 0, width);
            }
            catch (ArrayIndexOutOfBoundsException ignored)
            {
            }
            repaint(x, y, width, height);
        }

        @Override
        protected void clearInternal(int x, int y, int width, int height)
        {
            try
            {
                Frame.this.image.setRGB(x, y, width, height, Display.createRectangle(width, height, 0xff000000), 0, width);
            }
            catch (ArrayIndexOutOfBoundsException ignored)
            {
            }
            repaint(x, y, width, height);
        }

        @Override
        public int getWidth()
        {
            return Frame.this.getWidth();
        }

        @Override
        public int getHeight()
        {
            return Frame.this.getHeight();
        }
    }
}
