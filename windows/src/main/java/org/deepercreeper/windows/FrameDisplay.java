package org.deepercreeper.windows;

import org.deepercreeper.engine.display.Display;

public class FrameDisplay implements Display
{
    private final Frame frame;

    public FrameDisplay(Frame frame)
    {
        this.frame = frame;
    }

    @Override
    public void render(int x, int y, int width, int height, int[] image)
    {
        try
        {
            frame.getImage().setRGB(x, y, width, height, image, 0, width);
        }
        catch (ArrayIndexOutOfBoundsException ignored)
        {
        }
        frame.repaint(x, y, width, height);
    }

    @Override
    public void clear(int x, int y, int width, int height)
    {
        try
        {
            frame.getImage().setRGB(x, y, width, height, Display.createFilledRectangle(width, height, 0xff000000), 0, width);
        }
        catch (ArrayIndexOutOfBoundsException ignored)
        {
        }
        frame.repaint(x, y, width, height);
    }

    @Override
    public int getWidth()
    {
        return frame.getWidth();
    }

    @Override
    public int getHeight()
    {
        return frame.getHeight();
    }
}
