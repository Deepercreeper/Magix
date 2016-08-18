package org.deepercreeper.engine.util;

public class Point
{
    private int x;

    private int y;

    public Point(double x, double y)
    {
        this((int) Math.round(x), (int) Math.round(y));
    }

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
        this(0, 0);
    }

    public final void set(double x, double y)
    {
        this.x = (int) Math.round(x);
        this.y = (int) Math.round(y);
    }

    public final void set(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public final void set(Point point)
    {
        x = point.x;
        y = point.y;
    }

    public final void setX(double x)
    {
        this.x = (int) Math.round(x);
    }

    public final void setX(int x)
    {
        this.x = x;
    }

    public final void setY(double y)
    {
        this.y = (int) Math.round(y);
    }

    public final void setY(int y)
    {
        this.y = y;
    }

    public final void add(double x, double y)
    {
        this.x = (int) Math.round(this.x + x);
        this.y = (int) Math.round(this.y + y);
    }

    public final void add(int x, int y)
    {
        this.x += x;
        this.y += y;
    }

    public final void add(Point point, double scalar)
    {
        add(point.x * scalar, point.y * scalar);
    }

    public final void add(Point point, int scalar)
    {
        add(point.x * scalar, point.y * scalar);
    }

    public final void add(Point point)
    {
        x += point.x;
        y += point.y;
    }

    public final void subtract(Point point, double scalar)
    {
        add(point, -scalar);
    }

    public final void subtract(Point point, int scalar)
    {
        add(point, -scalar);
    }

    public final void subtract(Point point)
    {
        x -= point.x;
        y -= point.y;
    }

    public final void multiplicate(double scalar)
    {
        x *= scalar;
        y *= scalar;
    }

    public final void multiplicate(int scalar)
    {
        x *= scalar;
        y *= scalar;
    }

    public final int getX()
    {
        return x;
    }

    public final int getY()
    {
        return y;
    }

    public final int getAbsX()
    {
        return Math.abs(x);
    }

    public final int getAbsY()
    {
        return Math.abs(y);
    }

    public final Point plus(Point point, double scalar)
    {
        return new Point(x + point.x * scalar, y + point.y * scalar);
    }

    public final Point plus(Point point, int scalar)
    {
        return new Point(x + point.x * scalar, y + point.y * scalar);
    }

    public final Point plus(Point point)
    {
        return new Point(x + point.x, y + point.y);
    }

    public final Point minus(Point point, double scalar)
    {
        return plus(point, -scalar);
    }

    public final Point minus(Point point, int scalar)
    {
        return plus(point, -scalar);
    }

    public final Point minus(Point point)
    {
        return new Point(x - point.x, y - point.y);
    }

    public final Point times(double scalar)
    {
        return new Point(x * scalar, y * scalar);
    }

    public final Point times(int scalar)
    {
        return new Point(x * scalar, y * scalar);
    }

    public final Point negative()
    {
        return new Point(-x, -y);
    }

    public final double norm()
    {
        return Math.sqrt(x * x + y * y);
    }

    public final int times(Point point)
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
