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

    public Entity(double x, double y, double width, double height, double xVelocity, double yVelocity)
    {
        box = new Box(x, y, width, height);
        velocity = new Vector(xVelocity, yVelocity);
        lastBox = new Box(x, y, width, height);
        id = ID_COUNTER++;
    }

    public Entity(Box box, double xVelocity, double yVelocity)
    {
        this.box = new Box(box);
        this.velocity = new Vector(xVelocity, yVelocity);
        lastBox = new Box(box);
        id = ID_COUNTER++;
    }

    public Entity(double x, double y, double width, double height)
    {
        this(x, y, width, height, 0, 0);
    }

    public Entity(Box box, Vector velocity)
    {
        this(box, velocity.getX(), velocity.getY());
    }

    public Entity(double x, double y)
    {
        this(x, y, 0, 0);
    }

    public final void setBox(Box box)
    {
        this.box.set(box);
    }

    public final void setPosition(double x, double y)
    {
        box.setPosition(x, y);
    }

    public final void setPosition(Vector position)
    {
        box.setPosition(position);
    }

    public final void setSize(double width, double height)
    {
        box.setSize(width, height);
    }

    public final void setSize(Vector size)
    {
        box.setSize(size);
    }

    public final void setCenter(double x, double y)
    {
        box.setCenter(x, y);
    }

    public final void setCenter(Vector center)
    {
        box.setCenter(center);
    }

    public final void setVelocity(double xVelocity, double yVelocity)
    {
        velocity.set(xVelocity, yVelocity);
    }

    public final void setVelocity(Vector velocity)
    {
        this.velocity.set(velocity);
    }

    public final void setX(double x)
    {
        box.setX(x);
    }

    public final void setY(double y)
    {
        box.setY(y);
    }

    public final void setMaxX(double x)
    {
        box.setMaxX(x);
    }

    public final void setMaxY(double y)
    {
        box.setMaxY(y);
    }

    public final void setCenterX(double x)
    {
        box.setCenterX(x);
    }

    public final void setCenterY(double y)
    {
        box.setCenterY(y);
    }

    public final void setWidth(double width)
    {
        box.setWidth(width);
    }

    public final void setHeight(double height)
    {
        box.setHeight(height);
    }

    public final void setXVelocity(double xVelocity)
    {
        velocity.setX(xVelocity);
    }

    public final void setYVelocity(double yVelocity)
    {
        velocity.setY(yVelocity);
    }

    public final void moveBy(double x, double y)
    {
        box.moveBy(x, y);
    }

    public final void moveBy(Vector vector)
    {
        box.moveBy(vector);
    }

    public final Box getBox()
    {
        return box;
    }

    public final Vector getPosition()
    {
        return box.getPosition();
    }

    public final Vector getSize()
    {
        return box.getSize();
    }

    public final Vector getCenter()
    {
        return box.getCenter();
    }

    public final Vector getVelocity()
    {
        return velocity;
    }

    public final double getX()
    {
        return box.getX();
    }

    public final double getY()
    {
        return box.getY();
    }

    public final double getMaxX()
    {
        return box.getMaxX();
    }

    public final double getMaxY()
    {
        return box.getMaxY();
    }

    public final double getCenterX()
    {
        return box.getCenterX();
    }

    public final double getCenterY()
    {
        return box.getCenterY();
    }

    public final double getWidth()
    {
        return box.getWidth();
    }

    public final double getHeight()
    {
        return box.getHeight();
    }

    public final double getXVelocity()
    {
        return velocity.getX();
    }

    public final double getYVelocity()
    {
        return velocity.getY();
    }

    public final Box getLastBox()
    {
        return lastBox;
    }

    public final Box getVelocityBox(double delta)
    {
        return box.shift(velocity.times(getSpeed() * delta));
    }

    public final Box getVelocityBoxContainment(double delta)
    {
        return box.getContainment(getVelocityBox(delta));
    }

    public final boolean isVelocityTouching(Entity entity, double delta)
    {
        return getVelocityBoxContainment(delta).isTouching(entity.getVelocityBoxContainment(delta));
    }

    public final boolean isTouching(Entity entity)
    {
        return box.isTouching(entity.box);
    }

    public final void remove()
    {
        removed = true;
    }

    public final void move(double delta)
    {
        box.moveBy(velocity.times(getSpeed() * delta));
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
        getLastBox().asScaledRectangle(getEngine().getScale()).getSubtraction(getBox().asScaledRectangle(getEngine().getScale())).forEach(display::clear);
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

    public double getElasticity()
    {
        return .75;
    }

    public double getMass()
    {
        return 1;
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
    public String toString()
    {
        return "Entity-" + id;
    }
}
