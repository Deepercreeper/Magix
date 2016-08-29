package org.deepercreeper.engine.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class RectangleTest
{
    private static final int[][] MINUEND_BOUNDS = new int[][]{
            {-1, -1, 7, 7},
            {0, 0, 6, 6},
            {-1, -1, 6, 6},
            {0, 0, 5, 5},
            {1, 1, 4, 4},
            {0, 1, 4, 4},
            {1, 0, 4, 4},
            {0, 0, 4, 4},
            {0, 1, 3, 3},
            {1, 0, 3, 3},
            {1, 2, 3, 3},
            {2, 1, 3, 3},
            {1, 1, 3, 3}
    };

    @Test
    public void testSubtraction()
    {
        Rectangle subtrahend = new Rectangle.RectangleBuilder().setWidth(5).setHeight(5).build();
        Rectangle minuend = new Rectangle.RectangleBuilder().build();
        for (int[] bounds : MINUEND_BOUNDS)
        {
            minuend.setPosition(bounds[0], bounds[1]);
            minuend.setSize(bounds[2], bounds[3]);
            Assert.assertTrue(isSubtraction(subtrahend, minuend, subtrahend.getSubtraction(minuend)));
        }
    }

    private boolean isSubtraction(Rectangle subtrahend, Rectangle minuend, Set<Rectangle> difference)
    {
        Rectangle sum = minuend;
        for (Rectangle rectangle : difference)
        {
            if (sum == null)
            {
                sum = rectangle;
            }
            else
            {
                sum = sum.getContainment(rectangle);
            }
            if (!subtrahend.contains(rectangle) || minuend.isTouching(rectangle))
            {
                System.err.println(subtrahend + " - " + minuend + " =|= " + difference);
                if (!subtrahend.contains(rectangle))
                {
                    System.err.println("Subtrahend " + subtrahend + " does not contain " + rectangle);
                }
                else
                {
                    System.err.println("Minuend " + minuend + " is touching " + rectangle);
                }
                return false;
            }
        }
        if (!sum.contains(subtrahend))
        {
            System.err.println("Sum " + sum + " =|= " + subtrahend);
            return false;
        }
        return true;
    }

    @Test
    public void testCenterUpdate()
    {
        Rectangle box = new Rectangle.RectangleBuilder().setWidth(2).setHeight(2).build();

        Assert.assertEquals(1, box.getCenterX(), 0);

        box.setX(1);

        Assert.assertEquals(2, box.getCenterX(), 0);
    }

    @Test
    public void testSetters()
    {
        Rectangle rectangle = new Rectangle.RectangleBuilder().setWidth(2).setHeight(2).build();

        rectangle.setX(1);

        Assert.assertEquals(1, rectangle.getX(), 0);

        rectangle.setMaxX(2);

        Assert.assertEquals(0, rectangle.getX(), 0);

        rectangle.setCenterX(2);

        Assert.assertEquals(1, rectangle.getX(), 0);
    }

    @Test
    public void testMove()
    {
        Rectangle rectangle = new Rectangle.RectangleBuilder().setWidth(2).setHeight(2).build();

        rectangle.moveBy(1, 1);

        Assert.assertEquals(1, rectangle.getX(), 0);
        Assert.assertEquals(1, rectangle.getY(), 0);

        rectangle.moveBy(new Point(-1, -1));

        Assert.assertEquals(0, rectangle.getX(), 0);
        Assert.assertEquals(0, rectangle.getY(), 0);
    }

    @Test
    public void testTouching()
    {
        Rectangle firstRectangle = new Rectangle.RectangleBuilder().setWidth(2).setHeight(2).build();
        Rectangle secondRectangle = new Rectangle.RectangleBuilder().setX(2).setWidth(2).setHeight(2).build();

        Assert.assertFalse(firstRectangle.isTouching(secondRectangle));
        Assert.assertFalse(secondRectangle.isTouching(firstRectangle));

        secondRectangle.setX(1);

        Assert.assertTrue(firstRectangle.isTouching(secondRectangle));
        Assert.assertTrue(secondRectangle.isTouching(firstRectangle));

        secondRectangle.setX(-2);

        Assert.assertFalse(firstRectangle.isTouching(secondRectangle));
        Assert.assertFalse(secondRectangle.isTouching(firstRectangle));

        secondRectangle.setX(-1);

        Assert.assertTrue(firstRectangle.isTouching(secondRectangle));
        Assert.assertTrue(secondRectangle.isTouching(firstRectangle));

        secondRectangle.setY(-1);

        Assert.assertTrue(firstRectangle.isTouching(secondRectangle));
        Assert.assertTrue(secondRectangle.isTouching(firstRectangle));

        secondRectangle.setX(-2);

        Assert.assertFalse(firstRectangle.isTouching(secondRectangle));
        Assert.assertFalse(secondRectangle.isTouching(firstRectangle));
    }

    @Test
    public void testContainment()
    {
        Rectangle firstRectangle = new Rectangle.RectangleBuilder().setWidth(2).setHeight(2).build();
        Rectangle secondRectangle = new Rectangle.RectangleBuilder().setX(2).setY(2).setWidth(2).setHeight(2).build();
        Rectangle containment = firstRectangle.getContainment(secondRectangle);

        Assert.assertEquals(firstRectangle.getX(), containment.getX(), 0);
        Assert.assertEquals(secondRectangle.getMaxX(), containment.getMaxX(), 0);
        Assert.assertEquals(firstRectangle.getY(), containment.getY(), 0);
        Assert.assertEquals(secondRectangle.getMaxY(), containment.getMaxY(), 0);
    }
}
