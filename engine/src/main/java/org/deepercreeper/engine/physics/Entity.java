package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.display.Renderable;
import org.deepercreeper.engine.physics.engine.Engine;
import org.deepercreeper.engine.util.*;

public class Entity extends AcceleratedBox implements Updatable, Renderable
{
    private final boolean solid;

    private int id = -1;

    private double mass;

    private double elasticity;

    private double speed;

    private Engine engine;

    private boolean removed = false;

    private boolean onGround = false;

    private boolean updating = false;

    protected Entity(GenericEntityBuilder<?> builder)
    {
        super(builder);
        solid = builder.solid;
        setMass(builder.mass);
        setElasticity(builder.elasticity);
        setSpeed(builder.speed);
    }

    public final void setMass(double mass)
    {
        assertNotUpdating("change mass");
        if (mass < 0)
        {
            throw new IllegalArgumentException("Mass has to be a non negative value");
        }
        this.mass = mass;
    }

    public final void setElasticity(double elasticity)
    {
        assertNotUpdating("change elasticity");
        if (elasticity < 0 || elasticity > 1)
        {
            throw new IllegalArgumentException("Elasticity has to be a value between 0 and 1");
        }
        this.elasticity = elasticity;
    }

    public final void setSpeed(double speed)
    {
        assertNotUpdating("change speed");
        if (speed < 0)
        {
            throw new IllegalArgumentException("Speed has to be a non negative value");
        }
        this.speed = speed;
    }

    public final double getMass()
    {
        return mass;
    }

    public final double getElasticity()
    {
        return elasticity;
    }

    public final double getSpeed()
    {
        return speed;
    }

    public final boolean isSolid()
    {
        return solid;
    }

    public final int getId()
    {
        return id;
    }

    public final Vector getDeltaVelocity(double delta)
    {
        return getVelocity().plus(getAcceleration().times(getSpeed() * delta));
    }

    public final Box getDeltaBox(double delta)
    {
        return shift(getVelocity().times(getSpeed() * delta).plus(getAcceleration().times(.5 * getSpeed() * getSpeed() * delta * delta)));
    }

    public final boolean isDeltaTouching(Entity entity, double delta)
    {
        return getDeltaBox(delta).isTouching(entity.getDeltaBox(delta));
    }

    public final Box getVelocityBox(Box box, Vector velocity, double delta)
    {
        Vector distance = velocity.times(getSpeed() * delta);
        Box minXBox = box.shiftX(-distance.getAbsX());
        Box minYBox = box.shiftY(-distance.getAbsY());
        Box maxXBox = box.shiftX(distance.getAbsX());
        Box maxYBox = box.shiftY(distance.getAbsY());
        return getContainment(minXBox, minYBox, maxXBox, maxYBox);
    }

    public final Box getDistanceBox(Vector velocity, double delta)
    {
        Box acceleratedVelocityBox = getVelocityBox(shift(getAcceleration().times(.5 * getSpeed() * getSpeed() * delta * delta)), velocity, delta);
        return getVelocityBox(this, velocity, delta).getContainment(acceleratedVelocityBox);
    }

    public final boolean isDistanceTouching(Vector distance, Entity entity, Vector entityVelocity, double delta)
    {
        return getDistanceBox(distance, delta).isTouching(entity.getDistanceBox(entityVelocity, delta));
    }

    public final Engine getEngine()
    {
        return engine;
    }

    public final void remove()
    {
        removed = true;
    }

    @Override
    public final void update(double delta)
    {
        assertNotUpdating("update");
        updating = true;
        updateInternal(delta);
        updating = false;
        onGround = false;
    }

    public final void updateAll(double delta)
    {
        move(delta);
        accelerate(delta);
        update(delta);
        updateProperties();
    }

    public final void updateProperties()
    {
        assertNotUpdating("update properties");
        setPosition(computePosition());
        setSize(computeSize());
        setVelocity(computeVelocity());
        setAcceleration(computeAcceleration());
        setElasticity(computeElasticity());
        setSpeed(computeSpeed());
        setMass(computeMass());
    }

    public final void move(double delta)
    {
        assertNotUpdating("move");
        moveBy(getVelocity().times(getSpeed() * delta).plus(getAcceleration().times(.5 * getSpeed() * getSpeed() * delta * delta)));
    }

    public final void moveXAccelerated(double delta)
    {
        assertNotUpdating("move");
        moveBy(getXVelocity() * getSpeed() * delta + getXAcceleration() * .5 * getSpeed() * getSpeed() * delta * delta, 0);
    }

    public final void moveYAccelerated(double delta)
    {
        assertNotUpdating("move");
        moveBy(0, getYVelocity() * getSpeed() * delta + getYAcceleration() * .5 * getSpeed() * getSpeed() * delta * delta);
    }

    public final void accelerate(double delta)
    {
        assertNotUpdating("accelerate");
        getVelocity().add(getAcceleration().times(getSpeed() * delta));
    }

    public final void accelerateX(double delta)
    {
        assertNotUpdating("accelerate");
        getVelocity().add(getAcceleration().getX() * getSpeed() * delta, 0);
    }

    public final void accelerateY(double delta)
    {
        assertNotUpdating("accelerate");
        getVelocity().add(0, getAcceleration().getY() * getSpeed() * delta);
    }

    public final void hitGround()
    {
        assertNotUpdating("hit the ground");
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

    public final void init(int id, Engine engine)
    {
        assertNotUpdating("initialize");
        this.id = id;
        this.engine = engine;
    }

    public final void clear()
    {
        assertNotUpdating("clear");
        this.id = -1;
        this.engine = null;
    }

    private void assertNotUpdating(String action)
    {
        if (updating)
        {
            throw new IllegalStateException("Cannot " + action + " while updating");
        }
    }

    public final boolean isRemoved()
    {
        return removed;
    }

    public void collideWith(Entity entity)
    {
    }

    public void updateInternal(double delta)
    {
    }

    public Vector computeAcceleration()
    {
        return getAcceleration();
    }

    public Vector computePosition()
    {
        return getPosition();
    }

    public Vector computeSize()
    {
        return getSize();
    }

    public Vector computeVelocity()
    {
        return getVelocity();
    }

    public double computeMass()
    {
        return getMass();
    }

    public double computeSpeed()
    {
        return getSpeed();
    }

    public double computeElasticity()
    {
        return getElasticity();
    }

    @Override
    public Image generateImage(double scale)
    {
        Image image = new Image.ImageBuilder().set(asScaledRectangle(scale)).build();
        image.setData(Display.createFilledRectangle(image.getX(), image.getY(), 0xffffffff));
        return image;
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
        if (id == -1)
        {
            return "Entity";
        }
        return "Entity-" + id;
    }

    public static abstract class GenericEntityBuilder <T extends GenericEntityBuilder<T>> extends GenericAcceleratedBoxBuilder<T>
    {
        protected boolean solid = true;

        protected double mass = 1;

        protected double elasticity = .75;

        protected double speed = 1;

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

        public T setSpeed(double speed)
        {
            this.speed = speed;
            return getThis();
        }

        public T setSolid(boolean solid)
        {
            this.solid = solid;
            return getThis();
        }

        public T set(Entity entity)
        {
            set((AcceleratedBox) entity);
            mass = entity.getMass();
            elasticity = entity.getElasticity();
            speed = entity.getSpeed();
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
