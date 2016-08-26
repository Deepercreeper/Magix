package org.deepercreeper.engine.physics.engine.motion.splitters;

import org.deepercreeper.engine.physics.Entity;

import java.util.Set;

public interface Splitter
{
    void split(Set<Entity> entities);
}
