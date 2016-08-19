package org.deepercreeper.engine.display;

import org.deepercreeper.engine.util.Image;
import org.deepercreeper.engine.util.Point;
import org.deepercreeper.engine.util.Rectangle;

public class AbstractRenderer implements Renderer
{
    private final Point position = new Point();

    private final Rectangle rectangle = new Rectangle.RectangleBuilder().build();

    private Display display;

    public void setDisplay(Display display)
    {
        this.display = display;
    }

    @Override
    public void render(Image image)
    {
        if (display == null)
        {
            return;
        }
        image.validate();
        Rectangle visibleRectangle = getRectangle().getCut(image);
        if (visibleRectangle.isEmpty())
        {
            return;
        }
        int[] yCroppedImage = new int[image.getWidth() * visibleRectangle.getHeight()];
        System.arraycopy(image.getData(), Math.max(0, -visibleRectangle.getY()) * image.getWidth(), yCroppedImage, 0, image.getWidth() * visibleRectangle.getHeight());

        int[] xCroppedImage = new int[visibleRectangle.getWidth() * visibleRectangle.getHeight()];
        for (int i = 0; i < visibleRectangle.getHeight(); i++)
        {
            System.arraycopy(yCroppedImage, i * image.getWidth() + Math.max(0, -image.getX()), xCroppedImage, i * visibleRectangle.getWidth(), visibleRectangle.getWidth());
        }
        visibleRectangle.getPosition().subtract(position);
        display.render(visibleRectangle.getX(), visibleRectangle.getY(), visibleRectangle.getWidth(), visibleRectangle.getHeight(), xCroppedImage);
    }

    @Override
    public void clear(Rectangle rectangle)
    {
        if (display == null)
        {
            return;
        }
        Rectangle visibleRectangle = getRectangle().getCut(rectangle);
        if (visibleRectangle.isEmpty())
        {
            return;
        }
        visibleRectangle.getPosition().subtract(position);
        display.clear(visibleRectangle.getX(), visibleRectangle.getY(), visibleRectangle.getWidth(), visibleRectangle.getHeight());
    }

    @Override
    public void clear()
    {
        display.clear(0, 0, display.getWidth(), display.getHeight());
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position.set(position);
    }

    private Rectangle getRectangle()
    {
        rectangle.setPosition(position);
        rectangle.setSize(display.getWidth(), display.getHeight());
        return rectangle;
    }

    @Override
    public boolean isVisible(Rectangle rectangle)
    {
        return !getRectangle().getCut(rectangle).isEmpty();
    }
}