package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;

public abstract class Entity
{
    private static int ID_COUNTER = 0;

    private final int id;

    private final Box box;

    private final Box lastBox;

    private final Vector velocity;

    private Engine engine;

    private boolean removed = false;

    private boolean onGround = false;

    public Entity(Box box, Vector velocity)
    {
        this.box = new Box(box);
        this.velocity = new Vector(velocity);
        lastBox = new Box(box);
        id = ID_COUNTER++;
    }

    public Entity(Box box)
    {
        this(box, new Vector());
    }

    public Entity(Vector position)
    {
        this(position, new Vector());
    }

    public Entity(Vector position, Vector velocity)
    {
        this(new Box(position), velocity);
    }

    public Entity(double x, double y, double width, double height)
    {
        this(new Box(x, y, width, height));
    }

    public Entity(double x, double y, double width, double height, double xVelocity, double yVelocity)
    {
        this(new Box(x, y, width, height), new Vector(xVelocity, yVelocity));
    }

    final void setEngine(Engine engine)
    {
        this.engine = engine;
    }

    public final Vector getCenter()
    {
        return box.getCenter();
    }

    public final Box getBox()
    {
        return box;
    }

    public final Box getLastBox()
    {
        return lastBox;
    }

    public final Vector getVelocity()
    {
        return velocity;
    }

    public final void setVelocity(Vector velocity)
    {
        this.velocity.set(velocity);
    }

    public final Box getVelocityBox(double delta)
    {
        return box.getContainment(box.shift(velocity.times(getSpeed() * delta)));
    }

    public final boolean isVelocityTouching(Entity entity, double delta)
    {
        return getVelocityBox(delta).isTouching(entity.getVelocityBox(delta));
    }

    public final boolean isTouching(Entity entity)
    {
        return box.isTouching(entity.box);
    }

    public final void remove()
    {
        removed = true;
    }

    public final boolean isRemoved()
    {
        return removed;
    }

    public final void move(double delta)
    {
        box.move(velocity.times(getSpeed() * delta));
    }

    public final void saveBox()
    {
        lastBox.set(box);
    }

    public final Engine getEngine()
    {
        return engine;
    }

    public final void update(double delta)
    {
        if (isAccelerated())
        {
            accelerate(getSpeed() * delta);
        }
        update();
        onGround = false;
    }

    public final void onGround()
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

    public double getElasticity()
    {
        return .75;
    }

    public double getMass()
    {
        return 1;
    }

    public void accelerate(double delta)
    {
    }

    public boolean isSolid()
    {
        return true;
    }

    public boolean isAccelerated()
    {
        return false;
    }

    public void render()
    {
    }

    public final void clear()
    {
        Display display = getEngine().getDisplay();
        getLastBox().asScaledRectangle(getEngine().getScale()).getSubtraction(getBox().asScaledRectangle(getEngine().getScale())).forEach(display::clear);
    }

    @Override
    public final boolean equals(Object obj)
    {
        return this == obj;
    }

    @Override
    public String toString()
    {
        return "Entity-" + id;
    }
}
