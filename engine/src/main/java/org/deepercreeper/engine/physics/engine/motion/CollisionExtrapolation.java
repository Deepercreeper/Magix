package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;

public class CollisionExtrapolation
{
    private final Entity firstEntity;

    private final Entity secondEntity;

    private boolean horizontal;

    private double delta;

    private double velocity;

    public CollisionExtrapolation(Entity firstEntity, Entity secondEntity)
    {
        this.firstEntity = firstEntity;
        this.secondEntity = secondEntity;
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

    public double getVelocity()
    {
        return velocity;
    }

    public boolean isHorizontal()
    {
        return horizontal;
    }

    public Entity getTopLeftEntity()
    {
        if (horizontal)
        {
            return firstEntity.getCenterX() < secondEntity.getCenterX() ? firstEntity : secondEntity;
        }
        return firstEntity.getCenterY() < secondEntity.getCenterY() ? firstEntity : secondEntity;
    }

    public Entity getBottomRightEntity()
    {
        if (horizontal)
        {
            return firstEntity.getCenterX() < secondEntity.getCenterX() ? secondEntity : firstEntity;
        }
        return firstEntity.getCenterY() < secondEntity.getCenterY() ? secondEntity : firstEntity;
    }
}
