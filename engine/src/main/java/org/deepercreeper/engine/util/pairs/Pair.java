package org.deepercreeper.engine.util.pairs;

public interface Pair<K, V>
{
    K getKey();

    V getValue();

    void setKey(K key);

    void setValue(V value);
}
