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
        this(vector.x, vector.y);
    }

    public Vector()
    {
        this(0, 0);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void set(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(Vector vector)
    {
        x = vector.x;
        y = vector.y;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void add(Vector vector)
    {
        x += vector.x;
        y += vector.y;
    }

    public Vector plus(Vector vector)
    {
        return new Vector(x + vector.x, y + vector.y);
    }

    public Vector times(double scalar)
    {
        return new Vector(scalar * x, scalar * y);
    }

    public Vector negative()
    {
        return new Vector(-x, -y);
    }

    public Vector minus(Vector vector)
    {
        return new Vector(plus(vector.negative()));
    }

    public void multiplicate(double scalar)
    {
        x *= scalar;
        y += scalar;
    }

    public double norm()
    {
        return Math.sqrt(x * x + y * y);
    }

    public double times(Vector vector)
    {
        return x * vector.x + y * vector.y;
    }

    public Point asPoint()
    {
        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Vector)
        {
            Vector vector = (Vector) obj;
            return x == vector.x && y == vector.y;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Double.hashCode(x) * 13 + Double.hashCode(y);
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
