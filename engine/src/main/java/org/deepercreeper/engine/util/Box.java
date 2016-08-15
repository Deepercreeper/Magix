package org.deepercreeper.engine.util;

public class Box
{
    private final Vector center = new Vector();

    private final Vector position;

    private final Vector size;

    private int hashCode;

    private boolean hashCodeComputed = false;

    public Box(double x, double y, double width, double height)
    {
        position = new Vector(x, y);
        size = new Vector(width, height);
        updateCenterAndHashCode();
    }

    public Box(double x, double y)
    {
        this(x, y, 0, 0);
    }

    public Box(Vector position, Vector size)
    {
        this(position.getX(), position.getY(), size.getX(), size.getY());
    }

    public Box(Vector position)
    {
        this(position, new Vector());
    }

    public Box(Box box)
    {
        this(box.position, box.size);
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
        return new Box(getX() + x, getY(), size.getX(), size.getY());
    }

    public final Box shiftY(double y)
    {
        return new Box(getX(), getY() + y, size.getX(), size.getY());
    }

    public final Box shift(Vector vector)
    {
        return new Box(getX() + vector.getX(), getY() + vector.getY(), size.getX(), size.getY());
    }

    public final Box getContainment(Box box)
    {
        double x = Math.min(getX(), box.getX());
        double y = Math.min(getY(), box.getY());
        double width = Math.max(getMaxX(), box.getMaxX()) - x;
        double height = Math.max(getMaxY(), box.getMaxY()) - y;
        return new Box(x, y, width, height);
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
}
