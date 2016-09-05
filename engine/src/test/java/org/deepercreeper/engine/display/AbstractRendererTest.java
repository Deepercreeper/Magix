package org.deepercreeper.engine.display;

import org.deepercreeper.engine.util.Image;
import org.deepercreeper.engine.util.Point;
import org.junit.Assert;
import org.junit.Test;

public class AbstractRendererTest
{
    @Test
    public void testRender()
    {
        AbstractRenderer renderer = new AbstractRenderer();
        TestDisplay display = new TestDisplay(10, 10);
        renderer.setDisplay(display);

        renderer.render(new Image.ImageBuilder().setX(2).setWidth(6).setY(2).setHeight(6).setData(new int[36]).build());

        Assert.assertEquals(1, display.getRenderings());
    }

    @Test
    public void testInvisibleRender()
    {
        AbstractRenderer renderer = new AbstractRenderer();
        TestDisplay display = new TestDisplay(10, 10);
        renderer.setDisplay(display);

        renderer.render(new Image.ImageBuilder().setX(11).setWidth(4).setHeight(2).setData(new int[8]).build());

        Assert.assertEquals(0, display.getRenderings());
    }

    @Test
    public void testVisiblePositionRender()
    {
        AbstractRenderer renderer = new AbstractRenderer();
        TestDisplay display = new TestDisplay(10, 10);
        renderer.setDisplay(display);
        renderer.setPosition(new Point(-10, 0));

        renderer.render(new Image.ImageBuilder().setX(-8).setWidth(6).setY(2).setHeight(6).setData(new int[36]).build());

        Assert.assertEquals(1, display.getRenderings());
    }

    @Test
    public void testInvisiblePositionRender()
    {
        AbstractRenderer renderer = new AbstractRenderer();
        TestDisplay display = new TestDisplay(10, 10);
        renderer.setDisplay(display);
        renderer.setPosition(new Point(-10, 0));

        renderer.render(new Image.ImageBuilder().setX(2).setWidth(6).setY(2).setHeight(6).setData(new int[36]).build());

        Assert.assertEquals(0, display.getRenderings());
    }

    @Test
    public void testPartialRender()
    {
        AbstractRenderer renderer = new AbstractRenderer();
        TestDisplay display = new TestDisplay(10, 10);
        renderer.setDisplay(display);

        renderer.render(new Image.ImageBuilder().setX(5).setWidth(10).setY(5).setHeight(10).setData(new int[100]).build());

        Assert.assertEquals(1, display.getRenderings());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidImage()
    {
        AbstractRenderer renderer = new AbstractRenderer();
        TestDisplay display = new TestDisplay(10, 10);
        renderer.setDisplay(display);

        renderer.render(new Image.ImageBuilder().setX(-8).setWidth(6).setY(2).setHeight(6).setData(new int[30]).build());
    }
}
