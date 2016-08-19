package org.deepercreeper.engine.util;

public class AcceleratedBox extends VelocityBox
{
    private final Vector acceleration = new Vector();

    protected AcceleratedBox(GenericAcceleratedBoxBuilder<?> builder)
    {
        super(builder);
        setAcceleration(builder.xAcceleration, builder.yAcceleration);
    }

    public final void setAcceleration(double xAcceleration, double yAcceleration)
    {
        acceleration.set(xAcceleration, yAcceleration);
        updateHashCode();
    }

    public final void setAcceleration(Vector acceleration)
    {
        this.acceleration.set(acceleration);
        updateHashCode();
    }

    public final void setXAcceleration(double xAcceleration)
    {
        acceleration.setX(xAcceleration);
        updateHashCode();
    }

    public final void setYAcceleration(double yAcceleration)
    {
        acceleration.setY(yAcceleration);
        updateHashCode();
    }

    public final Vector getAcceleration()
    {
        return acceleration;
    }

    public final double getXAcceleration()
    {
        return acceleration.getX();
    }

    public final double getYAcceleration()
    {
        return acceleration.getY();
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
