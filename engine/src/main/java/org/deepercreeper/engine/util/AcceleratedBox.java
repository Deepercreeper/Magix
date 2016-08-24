package org.deepercreeper.engine.util;

public class AcceleratedBox extends VelocityBox
{
    private final Vector acceleration = new Vector();

    protected AcceleratedBox(GenericAcceleratedBoxBuilder<?> builder)
    {
        super(builder);
        setAcceleration(builder.xAcceleration, builder.yAcceleration);
    }

    public void setXAcceleration(double xAcceleration)
    {
        getAcceleration().setX(xAcceleration);
        updateHashCode();
    }

    public void setYAcceleration(double yAcceleration)
    {
        getAcceleration().setY(yAcceleration);
        updateHashCode();
    }

    public void setAcceleration(double xAcceleration, double yAcceleration)
    {
        getAcceleration().set(xAcceleration, yAcceleration);
        updateHashCode();
    }

    public final void setAcceleration(Vector acceleration)
    {
        setAcceleration(acceleration.getX(), acceleration.getY());
    }

    public final Vector getAcceleration()
    {
        return acceleration;
    }

    public final double getXAcceleration()
    {
        return getAcceleration().getX();
    }

    public final double getYAcceleration()
    {
        return getAcceleration().getY();
    }

    public static abstract class GenericAcceleratedBoxBuilder<T extends GenericVelocityBoxBuilder<T>> extends GenericVelocityBoxBuilder<T>
    {
        protected double xAcceleration;

        protected double yAcceleration;

        public T setXAcceleration(double xAcceleration)
        {
            this.xAcceleration = xAcceleration;
            return getThis();
        }

        public T setYAcceleration(double yAcceleration)
        {
            this.yAcceleration = yAcceleration;
            return getThis();
        }

        public T set(AcceleratedBox acceleratedBox)
        {
            set((VelocityBox) acceleratedBox);
            xAcceleration = acceleratedBox.getXAcceleration();
            yAcceleration = acceleratedBox.getYAcceleration();
            return getThis();
        }

        @Override
        public AcceleratedBox build()
        {
            return new AcceleratedBox(this);
        }
    }

    public static final class AcceleratedBoxBuilder extends GenericAcceleratedBoxBuilder<AcceleratedBoxBuilder>
    {
        @Override
        protected AcceleratedBoxBuilder getThis()
        {
            return this;
        }
    }
}
