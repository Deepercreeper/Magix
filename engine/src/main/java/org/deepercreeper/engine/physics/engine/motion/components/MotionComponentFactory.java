package org.deepercreeper.engine.physics.engine.motion.components;

import org.deepercreeper.engine.physics.engine.motion.components.motion.ComponentMotion;

public interface MotionComponentFactory<T extends MotionComponent<T>>
{
    T create(ComponentMotion componentMotion, double delta);
}
