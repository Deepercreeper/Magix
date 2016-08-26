package org.deepercreeper.engine.physics.engine.motion.colliders;

import org.deepercreeper.engine.physics.Entity;

import java.util.Set;

public interface Collider
{
    void init(Set<Entity> entities);

    void collide(double delta);

    boolean changedVelocities();
}
