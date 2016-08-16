package org.deepercreeper.engine.util;

public abstract class GenericBuilder<T extends GenericBuilder<T>>
{
    protected abstract T getThis();
}
