package org.deepercreeper.engine.physics.engine.motion.components.motion;

import org.deepercreeper.engine.physics.Entity;

import java.util.Set;

public interface ComponentMotion
{
    void init(Set<Entity> entities);

    void move(double delta);
}
