package org.deepercreeper.engine.util;

public class VelocityBox extends Box
{
    private final Vector velocity = new Vector(this::updateHashCode);

    protected VelocityBox(GenericVelocityBoxBuilder<?> builder)
    {
        super(builder);
        setVelocity(builder.xVelocity, builder.yVelocity);
    }

    public void setXVelocity(double xVelocity)
    {
        getVelocity().setX(xVelocity);
    }

    public void setYVelocity(double yVelocity)
    {
        getVelocity().setY(yVelocity);
    }

    public void setVelocity(double xVelocity, double yVelocity)
    {
        getVelocity().set(xVelocity, yVelocity);
    }

    public final void setVelocity(Vector velocity)
    {
        setVelocity(velocity.getX(), velocity.getY());
    }

    public final Vector getVelocity()
    {
        return velocity;
    }

    public final double getXVelocity()
    {
        return getVelocity().getX();
    }

    public final double getYVelocity()
    {
        return getVelocity().getY();
    }

    @Override
    protected int computeHashCode()
    {
        int hashCode = super.computeHashCode();
        hashCode = hashCode * 13 + Double.hashCode(getXVelocity());
        hashCode = hashCode * 13 + Double.hashCode(getYVelocity());
        return hashCode;
    }

    public static abstract class GenericVelocityBoxBuilder <T extends GenericBoxBuilder<T>> extends GenericBoxBuilder<T>
    {
        protected double xVelocity;

        protected double yVelocity;

        public T setXVelocity(double xVelocity)
        {
            this.xVelocity = xVelocity;
            return getThis();
        }

        public T setYVelocity(double yVelocity)
        {
            this.yVelocity = yVelocity;
            return getThis();
        }

        public T set(VelocityBox velocityBox)
        {
            set((Box) velocityBox);
            xVelocity = velocityBox.getXVelocity();
            yVelocity = velocityBox.getYVelocity();
            return getThis();
        }

        @Override
        public VelocityBox build()
        {
            return new VelocityBox(this);
        }
    }

    public static final class VelocityBoxBuilder extends GenericVelocityBoxBuilder<VelocityBoxBuilder>
    {
        @Override
        protected VelocityBoxBuilder getThis()
        {
            return this;
        }
    }
}
