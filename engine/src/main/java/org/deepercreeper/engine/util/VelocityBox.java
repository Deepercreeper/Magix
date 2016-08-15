package org.deepercreeper.engine.util;

public class VelocityBox extends Box
{
    private final Vector velocity = new Vector();

    public VelocityBox(double x, double y, double width, double height, double xVelocity, double yVelocity)
    {
        super(x, y, width, height);
        setVelocity(xVelocity, yVelocity);
    }

    public VelocityBox(double x, double y, double width, double height)
    {
        super(x, y, width, height);
    }

    public VelocityBox(double x, double y)
    {
        super(x, y);
    }

    public VelocityBox(Vector position, Vector size, Vector velocity)
    {
        super(position, size);
        setVelocity(velocity);
    }

    public VelocityBox(Vector position, Vector size)
    {
        super(position, size);
    }

    public VelocityBox(Vector position)
    {
        super(position);
    }

    public VelocityBox(Box box, double xVelocity, double yVelocity)
    {
        super(box);
        setVelocity(xVelocity, yVelocity);
    }

    public VelocityBox(Box box, Vector velocity)
    {
        super(box);
        setVelocity(velocity);
    }

    public VelocityBox(Box box)
    {
        super(box);
    }

    public VelocityBox(VelocityBox velocityBox)
    {
        super(velocityBox);
        setVelocity(velocityBox.velocity);
    }

    public final void setVelocity(double xVelocity, double yVelocity)
    {
        velocity.set(xVelocity, yVelocity);
        updateHashCode();
    }

    public final void setVelocity(Vector velocity)
    {
        this.velocity.set(velocity);
        updateHashCode();
    }

    public final void setXVelocity(double xVelocity)
    {
        velocity.setX(xVelocity);
        updateHashCode();
    }

    public final void setYVelocity(double yVelocity)
    {
        velocity.setY(yVelocity);
        updateHashCode();
    }

    public final Vector getVelocity()
    {
        return velocity;
    }

    public final double getXVelocity()
    {
        return velocity.getX();
    }

    public final double getYVelocity()
    {
        return velocity.getY();
    }

    @Override
    protected int computeHashCode()
    {
        int hashCode = super.computeHashCode();
        hashCode = hashCode * 13 + Double.hashCode(getXVelocity());
        hashCode = hashCode * 13 + Double.hashCode(getYVelocity());
        return hashCode;
    }
}
