package org.deepercreeper.windows;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Rectangle;

import java.awt.*;

public abstract class TestEntity extends Entity
{
    public TestEntity(double x, double y, double width, double height, double mass, double elasticity)
    {
        super(new EntityBuilder().setX(x).setY(y).setWidth(width).setHeight(height).setMass(mass).setElasticity(elasticity));
    }

    @Override
    public void render()
    {
        Rectangle rectangle = asScaledRectangle(getEngine().getScale());
        Color color;
        color = Color.getHSBColor(0, (float) (Math.min(getVelocity().norm() / 10, 1)), 1);
        getEngine().getDisplay().render(rectangle, Display.createRectangle(rectangle.getWidth(), rectangle.getHeight(), 0xff000000 | color.getRGB()));

        color = Color.getHSBColor(.5f, (float) getElasticity(), 1);
        rectangle = new BoxBuilder().setPosition(getPosition().plus(getSize().times(.25))).setSize(getSize().times(.5)).build().asScaledRectangle(getEngine().getScale());
        getEngine().getDisplay().render(rectangle, Display.createRectangle(rectangle.getWidth(), rectangle.getHeight(), 0xff000000 | color.getRGB()));
    }
}
