package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;

public abstract class Entity
{
    private final Box box;

    private final Box lastBox;

    private final Vector velocity;

    private Engine engine;

    private boolean removed = false;

    private boolean onGround = false;

    public Entity(Box box)
    {
        this.box = new Box(box);
        velocity = new Vector();
        lastBox = new Box(box);
    }

    public Entity(Box box, Vector velocity)
    {
        this.box = new Box(box);
        this.velocity = new Vector(velocity);
        lastBox = new Box(box);
    }

    public Entity(Vector position)
    {
        this.box = new Box(position);
        velocity = new Vector();
        lastBox = new Box(box);
    }

    public Entity(Vector position, Vector velocity)
    {
        this.box = new Box(position);
        this.velocity = new Vector(velocity);
        lastBox = new Box(box);
    }

    public Entity(double x, double y, double width, double height)
    {
        this(new Box(x, y, width, height));
    }

    public Entity(double x, double y, double width, double height, double xVelocity, double yVelocity)
    {
        this(new Box(x, y, width, height), new Vector(xVelocity, yVelocity));
    }

    void setEngine(Engine engine)
    {
        this.engine = engine;
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

    public boolean isPossiblyTouching(Entity entity, double delta)
    {
        return getVelocityBox(delta).isTouching(entity.getVelocityBox(delta));
    }

    public boolean isTouching(Entity entity)
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

    public void collideWith(Entity entity)
    {
        Vector difference = entity.getCenter().minus(getCenter());
        Vector corner = new Vector(difference.getX() > 0 ? getBox().getMaxX() : getBox().getX(), difference.getY() > 0 ? getBox().getMaxY() : getBox().getY());
        Vector entityCorner = new Vector(difference.getX() > 0 ? entity.getBox().getX() : entity.getBox().getMaxX(), difference.getY() > 0 ? entity.getBox().getY() : entity.getBox().getMaxY());
        if (Math.abs(corner.getX() - entityCorner.getX()) > Math.abs(corner.getY() - entityCorner.getY()))
        {
            collideVertical(entity);
        }
        else
        {
            collideHorizontal(entity);
        }
    }

    private void collideVertical(Entity entity)
    {
        double elasticity = Math.sqrt(getElasticity() * entity.getElasticity());
        double mass = getMass() + entity.getMass();
        double massPoint = (getMass() * getVelocity().getY() + entity.getMass() * entity.getVelocity().getY()) / mass;
        double yVelocity;
        double yEntityVelocity;
        if (Double.isInfinite(entity.getMass()) && Double.isInfinite(getMass()))
        {
            double average = (getVelocity().getY() + entity.getVelocity().getY()) / 2;
            yVelocity = average - (getVelocity().getY() - entity.getVelocity().getY()) * elasticity / 2;
            yEntityVelocity = average - (entity.getVelocity().getY() - getVelocity().getY()) * elasticity / 2;
        }
        else if (Double.isInfinite(entity.getMass()))
        {
            yVelocity = entity.getVelocity().getY() - (getVelocity().getY() - entity.getVelocity().getY()) * elasticity;
            yEntityVelocity = entity.getVelocity().getY();
        }
        else if (Double.isInfinite(getMass()))
        {
            yVelocity = getVelocity().getY();
            yEntityVelocity = getVelocity().getY() - (entity.getVelocity().getY() - getVelocity().getY()) * elasticity;
        }
        else
        {
            yVelocity = massPoint - (entity.getMass() * (getVelocity().getY() - entity.getVelocity().getY()) * elasticity) / mass;
            yEntityVelocity = massPoint - (getMass() * (entity.getVelocity().getY() - getVelocity().getY()) * elasticity) / mass;
        }

        Vector velocity = new Vector(getVelocity().getX(), yVelocity);
        Vector entityVelocity = new Vector(entity.getVelocity().getX(), yEntityVelocity);

        setVelocity(velocity);
        entity.setVelocity(entityVelocity);

        if (getBox().getY() < entity.getBox().getY())
        {
            onGround = true;
        }
        else
        {
            entity.onGround = true;
        }
    }

    private void collideHorizontal(Entity entity)
    {
        double elasticity = Math.sqrt(getElasticity() * entity.getElasticity());
        double mass = getMass() + entity.getMass();
        double massPoint = (getMass() * getVelocity().getX() + entity.getMass() * entity.getVelocity().getX()) / mass;
        double xVelocity;
        double xEntityVelocity;
        if (Double.isInfinite(entity.getMass()) && Double.isInfinite(getMass()))
        {
            double average = (getVelocity().getX() + entity.getVelocity().getX()) / 2;
            xVelocity = average - (getVelocity().getX() - entity.getVelocity().getX()) * elasticity / 2;
            xEntityVelocity = average - (entity.getVelocity().getX() - getVelocity().getX()) * elasticity / 2;
        }
        else if (Double.isInfinite(entity.getMass()))
        {
            xVelocity = entity.getVelocity().getX() - (getVelocity().getX() - entity.getVelocity().getX()) * elasticity;
            xEntityVelocity = entity.getVelocity().getX();
        }
        else if (Double.isInfinite(getMass()))
        {
            xVelocity = getVelocity().getX();
            xEntityVelocity = getVelocity().getX() - (entity.getVelocity().getX() - getVelocity().getX()) * elasticity;
        }
        else
        {
            xVelocity = massPoint - (entity.getMass() * (getVelocity().getX() - entity.getVelocity().getX()) * elasticity) / mass;
            xEntityVelocity = massPoint - (getMass() * (entity.getVelocity().getX() - getVelocity().getX()) * elasticity) / mass;
        }
        Vector velocity = new Vector(xVelocity, getVelocity().getY());
        Vector entityVelocity = new Vector(xEntityVelocity, entity.getVelocity().getY());

        setVelocity(velocity);
        entity.setVelocity(entityVelocity);
    }

    public Engine getEngine()
    {
        return engine;
    }

    public void update(double delta)
    {
        if (isAccelerated())
        {
            accelerate(delta);
        }
        update();
        onGround = false;
    }

    public boolean isOnGround()
    {
        return onGround;
    }

    public abstract void update();

    public abstract double getElasticity();

    public abstract double getMass();

    public abstract void accelerate(double delta);

    public abstract boolean isAccelerated();

    public abstract void render();

    @Override
    public boolean equals(Object obj)
    {
        return this == obj;
    }

    @Override
    public String toString()
    {
        return "Entity";
    }
}
