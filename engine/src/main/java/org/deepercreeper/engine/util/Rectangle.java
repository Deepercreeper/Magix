package org.deepercreeper.engine.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Rectangle
{
    private final Point position = new Point(this::updateCenterAndHashCode);

    private final Point size = new Point(this::updateCenterAndHashCode);

    private final Point center = new Point();

    private int hashCode;

    public Rectangle(GenericRectangleBuilder<?> builder)
    {
        setPosition(builder.x, builder.y);
        setSize(builder.width, builder.height);
    }

    public final void setX(int x)
    {
        getPosition().setX(x);
    }

    public final void setX(double x)
    {
        getPosition().setX(x);
    }

    public final void setY(int y)
    {
        getPosition().setY(y);
    }

    public final void setY(double y)
    {
        getPosition().setY(y);
    }

    public final void setPosition(int x, int y)
    {
        getPosition().set(x, y);
    }

    public final void setPosition(double x, double y)
    {
        getPosition().set(x, y);
    }

    public final void setPosition(Point position)
    {
        getPosition().set(position);
    }

    public final void setWidth(int width)
    {
        getSize().setX(width);
    }

    public final void setWidth(double width)
    {
        getSize().setX(width);
    }

    public final void setHeight(int height)
    {
        getSize().setY(height);
    }

    public final void setHeight(double height)
    {
        getSize().setY(height);
    }

    public final void setSize(int width, int height)
    {
        getSize().set(width, height);
    }

    public final void setSize(double width, double height)
    {
        getSize().set(width, height);
    }

    public final void setSize(Point size)
    {
        getSize().set(size);
    }

    public final void set(Rectangle rectangle)
    {
        setPosition(rectangle.getPosition());
        setSize(rectangle.getSize());
    }

    public final void setCenter(double x, double y)
    {
        getPosition().set(x - getWidth() * .5, y - getHeight() * .5);
    }

    public final void setCenter(int x, int y)
    {
        getPosition().set(x - getWidth() * .5, y - getHeight() * .5);
    }

    public final void setCenter(Point center)
    {
        getPosition().set(center.getX() - getWidth() * .5, center.getY() - getHeight() * .5);
    }

    public final void setMaxX(double x)
    {
        setX(x - getWidth());
    }

    public final void setMaxX(int x)
    {
        setX(x - getWidth());
    }

    public final void setMaxY(double y)
    {
        setY(y - getHeight());
    }

    public final void setMaxY(int y)
    {
        setY(y - getHeight());
    }

    public final void setCenterX(double x)
    {
        setX(x - getWidth() * .5);
    }

    public final void setCenterX(int x)
    {
        setX(x - getWidth() * .5);
    }

    public final void setCenterY(double y)
    {
        setY(y - getHeight() * .5);
    }

    public final void setCenterY(int y)
    {
        setY(y - getHeight() * .5);
    }

    public final void moveBy(double x, double y)
    {
        getPosition().add(x, y);
    }

    public final void moveBy(int x, int y)
    {
        getPosition().add(x, y);
    }

    public final void moveBy(Point point)
    {
        getPosition().add(point);
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
        return new RectangleBuilder().setX(getX() + point.getX()).setY(getY() + point.getY()).setSize(getSize()).build();
    }

    public final int getX()
    {
        return getPosition().getX();
    }

    public final int getY()
    {
        return getPosition().getY();
    }

    public final int getWidth()
    {
        return getSize().getX();
    }

    public final int getHeight()
    {
        return getSize().getY();
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
        return getCenter().getX();
    }

    public final int getCenterY()
    {
        return getCenter().getY();
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
        getCenter().set(getX() + getWidth() * .5, getY() + getHeight() * .5);
        updateHashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Rectangle)
        {
            Rectangle rectangle = (Rectangle) obj;
            return getPosition().equals(rectangle.getPosition()) && getSize().equals(rectangle.getSize());
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
        return "(" + getPosition() + ", " + getSize() + ")";
    }

    public static abstract class GenericRectangleBuilder <T extends GenericRectangleBuilder<T>> extends GenericBuilder<T>
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
