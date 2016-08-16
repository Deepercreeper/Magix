package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;
import org.deepercreeper.engine.util.VelocityBox;

public class Entity extends VelocityBox
{
    private static int ID_COUNTER = 0;

    private final int id;

    private final Box lastBox;

    private Engine engine;

    private boolean removed = false;

    private boolean onGround = false;

    public Entity(double x, double y, double width, double height, double xVelocity, double yVelocity)
    {
        super(x, y, width, height, xVelocity, yVelocity);
        lastBox = new Box(x, y, width, height);
        id = ID_COUNTER++;
    }

    public Entity(Box box, double xVelocity, double yVelocity)
    {
        super(box, xVelocity, yVelocity);
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
        lastBox.set(computeRenderingBox());
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
        getLastBox().asScaledRectangle(getEngine().getScale()).getSubtraction(computeRenderingBox().asScaledRectangle(getEngine().getScale())).forEach(display::clear);
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

    protected Box computeRenderingBox()
    {
        return this;
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
}
