package org.deepercreeper.engine.util;

public class Box
{
    private final Vector position;

    private final Vector size;

    public Box(double x, double y, double width, double height)
    {
        position = new Vector(x, y);
        size = new Vector(width, height);
    }

    public Box(double x, double y)
    {
        this(x, y, 0, 0);
    }

    public Box(Vector position, Vector size)
    {
        this.position = new Vector(position);
        this.size = new Vector(size);
    }

    public Box(Vector position)
    {
        this(position, new Vector());
    }

    public Box(Box box)
    {
        this(box.position, box.size);
    }

    public Vector getPosition()
    {
        return new Vector(position);
    }

    public Vector getCenter()
    {
        return position.plus(size.times(0.5));
    }

    public Vector getSize()
    {
        return new Vector(size);
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

    public void moveTo(Vector position)
    {
        this.position.set(position);
    }

    public void move(Vector vector)
    {
        position.add(vector);
    }

    public Box shift(Vector vector)
    {
        return new Box(position.plus(vector), size);
    }

    public boolean isTouching(Box box)
    {
        return !(getMaxX() < box.getX() || box.getMaxX() < getX() || getMaxY() < box.getY() || box.getMaxY() < getY());
    }

    public Box getContainment(Box box)
    {
        double x = Math.min(getX(), box.getX());
        double y = Math.min(getY(), box.getY());
        double width = Math.max(getMaxX(), box.getMaxX()) - x;
        double height = Math.max(getMaxY(), box.getMaxY()) - y;
        return new Box(x, y, width, height);
    }

    public Rectangle asRectangle()
    {
        return new Rectangle(position.asPoint(), size.asPoint());
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
        return position.hashCode() * 17 + size.hashCode();
    }

    @Override
    public String toString()
    {
        return "(" + position + ", " + size + ")";
    }
}
