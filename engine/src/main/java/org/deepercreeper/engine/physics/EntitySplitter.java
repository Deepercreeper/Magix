package org.deepercreeper.engine.physics;

import java.util.*;

@Deprecated
public class EntitySplitter
{
    private static final double EPSILON = 10E-10;

    private final List<Entity> entities = new ArrayList<>();

    public void split(Set<Entity> entities)
    {
        this.entities.clear();
        this.entities.addAll(entities);
        Collections.sort(this.entities, (e1, e2) -> Double.compare(e1.getMass(), e2.getMass()));
        split();
    }

    private void split()
    {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext())
        {
            Entity entity = iterator.next();
            iterator.remove();
            if (iterator.hasNext())
            {
                split(entity);
            }
        }
    }

    private void split(Entity entity)
    {
        entities.stream().filter(entity::isTouching).forEach(splitEntity -> split(entity, splitEntity));
    }

    private void split(Entity firstEntity, Entity secondEntity)
    {
        double positiveXDistance = firstEntity.getMaxX() - secondEntity.getX();
        double negativeXDistance = secondEntity.getMaxX() - firstEntity.getX();
        double positiveYDistance = firstEntity.getMaxY() - secondEntity.getY();
        double negativeYDistance = secondEntity.getMaxY() - firstEntity.getY();
        if (Math.min(Math.abs(positiveXDistance), Math.abs(negativeXDistance)) < Math.min(Math.abs(positiveYDistance), Math.abs(negativeYDistance)))
        {
            splitHorizontal(firstEntity, secondEntity, firstEntity.getCenterX() < secondEntity.getCenterX());
        }
        else
        {
            splitVertical(firstEntity, secondEntity, firstEntity.getCenterY() < secondEntity.getCenterY());
        }
    }

    private void splitHorizontal(Entity firstEntity, Entity secondEntity, boolean firstOnLeft)
    {
        double scale = firstEntity.getMassScaleTo(secondEntity);
        double position;
        double firstX;
        double secondX;
        if (firstOnLeft)
        {
            position = (1 - scale) * secondEntity.getX() + scale * firstEntity.getMaxX();
            firstX = position - firstEntity.getWidth() - (1 - scale) * EPSILON;
            secondX = position + scale * EPSILON;
        }
        else
        {
            position = (1 - scale) * secondEntity.getMaxX() + scale * firstEntity.getX();
            firstX = position + (1 - scale) * EPSILON;
            secondX = position - secondEntity.getWidth() - scale * EPSILON;
        }
        firstEntity.setX(firstX);
        secondEntity.setX(secondX);
    }

    private void splitVertical(Entity firstEntity, Entity secondEntity, boolean firstOnTop)
    {
        double scale = firstEntity.getMassScaleTo(secondEntity);
        double position;
        double firstY;
        double secondY;
        if (firstOnTop)
        {
            position = (1 - scale) * secondEntity.getY() + scale * firstEntity.getMaxY();
            firstY = position - firstEntity.getHeight() - (1 - scale) * EPSILON;
            secondY = position + scale * EPSILON;
        }
        else
        {
            position = (1 - scale) * secondEntity.getMaxY() + scale * firstEntity.getY();
            firstY = position + (1 - scale) * EPSILON;
            secondY = position - secondEntity.getHeight() - scale * EPSILON;
        }
        firstEntity.setY(firstY);
        secondEntity.setY(secondY);
    }
}
