package org.deepercreeper.engine.display;

import org.deepercreeper.engine.util.Rectangle;

public abstract class AbstractDisplay implements Display
{
    @Override
    public void render(Rectangle rectangle, int[] image)
    {
        Rectangle visibleRectangle = getRectangle().getCut(rectangle);
        if (visibleRectangle.isEmpty())
        {
            return;
        }
        int[] yCroppedImage = new int[rectangle.getWidth() * visibleRectangle.getHeight()];
        System.arraycopy(image, Math.max(0, -visibleRectangle.getY()) * rectangle
                .getWidth(), yCroppedImage, 0, rectangle
                .getWidth() * visibleRectangle.getHeight());

        int[] xCroppedImage = new int[visibleRectangle.getWidth() * visibleRectangle.getHeight()];
        for (int i = 0; i < visibleRectangle.getHeight(); i++)
        {
            System.arraycopy(yCroppedImage, i * rectangle.getWidth() + Math
                    .max(0, -rectangle.getX()), xCroppedImage, i * visibleRectangle
                    .getWidth(), visibleRectangle.getWidth());
        }
        renderInternal(visibleRectangle.getX(), visibleRectangle.getY(), visibleRectangle.getWidth(), visibleRectangle
                .getHeight(), xCroppedImage);
    }

    @Override
    public void render(int x, int y, int width, int height, int[] image)
    {
        render(new Rectangle(x, y, width, height), image);
    }

    @Override
    public void clear(Rectangle rectangle)
    {
        Rectangle visibleRectangle = getRectangle().getCut(rectangle);
        if (visibleRectangle.isEmpty())
        {
            return;
        }
        clearInternal(visibleRectangle.getX(), visibleRectangle.getY(), visibleRectangle.getWidth(), visibleRectangle
                .getHeight());
    }

    @Override
    public void clear(int x, int y, int width, int height)
    {
        clear(new Rectangle(x, y, width, height));
    }

    @Override
    public Rectangle getRectangle()
    {
        return new Rectangle(0, 0, getWidth(), getHeight());
    }

    protected abstract void renderInternal(int x, int y, int width, int height, int[] image);

    protected abstract void clearInternal(int x, int y, int width, int height);
}
