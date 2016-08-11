package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;

public abstract class PhysicsEntity
{
    private static int ID_COUNTER = 0;

    private final int id;

    private final Box box;

    private final Box lastBox;

    private final Vector velocity;

    private final double mass;

    private boolean removed = false;

    public PhysicsEntity(Box box, double mass)
    {
        this.box = new Box(box);
        velocity = new Vector();
        this.mass = mass;
        lastBox = new Box(box);
        id = ID_COUNTER++;
    }

    public PhysicsEntity(Box box, Vector velocity, double mass)
    {
        this.box = new Box(box);
        this.velocity = new Vector(velocity);
        this.mass = mass;
        lastBox = new Box(box);
        id = ID_COUNTER++;
    }

    public PhysicsEntity(Vector position, double mass)
    {
        this.box = new Box(position);
        velocity = new Vector();
        this.mass = mass;
        lastBox = new Box(box);
        id = ID_COUNTER++;
    }

    public PhysicsEntity(Vector position, Vector velocity, double mass)
    {
        this.box = new Box(position);
        this.velocity = new Vector(velocity);
        this.mass = mass;
        lastBox = new Box(box);
        id = ID_COUNTER++;
    }

    public PhysicsEntity(double x, double y, double width, double height, double mass)
    {
        this(new Box(x, y, width, height), mass);
    }

    public PhysicsEntity(double x, double y, double width, double height, double xVelocity, double yVelocity, double
            mass)
    {
        this(new Box(x, y, width, height), new Vector(xVelocity, yVelocity), mass);
    }

    public Vector getCenter()
    {
        return box.getCenter();
    }

    public Box getBox()
    {
        return box;
    }

    public Box getLastBox()
    {
        return lastBox;
    }

    public Vector getVelocity()
    {
        return velocity;
    }

    public double getMass()
    {
        return mass;
    }

    public void accelerate(Vector acceleration)
    {
        velocity.add(acceleration);
    }

    public void setVelocity(Vector velocity)
    {
        this.velocity.set(velocity);
    }

    public Box getVelocityBox(double delta)
    {
        return box.getContainment(box.shift(velocity.times(delta)));
    }

    public boolean isPossiblyTouching(PhysicsEntity entity, double delta)
    {
        return getVelocityBox(delta).isTouching(entity.getVelocityBox(delta));
    }

    public boolean isTouching(PhysicsEntity entity)
    {
        return box.isTouching(entity.box);
    }

    public void remove()
    {
        removed = true;
    }

    public boolean isRemoved()
    {
        return removed;
    }

    public void move(double delta)
    {
        box.move(velocity.times(delta));
    }

    public void saveBox()
    {
        lastBox.moveTo(box.getPosition());
    }

    public void collideWith(PhysicsEntity entity)
    {
        double mass = getMass() + entity.getMass();
        Vector distance = getCenter().minus(entity.getCenter());
        Vector entityDistance = distance.negative();
        double distanceNormSquared = Math.pow(distance.norm(), 2);
        double factor = 2 * entity.getMass() / mass * getVelocity().minus(entity.getVelocity())
                                                                   .times(distance) / distanceNormSquared;
        double entityFactor = 2 * getMass() / mass * entity.getVelocity().minus(getVelocity())
                                                           .times(entityDistance) / distanceNormSquared;

        Vector velocity = getVelocity().minus(distance.times(factor));
        Vector entityVelocity = entity.getVelocity().minus(entityDistance.times(entityFactor));
        setVelocity(velocity);
        entity.setVelocity(entityVelocity);
    }

    public abstract boolean isAccelerated();

    public abstract Vector computeAcceleration();

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof PhysicsEntity)
        {
            PhysicsEntity entity = (PhysicsEntity) obj;
            return id == entity.id;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "Entity-" + id;
    }
}
