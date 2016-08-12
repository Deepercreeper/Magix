package org.deepercreeper.engine.util.pairs;

public class MutablePair<K, V> implements Pair<K, V>
{
    private K key;

    private V value;

    public MutablePair(K key, V value)
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
        this.key = key;
    }

    @Override
    public void setValue(V value)
    {
        this.value = value;
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
