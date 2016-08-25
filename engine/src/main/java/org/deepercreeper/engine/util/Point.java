package org.deepercreeper.engine.util;

public class Point
{
    private final Runnable modifyAction;

    private int x;

    private int y;

    private int hashCode;

    public Point(int x, int y, Runnable modifyAction)
    {
        this.x = x;
        this.y = y;
        this.modifyAction = modifyAction;
        updateHashCode();
    }

    public Point(int x, int y)
    {
        this(x, y, null);
    }

    public Point(double x, double y)
    {
        this((int) Math.round(x), (int) Math.round(y));
    }

    public Point(Point point)
    {
        this(point.getX(), point.getY());
    }

    public Point(Runnable modifyAction)
    {
        this(0, 0, modifyAction);
    }

    public Point()
    {
        this(0, 0);
    }

    public final void setX(int x)
    {
        if (this.x != x)
        {
            this.x = x;
            modified();
        }
    }

    public final void setY(int y)
    {
        if (this.y != y)
        {
            this.y = y;
            modified();
        }
    }

    public final void set(int x, int y)
    {
        if (this.x != x || this.y != y)
        {
            this.x = x;
            this.y = y;
            modified();
        }
    }

    public final void set(double x, double y)
    {
        set((int) Math.round(x), (int) Math.round(y));
    }

    public final void set(Point point)
    {
        set(point.getX(), point.getY());
    }

    public final void setX(double x)
    {
        setX((int) Math.round(x));
    }

    public final void setY(double y)
    {
        setY((int) Math.round(y));
    }

    public final void add(double x, double y)
    {
        add((int) Math.round(x), (int) Math.round(y));
    }

    public final void add(int x, int y)
    {
        set(getX() + x, getY() + y);
    }

    public final void add(Point point, double scalar)
    {
        add(point.getX() * scalar, point.getY() * scalar);
    }

    public final void add(Point point, int scalar)
    {
        add(point.getX() * scalar, point.getY() * scalar);
    }

    public final void add(Point point)
    {
        add(point.getX(), point.getY());
    }

    public final void subtract(Point point)
    {
        add(-point.getX(), -point.getY());
    }

    public final int getX()
    {
        return x;
    }

    public final int getY()
    {
        return y;
    }

    private void modified()
    {
        updateHashCode();
        if (modifyAction != null)
        {
            modifyAction.run();
        }
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

    private void updateHashCode()
    {
        hashCode = getX() * 13 + getY();
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
