package org.deepercreeper.engine.physics.engine;

public abstract class AbstractEngine
{
    private final Engine engine;

    public AbstractEngine(Engine engine)
    {
        this.engine = engine;
    }

    public Engine getEngine()
    {
        return engine;
    }
}
