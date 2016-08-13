package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class EntityCollider
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityCollider.class);

    private static final double EPSILON = 56;

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
        Box box = entity.getBox().shift(entity.getVelocity().times(delta));
        for (Entity collisionEntity : connectedEntities.get(entity))
        {
            Box entityBox = collisionEntity.getBox().shift(collisionEntity.getVelocity().times(delta));
            if (box.isTouching(entityBox))
            {
                Set<Entity> collision = new HashSet<>();
                collision.add(entity);
                collision.add(collisionEntity);
                collisions.add(collision);
            }
        }
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
        Vector difference = secondEntity.getCenter().minus(firstEntity.getCenter());

        double xFirstCorner = difference.getX() > 0 ? firstEntity.getBox().getMaxX() : firstEntity.getBox().getX();
        double yFirstCorner = difference.getY() > 0 ? firstEntity.getBox().getMaxY() : firstEntity.getBox().getY();
        Vector firstCorner = new Vector(xFirstCorner, yFirstCorner);

        double xSecondCorner = difference.getX() > 0 ? secondEntity.getBox().getX() : secondEntity.getBox().getMaxX();
        double ySecondCorner = difference.getY() > 0 ? secondEntity.getBox().getY() : secondEntity.getBox().getMaxY();
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
        double mass = firstEntity.getMass() + secondEntity.getMass();
        double firstVelocity;
        double secondVelocity;
        if (Double.isInfinite(secondEntity.getMass()) && Double.isInfinite(firstEntity.getMass()))
        {
            double average = (firstEntity.getVelocity().getY() + secondEntity.getVelocity().getY()) / 2;
            firstVelocity = average - (firstEntity.getVelocity().getY() - secondEntity.getVelocity().getY()) * elasticity / 2;
            secondVelocity = average - (secondEntity.getVelocity().getY() - firstEntity.getVelocity().getY()) * elasticity / 2;
        }
        else if (Double.isInfinite(secondEntity.getMass()))
        {
            firstVelocity = secondEntity.getVelocity().getY() - (firstEntity.getVelocity().getY() - secondEntity.getVelocity().getY()) * elasticity;
            secondVelocity = secondEntity.getVelocity().getY();
        }
        else if (Double.isInfinite(firstEntity.getMass()))
        {
            firstVelocity = firstEntity.getVelocity().getY();
            secondVelocity = firstEntity.getVelocity().getY() - (secondEntity.getVelocity().getY() - firstEntity.getVelocity().getY()) * elasticity;
        }
        else
        {
            double massPoint = (firstEntity.getMass() * firstEntity.getVelocity().getY() + secondEntity.getMass() * secondEntity.getVelocity().getY()) / mass;
            firstVelocity = massPoint - (secondEntity.getMass() * (firstEntity.getVelocity().getY() - secondEntity.getVelocity().getY()) * elasticity) / mass;
            secondVelocity = massPoint - (firstEntity.getMass() * (secondEntity.getVelocity().getY() - firstEntity.getVelocity().getY()) * elasticity) / mass;
        }

        setVerticalVelocity(firstEntity, firstVelocity, secondEntity, secondVelocity);

        if (firstEntity.getBox().getY() < secondEntity.getBox().getY())
        {
            firstEntity.onGround();
        }
        else
        {
            secondEntity.onGround();
        }
    }

    private void setVerticalVelocity(Entity firstEntity, double firstVelocity, Entity secondEntity, double secondVelocity)
    {
        double differenceLength = Math
                .min(Math.abs(secondEntity.getBox().getY() - firstEntity.getBox().getMaxY()), Math.abs(firstEntity.getBox().getY() - secondEntity.getBox().getMaxY()));
        double difference = Math.signum(secondEntity.getCenter().getY() - firstEntity.getCenter().getY()) * differenceLength;
        if (Math.abs(firstVelocity) < EPSILON || Math.abs(secondVelocity) < EPSILON)
        {
            firstVelocity = Math.abs(firstVelocity) < EPSILON ? 0 : firstVelocity;
            secondVelocity = Math.abs(secondVelocity) < EPSILON ? 0 : secondVelocity;

            double scale = firstEntity.getMassScaleTo(secondEntity);

            firstEntity.getBox().move(new Vector(0, (1 - scale) * difference));
            secondEntity.getBox().move(new Vector(0, scale * -difference));
        }

        firstEntity.setVelocity(new Vector(firstEntity.getVelocity().getX(), firstVelocity));
        secondEntity.setVelocity(new Vector(secondEntity.getVelocity().getX(), secondVelocity));
    }

    private void collideHorizontal(Entity firstEntity, Entity secondEntity)
    {
        double elasticity = Math.sqrt(firstEntity.getElasticity() * secondEntity.getElasticity());
        double mass = firstEntity.getMass() + secondEntity.getMass();
        double firstVelocity;
        double secondVelocity;
        if (Double.isInfinite(secondEntity.getMass()) && Double.isInfinite(firstEntity.getMass()))
        {
            double average = (firstEntity.getVelocity().getX() + secondEntity.getVelocity().getX()) / 2;
            firstVelocity = average - (firstEntity.getVelocity().getX() - secondEntity.getVelocity().getX()) * elasticity / 2;
            secondVelocity = average - (secondEntity.getVelocity().getX() - firstEntity.getVelocity().getX()) * elasticity / 2;
        }
        else if (Double.isInfinite(secondEntity.getMass()))
        {
            firstVelocity = secondEntity.getVelocity().getX() - (firstEntity.getVelocity().getX() - secondEntity.getVelocity().getX()) * elasticity;
            secondVelocity = secondEntity.getVelocity().getX();
        }
        else if (Double.isInfinite(firstEntity.getMass()))
        {
            firstVelocity = firstEntity.getVelocity().getX();
            secondVelocity = firstEntity.getVelocity().getX() - (secondEntity.getVelocity().getX() - firstEntity.getVelocity().getX()) * elasticity;
        }
        else
        {
            double massPoint = (firstEntity.getMass() * firstEntity.getVelocity().getX() + secondEntity.getMass() * secondEntity.getVelocity().getX()) / mass;
            firstVelocity = massPoint - (secondEntity.getMass() * (firstEntity.getVelocity().getX() - secondEntity.getVelocity().getX()) * elasticity) / mass;
            secondVelocity = massPoint - (firstEntity.getMass() * (secondEntity.getVelocity().getX() - firstEntity.getVelocity().getX()) * elasticity) / mass;
        }

        setHorizontalVelocity(firstEntity, firstVelocity, secondEntity, secondVelocity);
    }

    private void setHorizontalVelocity(Entity firstEntity, double firstVelocity, Entity secondEntity, double secondVelocity)
    {
        double differenceLength = Math
                .min(Math.abs(secondEntity.getBox().getX() - firstEntity.getBox().getMaxX()), Math.abs(firstEntity.getBox().getX() - secondEntity.getBox().getMaxX()));
        double difference = Math.signum(secondEntity.getCenter().getX() - firstEntity.getCenter().getX()) * differenceLength;
        if (Math.abs(firstVelocity) < EPSILON || Math.abs(secondVelocity) < EPSILON)
        {
            firstVelocity = Math.abs(firstVelocity) < EPSILON ? 0 : firstVelocity;
            secondVelocity = Math.abs(secondVelocity) < EPSILON ? 0 : secondVelocity;

            double scale = firstEntity.getMassScaleTo(secondEntity);

            firstEntity.getBox().move(new Vector((1 - scale) * difference, 0));
            secondEntity.getBox().move(new Vector(scale * -difference, 0));
        }

        firstEntity.setVelocity(new Vector(firstVelocity, firstEntity.getVelocity().getY()));
        secondEntity.setVelocity(new Vector(secondVelocity, secondEntity.getVelocity().getY()));
    }

    public boolean hasCollisions()
    {
        return !collisions.isEmpty();
    }
}
