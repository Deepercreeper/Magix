package org.deepercreeper.engine.display;

import org.deepercreeper.engine.util.Image;
import org.deepercreeper.engine.util.Point;
import org.deepercreeper.engine.util.Rectangle;
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

    @Test
    public void testVisibility()
    {
        AbstractRenderer renderer = new AbstractRenderer();
        TestDisplay display = new TestDisplay(10, 10);
        renderer.setDisplay(display);
        renderer.setPosition(new Point(-5, -5));

        Assert.assertTrue(renderer.isVisible(new Rectangle.RectangleBuilder().setX(-9).setY(-9).setWidth(5).setHeight(5).build()));
        Assert.assertFalse(renderer.isVisible(new Rectangle.RectangleBuilder().setX(-10).setY(-10).setWidth(5).setHeight(5).build()));
        Assert.assertTrue(renderer.isVisible(new Rectangle.RectangleBuilder().setX(4).setY(4).setWidth(5).setHeight(5).build()));
        Assert.assertFalse(renderer.isVisible(new Rectangle.RectangleBuilder().setX(5).setY(5).setWidth(5).setHeight(5).build()));
    }
}
