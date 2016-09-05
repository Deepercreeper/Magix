package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.physics.Entity;
import org.junit.Assert;
import org.junit.Test;

public class EntityEngineTest
{
    @Test
    public void testSolidAddition()
    {
        EntityEngine engine = new EntityEngine(new TestEngine());

        engine.add(new Entity.EntityBuilder().build());

        Assert.assertTrue(engine.getEntities().isEmpty());
        Assert.assertTrue(engine.getSolidEntities().isEmpty());

        engine.update(1);

        Assert.assertFalse(engine.getEntities().isEmpty());
        Assert.assertFalse(engine.getSolidEntities().isEmpty());
        Assert.assertTrue(engine.getNonSolidEntities().isEmpty());
    }

    @Test
    public void testNonSolidAddition()
    {
        EntityEngine engine = new EntityEngine(new TestEngine());

        engine.add(new Entity.EntityBuilder().setSolid(false).build());

        Assert.assertTrue(engine.getEntities().isEmpty());
        Assert.assertTrue(engine.getNonSolidEntities().isEmpty());

        engine.update(1);

        Assert.assertFalse(engine.getEntities().isEmpty());
        Assert.assertFalse(engine.getNonSolidEntities().isEmpty());
        Assert.assertTrue(engine.getSolidEntities().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalModification()
    {
        EntityEngine engine = new EntityEngine(new TestEngine());
        Entity entity = new Entity.EntityBuilder().build();

        engine.add(entity);
        engine.update(1);

        engine.getEntities().remove(entity);
    }
    
    @Test
    public void testRemoval(){
        
    }
}
