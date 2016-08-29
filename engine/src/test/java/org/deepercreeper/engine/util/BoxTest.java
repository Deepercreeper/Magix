package org.deepercreeper.engine.util;

import org.junit.Assert;
import org.junit.Test;

public class BoxTest
{
    @Test
    public void testCenterUpdate()
    {
        Box box = new Box.BoxBuilder().setWidth(2).setHeight(2).build();

        Assert.assertEquals(1, box.getCenterX(), 0);

        box.setX(1);

        Assert.assertEquals(2, box.getCenterX(), 0);
    }

    @Test
    public void testSetters()
    {
        Box box = new Box.BoxBuilder().setWidth(2).setHeight(2).build();

        box.setX(1);

        Assert.assertEquals(1, box.getX(), 0);

        box.setMaxX(2);

        Assert.assertEquals(0, box.getX(), 0);

        box.setCenterX(2);

        Assert.assertEquals(1, box.getX(), 0);
    }

    @Test
    public void testMove()
    {
        Box box = new Box.BoxBuilder().setWidth(2).setHeight(2).build();

        box.moveBy(1, 1);

        Assert.assertEquals(1, box.getX(), 0);
        Assert.assertEquals(1, box.getY(), 0);

        box.moveBy(new Vector(-1, -1));

        Assert.assertEquals(0, box.getX(), 0);
        Assert.assertEquals(0, box.getY(), 0);
    }

    @Test
    public void testTouching()
    {
        Box firstBox = new Box.BoxBuilder().setWidth(2).setHeight(2).build();
        Box secondBox = new Box.BoxBuilder().setX(2).setWidth(2).setHeight(2).build();

        Assert.assertTrue(firstBox.isTouching(secondBox));
        Assert.assertTrue(secondBox.isTouching(firstBox));

        secondBox.setX(2.0001);

        Assert.assertFalse(firstBox.isTouching(secondBox));
        Assert.assertFalse(secondBox.isTouching(firstBox));

        secondBox.setX(-2);

        Assert.assertTrue(firstBox.isTouching(secondBox));
        Assert.assertTrue(secondBox.isTouching(firstBox));

        secondBox.setY(-2);

        Assert.assertTrue(firstBox.isTouching(secondBox));
        Assert.assertTrue(secondBox.isTouching(firstBox));

        secondBox.setX(-2.0001);

        Assert.assertFalse(firstBox.isTouching(secondBox));
        Assert.assertFalse(secondBox.isTouching(firstBox));
    }

    @Test
    public void testContainment()
    {
        Box firstBox = new Box.BoxBuilder().setWidth(2).setHeight(2).build();
        Box secondBox = new Box.BoxBuilder().setX(2).setY(2).setWidth(2).setHeight(2).build();
        Box containment = firstBox.getContainment(secondBox);

        Assert.assertEquals(firstBox.getX(), containment.getX(), 0);
        Assert.assertEquals(secondBox.getMaxX(), containment.getMaxX(), 0);
        Assert.assertEquals(firstBox.getY(), containment.getY(), 0);
        Assert.assertEquals(secondBox.getMaxY(), containment.getMaxY(), 0);
    }

    @Test
    public void testScaledRectangle()
    {
        Box box = new Box.BoxBuilder().setX(1).setY(1).setWidth(2).setHeight(2).build();
        Rectangle scaledRectangle = box.asScaledRectangle(1);

        Assert.assertEquals(1, scaledRectangle.getX());
        Assert.assertEquals(1, scaledRectangle.getY());
        Assert.assertEquals(2, scaledRectangle.getWidth());
        Assert.assertEquals(2, scaledRectangle.getHeight());

        scaledRectangle = box.asScaledRectangle(.5);

        Assert.assertEquals(1, scaledRectangle.getX());
        Assert.assertEquals(1, scaledRectangle.getY());
        Assert.assertEquals(1, scaledRectangle.getWidth());
        Assert.assertEquals(1, scaledRectangle.getHeight());
    }

    @Test
    public void testExpandedBox()
    {
        Box box = new Box.BoxBuilder().setX(1).setY(1).setWidth(2).setHeight(2).build();
        Box expandedBox = box.getExpandedBox(new Vector(1, 1));

        Assert.assertEquals(0, expandedBox.getX(), 0);
        Assert.assertEquals(0, expandedBox.getY(), 0);
        Assert.assertEquals(4, expandedBox.getMaxX(), 0);
        Assert.assertEquals(4, expandedBox.getMaxY(), 0);
    }

    @Test
    public void testXDistance()
    {
        Box firstBox = new Box.BoxBuilder().setWidth(1).setHeight(1).build();
        Box secondBox = new Box.BoxBuilder().setX(2).setY(2).setWidth(1).setHeight(1).build();
        
        Assert.assertEquals(1, firstBox.getXDistanceTo(secondBox), 0);
        Assert.assertEquals(1, secondBox.getXDistanceTo(firstBox), 0);

        secondBox.setPosition(.5, .5);

        Assert.assertEquals(0, firstBox.getXDistanceTo(secondBox), 0);
        Assert.assertEquals(0, secondBox.getXDistanceTo(firstBox), 0);
    }

    @Test
    public void testYDistance()
    {
        Box firstBox = new Box.BoxBuilder().setWidth(1).setHeight(1).build();
        Box secondBox = new Box.BoxBuilder().setX(2).setY(2).setWidth(1).setHeight(1).build();

        Assert.assertEquals(1, firstBox.getYDistanceTo(secondBox), 0);
        Assert.assertEquals(1, secondBox.getYDistanceTo(firstBox), 0);

        secondBox.setPosition(.5, .5);

        Assert.assertEquals(0, firstBox.getYDistanceTo(secondBox), 0);
        Assert.assertEquals(0, secondBox.getYDistanceTo(firstBox), 0);
    }
}
