package org.deepercreeper.engine.util;

import org.deepercreeper.engine.display.Display;
import org.junit.Assert;
import org.junit.Test;

public class ImageTest
{
    @Test(expected = IllegalStateException.class)
    public void testNullValidation()
    {
        Image image = new Image.ImageBuilder().build();

        image.validate();
    }

    @Test(expected = IllegalStateException.class)
    public void testWrongLengthValidation()
    {
        Image image = new Image.ImageBuilder().setWidth(10).setHeight(1).setData(new int[15]).build();

        image.validate();
    }

    @Test
    public void testValidation()
    {
        Image image = new Image.ImageBuilder().setWidth(10).setHeight(1).setData(new int[10]).build();

        image.validate();
    }

    @Test
    public void testDrawOver()
    {
        Image firstImage = new Image.ImageBuilder().setWidth(10).setHeight(10).setData(new int[100]).build();
        Image secondImage = new Image.ImageBuilder().setWidth(2).setHeight(2).setData(Display.createFilledRectangle(2, 2, 0xffffffff)).build();

        secondImage.drawOver(firstImage);

        Assert.assertTrue(isDataColorAt(firstImage.getData(), 10, 0, 0, 2, 2, 0xffffffff));
        Assert.assertTrue(isDataColorAt(firstImage.getData(), 10, 2, 0, 2, 2, 0));
        Assert.assertTrue(isDataColorAt(firstImage.getData(), 10, 0, 2, 2, 2, 0));

        secondImage.setX(9);
        secondImage.setY(-1);
        secondImage.drawOver(firstImage);

        Assert.assertTrue(isDataColorAt(firstImage.getData(), 10, 9, 0, 1, 1, 0xffffffff));
        Assert.assertTrue(isDataColorAt(firstImage.getData(), 10, 8, 0, 1, 1, 0));
        Assert.assertTrue(isDataColorAt(firstImage.getData(), 10, 9, 1, 1, 1, 0));
    }

    private boolean isDataColorAt(int[] data, int dataWidth, int x, int y, int width, int height, int color)
    {
        for (int i = y; i < y + height; i++)
        {
            for (int j = x; j < x + width; j++)
            {
                if (data[i * dataWidth + j] != color)
                {
                    return false;
                }
            }
        }
        return true;
    }
}
