package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Vector;

import java.util.*;

public class EntitySplitter
{
    private static final double EPSILON = .1;

    private final Set<Entity> entities = new HashSet<>();

    public void split(Set<Entity> entities)
    {
        this.entities.clear();
        this.entities.addAll(entities);
        split();
    }

    private void split()
    {
        List<Entity> entities = new ArrayList<>(this.entities);
        Collections.sort(entities, (o1, o2) -> -Double.compare(o1.getMass(), o2.getMass()));
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext())
        {
            Entity entity = iterator.next();
            iterator.remove();
            if (iterator.hasNext())
            {
                splitEntityFrom(entity, entities);
            }
        }
    }

    private void splitEntityFrom(Entity entity, List<Entity> splitEntities)
    {
        splitEntities.stream().filter(entity::isTouching).forEach(splitEntity -> split(entity, splitEntity));
    }

    private void split(Entity firstEntity, Entity secondEntity)
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

    private void splitHorizontal(Entity firstEntity, Entity secondEntity, boolean positive)
    {
        double scale = computeScaleOf(firstEntity, secondEntity);
        double position;
        double firstX;
        double secondX;
        if (positive)
        {
            position = (1 - scale) * secondEntity.getBox().getX() + scale * firstEntity.getBox().getMaxX();
            firstX = position - firstEntity.getBox().getWidth() - (1 - scale) * EPSILON / 2;
            secondX = position + scale * EPSILON / 2;
        }
        else
        {
            position = (1 - scale) * secondEntity.getBox().getMaxX() + scale * firstEntity.getBox().getX();
            firstX = position + (1 - scale) * EPSILON / 2;
            secondX = position - secondEntity.getBox().getWidth() - scale * EPSILON / 2;
        }
        firstEntity.getBox().moveTo(new Vector(firstX, firstEntity.getBox().getY()));
        secondEntity.getBox().moveTo(new Vector(secondX, secondEntity.getBox().getY()));
    }

    private void splitVertical(Entity firstEntity, Entity secondEntity, boolean positive)
    {
        double scale = computeScaleOf(firstEntity, secondEntity);
        double position;
        double firstY;
        double secondY;
        if (positive)
        {
            position = (1 - scale) * secondEntity.getBox().getY() + scale * firstEntity.getBox().getMaxY();
            firstY = position - firstEntity.getBox().getHeight() - (1 - scale) * EPSILON / 2;
            secondY = position + scale * EPSILON / 2;
        }
        else
        {
            position = (1 - scale) * secondEntity.getBox().getMaxY() + scale * firstEntity.getBox().getY();
            firstY = position + (1 - scale) * EPSILON / 2;
            secondY = position - secondEntity.getBox().getHeight() - scale * EPSILON / 2;
        }
        firstEntity.getBox().moveTo(new Vector(firstEntity.getBox().getX(), firstY));
        secondEntity.getBox().moveTo(new Vector(secondEntity.getBox().getX(), secondY));
    }

    private double computeScaleOf(Entity firstEntity, Entity secondEntity)
    {
        if (Double.isInfinite(firstEntity.getMass()))
        {
            return Double.isInfinite(secondEntity.getMass()) ? 0.5 : 1;
        }
        if (Double.isInfinite(secondEntity.getMass()))
        {
            return 0;
        }
        return firstEntity.getMass() / (firstEntity.getMass() + secondEntity.getMass());
    }
}
