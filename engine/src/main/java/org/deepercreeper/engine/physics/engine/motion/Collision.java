package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;

public class Collision
{
    private static final double WEAK_COLLISION_VELOCITY = 0.1;

    private final Entity firstEntity;

    private final Entity secondEntity;

    private final double scale;

    private final int hashCode;

    private double velocity;

    private double delta;

    private boolean horizontal;

    public Collision(Entity firstEntity, Entity secondEntity)
    {
        this.firstEntity = firstEntity;
        this.secondEntity = secondEntity;
        scale = firstEntity.getMassScaleTo(secondEntity);
        hashCode = computeHashCode();
    }

    public void computeDelta()
    {
        if (firstEntity.isTouching(secondEntity))
        {
            delta = 0;
            return;
        }
        double verticalDelta = computeVerticalDelta();
        double horizontalDelta = computeHorizontalDelta();
        if (!isValidDelta(verticalDelta))
        {
            horizontal = true;
            delta = horizontalDelta;
            return;
        }
        if (!isValidDelta(horizontalDelta))
        {
            horizontal = false;
            delta = verticalDelta;
            return;
        }
        horizontal = horizontalDelta < verticalDelta;
        delta = Math.min(horizontalDelta, verticalDelta);
    }

    public void optimizeDelta()
    {
        if (!firstEntity.isDeltaTouching(secondEntity, delta))
        {
            return;
        }
        double epsilon = 10E-10;
        while (epsilon < delta && firstEntity.isDeltaTouching(secondEntity, delta - epsilon))
        {
            epsilon *= 2;
        }
        if (epsilon >= delta)
        {
            delta = 0;
        }
        delta -= epsilon;
    }

    public void computeVelocity()
    {
        if (horizontal)
        {
            velocity = Math.abs(firstEntity.getDeltaVelocity(delta).getX() - secondEntity.getDeltaVelocity(delta).getX());
        }
        else
        {
            velocity = Math.abs(firstEntity.getDeltaVelocity(delta).getY() - secondEntity.getDeltaVelocity(delta).getY());
        }
    }

    private double computeHorizontalDelta()
    {
        if (firstEntity.getXAcceleration() != secondEntity.getXAcceleration())
        {
            double ad = 1 / (firstEntity.getXAcceleration() - secondEntity.getXAcceleration());
            double vd = firstEntity.getXVelocity() - secondEntity.getXVelocity();
            double quotient = vd * ad;
            double qq = quotient * quotient;
            double firstDelta = -quotient + Math.sqrt(qq - 2 * (firstEntity.getMaxX() - secondEntity.getX()) * ad);
            double secondDelta = -quotient - Math.sqrt(qq - 2 * (firstEntity.getMaxX() - secondEntity.getX()) * ad);
            double thirdDelta = -quotient + Math.sqrt(qq - 2 * (firstEntity.getX() - secondEntity.getMaxX()) * ad);
            double fourthDelta = -quotient - Math.sqrt(qq - 2 * (firstEntity.getX() - secondEntity.getMaxX()) * ad);
            return computeMinDelta(firstDelta, secondDelta, thirdDelta, fourthDelta);
        }
        double divisor = firstEntity.getXVelocity() * firstEntity.getSpeed() - secondEntity.getXVelocity() * secondEntity.getSpeed();
        double firstDelta = (secondEntity.getMaxX() - firstEntity.getX()) / divisor;
        double secondDelta = (secondEntity.getX() - firstEntity.getMaxX()) / divisor;
        return computeMinDelta(firstDelta, secondDelta);
    }

    private double computeVerticalDelta()
    {
        if (firstEntity.getYAcceleration() != secondEntity.getYAcceleration())
        {
            double ad = 1 / (firstEntity.getYAcceleration() - secondEntity.getYAcceleration());
            double vd = firstEntity.getYVelocity() - secondEntity.getYVelocity();
            double quotient = vd * ad;
            double qq = quotient * quotient;
            double firstDelta = -quotient + Math.sqrt(qq - 2 * (firstEntity.getMaxY() - secondEntity.getY()) * ad);
            double secondDelta = -quotient - Math.sqrt(qq - 2 * (firstEntity.getMaxY() - secondEntity.getY()) * ad);
            double thirdDelta = -quotient + Math.sqrt(qq - 2 * (firstEntity.getY() - secondEntity.getMaxY()) * ad);
            double fourthDelta = -quotient - Math.sqrt(qq - 2 * (firstEntity.getY() - secondEntity.getMaxY()) * ad);
            return computeMinDelta(firstDelta, secondDelta, thirdDelta, fourthDelta);
        }
        double divisor = firstEntity.getYVelocity() * firstEntity.getSpeed() - secondEntity.getYVelocity() * secondEntity.getSpeed();
        double firstDelta = (secondEntity.getMaxY() - firstEntity.getY()) / divisor;
        double secondDelta = (secondEntity.getY() - firstEntity.getMaxY()) / divisor;
        return computeMinDelta(firstDelta, secondDelta);
    }

    private double computeMinDelta(double firstDelta, double secondDelta)
    {
        double value = Double.POSITIVE_INFINITY;
        if (isValidDelta(firstDelta))
        {
            value = firstDelta;
        }
        if (isValidDelta(secondDelta) && secondDelta < value)
        {
            value = secondDelta;
        }
        return value;
    }

    private double computeMinDelta(double... deltas)
    {
        double value = Double.POSITIVE_INFINITY;
        for (double delta : deltas)
        {
            if (!isValidDelta(value))
            {
                value = delta;
            }
            else
            {
                value = computeMinDelta(value, delta);
            }
        }
        return value;
    }

    private boolean isValidDelta(double delta)
    {
        return Double.isFinite(delta) && delta >= 0 && firstEntity.isDeltaTouching(secondEntity, delta + 10E-5);
    }

    public double getDelta()
    {
        return delta;
    }

    public boolean isInstant()
    {
        return delta == 0;
    }

    public boolean isWeak()
    {
        return velocity < WEAK_COLLISION_VELOCITY;
    }

    public Entity getFirstEntity()
    {
        return firstEntity;
    }

    public Entity getSecondEntity()
    {
        return secondEntity;
    }

    public boolean isHorizontal()
    {
        return horizontal;
    }

    public void collide()
    {
        firstEntity.collideWith(secondEntity);
        secondEntity.collideWith(firstEntity);

        if (horizontal)
        {
            collideHorizontal();
        }
        else
        {
            collideVertical();
        }
    }

    private void collideVertical()
    {
        if (isWeak())
        {
            double velocity = scale * firstEntity.getYVelocity() + (1 - scale) * secondEntity.getYVelocity();

            firstEntity.setYVelocity(velocity);
            secondEntity.setYVelocity(velocity);
            return;
        }

        double elasticity = Math.sqrt(firstEntity.getElasticity() * secondEntity.getElasticity());
        double firstVelocity;
        double secondVelocity;
        if (Double.isInfinite(secondEntity.getMass()) && Double.isInfinite(firstEntity.getMass()))
        {
            double average = (firstEntity.getYVelocity() + secondEntity.getYVelocity()) / 2;
            firstVelocity = average - (firstEntity.getYVelocity() - secondEntity.getYVelocity()) * elasticity / 2;
            secondVelocity = average - (secondEntity.getYVelocity() - firstEntity.getYVelocity()) * elasticity / 2;
        }
        else if (Double.isInfinite(secondEntity.getMass()))
        {
            firstVelocity = secondEntity.getYVelocity() - (firstEntity.getYVelocity() - secondEntity.getYVelocity()) * elasticity;
            secondVelocity = secondEntity.getYVelocity();
        }
        else if (Double.isInfinite(firstEntity.getMass()))
        {
            firstVelocity = firstEntity.getYVelocity();
            secondVelocity = firstEntity.getYVelocity() - (secondEntity.getYVelocity() - firstEntity.getYVelocity()) * elasticity;
        }
        else
        {
            double mass = firstEntity.getMass() + secondEntity.getMass();
            double massPoint = (firstEntity.getMass() * firstEntity.getYVelocity() + secondEntity.getMass() * secondEntity.getYVelocity()) / mass;
            firstVelocity = massPoint - (secondEntity.getMass() * (firstEntity.getYVelocity() - secondEntity.getYVelocity()) * elasticity) / mass;
            secondVelocity = massPoint - (firstEntity.getMass() * (secondEntity.getYVelocity() - firstEntity.getYVelocity()) * elasticity) / mass;
        }

        firstEntity.setYVelocity(firstVelocity);
        secondEntity.setYVelocity(secondVelocity);

        if (firstEntity.getCenterY() < secondEntity.getCenterY())
        {
            firstEntity.hitGround();
        }
        else
        {
            secondEntity.hitGround();
        }
    }

    private void collideHorizontal()
    {
        if (isWeak())
        {
            double velocity = scale * firstEntity.getXVelocity() + (1 - scale) * secondEntity.getXVelocity();

            firstEntity.setXVelocity(velocity);
            secondEntity.setXVelocity(velocity);
            return;
        }

        double elasticity = Math.sqrt(firstEntity.getElasticity() * secondEntity.getElasticity());
        double firstVelocity;
        double secondVelocity;
        if (Double.isInfinite(secondEntity.getMass()) && Double.isInfinite(firstEntity.getMass()))
        {
            double average = (firstEntity.getXVelocity() + secondEntity.getXVelocity()) / 2;
            firstVelocity = average - (firstEntity.getXVelocity() - secondEntity.getXVelocity()) * elasticity / 2;
            secondVelocity = average - (secondEntity.getXVelocity() - firstEntity.getXVelocity()) * elasticity / 2;
        }
        else if (Double.isInfinite(secondEntity.getMass()))
        {
            firstVelocity = secondEntity.getXVelocity() - (firstEntity.getXVelocity() - secondEntity.getXVelocity()) * elasticity;
            secondVelocity = secondEntity.getXVelocity();
        }
        else if (Double.isInfinite(firstEntity.getMass()))
        {
            firstVelocity = firstEntity.getXVelocity();
            secondVelocity = firstEntity.getXVelocity() - (secondEntity.getXVelocity() - firstEntity.getXVelocity()) * elasticity;
        }
        else
        {
            double mass = firstEntity.getMass() + secondEntity.getMass();
            double massPoint = (firstEntity.getMass() * firstEntity.getXVelocity() + secondEntity.getMass() * secondEntity.getXVelocity()) / mass;
            firstVelocity = massPoint - (secondEntity.getMass() * (firstEntity.getXVelocity() - secondEntity.getXVelocity()) * elasticity) / mass;
            secondVelocity = massPoint - (firstEntity.getMass() * (secondEntity.getXVelocity() - firstEntity.getXVelocity()) * elasticity) / mass;
        }

        firstEntity.setXVelocity(firstVelocity);
        secondEntity.setXVelocity(secondVelocity);
    }

    public void move()
    {
        if (horizontal)
        {
            moveHorizontal();
        }
        else
        {
            moveVertical();
        }
    }

    private void moveHorizontal()
    {
        boolean firstOnLeft = firstEntity.getCenterX() < secondEntity.getCenterX();
        double scale = firstOnLeft ? this.scale : 1 - this.scale;
        Entity leftEntity = firstOnLeft ? firstEntity : secondEntity;
        Entity rightEntity = firstOnLeft ? firstEntity : secondEntity;

        double position = scale * leftEntity.getMaxX() + (1 - scale) * rightEntity.getX();

        leftEntity.setMaxX(position);
        rightEntity.setX(position);
    }

    private void moveVertical()
    {
        boolean firstOnTop = firstEntity.getCenterY() < secondEntity.getCenterY();
        double scale = firstOnTop ? this.scale : 1 - this.scale;
        Entity topEntity = firstOnTop ? firstEntity : secondEntity;
        Entity bottomEntity = firstOnTop ? firstEntity : secondEntity;

        double position = scale * topEntity.getMaxY() + (1 - scale) * bottomEntity.getY();

        topEntity.setMaxY(position);
        bottomEntity.setY(position);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Collision)
        {
            Collision collision = (Collision) obj;
            return firstEntity.equals(collision.firstEntity) && secondEntity.equals(collision.secondEntity) || firstEntity.equals(collision.secondEntity) && secondEntity
                    .equals(collision.firstEntity);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    private int computeHashCode()
    {
        return firstEntity.hashCode() * secondEntity.hashCode();
    }

    @Override
    public String toString()
    {
        return firstEntity + " <-> " + secondEntity;
    }
}
