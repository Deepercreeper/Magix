package org.deepercreeper.engine.display;

public class TestDisplay implements Display
{
    private int renderings = 0;

    private int clears = 0;

    private final int width;

    private final int height;

    public TestDisplay(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    public int getRenderings()
    {
        return renderings;
    }

    public int getClears()
    {
        return clears;
    }

    @Override
    public void render(int x, int y, int width, int height, int[] image)
    {
        if (x < 0 || y < 0 || x + width > getWidth() || y + height > getHeight())
        {
            throw new IllegalArgumentException("Cannot draw outside of display bounds");
        }
        if (image == null || image.length != width * height)
        {
            throw new IllegalArgumentException("Image data has a different length as the given width * height");
        }
        renderings++;
    }

    @Override
    public void clear(int x, int y, int width, int height)
    {
        if (x < 0 || y < 0 || x + width > getWidth() || y + height > getHeight())
        {
            throw new IllegalArgumentException("Cannot draw outside of display bounds");
        }
        clears++;
    }
}
