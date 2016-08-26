package org.deepercreeper.engine.physics.engine.motion.components;

import org.deepercreeper.engine.physics.Entity;

public interface MotionComponent<T extends MotionComponent<T>> extends Runnable
{
    void add(Entity entity);

    void consume(T component);

    boolean isTouching(T component);

    void init(Runnable finishAction);
}