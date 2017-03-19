package org.deepercreeper.magix.game;

import org.deepercreeper.engine.geometry.position.Vector;
import org.deepercreeper.engine.input.Input;
import org.deepercreeper.engine.input.Key;
import org.deepercreeper.engine.physics.Engine;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.services.EntityService;
import org.deepercreeper.engine.services.MotionService;
import org.deepercreeper.engine.services.PhysicsService;
import org.deepercreeper.engine.services.RenderService;
import org.deepercreeper.magix.windows.Frame;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.*;

@Component
public class Magix implements InitializingBean {
    private final MotionService motionService;

    private final RenderService renderService;

    private final EntityService entityService;

    private final PhysicsService physicsService;

    private final Engine engine;

    private final Input input;

    private final Frame frame;

    @Autowired
    public Magix(MotionService motionService, RenderService renderService, EntityService entityService, PhysicsService physicsService, Engine engine, Input input, Frame frame) {
        this.motionService = motionService;
        this.renderService = renderService;
        this.entityService = entityService;
        this.physicsService = physicsService;
        this.engine = engine;
        this.input = input;
        this.frame = frame;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                engine.shutDown();
            }
        });
        engine.add((entity) -> new Vector(0, 9.81 / entity.getMass()));
        engine.add((entity) -> {
            double accelerationCoefficient = entity.isOnGround() ? 20 : 12;
            double rightAcceleration = input.isActive(Key.RIGHT) ? accelerationCoefficient : 0;
            double leftAcceleration = input.isActive(Key.LEFT) ? -accelerationCoefficient : 0;
            double friction = entity.isOnGround() ? entity.getXVelocity() * 5 : 0;
            return new Vector(rightAcceleration + leftAcceleration - friction, 0);
        });

        frame.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                motionService.setPause(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                motionService.setPause(true);
            }
        });
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                final boolean movable = e.getButton() != MouseEvent.BUTTON3;
                double scale = renderService.getScale();
                double x = (e.getX() + renderService.getPosition().getX()) / scale;
                double y = (e.getY() + renderService.getPosition().getY()) / scale;

                Entity entity = getEntityAt(x, y);
                if (entity != null) {
                    entity.setMass(entity.getMass() * 2);
                    return;
                }

                double mass = movable ? 1 : Double.POSITIVE_INFINITY;
                double width = movable ? .5 : 30;
                double height = movable ? .5 : .5;
                engine.add(createTestEntity(movable, x, y, width, height, mass, e.getButton() == MouseEvent.BUTTON2 ? 2 : 1));
            }

            private Entity getEntityAt(double x, double y) {
                for (Entity entity : entityService.getEntities()) {
                    if (entity.getX() <= x && x <= entity.getMaxX() && entity.getY() <= y && y <= entity.getMaxY()) {
                        return entity;
                    }
                }
                return null;
            }
        });
    }

    private Entity<?> createTestEntity(boolean movable, double x, double y, double width, double height, double mass, double speed) {
        return new TestEntity(physicsService) {
            @Override
            public Vector computeVelocity() {
                if (movable && isOnGround() && input.isActive(Key.JUMP)) {
                    return new Vector(getXVelocity(), getYVelocity() - 20);
                }
                return getVelocity();
            }

            @Override
            public Vector computePosition() {
                if (!movable) {
                    return getPosition();
                }
                if (!isCrouching() && input.isActive(Key.CROUCH)) {
                    return new Vector(getX(), getY() + getHeight() / 2);
                }
                else if (isCrouching() && !input.isActive(Key.CROUCH)) {
                    return new Vector(getX(), getY() - getHeight() / 2);
                }
                return getPosition();
            }

            @Override
            public Vector computeSize() {
                if (!movable) {
                    return getSize();
                }
                if (input.isActive(Key.CROUCH)) {
                    return new Vector(getWidth(), height / 2);
                }
                return new Vector(getWidth(), height);
            }

            @Override
            public void updateInternal(double delta) {
                if (!renderService.isVisible(this)) {
                    remove();
                }
            }

            private boolean isCrouching() {
                return getHeight() == height / 2;
            }
        }.setPosition(x - width / 2, y - height / 2).setSize(width, height).setMass(mass).setSpeed(speed);
    }
}
