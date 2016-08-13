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
        Rectangle subtrahend = new Rectangle(0, 0, 5, 5);
        for (int[] bounds : MINUEND_BOUNDS)
        {
            Rectangle minuend = new Rectangle(bounds[0], bounds[1], bounds[2], bounds[3]);
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
}
