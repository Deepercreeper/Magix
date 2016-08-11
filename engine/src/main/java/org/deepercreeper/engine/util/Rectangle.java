package org.deepercreeper.engine.util;

public class Rectangle
{
    private final Point position;

    private final Point size;

    public Rectangle(int x, int y, int width, int height)
    {
        position = new Point(Math.min(x, x + width), Math.min(y, y + height));
        size = new Point(Math.abs(width), Math.abs(height));
    }

    public Rectangle(int x, int y)
    {
        this(x, y, 0, 0);
    }

    public Rectangle()
    {
        this(0, 0);
    }

    public Rectangle(Point position, Point size)
    {
        this(position.getX(), position.getY(), size.getX(), size.getY());
    }

    public Rectangle(Point position)
    {
        this(position, new Point());
    }

    public Rectangle(Rectangle rectangle)
    {
        this(rectangle.position, rectangle.size);
    }

    public Point getPosition()
    {
        return new Point(position);
    }

    public Point getCenter()
    {
        return position.plus(size.times(0.5));
    }

    public Point getSize()
    {
        return new Point(size);
    }

    public int getX()
    {
        return position.getX();
    }

    public int getY()
    {
        return position.getY();
    }

    public int getWidth()
    {
        return size.getX();
    }

    public int getHeight()
    {
        return size.getY();
    }

    public int getMaxX()
    {
        return getX() + getWidth();
    }

    public int getMaxY()
    {
        return getY() + getHeight();
    }

    public void move(Point point)
    {
        position.add(point);
    }

    public Rectangle shift(Point point)
    {
        return new Rectangle(position.plus(point), size);
    }

    public boolean isTouching(Rectangle rectangle)
    {
        return !(getMaxX() < rectangle.getX() || rectangle.getMaxX() < getX() || getMaxY() < rectangle
                .getY() || rectangle.getMaxY() < getY());
    }

    public Rectangle getCut(Rectangle rectangle)
    {
        if (!isTouching(rectangle))
        {
            return new Rectangle();
        }
        int x = Math.max(getX(), rectangle.getX());
        int y = Math.max(getY(), rectangle.getY());
        int width = Math.min(getMaxX(), rectangle.getMaxX()) - x;
        int height = Math.min(getMaxY(), rectangle.getMaxY()) - y;
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getContainment(Rectangle rectangle)
    {
        int x = Math.min(getX(), rectangle.getX());
        int y = Math.min(getY(), rectangle.getY());
        int width = Math.max(getMaxX(), rectangle.getMaxX()) - x;
        int height = Math.max(getMaxY(), rectangle.getMaxY()) - y;
        return new Rectangle(x, y, width, height);
    }

    public boolean isEmpty()
    {
        return getWidth() == 0 || getHeight() == 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Rectangle)
        {
            Rectangle rectangle = (Rectangle) obj;
            return position.equals(rectangle.position) && size.equals(rectangle.size);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return position.hashCode() * 17 + size.hashCode();
    }

    @Override
    public String toString()
    {
        return "(" + position + ", " + size + ")";
    }
}
