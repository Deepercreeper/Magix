package org.deepercreeper.engine.physics.engine.motion.splitters;

import org.deepercreeper.engine.physics.Entity;

import java.util.*;

public class AxialSplitter implements Splitter
{
    private final List<Entity> entities = new ArrayList<>();

    private final double epsilon;

    private Entity firstEntity;

    private Entity secondEntity;

    public AxialSplitter(double epsilon)
    {
        this.epsilon = epsilon;
    }

    public AxialSplitter()
    {
        this(10E-5);
    }

    @Override
    public void split(Set<Entity> entities)
    {
        this.entities.clear();
        this.entities.addAll(entities);
        Collections.sort(this.entities, (e1, e2) -> Double.compare(e2.getMass(), e1.getMass()));
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

    private void split(Entity firstEntity)
    {
        for (Entity secondEntity : entities)
        {
            split(firstEntity, secondEntity);
        }
    }

    private void split(Entity firstEntity, Entity secondEntity)
    {
        if (!firstEntity.isTouching(secondEntity) || !firstEntity.canTouch(secondEntity) && !secondEntity.canTouch(firstEntity))
        {
            return;
        }
        this.firstEntity = firstEntity;
        this.secondEntity = secondEntity;

        double positiveXDistance = firstEntity.getMaxX() - secondEntity.getX();
        double negativeXDistance = secondEntity.getMaxX() - firstEntity.getX();
        double positiveYDistance = firstEntity.getMaxY() - secondEntity.getY();
        double negativeYDistance = secondEntity.getMaxY() - firstEntity.getY();
        if (Math.min(Math.abs(positiveXDistance), Math.abs(negativeXDistance)) < Math.min(Math.abs(positiveYDistance), Math.abs(negativeYDistance)))
        {
            splitHorizontal(firstEntity.getCenterX() < secondEntity.getCenterX());
        }
        else
        {
            splitVertical(firstEntity.getCenterY() < secondEntity.getCenterY());
        }
    }

    private void splitHorizontal(boolean firstOnLeft)
    {
        double scale = getScale();
        double position;
        double firstX;
        double secondX;
        if (firstOnLeft)
        {
            position = (1 - scale) * secondEntity.getX() + scale * firstEntity.getMaxX();
            firstX = position - firstEntity.getWidth() - (1 - scale) * epsilon;
            secondX = position + scale * epsilon;
        }
        else
        {
            position = (1 - scale) * secondEntity.getMaxX() + scale * firstEntity.getX();
            firstX = position + (1 - scale) * epsilon;
            secondX = position - secondEntity.getWidth() - scale * epsilon;
        }
        firstEntity.setX(firstX);
        secondEntity.setX(secondX);
    }

    private void splitVertical(boolean firstOnTop)
    {
        double scale = getScale();
        double position;
        double firstY;
        double secondY;
        if (firstOnTop)
        {
            position = (1 - scale) * secondEntity.getY() + scale * firstEntity.getMaxY();
            firstY = position - firstEntity.getHeight() - (1 - scale) * epsilon;
            secondY = position + scale * epsilon;
        }
        else
        {
            position = (1 - scale) * secondEntity.getMaxY() + scale * firstEntity.getY();
            firstY = position + (1 - scale) * epsilon;
            secondY = position - secondEntity.getHeight() - scale * epsilon;
        }
        firstEntity.setY(firstY);
        secondEntity.setY(secondY);
    }

    private double getScale()
    {
        if (!firstEntity.canTouch(secondEntity))
        {
            return 1;
        }
        if (!secondEntity.canTouch(firstEntity))
        {
            return 0;
        }
        return firstEntity.getMassScaleTo(secondEntity);
    }
}
