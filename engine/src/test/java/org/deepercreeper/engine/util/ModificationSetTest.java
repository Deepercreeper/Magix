package org.deepercreeper.engine.util;

import org.junit.Assert;
import org.junit.Test;

public class ModificationSetTest
{
    @Test
    public void testAllowedModification()
    {
        ModificationSet<String> set = new ModificationSet<>();

        set.setModifiable(true);

        set.add("First");

        Assert.assertFalse(set.isEmpty());

        set.remove("First");

        Assert.assertTrue(set.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPermittedModification()
    {
        ModificationSet<String> set = new ModificationSet<>();

        set.add("First");

        set.setModifiable(false);

        Assert.assertTrue(set.contains("First"));

        set.remove("Second");
    }

    @Test
    public void testDefaultState()
    {
        new ModificationSet<String>().add("First");
    }
}
