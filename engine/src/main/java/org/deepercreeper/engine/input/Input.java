package org.deepercreeper.engine.input;

public interface Input
{
    boolean isActive(Key key);

    boolean checkHit(Key key);
}