package org.deepercreeper.engine.input;

import org.junit.Assert;
import org.junit.Test;

public class AbstractInputTest
{
    @Test
    public void testActive()
    {
        AbstractInput input = new AbstractInput();

        Assert.assertFalse(input.isActive(Key.JUMP));

        input.keyPressed(Key.JUMP);

        Assert.assertTrue(input.isActive(Key.JUMP));

        input.keyReleased(Key.JUMP);

        Assert.assertFalse(input.isActive(Key.JUMP));
    }

    @Test
    public void testHit()
    {
        AbstractInput input = new AbstractInput();

        Assert.assertFalse(input.checkHit(Key.JUMP));

        input.keyPressed(Key.JUMP);

        Assert.assertTrue(input.checkHit(Key.JUMP));
        Assert.assertFalse(input.checkHit(Key.JUMP));

        input.keyReleased(Key.JUMP);
        input.keyPressed(Key.JUMP);
        input.keyReleased(Key.JUMP);

        Assert.assertTrue(input.checkHit(Key.JUMP));
        Assert.assertFalse(input.checkHit(Key.JUMP));
    }

    @Test
    public void testNullPressed()
    {
        AbstractInput input = new AbstractInput();

        input.keyPressed(null);
        input.keyReleased(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullActive()
    {
        AbstractInput input = new AbstractInput();

        input.isActive(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullHit()
    {
        AbstractInput input = new AbstractInput();

        input.checkHit(null);
    }
}
