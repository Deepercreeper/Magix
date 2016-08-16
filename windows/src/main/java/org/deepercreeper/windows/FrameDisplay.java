package org.deepercreeper.windows;

import org.deepercreeper.engine.display.AbstractDisplay;
import org.deepercreeper.engine.display.Display;

public class FrameDisplay extends AbstractDisplay
{
    private final Frame frame;

    public FrameDisplay(Frame frame)
    {
        this.frame = frame;
    }

    @Override
    protected void renderInternal(int x, int y, int width, int height, int[] image)
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
    protected void clearInternal(int x, int y, int width, int height)
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
