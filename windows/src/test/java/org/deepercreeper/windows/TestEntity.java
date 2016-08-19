package org.deepercreeper.windows;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Image;
import org.deepercreeper.engine.util.Rectangle;

import java.awt.*;

public abstract class TestEntity extends Entity
{
    public TestEntity(double x, double y, double width, double height, double mass, double elasticity, double speed)
    {
        super(new EntityBuilder().setX(x).setY(y).setWidth(width).setHeight(height).setMass(mass).setElasticity(elasticity).setSpeed(speed));
    }

    @Override
    public Image generateImage(double scale)
    {
        Color color = Color.getHSBColor(0, (float) (Math.min(getVelocity().norm() / 10, 1)), 1);
        Image image = new Image.ImageBuilder().set(asScaledRectangle(scale)).build();
        image.setData(Display.createFilledRectangle(image.getWidth(), image.getHeight(), 0xff000000 | color.getRGB()));

        color = Color.getHSBColor(.5f, (float) getElasticity(), 1);
        Rectangle overlayRectangle = new BoxBuilder().setPosition(getPosition().plus(getSize().times(.25))).setSize(getSize().times(.5)).build().asScaledRectangle(scale);
        Image overlay = new Image.ImageBuilder().set(overlayRectangle).build();
        overlay.setData(Display.createFilledRectangle(overlay.getWidth(), overlay.getHeight(), 0xff000000 | color.getRGB()));
        overlay.drawOver(image);

        return image;
    }
}
