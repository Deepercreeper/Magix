package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class EntityCollider
{
    private static final double DEFAULT_DAMPING_LIMIT = 2;

    private final Set<Entity> entities = new HashSet<>();

    private final Set<Set<Entity>> collisions = new HashSet<>();

    private final Map<Entity, Set<Entity>> connectedEntities = new HashMap<>();

    private double delta;

    public void collide(Set<Entity> entities, double delta)
    {
        this.delta = delta;
        this.entities.clear();
        this.entities.addAll(entities);
        collisions.clear();
        computeConnectedEntities();
        checkCollisions();
        collide();
    }

    private void computeConnectedEntities()
    {
        connectedEntities.clear();
        for (Entity entity : entities)
        {
            Set<Entity> connectedEntities = entities.stream().filter(connectedEntity -> !entity.equals(connectedEntity) && entity.isDeltaTouching(connectedEntity, delta))
                                                    .collect(Collectors.toSet());
            this.connectedEntities.put(entity, connectedEntities);
        }
    }

    private void checkCollisions()
    {
        entities.forEach(this::checkCollisionsOf);
    }

    private void checkCollisionsOf(Entity entity)
    {
        Box box = entity.getDeltaBox(delta);
        connectedEntities.get(entity).stream().filter(collisionEntity -> box.isTouching(collisionEntity.getDeltaBox(delta))).forEach(collisionEntity ->
        {
            Set<Entity> collision = new HashSet<>();
            collision.add(entity);
            collision.add(collisionEntity);
            collisions.add(collision);
        });
    }

    private void collide()
    {
        for (Set<Entity> collision : collisions)
        {
            Iterator<Entity> iterator = collision.iterator();
            Entity firstEntity = iterator.next();
            Entity secondEntity = iterator.next();
            collide(firstEntity, secondEntity);
        }
    }

    public void collide(Entity firstEntity, Entity secondEntity)
    {
        firstEntity.collideWith(secondEntity);
        secondEntity.collideWith(firstEntity);

        Vector difference = secondEntity.getCenter().minus(firstEntity.getCenter());

        double xFirstCorner = difference.getX() > 0 ? firstEntity.getMaxX() : firstEntity.getX();
        double yFirstCorner = difference.getY() > 0 ? firstEntity.getMaxY() : firstEntity.getY();
        Vector firstCorner = new Vector(xFirstCorner, yFirstCorner);

        double xSecondCorner = difference.getX() > 0 ? secondEntity.getX() : secondEntity.getMaxX();
        double ySecondCorner = difference.getY() > 0 ? secondEntity.getY() : secondEntity.getMaxY();
        Vector secondCorner = new Vector(xSecondCorner, ySecondCorner);

        if (Math.abs(firstCorner.getX() - secondCorner.getX()) > Math.abs(firstCorner.getY() - secondCorner.getY()))
        {
            collideVertical(firstEntity, secondEntity);
        }
        else
        {
            collideHorizontal(firstEntity, secondEntity);
        }
    }

    private void collideVertical(Entity firstEntity, Entity secondEntity)
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

        setVerticalVelocity(firstEntity, firstVelocity, secondEntity, secondVelocity);

        if (firstEntity.getCenterY() < secondEntity.getCenterY())
        {
            firstEntity.hitGround();
        }
        else
        {
            secondEntity.hitGround();
        }
    }

    private void setVerticalVelocity(Entity firstEntity, double firstVelocity, Entity secondEntity, double secondVelocity)
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

    private void collideHorizontal(Entity firstEntity, Entity secondEntity)
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

        setHorizontalVelocity(firstEntity, firstVelocity, secondEntity, secondVelocity);
    }

    private void setHorizontalVelocity(Entity firstEntity, double firstVelocity, Entity secondEntity, double secondVelocity)
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

    public boolean hasCollisions()
    {
        return !collisions.isEmpty();
    }
}
