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
        Image visibleImage = new Image.ImageBuilder().set(getRectangle().getCut(image)).build();
        if (visibleImage.isEmpty())
        {
            return;
        }
        visibleImage.setData(Display.createFilledRectangle(visibleImage.getWidth(), visibleImage.getHeight(), 0));
        image.drawOver(visibleImage);
        visibleImage.getPosition().subtract(position);
        display.render(visibleImage.getX(), visibleImage.getY(), visibleImage.getWidth(), visibleImage.getHeight(), visibleImage.getData());
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