package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class EntityCollider
{
    private static final double DEFAULT_DAMPING_LIMIT = 1;

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
            Set<Entity> connectedEntities = entities.stream().filter(connectedEntity -> !entity.equals(connectedEntity) && entity.isVelocityTouching(connectedEntity, delta))
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
        Box box = entity.getVelocityBox(delta);
        connectedEntities.get(entity).stream().filter(collisionEntity -> box.isTouching(collisionEntity.getVelocityBox(delta))).forEach(collisionEntity ->
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
        double differenceLength = Math.min(Math.abs(secondEntity.getY() - firstEntity.getMaxY()), Math.abs(firstEntity.getY() - secondEntity.getMaxY()));
        double difference = Math.signum(secondEntity.getY() - firstEntity.getY()) * differenceLength;
        if (Math.abs(firstVelocity) < DEFAULT_DAMPING_LIMIT * firstEntity.getSpeed() || Math.abs(secondVelocity) < DEFAULT_DAMPING_LIMIT * secondEntity.getSpeed())
        {
            firstVelocity = Math.abs(firstVelocity) < DEFAULT_DAMPING_LIMIT * firstEntity.getSpeed() ? 0 : firstVelocity;
            secondVelocity = Math.abs(secondVelocity) < DEFAULT_DAMPING_LIMIT * secondEntity.getSpeed() ? 0 : secondVelocity;

            double scale = firstEntity.getMassScaleTo(secondEntity);

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
        double differenceLength = Math.min(Math.abs(secondEntity.getX() - firstEntity.getMaxX()), Math.abs(firstEntity.getX() - secondEntity.getMaxX()));
        double difference = Math.signum(secondEntity.getX() - firstEntity.getX()) * differenceLength;
        if (Math.abs(firstVelocity) < DEFAULT_DAMPING_LIMIT * firstEntity.getSpeed() || Math.abs(secondVelocity) < DEFAULT_DAMPING_LIMIT * secondEntity.getSpeed())
        {
            firstVelocity = Math.abs(firstVelocity) < DEFAULT_DAMPING_LIMIT * firstEntity.getSpeed() ? 0 : firstVelocity;
            secondVelocity = Math.abs(secondVelocity) < DEFAULT_DAMPING_LIMIT * secondEntity.getSpeed() ? 0 : secondVelocity;

            double scale = firstEntity.getMassScaleTo(secondEntity);

            firstEntity.moveBy((1 - scale) * difference, 0);
            secondEntity.moveBy(scale * -difference, 0);
        }

        firstEntity.setXVelocity(firstVelocity);
        secondEntity.setXVelocity(secondVelocity);
    }

    public boolean hasCollisions()
    {
        return !collisions.isEmpty();
    }
}
