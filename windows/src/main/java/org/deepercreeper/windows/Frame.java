package org.deepercreeper.windows;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.input.Input;
import org.deepercreeper.engine.input.Key;
import org.deepercreeper.engine.util.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Frame extends JFrame implements Display, Input
{
    private final FrameDisplay frameDisplay = new FrameDisplay(this);

    private final FrameInput frameInput = new FrameInput(this);

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
    public boolean isActive(Key key)
    {
        return frameInput.isActive(key);
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

    public BufferedImage getImage()
    {
        return image;
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
    public void clear()
    {
        frameDisplay.clear();
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
}
