package org.deepercreeper.engine.util;

import java.util.HashSet;

public class ModificationSet<T> extends HashSet<T>
{
    private boolean modifiable = true;

    public void setModifiable(boolean modifiable)
    {
        this.modifiable = modifiable;
    }

    @Override
    public boolean add(T t)
    {
        checkModifiable();
        return super.add(t);
    }

    @Override
    public boolean remove(Object o)
    {
        checkModifiable();
        return super.remove(o);
    }

    private void checkModifiable()
    {
        if (!modifiable)
        {
            throw new UnsupportedOperationException("Modification is not supported");
        }
    }
}
