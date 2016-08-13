package org.deepercreeper.windows;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Box;
import org.deepercreeper.engine.util.Rectangle;

import java.awt.*;

public abstract class TestEntity extends Entity
{
    private final double mass;

    public TestEntity(double x, double y, double width, double height, double mass)
    {
        super(x, y, width, height);
        this.mass = mass;
    }

    @Override
    public double getMass()
    {
        return mass;
    }

    @Override
    public boolean isAccelerated()
    {
        return true;
    }

    @Override
    public double getElasticity()
    {
        return .75;
    }

    @Override
    public void render()
    {
        Rectangle rectangle = getBox().asRectangle();
        Color color;
        color = Color.getHSBColor(0, (float) (Math.min(getVelocity().norm() / 1000, 1)), 1);
        getEngine().getDisplay().render(rectangle, Display.createRectangle(rectangle.getWidth(), rectangle.getHeight(), 0xff000000 | color.getRGB()));

        color = Color.getHSBColor(0.001f * (System.currentTimeMillis() % 1000), 1, .5f);
        rectangle = new Box(getBox().getPosition().plus(getBox().getSize().times(.25)), getBox().getSize().times(.5)).asRectangle();
        getEngine().getDisplay().render(rectangle, Display.createRectangle(rectangle.getWidth(), rectangle.getHeight(), 0xff000000 | color.getRGB()));
    }
}
