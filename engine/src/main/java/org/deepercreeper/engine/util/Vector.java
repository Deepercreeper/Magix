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

    public void add(Vector vector, double scalar)
    {
        x += vector.x * scalar;
        y += vector.y * scalar;
    }

    public void add(double x, double y)
    {
        this.x += x;
        this.y += y;
    }

    public void add(Vector vector)
    {
        add(vector.x, vector.y);
    }

    public void subtract(Vector vector, double scalar)
    {
        add(vector, -scalar);
    }

    public void subtract(Vector vector)
    {
        x -= vector.x;
        y -= vector.y;
    }

    public void multiplicate(double scalar)
    {
        x *= scalar;
        y *= scalar;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getAbsX()
    {
        return Math.abs(x);
    }

    public double getAbsY()
    {
        return Math.abs(y);
    }

    public Vector absolute(double scalar)
    {
        return new Vector(getAbsX() * Math.abs(scalar), getAbsY() * Math.abs(scalar));
    }

    public Vector absolute()
    {
        return new Vector(getAbsX(), getAbsY());
    }

    public Vector plus(Vector vector, double scalar)
    {
        return new Vector(x + vector.x * scalar, y + vector.y * scalar);
    }

    public Vector plus(Vector vector)
    {
        return new Vector(x + vector.x, y + vector.y);
    }

    public Vector minus(Vector vector, double scalar)
    {
        return plus(vector, -scalar);
    }

    public Vector minus(Vector vector)
    {
        return new Vector(x - vector.x, y - vector.y);
    }

    public Vector times(double scalar)
    {
        return new Vector(scalar * x, scalar * y);
    }

    public Vector negative()
    {
        return new Vector(-x, -y);
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
        return new Point(x, y);
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
