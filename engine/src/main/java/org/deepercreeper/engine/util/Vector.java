package org.deepercreeper.engine.util;

public class Vector
{
    private double x;

    private double y;

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector vector)
    {
        this(vector.getX(), vector.getY());
    }

    public Vector()
    {
        this(0, 0);
    }

    public final void setX(double x)
    {
        this.x = x;
        modified();
    }

    public final void setY(double y)
    {
        this.y = y;
        modified();
    }

    public final void set(double x, double y)
    {
        this.x = x;
        this.y = y;
        modified();
    }

    public final double getX()
    {
        return x;
    }

    public final double getY()
    {
        return y;
    }

    public final void set(Vector vector)
    {
        set(vector.getX(), vector.getY());
    }

    public final void add(Vector vector, double scalar)
    {
        add(vector.getX() * scalar, vector.getY() * scalar);
    }

    public final void add(double x, double y)
    {
        set(getX() + x, getY() + y);
    }

    public final void add(Vector vector)
    {
        add(vector.getX(), vector.getY());
    }

    public final double getAbsX()
    {
        return Math.abs(getX());
    }

    public final double getAbsY()
    {
        return Math.abs(getY());
    }

    public final Vector absolute()
    {
        return new Vector(getAbsX(), getAbsY());
    }

    public final Vector plus(Vector vector)
    {
        return new Vector(getX() + vector.getX(), getY() + vector.getY());
    }

    public final Vector minus(Vector vector)
    {
        return new Vector(getX() - vector.getX(), getY() - vector.getY());
    }

    public final Vector times(double scalar)
    {
        return new Vector(getX() * scalar, getY() * scalar);
    }

    public final double norm()
    {
        return Math.sqrt(getX() * getX() + getY() * getY());
    }

    public final Point asPoint()
    {
        return new Point(getX(), getY());
    }

    protected void modified()
    {
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Vector)
        {
            Vector vector = (Vector) obj;
            return getX() == vector.getX() && getY() == vector.getY();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Double.hashCode(getX()) * 13 + Double.hashCode(getY());
    }

    @Override
    public String toString()
    {
        return "(" + getX() + ", " + getY() + ")";
    }
}
