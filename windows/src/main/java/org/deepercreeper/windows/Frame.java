package org.deepercreeper.windows;

import org.deepercreeper.common.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Frame extends JFrame
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

    public FrameDisplay getDisplay()
    {
        return frameDisplay;
    }

    public FrameInput getInput()
    {
        return frameInput;
    }

    @Override
    public void paint(Graphics g)
    {
        g.drawImage(image, 0, 0, null);
    }

    private void resized()
    {
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        frameDisplay.clear(0, 0, getWidth(), getHeight());
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

    public void waitUntilClosed()
    {
        while (!closed)
        {
            Util.sleep(100);
        }
    }
}
