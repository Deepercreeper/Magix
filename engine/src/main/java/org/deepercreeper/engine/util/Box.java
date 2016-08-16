package org.deepercreeper.engine.util;

public class Box
{
    private final Vector center = new Vector();

    private final Vector position;

    private final Vector size;

    private int hashCode;

    private boolean hashCodeComputed = false;

    protected Box(GenericBoxBuilder builder)
    {
        position = new Vector(builder.x, builder.y);
        size = new Vector(builder.width, builder.height);
        updateCenterAndHashCode();
    }

    public final void set(Box box)
    {
        position.set(box.position);
        size.set(box.size);
        center.set(box.center);
        hashCode = box.hashCode;
    }

    public final void setPosition(double x, double y)
    {
        position.set(x, y);
        updateCenterAndHashCode();
    }

    public final void setPosition(Vector position)
    {
        this.position.set(position);
        updateCenterAndHashCode();
    }

    public final void setSize(double width, double height)
    {
        size.set(width, height);
        updateCenterAndHashCode();
    }

    public final void setSize(Vector size)
    {
        this.size.set(size);
        updateCenterAndHashCode();
    }

    public final void setCenter(double x, double y)
    {
        center.set(x, y);
        position.set(x - getWidth() * .5, y - getHeight() * .5);
        updateHashCode();
    }

    public final void setCenter(Vector center)
    {
        this.center.set(center);
        position.set(center.getX() - getWidth() * .5, center.getY() - getHeight() * .5);
        updateHashCode();
    }

    public final void setX(double x)
    {
        position.setX(x);
        updateCenterAndHashCode();
    }

    public final void setY(double y)
    {
        position.setY(y);
        updateCenterAndHashCode();
    }

    public final void setMaxX(double x)
    {
        position.setX(x - getWidth());
        updateCenterAndHashCode();
    }

    public final void setMaxY(double y)
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

    public final void setCenterY(double y)
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

    public final void setHeight(double height)
    {
        size.setY(height);
        updateCenterAndHashCode();
    }

    public final void moveBy(double x, double y)
    {
        position.add(x, y);
        updateCenterAndHashCode();
    }

    public final void moveBy(Vector vector)
    {
        position.add(vector);
        updateCenterAndHashCode();
    }

    public final Vector getPosition()
    {
        return position;
    }

    public final Vector getCenter()
    {
        return center;
    }

    public final Vector getSize()
    {
        return size;
    }

    public final double getX()
    {
        return position.getX();
    }

    public final double getY()
    {
        return position.getY();
    }

    public final double getWidth()
    {
        return size.getX();
    }

    public final double getHeight()
    {
        return size.getY();
    }

    public final double getMaxX()
    {
        return getX() + getWidth();
    }

    public final double getMaxY()
    {
        return getY() + getHeight();
    }

    public final double getCenterX()
    {
        return center.getX();
    }

    public final double getCenterY()
    {
        return center.getY();
    }

    public final boolean isEmpty()
    {
        return getWidth() == 0 || getHeight() == 0;
    }

    public final boolean isTouching(Box box)
    {
        return box.getX() <= getMaxX() && getX() <= box.getMaxX() && box.getY() <= getMaxY() && getY() <= box.getMaxY();
    }

    public final Box shiftX(double x)
    {
        return new BoxBuilder().set(this).setX(getX() + x).build();
    }

    public final Box shiftY(double y)
    {
        return new BoxBuilder().set(this).setY(getY() + y).build();
    }

    public final Box shift(Vector vector)
    {
        return new BoxBuilder().setX(getX() + vector.getX()).setY(getY() + vector.getY()).setSize(size).build();
    }

    public final Box getContainment(Box box)
    {
        BoxBuilder builder = new BoxBuilder();
        builder.setX(Math.min(getX(), box.getX()));
        builder.setY(Math.min(getY(), box.getY()));
        builder.setWidth(Math.max(getMaxX(), box.getMaxX()) - builder.x);
        builder.setHeight(Math.max(getMaxY(), box.getMaxY()) - builder.y);
        return builder.build();
    }

    public final Box getContainment(Box... boxes)
    {
        Box containmentBox = this;
        for (Box box : boxes)
        {
            containmentBox = containmentBox.getContainment(box);
        }
        return containmentBox;
    }

    public final Rectangle asScaledRectangle(double scale)
    {
        return new Rectangle(position.times(scale).asPoint(), size.times(scale).asPoint());
    }

    private void updateCenterAndHashCode()
    {
        center.set(getX() + getWidth() * .5, getY() * getHeight() * .5);
        updateHashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Box)
        {
            Box box = (Box) obj;
            return position.equals(box.position) && size.equals(box.size);
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
        if (!hashCodeComputed || !isHashCodeFinal())
        {
            hashCode = computeHashCode();
            hashCodeComputed = true;
        }
    }

    protected boolean isHashCodeFinal()
    {
        return false;
    }

    protected int computeHashCode()
    {
        int hashCode = Double.hashCode(getX());
        hashCode = hashCode * 13 + Double.hashCode(getY());
        hashCode = hashCode * 13 + Double.hashCode(getWidth());
        hashCode = hashCode * 13 + Double.hashCode(getHeight());
        return hashCode;
    }

    @Override
    public String toString()
    {
        return "(" + position + ", " + size + ")";
    }

    public static abstract class GenericBoxBuilder<T extends GenericBoxBuilder<T>> extends GenericBuilder<T>
    {
        protected double x = 0;

        protected double y = 0;

        protected double width = 0;

        protected double height = 0;

        public T setX(double x)
        {
            this.x = x;
            return getThis();
        }

        public T setY(double y)
        {
            this.y = y;
            return getThis();
        }

        public T setWidth(double width)
        {
            this.width = width;
            return getThis();
        }

        public T setHeight(double height)
        {
            this.height = height;
            return getThis();
        }

        public T setPosition(Vector position)
        {
            x = position.getX();
            y = position.getY();
            return getThis();
        }

        public T setSize(Vector size)
        {
            width = size.getX();
            height = size.getY();
            return getThis();
        }

        public T set(Box box)
        {
            x = box.getX();
            y = box.getY();
            width = box.getWidth();
            height = box.getHeight();
            return getThis();
        }

        public Box build()
        {
            return new Box(this);
        }
    }

    public static final class BoxBuilder extends GenericBoxBuilder<BoxBuilder>
    {
        @Override
        protected BoxBuilder getThis()
        {
            return this;
        }
    }
}
