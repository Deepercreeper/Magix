package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;

public class Collision
{
    private static final double DEFAULT_DAMPING_LIMIT = 0;

    private final Entity firstEntity;

    private final Entity secondEntity;

    private final int hashCode;

    private double delta;

    private boolean horizontal;

    public Collision(Entity firstEntity, Entity secondEntity)
    {
        this.firstEntity = firstEntity;
        this.secondEntity = secondEntity;
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
        return delta <= 0;
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

        setVerticalVelocity(firstVelocity, secondVelocity);

        if (firstEntity.getCenterY() < secondEntity.getCenterY())
        {
            firstEntity.hitGround();
        }
        else
        {
            secondEntity.hitGround();
        }
    }

    private void setVerticalVelocity(double firstVelocity, double secondVelocity)
    {
        double positionSignum = Math.signum(secondEntity.getCenterY() - firstEntity.getCenterY());
        double collisionVelocity = positionSignum * (firstEntity.getYVelocity() - secondEntity.getYVelocity());
        if (0 < collisionVelocity && collisionVelocity < DEFAULT_DAMPING_LIMIT)
        {
            double scale = firstEntity.getMassScaleTo(secondEntity);

            if (Math.signum(firstEntity.getYVelocity() * secondEntity.getYVelocity()) > -1)
            {
                firstVelocity = secondVelocity = scale * firstEntity.getYVelocity() + (1 - scale) * secondEntity.getYVelocity();
            }
            else
            {
                if (firstEntity.getVelocity().getAbsY() < collisionVelocity)
                {
                    firstVelocity = secondVelocity;
                }
                else
                {
                    secondVelocity = firstVelocity;
                }
            }

            double differenceLength = Math.min(Math.abs(secondEntity.getY() - firstEntity.getMaxY()), Math.abs(firstEntity.getY() - secondEntity.getMaxY()));
            double difference = Math.signum(secondEntity.getCenterY() - firstEntity.getCenterY()) * differenceLength;

            firstEntity.moveBy(0, (1 - scale) * difference);
            secondEntity.moveBy(0, scale * -difference);
        }

        firstEntity.setYVelocity(firstVelocity);
        secondEntity.setYVelocity(secondVelocity);
    }

    private void collideHorizontal()
    {
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

        setHorizontalVelocity(firstVelocity, secondVelocity);
    }

    private void setHorizontalVelocity(double firstVelocity, double secondVelocity)
    {
        double positionSignum = Math.signum(secondEntity.getCenterX() - firstEntity.getCenterX());
        double collisionVelocity = positionSignum * (firstEntity.getXVelocity() - secondEntity.getXVelocity());
        if (0 < collisionVelocity && collisionVelocity < DEFAULT_DAMPING_LIMIT)
        {
            double scale = firstEntity.getMassScaleTo(secondEntity);

            if (Math.signum(firstEntity.getXVelocity() * secondEntity.getXVelocity()) > -1)
            {
                firstVelocity = secondVelocity = scale * firstEntity.getXVelocity() + (1 - scale) * secondEntity.getXVelocity();
            }
            else
            {
                if (firstEntity.getVelocity().getAbsX() < collisionVelocity)
                {
                    firstVelocity = secondVelocity;
                }
                else
                {
                    secondVelocity = firstVelocity;
                }
            }

            double differenceLength = Math.min(Math.abs(secondEntity.getX() - firstEntity.getMaxX()), Math.abs(firstEntity.getX() - secondEntity.getMaxX()));
            double difference = Math.signum(secondEntity.getCenterX() - firstEntity.getCenterX()) * differenceLength;

            firstEntity.moveBy(0, (1 - scale) * difference);
            secondEntity.moveBy(0, scale * -difference);
        }

        firstEntity.setXVelocity(firstVelocity);
        secondEntity.setXVelocity(secondVelocity);
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
