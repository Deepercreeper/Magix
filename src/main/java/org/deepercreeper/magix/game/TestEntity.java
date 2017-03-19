package org.deepercreeper.magix.game;

import org.deepercreeper.engine.annotations.PrototypeComponent;
import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.geometry.box.Box;
import org.deepercreeper.engine.geometry.rectangle.AbstractRectangle;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.services.PhysicsService;
import org.deepercreeper.engine.util.Image;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;

@PrototypeComponent
public class TestEntity extends Entity<TestEntity> {
    @Autowired
    public TestEntity(PhysicsService physicsService) {
        super(physicsService);
    }

    @Override
    public Image generateImage(double scale) {
        Color color = Color.getHSBColor(0, (float) (Math.min(getVelocity().norm() / 10, 1)), 1);
        Image image = new Image().set(asScaledRectangle(scale));
        image.setData(Display.createFilledRectangle(image.getWidth(), image.getHeight(), 0xff000000 | color.getRGB()));

        color = Color.getHSBColor(.5f, (float) getElasticity(), 1);
        AbstractRectangle<?> overlayRectangle = new Box().setPosition(getPosition().plus(getSize().times(.25))).setWidth(getWidth() * .5).setHeight(getHeight() * .25)
                .asScaledRectangle(scale);
        Image overlay = new Image().set(overlayRectangle);
        overlay.setData(Display.createFilledRectangle(overlay.getWidth(), overlay.getHeight(), 0xff000000 | color.getRGB()));
        overlay.drawOver(image);

        color = Color.getHSBColor(.75f, (float) (Math.log(getMass()) / Math.log(2)) / 20, 1);
        overlayRectangle = new Box().setX(getX() + getWidth() * .25).setY(getY() + getHeight() * .5).setWidth(getWidth() * .5).setHeight(getHeight() * .25).asScaledRectangle
                (scale);
        overlay = new Image().set(overlayRectangle);
        overlay.setData(Display.createFilledRectangle(overlay.getWidth(), overlay.getHeight(), 0xff000000 | color.getRGB()));
        overlay.drawOver(image);

        return image;
    }

    @Override
    protected TestEntity getThis() {
        return this;
    }
}
