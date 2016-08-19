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

    public Rectangle(GenericRectangleBuilder<?> builder)
    {
        position = new Point(builder.x, builder.y);
        size = new Point(builder.width, builder.height);
        updateCenterAndHashCode();
    }

    public final void set(Rectangle rectangle)
    {
        position.set(rectangle.position);
        size.set(rectangle.size);
        center.set(rectangle.center);
        hashCode = rectangle.hashCode;
    }

    public final void setPosition(double x, double y)
    {
        position.set(x, y);
        updateCenterAndHashCode();
    }

    public final void setPosition(int x, int y)
    {
        position.set(x, y);
        updateCenterAndHashCode();
    }

    public final void setPosition(Point position)
    {
        this.position.set(position);
        updateCenterAndHashCode();
    }

    public final void setSize(double width, double height)
    {
        size.set(width, height);
        updateCenterAndHashCode();
    }

    public final void setSize(int width, int height)
    {
        size.set(width, height);
        updateCenterAndHashCode();
    }

    public final void setSize(Point size)
    {
        this.size.set(size);
        updateCenterAndHashCode();
    }

    public final void setCenter(double x, double y)
    {
        position.set(x - getWidth() * .5, y - getHeight() * .5);
        center.set(x, y);
        updateHashCode();
    }

    public final void setCenter(int x, int y)
    {
        position.set(x - getWidth() * .5, y - getHeight() * .5);
        center.set(x, y);
        updateHashCode();
    }

    public final void setCenter(Point center)
    {
        position.set(center.getX() - getWidth() * .5, center.getY() - getHeight() * .5);
        this.center.set(center);
        updateHashCode();
    }

    public final void setX(double x)
    {
        position.setX(x);
        updateCenterAndHashCode();
    }

    public final void setX(int x)
    {
        position.setX(x);
        updateCenterAndHashCode();
    }

    public final void setY(double y)
    {
        position.setY(y);
        updateCenterAndHashCode();
    }

    public final void setY(int y)
    {
        position.setY(y);
        updateCenterAndHashCode();
    }

    public final void setMaxX(double x)
    {
        position.setX(x - getWidth());
        updateCenterAndHashCode();
    }

    public final void setMaxX(int x)
    {
        position.setX(x - getWidth());
        updateCenterAndHashCode();
    }

    public final void setMaxY(double y)
    {
        position.setY(y - getHeight());
        updateCenterAndHashCode();
    }

    public final void setMaxY(int y)
    {
        position.setY(y - getHeight());
        updateCenterAndHashCode();
    }

    public final void setCenterX(double x)
    {
        center.setX(x);
        position.setX(x - getWidth() * .5);
        updateHashCode();
    }

    public final void setCenterX(int x)
    {
        center.setX(x);
        position.setX(x - getWidth() * .5);
        updateHashCode();
    }

    public final void setCenterY(double y)
    {
        center.setY(y);
        position.setY(y - getHeight() * .5);
        updateHashCode();
    }

    public final void setCenterY(int y)
    {
        center.setY(y);
        position.setY(y - getHeight() * .5);
        updateHashCode();
    }

    public final void setWidth(double width)
    {
        size.setX(width);
        updateCenterAndHashCode();
    }

    public final void setWidth(int width)
    {
        size.setX(width);
        updateCenterAndHashCode();
    }

    public final void setHeight(double height)
    {
        size.setY(height);
        updateCenterAndHashCode();
    }

    public final void setHeight(int height)
    {
        size.setY(height);
        updateCenterAndHashCode();
    }

    public final void moveBy(Point point)
    {
        position.add(point);
        updateCenterAndHashCode();
    }

    public final Point getPosition()
    {
        return position;
    }

    public final Point getCenter()
    {
        return center;
    }

    public final Point getSize()
    {
        return size;
    }

    public final Rectangle shift(Point point)
    {
        return new RectangleBuilder().setX(getX() + point.getX()).setY(getY() + point.getY()).setSize(size).build();
    }

    public final int getX()
    {
        return position.getX();
    }

    public final int getY()
    {
        return position.getY();
    }

    public final int getWidth()
    {
        return size.getX();
    }

    public final int getHeight()
    {
        return size.getY();
    }

    public final int getMaxX()
    {
        return getX() + getWidth() - 1;
    }

    public final int getMaxY()
    {
        return getY() + getHeight() - 1;
    }

    public final int getCenterX()
    {
        return center.getX();
    }

    public final int getCenterY()
    {
        return center.getY();
    }

    public final boolean isEmpty()
    {
        return getWidth() == 0 || getHeight() == 0;
    }

    public final boolean isTouching(Rectangle rectangle)
    {
        return rectangle.getX() <= getMaxX() && getX() <= rectangle.getMaxX() && rectangle.getY() <= getMaxY() && getY() <= rectangle.getMaxY();
    }

    public final Rectangle getCut(Rectangle rectangle)
    {
        if (!isTouching(rectangle))
        {
            return new RectangleBuilder().build();
        }
        RectangleBuilder builder = new RectangleBuilder();
        builder.setX(Math.max(getX(), rectangle.getX()));
        builder.setY(Math.max(getY(), rectangle.getY()));
        builder.setWidth(Math.min(getMaxX(), rectangle.getMaxX()) - builder.x + 1);
        builder.setHeight(Math.min(getMaxY(), rectangle.getMaxY()) - builder.y + 1);
        return builder.build();
    }

    public final Rectangle getContainment(Rectangle rectangle)
    {
        RectangleBuilder builder = new RectangleBuilder();
        builder.setX(Math.min(getX(), rectangle.getX()));
        builder.setY(Math.min(getY(), rectangle.getY()));
        builder.setWidth(Math.max(getMaxX(), rectangle.getMaxX()) - builder.x + 1);
        builder.setHeight(Math.max(getMaxY(), rectangle.getMaxY()) - builder.y + 1);
        return builder.build();
    }

    public final Set<Rectangle> getSubtraction(Rectangle rectangle)
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
        component = new RectangleBuilder().set(this).setHeight(Math.max(0, rectangle.getY() - getY())).build();
        if (!component.isEmpty())
        {
            subtraction.add(component);
        }
        component = new RectangleBuilder().set(this).setY(rectangle.getMaxY() + 1).setHeight(Math.max(0, getMaxY() - rectangle.getMaxY())).build();
        if (!component.isEmpty())
        {
            subtraction.add(component);
        }
        component = new RectangleBuilder().set(this).setWidth(Math.max(0, rectangle.getX() - getX())).build();
        if (!component.isEmpty())
        {
            subtraction.add(component);
        }
        component = new RectangleBuilder().set(this).setX(rectangle.getMaxX() + 1).setWidth(Math.max(0, getMaxX() - rectangle.getMaxX())).build();
        if (!component.isEmpty())
        {
            subtraction.add(component);
        }
        return subtraction;
    }

    public final boolean contains(Rectangle rectangle)
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
    public final int hashCode()
    {
        return hashCode;
    }

    protected final void updateHashCode()
    {
        hashCode = computeHashCode();
    }

    protected int computeHashCode()
    {
        int hashCode = getX();
        hashCode = hashCode * 13 + getY();
        hashCode = hashCode * 13 + getWidth();
        hashCode = hashCode * 13 + getHeight();
        return hashCode;
    }

    @Override
    public String toString()
    {
        return "(" + position + ", " + size + ")";
    }

    public static abstract class GenericRectangleBuilder<T extends GenericRectangleBuilder<T>> extends GenericBuilder<T>
    {
        protected int x = 0;

        protected int y = 0;

        protected int width = 0;

        protected int height = 0;

        public T setX(int x)
        {
            this.x = x;
            return getThis();
        }

        public T setY(int y)
        {
            this.y = y;
            return getThis();
        }

        public T setWidth(int width)
        {
            this.width = width;
            return getThis();
        }

        public T setHeight(int height)
        {
            this.height = height;
            return getThis();
        }

        public T setPosition(Point position)
        {
            x = position.getX();
            y = position.getY();
            return getThis();
        }

        public T setSize(Point size)
        {
            width = size.getX();
            height = size.getY();
            return getThis();
        }

        public T set(Rectangle rectangle)
        {
            x = rectangle.getX();
            y = rectangle.getY();
            width = rectangle.getWidth();
            height = rectangle.getHeight();
            return getThis();
        }

        public Rectangle build()
        {
            return new Rectangle(this);
        }
    }

    public static final class RectangleBuilder extends GenericRectangleBuilder<RectangleBuilder>
    {
        @Override
        protected RectangleBuilder getThis()
        {
            return this;
        }
    }
}
