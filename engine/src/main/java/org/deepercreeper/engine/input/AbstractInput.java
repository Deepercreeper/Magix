package org.deepercreeper.engine.input;

public class AbstractInput implements Input
{
    private final boolean[] activeKeys = new boolean[Key.values().length];

    private final boolean[] hitKeys = new boolean[Key.values().length];

    protected final void keyPressed(Key key)
    {
        if (key == null)
        {
            return;
        }
        int index = key.ordinal();
        if (!activeKeys[index])
        {
            hitKeys[index] = true;
        }
        activeKeys[index] = true;
    }

    protected final void keyReleased(Key key)
    {
        if (key == null)
        {
            return;
        }
        activeKeys[key.ordinal()] = false;
    }

    @Override
    public final boolean isActive(Key key)
    {
        if (key == null)
        {
            throw new NullPointerException("Key must not be null");
        }
        return activeKeys[key.ordinal()];
    }

    @Override
    public final boolean checkHit(Key key)
    {
        if (key == null)
        {
            throw new NullPointerException("Key must not be null");
        }
        if (hitKeys[key.ordinal()])
        {
            hitKeys[key.ordinal()] = false;
            return true;
        }
        return false;
    }
}
