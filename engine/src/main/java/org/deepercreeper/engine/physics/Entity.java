package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.util.AcceleratedBox;
import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;

public class Entity extends AcceleratedBox
{
    private static int ID_COUNTER = 0;

    private final int id;

    private final Box lastBox;

    private double mass;

    private double elasticity;

    private Engine engine;

    private boolean removed = false;

    private boolean onGround = false;

    protected Entity(GenericEntityBuilder builder)
    {
        super(builder);
        mass = builder.mass;
        elasticity = builder.elasticity;
        lastBox = new BoxBuilder().set(this).build();
        id = ID_COUNTER++;
    }

    public final void setMass(double mass)
    {
        this.mass = mass;
    }

    public final void setElasticity(double elasticity)
    {
        this.elasticity = elasticity;
    }

    public final double getMass()
    {
        return mass;
    }

    public final double getElasticity()
    {
        return elasticity;
    }

    public final Box getLastBox()
    {
        return lastBox;
    }

    public final Box getDeltaBox(double delta)
    {
        return shift(getVelocity().times(getSpeed() * delta));
    }

    public final boolean isDeltaTouching(Entity entity, double delta)
    {
        return getDeltaBox(delta).isTouching(entity.getDeltaBox(delta));
    }

    public final Box getDistanceBox(Vector velocity, double delta)
    {
        Vector distance = velocity.times(getSpeed() * delta);
        Box minXBox = shiftX(-distance.getAbsX());
        Box minYBox = shiftY(-distance.getAbsY());
        Box maxXBox = shiftX(distance.getAbsX());
        Box maxYBox = shiftY(distance.getAbsY());
        return getContainment(minXBox, minYBox, maxXBox, maxYBox);
    }

    public final Box getDistanceBox(double delta)
    {
        return getDistanceBox(getVelocity(), delta);
    }

    public final boolean isDistanceTouching(Vector distance, Entity entity, Vector entityVelocity, double delta)
    {
        return getDistanceBox(distance, delta).isTouching(entity.getDistanceBox(entityVelocity, delta));
    }

    public final void remove()
    {
        removed = true;
    }

    public final void move(double delta)
    {
        moveBy(getVelocity().times(getSpeed() * delta));
    }

    public final void saveBox()
    {
        lastBox.set(this);
    }

    public final Engine getEngine()
    {
        return engine;
    }

    public final void update(double delta)
    {
        updateVelocity(getSpeed() * delta);
        update();
        onGround = false;
    }

    public final void hitGround()
    {
        onGround = true;
    }

    public final boolean isOnGround()
    {
        return onGround;
    }

    public final double getMassScaleTo(Entity entity)
    {
        if (Double.isInfinite(getMass()))
        {
            return Double.isInfinite(entity.getMass()) ? 0.5 : 1;
        }
        if (Double.isInfinite(entity.getMass()))
        {
            return 0;
        }
        return getMass() / (getMass() + entity.getMass());
    }

    public final void clearLastBox()
    {
        Display display = getEngine().getDisplay();
        getLastBox().asScaledRectangle(getEngine().getScale()).getSubtraction(asScaledRectangle(getEngine().getScale())).forEach(display::clear);
    }

    final void setEngine(Engine engine)
    {
        this.engine = engine;
    }

    final boolean isRemoved()
    {
        return removed;
    }

    public void collideWith(Entity entity)
    {
    }

    public double getSpeed()
    {
        return 1;
    }

    public void update()
    {
    }

    public void updateVelocity(double delta)
    {
    }

    public boolean isSolid()
    {
        return true;
    }

    public void render()
    {
    }

    @Override
    public final boolean equals(Object obj)
    {
        return this == obj;
    }

    @Override
    protected boolean isHashCodeFinal()
    {
        return true;
    }

    @Override
    protected int computeHashCode()
    {
        return id;
    }


    @Override
    public String toString()
    {
        return "Entity-" + id;
    }

    public static abstract class GenericEntityBuilder<T extends GenericEntityBuilder<T>> extends GenericAcceleratedBoxBuilder<T>
    {
        protected double mass = 1;

        protected double elasticity = .75;

        public T setMass(double mass)
        {
            this.mass = mass;
            return getThis();
        }

        public T setElasticity(double elasticity)
        {
            this.elasticity = elasticity;
            return getThis();
        }

        public T set(Entity entity)
        {
            set((AcceleratedBox) entity);
            mass = entity.getMass();
            return getThis();
        }

        @Override
        public Entity build()
        {
            return new Entity(this);
        }
    }

    public static final class EntityBuilder extends GenericEntityBuilder<EntityBuilder>
    {
        @Override
        protected EntityBuilder getThis()
        {
            return this;
        }
    }
}
