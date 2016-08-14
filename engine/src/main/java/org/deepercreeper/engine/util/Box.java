package org.deepercreeper.engine.util;

public class Box
{
    private final Vector center = new Vector();

    private final Vector position;

    private final Vector size;

    private int hashCode;

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

    public void set(Box box)
    {
        position.set(box.position);
        size.set(box.size);
        center.set(box.center);
        hashCode = box.hashCode;
    }

    public void setPosition(double x, double y)
    {
        position.set(x, y);
        updateCenterAndHashCode();
    }

    public void setPosition(Vector position)
    {
        this.position.set(position);
        updateCenterAndHashCode();
    }

    public void setSize(double width, double height)
    {
        size.set(width, height);
        updateCenterAndHashCode();
    }

    public void setSize(Vector size)
    {
        this.size.set(size);
        updateCenterAndHashCode();
    }

    public void setCenter(double x, double y)
    {
        center.set(x, y);
        position.set(x - getWidth() * .5, y - getHeight() * .5);
        updateHashCode();
    }

    public void setCenter(Vector center)
    {
        this.center.set(center);
        position.set(center.getX() - getWidth() * .5, center.getY() - getHeight() * .5);
        updateHashCode();
    }

    public void setX(double x)
    {
        position.setX(x);
        updateCenterAndHashCode();
    }

    public void setY(double y)
    {
        position.setY(y);
        updateCenterAndHashCode();
    }

    public void setMaxX(double x)
    {
        position.setX(x - getWidth());
        updateCenterAndHashCode();
    }

    public void setMaxY(double y)
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

    public void setCenterY(double y)
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

    public void setHeight(double height)
    {
        size.setY(height);
        updateCenterAndHashCode();
    }

    public void moveBy(double x, double y)
    {
        position.add(x, y);
        updateCenterAndHashCode();
    }

    public void moveBy(Vector vector)
    {
        position.add(vector);
        updateCenterAndHashCode();
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

    public double getX()
    {
        return position.getX();
    }

    public double getY()
    {
        return position.getY();
    }

    public double getWidth()
    {
        return size.getX();
    }

    public double getHeight()
    {
        return size.getY();
    }

    public double getMaxX()
    {
        return getX() + getWidth();
    }

    public double getMaxY()
    {
        return getY() + getHeight();
    }

    public double getCenterX()
    {
        return center.getX();
    }

    public double getCenterY()
    {
        return center.getY();
    }

    public boolean isEmpty()
    {
        return getWidth() == 0 || getHeight() == 0;
    }

    public boolean isTouching(Box box)
    {
        return box.getX() <= getMaxX() && getX() <= box.getMaxX() && box.getY() <= getMaxY() && getY() <= box.getMaxY();
    }

    public Box shift(Vector vector)
    {
        return new Box(getX() + vector.getX(), getY() + vector.getY(), size.getX(), size.getY());
    }

    public Box getContainment(Box box)
    {
        double x = Math.min(getX(), box.getX());
        double y = Math.min(getY(), box.getY());
        double width = Math.max(getMaxX(), box.getMaxX()) - x;
        double height = Math.max(getMaxY(), box.getMaxY()) - y;
        return new Box(x, y, width, height);
    }

    public Rectangle asScaledRectangle(double scale)
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
    public int hashCode()
    {
        return hashCode;
    }

    private void updateHashCode()
    {
        int hashCode = Double.hashCode(getX());
        hashCode = hashCode * 13 + Double.hashCode(getY());
        hashCode = hashCode * 13 + Double.hashCode(getWidth());
        hashCode = hashCode * 13 + Double.hashCode(getHeight());
        this.hashCode = hashCode;
    }

    @Override
    public String toString()
    {
        return "(" + position + ", " + size + ")";
    }
}
