package org.deepercreeper.engine.util;

public class Point
{
    private int x;

    private int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Point(Point point)
    {
        this(point.x, point.y);
    }

    public Point()
    {
        x = 0;
        y = 0;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void set(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void add(Point point)
    {
        x += point.x;
        y += point.y;
    }

    public Point plus(Point point)
    {
        return new Point(x + point.x, y + point.y);
    }

    public Point times(double scalar)
    {
        return new Point((int) Math.round(x * scalar), (int) Math.round(y * scalar));
    }

    public Point times(int scalar)
    {
        return new Point(x * scalar, y * scalar);
    }

    public Point negative()
    {
        return new Point(-x, -y);
    }

    public Point minus(Point point)
    {
        return plus(point.negative());
    }

    public void multiplicate(int scalar)
    {
        x *= scalar;
        y *= scalar;
    }

    public double norm()
    {
        return Math.sqrt(x * x + y * y);
    }

    public int times(Point point)
    {
        return x * point.x + y * point.y;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Point)
        {
            Point point = (Point) obj;
            return x == point.x && y == point.y;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return x * 13 + y;
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
