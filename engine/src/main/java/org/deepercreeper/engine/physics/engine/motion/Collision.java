package org.deepercreeper.engine.physics.engine.motion;

import org.deepercreeper.engine.physics.Entity;

public class Collision
{
    private static final double WEAK_COLLISION_VELOCITY = .1;

    private final Entity topLeftEntity;

    private final Entity bottomRightEntity;

    private final boolean valid;

    private final double delta;

    private final double velocity;

    private final boolean horizontal;

    private final double scale;

    private final int hashCode;

    public Collision(Entity firstEntity, Entity secondEntity)
    {
        CollisionExtrapolation extrapolation = new CollisionExtrapolation(firstEntity, secondEntity);
        extrapolation.computeDelta();
        extrapolation.optimizeDelta();
        extrapolation.computeVelocity();

        valid = extrapolation.isValid();
        topLeftEntity = extrapolation.getTopLeftEntity();
        bottomRightEntity = extrapolation.getBottomRightEntity();
        delta = extrapolation.getDelta();
        velocity = extrapolation.getVelocity();
        horizontal = extrapolation.isHorizontal();

        scale = topLeftEntity.getMassScaleTo(bottomRightEntity);
        hashCode = computeHashCode();
    }

    public boolean contains(Entity entity)
    {
        return topLeftEntity.equals(entity) || bottomRightEntity.equals(entity);
    }

    public boolean isValid()
    {
        return valid;
    }

    public double getDelta()
    {
        return delta;
    }

    public boolean isWeak()
    {
        return velocity < WEAK_COLLISION_VELOCITY;
    }

    public boolean isHorizontal()
    {
        return horizontal;
    }

    public void collide()
    {
        if (topLeftEntity.canTouch(bottomRightEntity))
        {
            topLeftEntity.collideWith(bottomRightEntity);
        }
        if (bottomRightEntity.canTouch(topLeftEntity))
        {
            bottomRightEntity.collideWith(topLeftEntity);
        }

        if (horizontal)
        {
            collideHorizontal();
        }
        else
        {
            collideVertical();
        }
    }

    private void collideHorizontal()
    {
        if (isWeak())
        {
            double velocity = scale * topLeftEntity.getXVelocity() + (1 - scale) * bottomRightEntity.getXVelocity();

            if (topLeftEntity.canTouch(bottomRightEntity))
            {
                topLeftEntity.setXVelocity(velocity);
            }
            if (bottomRightEntity.canTouch(topLeftEntity))
            {
                bottomRightEntity.setXVelocity(velocity);
            }
            return;
        }

        double elasticity = Math.min(topLeftEntity.getElasticity(), bottomRightEntity.getElasticity());
        double firstVelocity;
        double secondVelocity;
        if (Double.isInfinite(bottomRightEntity.getMass()) && Double.isInfinite(topLeftEntity.getMass()))
        {
            double average = (topLeftEntity.getXVelocity() + bottomRightEntity.getXVelocity()) / 2;
            firstVelocity = average - (topLeftEntity.getXVelocity() - bottomRightEntity.getXVelocity()) * elasticity / 2;
            secondVelocity = average - (bottomRightEntity.getXVelocity() - topLeftEntity.getXVelocity()) * elasticity / 2;
        }
        else if (Double.isInfinite(bottomRightEntity.getMass()))
        {
            firstVelocity = bottomRightEntity.getXVelocity() - (topLeftEntity.getXVelocity() - bottomRightEntity.getXVelocity()) * elasticity;
            secondVelocity = bottomRightEntity.getXVelocity();
        }
        else if (Double.isInfinite(topLeftEntity.getMass()))
        {
            firstVelocity = topLeftEntity.getXVelocity();
            secondVelocity = topLeftEntity.getXVelocity() - (bottomRightEntity.getXVelocity() - topLeftEntity.getXVelocity()) * elasticity;
        }
        else
        {
            double mass = topLeftEntity.getMass() + bottomRightEntity.getMass();
            double massPoint = (topLeftEntity.getMass() * topLeftEntity.getXVelocity() + bottomRightEntity.getMass() * bottomRightEntity.getXVelocity()) / mass;
            firstVelocity = massPoint - (bottomRightEntity.getMass() * (topLeftEntity.getXVelocity() - bottomRightEntity.getXVelocity()) * elasticity) / mass;
            secondVelocity = massPoint - (topLeftEntity.getMass() * (bottomRightEntity.getXVelocity() - topLeftEntity.getXVelocity()) * elasticity) / mass;
        }

        if (topLeftEntity.canTouch(bottomRightEntity))
        {
            topLeftEntity.setXVelocity(firstVelocity);
        }
        if (bottomRightEntity.canTouch(topLeftEntity))
        {
            bottomRightEntity.setXVelocity(secondVelocity);
        }
    }

    private void collideVertical()
    {
        if (isWeak())
        {
            double velocity = scale * topLeftEntity.getYVelocity() + (1 - scale) * bottomRightEntity.getYVelocity();

            if (topLeftEntity.canTouch(bottomRightEntity))
            {
                topLeftEntity.setYVelocity(velocity);
                topLeftEntity.hitGround();
            }
            if (bottomRightEntity.canTouch(topLeftEntity))
            {
                bottomRightEntity.setYVelocity(velocity);
            }

            return;
        }

        double elasticity = Math.min(topLeftEntity.getElasticity(), bottomRightEntity.getElasticity());
        double firstVelocity;
        double secondVelocity;
        if (Double.isInfinite(bottomRightEntity.getMass()) && Double.isInfinite(topLeftEntity.getMass()))
        {
            double average = (topLeftEntity.getYVelocity() + bottomRightEntity.getYVelocity()) / 2;
            firstVelocity = average - (topLeftEntity.getYVelocity() - bottomRightEntity.getYVelocity()) * elasticity / 2;
            secondVelocity = average - (bottomRightEntity.getYVelocity() - topLeftEntity.getYVelocity()) * elasticity / 2;
        }
        else if (Double.isInfinite(bottomRightEntity.getMass()))
        {
            firstVelocity = bottomRightEntity.getYVelocity() - (topLeftEntity.getYVelocity() - bottomRightEntity.getYVelocity()) * elasticity;
            secondVelocity = bottomRightEntity.getYVelocity();
        }
        else if (Double.isInfinite(topLeftEntity.getMass()))
        {
            firstVelocity = topLeftEntity.getYVelocity();
            secondVelocity = topLeftEntity.getYVelocity() - (bottomRightEntity.getYVelocity() - topLeftEntity.getYVelocity()) * elasticity;
        }
        else
        {
            double mass = topLeftEntity.getMass() + bottomRightEntity.getMass();
            double massPoint = (topLeftEntity.getMass() * topLeftEntity.getYVelocity() + bottomRightEntity.getMass() * bottomRightEntity.getYVelocity()) / mass;
            firstVelocity = massPoint - (bottomRightEntity.getMass() * (topLeftEntity.getYVelocity() - bottomRightEntity.getYVelocity()) * elasticity) / mass;
            secondVelocity = massPoint - (topLeftEntity.getMass() * (bottomRightEntity.getYVelocity() - topLeftEntity.getYVelocity()) * elasticity) / mass;
        }

        if (topLeftEntity.canTouch(bottomRightEntity))
        {
            topLeftEntity.setYVelocity(firstVelocity);
            topLeftEntity.hitGround();
        }
        if (bottomRightEntity.canTouch(topLeftEntity))
        {
            bottomRightEntity.setYVelocity(secondVelocity);
        }

    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Collision)
        {
            Collision collision = (Collision) obj;
            return topLeftEntity.equals(collision.topLeftEntity) && bottomRightEntity.equals(collision.bottomRightEntity) || topLeftEntity
                    .equals(collision.bottomRightEntity) && bottomRightEntity.equals(collision.topLeftEntity);
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
        return topLeftEntity.hashCode() * bottomRightEntity.hashCode();
    }

    @Override
    public String toString()
    {
        return topLeftEntity + " <-> " + bottomRightEntity;
    }
}
