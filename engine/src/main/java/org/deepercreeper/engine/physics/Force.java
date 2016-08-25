package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.util.Vector;

public interface Force
{
    Vector of(Entity entity);
}
