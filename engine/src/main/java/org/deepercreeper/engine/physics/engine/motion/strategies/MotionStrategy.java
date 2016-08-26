package org.deepercreeper.engine.physics.engine.motion.strategies;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.Updatable;

import java.util.Set;

public interface MotionStrategy extends Updatable
{
    void init(Set<Entity> entities);
}
