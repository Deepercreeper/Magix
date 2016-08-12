package org.deepercreeper.engine.util.pairs;

public class ImmutablePair<K, V> implements Pair<K, V>
{
    private final K key;

    private final V value;

    public ImmutablePair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey()
    {
        return key;
    }

    @Override
    public V getValue()
    {
        return value;
    }

    @Override
    public void setKey(K key)
    {
        throw new UnsupportedOperationException("Cannot set the key of an immutable pair");
    }

    @Override
    public void setValue(V value)
    {
        throw new UnsupportedOperationException("Cannot set the value of an immutable pair");
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Pair<?, ?>)
        {
            Pair<?, ?> pair = (Pair<?, ?>) obj;
            boolean keyEquals = getKey() == null ? pair.getKey() == null : getKey().equals(pair.getKey());
            boolean valueEquals = getValue() == null ? pair.getValue() == null : getValue().equals(pair.getValue());
            return keyEquals && valueEquals;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode()
    {
        int keyHashCode = getKey() == null ? 0 : getKey().hashCode();
        int valueHashCode = getValue() == null ? 0 : getValue().hashCode();
        return keyHashCode * 13 + valueHashCode;
    }

    @Override
    public String toString()
    {
        return "(" + key + ", " + value + ")";
    }
}
