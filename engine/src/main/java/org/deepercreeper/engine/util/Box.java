package org.deepercreeper.engine.util;

public class Box
{
    private final Vector position = new Vector();

    private final Vector size = new Vector();

    private final Vector center = new Vector();

    private int hashCode;

    private boolean hashCodeComputed = false;

    protected Box(GenericBoxBuilder<?> builder)
    {
        setPosition(builder.x, builder.y);
        setSize(builder.width, builder.height);
        updateCenterAndHashCode();
    }

    public void setX(double x)
    {
        getPosition().setX(x);
        updateCenterAndHashCode();
    }

    public void setY(double y)
    {
        getPosition().setY(y);
        updateCenterAndHashCode();
    }

    public void setPosition(double x, double y)
    {
        getPosition().set(x, y);
        updateCenterAndHashCode();
    }

    public void setWidth(double width)
    {
        getSize().setX(width);
        updateCenterAndHashCode();
    }

    public void setHeight(double height)
    {
        getSize().setY(height);
        updateCenterAndHashCode();
    }

    public void setSize(double width, double height)
    {
        getSize().set(width, height);
        updateCenterAndHashCode();
    }

    public void setCenterX(double x)
    {
        getCenter().setX(x);
        getPosition().setX(x - getWidth() * .5);
        updateHashCode();
    }

    public void setCenterY(double y)
    {
        getCenter().setY(y);
        getPosition().setY(y - getHeight() * .5);
        updateHashCode();
    }

    public void setCenter(double x, double y)
    {
        getCenter().set(x, y);
        getPosition().set(x - getWidth() * .5, y - getHeight() * .5);
        updateHashCode();
    }

    public Vector getPosition()
    {
        return position;
    }

    public Vector getCenter()
    {
        return center;
    }

    public Vector getSize()
    {
        return size;
    }

    public final void set(Box box)
    {
        setPosition(box.getPosition());
        setSize(box.getSize());
    }

    public final void setPosition(Vector position)
    {
        setPosition(position.getX(), position.getY());
    }

    public final void setSize(Vector size)
    {
        setSize(size.getX(), size.getY());
    }

    public final void setCenter(Vector center)
    {
        setCenter(center.getX(), center.getY());
    }

    public final void setMaxX(double x)
    {
        setX(x - getWidth());
    }

    public final void setMaxY(double y)
    {
        setY(y - getHeight());
    }

    public final void moveBy(double x, double y)
    {
        setPosition(getX() + x, getY() + y);
    }

    public final void moveBy(Vector vector)
    {
        moveBy(vector.getX(), vector.getY());
    }

    public final double getX()
    {
        return getPosition().getX();
    }

    public final double getY()
    {
        return getPosition().getY();
    }

    public final double getWidth()
    {
        return getSize().getX();
    }

    public final double getHeight()
    {
        return getSize().getY();
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
        return getCenter().getX();
    }

    public final double getCenterY()
    {
        return getCenter().getY();
    }

    public final double getVolume() { return getWidth() * getHeight(); }

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
        return new BoxBuilder().setX(getX() + vector.getX()).setY(getY() + vector.getY()).setSize(getSize()).build();
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
        return new Rectangle.RectangleBuilder().setPosition(getPosition().times(scale).asPoint()).setSize(getSize().times(scale).asPoint()).build();
    }

    private void updateCenterAndHashCode()
    {
        getCenter().set(getX() + getWidth() * .5, getY() * getHeight() * .5);
        updateHashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Box)
        {
            Box box = (Box) obj;
            return getPosition().equals(box.getPosition()) && getSize().equals(box.getSize());
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
        return "(" + getPosition() + ", " + getSize() + ")";
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
