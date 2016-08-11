package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PhysicsEntityUpdater
{
    private static final double EPSILON = .1;

    private static final double MAX_STEP_VELOCITY = 1;

    private final Set<PhysicsEntity> entities = new HashSet<>();

    private Set<PhysicsEntity> collidedEntities = new HashSet<>();

    private double delta;

    private double leftDelta;

    private double stepDelta;

    private int steps;

    public void setDelta(double delta)
    {
        this.delta = delta;
    }

    public void update(Set<PhysicsEntity> entities)
    {
        this.entities.clear();
        this.entities.addAll(entities);
        update();
    }

    private void update()
    {
        if (entities.size() == 1)
        {
            updateSingleEntity();
            return;
        }
        leftDelta = delta;
        initDelta();
        while (steps > 0)
        {
            collidedEntities.clear();
            collideEntities();
            moveEntities();
            splitEntities();
            leftDelta -= stepDelta;
            steps--;
            if (!collidedEntities.isEmpty())
            {
                initDelta();
            }
        }
    }

    private void updateSingleEntity()
    {
        PhysicsEntity entity = entities.iterator().next();
        entity.move(delta);
    }

    private void collideEntities()
    {
        Set<PhysicsEntity> collisionEntities = new HashSet<>(entities);
        Iterator<PhysicsEntity> iterator = collisionEntities.iterator();
        while (iterator.hasNext())
        {
            PhysicsEntity entity = iterator.next();
            iterator.remove();
            if (iterator.hasNext())
            {
                collideEntityWith(entity, collisionEntities);
            }
        }
    }

    private void collideEntityWith(PhysicsEntity entity, Set<PhysicsEntity> collisionEntities)
    {
        Box box = entity.getBox().shift(entity.getVelocity().times(stepDelta));
        for (PhysicsEntity collisionEntity : collisionEntities)
        {
            Box entityBox = collisionEntity.getBox().shift(collisionEntity.getVelocity().times(stepDelta));
            if (box.isTouching(entityBox))
            {
                entity.collideWith(collisionEntity, 0.75);
                collidedEntities.add(entity);
                collidedEntities.add(collisionEntity);
            }
        }
    }

    private void moveEntities()
    {
        entities.forEach(entity -> entity.move(stepDelta));
    }

    private void splitEntities()
    {
        Set<PhysicsEntity> entities = new HashSet<>(this.entities);
        Iterator<PhysicsEntity> iterator = entities.iterator();
        while (iterator.hasNext())
        {
            PhysicsEntity entity = iterator.next();
            iterator.remove();
            if (iterator.hasNext())
            {
                splitEntityFrom(entity, entities);
            }
        }
    }

    private void splitEntityFrom(PhysicsEntity entity, Set<PhysicsEntity> splitEntities)
    {
        splitEntities.stream().filter(entity::isTouching).forEach(splitEntity -> split(entity, splitEntity));
    }

    private void split(PhysicsEntity firstEntity, PhysicsEntity secondEntity)
    {
        Box firstBox = firstEntity.getBox();
        Box secondBox = secondEntity.getBox();
        double positiveXDistance = firstBox.getMaxX() - secondBox.getX();
        double negativeXDistance = secondBox.getMaxX() - firstBox.getX();
        double positiveYDistance = firstBox.getMaxY() - secondBox.getY();
        double negativeYDistance = secondBox.getMaxY() - firstBox.getY();
        if (Math.min(Math.abs(positiveXDistance), Math.abs(negativeXDistance)) < Math.min(Math.abs(positiveYDistance), Math.abs(negativeYDistance)))
        {
            splitHorizontal(firstEntity, secondEntity, Math.abs(positiveXDistance) < Math.abs(negativeXDistance));
        }
        else
        {
            splitVertical(firstEntity, secondEntity, Math.abs(positiveYDistance) < Math.abs(negativeYDistance));
        }
    }

    private void splitHorizontal(PhysicsEntity firstEntity, PhysicsEntity secondEntity, boolean positive)
    {
        double x = positive ? firstEntity.getBox().getMaxX() + EPSILON : firstEntity.getBox().getX() - secondEntity.getBox().getWidth() - EPSILON;
        secondEntity.getBox().moveTo(new Vector(x, secondEntity.getBox().getY()));
    }

    private void splitVertical(PhysicsEntity firstEntity, PhysicsEntity secondEntity, boolean positive)
    {
        double y = positive ? firstEntity.getBox().getMaxY() + EPSILON : firstEntity.getBox().getY() - secondEntity.getBox().getHeight() - EPSILON;
        secondEntity.getBox().moveTo(new Vector(secondEntity.getBox().getX(), y));
    }

    private void initDelta()
    {
        double maxVelocity = entities.stream().map(entity -> entity.getVelocity().norm()).max(Double::compare).orElse(.0);
        if (maxVelocity < MAX_STEP_VELOCITY)
        {
            stepDelta = leftDelta;
            steps = 1;
            return;
        }
        stepDelta = leftDelta * MAX_STEP_VELOCITY / maxVelocity;
        steps = (int) Math.ceil(leftDelta / stepDelta);
    }
}
