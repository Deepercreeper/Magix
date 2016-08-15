package org.deepercreeper.engine.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Rectangle
{
    private final Point center = new Point();

    private final Point position;

    private final Point size;

    private int hashCode;

    public Rectangle(int x, int y, int width, int height)
    {
        position = new Point(Math.min(x, x + width), Math.min(y, y + height));
        size = new Point(Math.abs(width), Math.abs(height));
        updateCenterAndHashCode();
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

    public void set(Rectangle rectangle)
    {
        position.set(rectangle.position);
        size.set(rectangle.size);
        center.set(rectangle.center);
        hashCode = rectangle.hashCode;
    }

    public void setPosition(double x, double y)
    {
        position.set(x, y);
        updateCenterAndHashCode();
    }

    public void setPosition(int x, int y)
    {
        position.set(x, y);
        updateCenterAndHashCode();
    }

    public void setPosition(Point position)
    {
        this.position.set(position);
        updateCenterAndHashCode();
    }

    public void setSize(double width, double height)
    {
        size.set(width, height);
        updateCenterAndHashCode();
    }

    public void setSize(int width, int height)
    {
        size.set(width, height);
        updateCenterAndHashCode();
    }

    public void setSize(Point size)
    {
        this.size.set(size);
        updateCenterAndHashCode();
    }

    public void setCenter(double x, double y)
    {
        position.set(x - getWidth() * .5, y - getHeight() * .5);
        center.set(x, y);
        updateHashCode();
    }

    public void setCenter(int x, int y)
    {
        position.set(x - getWidth() * .5, y - getHeight() * .5);
        center.set(x, y);
        updateHashCode();
    }

    public void setCenter(Point center)
    {
        position.set(center.getX() - getWidth() * .5, center.getY() - getHeight() * .5);
        this.center.set(center);
        updateHashCode();
    }

    public void setX(double x)
    {
        position.setX(x);
        updateCenterAndHashCode();
    }

    public void setX(int x)
    {
        position.setX(x);
        updateCenterAndHashCode();
    }

    public void setY(double y)
    {
        position.setY(y);
        updateCenterAndHashCode();
    }

    public void setY(int y)
    {
        position.setY(y);
        updateCenterAndHashCode();
    }

    public void setMaxX(double x)
    {
        position.setX(x - getWidth());
        updateCenterAndHashCode();
    }

    public void setMaxX(int x)
    {
        position.setX(x - getWidth());
        updateCenterAndHashCode();
    }

    public void setMaxY(double y)
    {
        position.setY(y - getHeight());
        updateCenterAndHashCode();
    }

    public void setMaxY(int y)
    {
        position.setY(y - getHeight());
        updateCenterAndHashCode();
    }

    public void setCenterX(double x)
    {
        center.setX(x);
        position.setX(x - getWidth() * .5);
        updateHashCode();
    }

    public void setCenterX(int x)
    {
        center.setX(x);
        position.setX(x - getWidth() * .5);
        updateHashCode();
    }

    public void setCenterY(double y)
    {
        center.setY(y);
        position.setY(y - getHeight() * .5);
        updateHashCode();
    }

    public void setCenterY(int y)
    {
        center.setY(y);
        position.setY(y - getHeight() * .5);
        updateHashCode();
    }

    public void setWidth(double width)
    {
        size.setX(width);
        updateCenterAndHashCode();
    }

    public void setWidth(int width)
    {
        size.setX(width);
        updateCenterAndHashCode();
    }

    public void setHeight(double height)
    {
        size.setY(height);
        updateCenterAndHashCode();
    }

    public void setHeight(int height)
    {
        size.setY(height);
        updateCenterAndHashCode();
    }

    public void moveBy(Point point)
    {
        position.add(point);
        updateCenterAndHashCode();
    }

    public Point getPosition()
    {
        return position;
    }

    public Point getCenter()
    {
        return center;
    }

    public Point getSize()
    {
        return size;
    }

    public Rectangle shift(Point point)
    {
        return new Rectangle(getX() + point.getX(), getY() + point.getY(), size.getX(), size.getY());
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
        return getX() + getWidth() - 1;
    }

    public int getMaxY()
    {
        return getY() + getHeight() - 1;
    }

    public int getCenterX()
    {
        return center.getX();
    }

    public int getCenterY()
    {
        return center.getY();
    }

    public boolean isEmpty()
    {
        return getWidth() == 0 || getHeight() == 0;
    }

    public boolean isTouching(Rectangle rectangle)
    {
        return rectangle.getX() <= getMaxX() && getX() <= rectangle.getMaxX() && rectangle.getY() <= getMaxY() && getY() <= rectangle.getMaxY();
    }

    public Rectangle getCut(Rectangle rectangle)
    {
        if (!isTouching(rectangle))
        {
            return new Rectangle();
        }
        int x = Math.max(getX(), rectangle.getX());
        int y = Math.max(getY(), rectangle.getY());
        int width = Math.min(getMaxX(), rectangle.getMaxX()) - x + 1;
        int height = Math.min(getMaxY(), rectangle.getMaxY()) - y + 1;
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getContainment(Rectangle rectangle)
    {
        int x = Math.min(getX(), rectangle.getX());
        int y = Math.min(getY(), rectangle.getY());
        int width = Math.max(getMaxX(), rectangle.getMaxX()) - x + 1;
        int height = Math.max(getMaxY(), rectangle.getMaxY()) - y + 1;
        return new Rectangle(x, y, width, height);
    }

    public Set<Rectangle> getSubtraction(Rectangle rectangle)
    {
        if (!isTouching(rectangle))
        {
            return Collections.singleton(this);
        }
        if (rectangle.contains(this))
        {
            return Collections.emptySet();
        }
        Set<Rectangle> subtraction = new HashSet<>();
        Rectangle component;
        component = new Rectangle(getX(), getY(), getWidth(), Math.max(0, rectangle.getY() - getY()));
        if (!component.isEmpty())
        {
            subtraction.add(component);
        }
        component = new Rectangle(getX(), rectangle.getMaxY() + 1, getWidth(), Math.max(0, getMaxY() - rectangle.getMaxY()));
        if (!component.isEmpty())
        {
            subtraction.add(component);
        }
        component = new Rectangle(getX(), rectangle.getY(), Math.max(0, rectangle.getX() - getX()), rectangle.getHeight());
        if (!component.isEmpty())
        {
            subtraction.add(component);
        }
        component = new Rectangle(rectangle.getMaxX() + 1, rectangle.getY(), Math.max(0, getMaxX() - rectangle.getMaxX()), rectangle.getHeight());
        if (!component.isEmpty())
        {
            subtraction.add(component);
        }
        return subtraction;
    }

    public boolean contains(Rectangle rectangle)
    {
        return getX() <= rectangle.getX() && getY() <= rectangle.getY() && getMaxX() >= rectangle.getMaxX() && getMaxY() >= rectangle.getMaxY();
    }

    private void updateCenterAndHashCode()
    {
        center.set(getX() + getWidth() * .5, getY() + getHeight() * .5);
        updateHashCode();
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
        return hashCode;
    }

    private void updateHashCode()
    {
        int hashCode = getX();
        hashCode = hashCode * 13 + getY();
        hashCode = hashCode * 13 + getWidth();
        hashCode = hashCode * 13 + getHeight();
        this.hashCode = hashCode;
    }

    @Override
    public String toString()
    {
        return "(" + position + ", " + size + ")";
    }
}
