package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.display.TestDisplay;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.TestEntity;
import org.deepercreeper.engine.util.Image;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class RenderingEngineTest
{
    @Test
    public void testRendering()
    {
        Engine engine = new Engine();
        EntityEngine entityEngine = new EntityEngine(engine);
        TestDisplay display = new TestDisplay(100, 100);
        RenderingEngine renderingEngine = new RenderingEngine(entityEngine, engine.getUpdateEngine());

        renderingEngine.setDisplay(display);
        renderingEngine.update(1);

        Assert.assertEquals(0, display.getRenderings());
        Assert.assertEquals(0, display.getClears());

        entityEngine.add(new TestEntity(0, 0, 1, 11));

        entityEngine.update(1);
        renderingEngine.update(1);

        Assert.assertEquals(1, display.getRenderings());
        Assert.assertEquals(0, display.getClears());

        renderingEngine.update(1);

        Assert.assertEquals(2, display.getRenderings());
        Assert.assertEquals(0, display.getClears());
    }

    @Test
    public void testClearing()
    {
        Engine engine = new Engine();
        EntityEngine entityEngine = new EntityEngine(engine);
        TestDisplay display = new TestDisplay(100, 100);
        RenderingEngine renderingEngine = new RenderingEngine(entityEngine, engine.getUpdateEngine());
        Entity entity = new Entity.EntityBuilder().setXVelocity(1).setWidth(1).setHeight(1).build();

        renderingEngine.setDisplay(display);
        entityEngine.add(entity);

        entityEngine.update(1);
        renderingEngine.update(1);

        entity.move(1);

        renderingEngine.update(1);

        Assert.assertEquals(2, display.getRenderings());
        Assert.assertEquals(1, display.getClears());
    }

    @Test
    public void testImageGeneration()
    {
        Engine engine = new Engine();
        EntityEngine entityEngine = new EntityEngine(engine);
        TestDisplay display = new TestDisplay(100, 100);
        RenderingEngine renderingEngine = new RenderingEngine(entityEngine, engine.getUpdateEngine());

        AtomicInteger generationCounter = new AtomicInteger();
        Entity entity = new TestEntity(0, 0, 1, 1)
        {
            @Override
            public Image generateImage(double scale)
            {
                generationCounter.incrementAndGet();
                return super.generateImage(scale);
            }
        };
        entity.setXVelocity(1);

        renderingEngine.setDisplay(display);
        entityEngine.add(entity);

        entityEngine.update(1);
        renderingEngine.update(1);

        Assert.assertEquals(1, generationCounter.get());

        entity.move(1);
        renderingEngine.update(1);

        Assert.assertEquals(2, generationCounter.get());
    }
}
