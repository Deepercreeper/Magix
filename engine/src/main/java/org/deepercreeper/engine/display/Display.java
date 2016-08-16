package org.deepercreeper.engine.display;

import org.deepercreeper.engine.util.Rectangle;

import java.util.Arrays;

public interface Display
{
    void render(int x, int y, int width, int height, int[] image);

    void render(Rectangle rectangle, int[] image);

    void clear(int x, int y, int width, int height);

    void clear(Rectangle rectangle);

    void clear();

    int getWidth();

    int getHeight();

    Rectangle getRectangle();

    static int[] createRectangle(int width, int height, int color)
    {
        int[] image = new int[width * height];
        for (int y = 0; y < height; y++)
        {
            image[y * width] = image[y * width + width - 1] = color;
        }
        for (int x = 0; x < width; x++)
        {
            image[x] = image[height * width - width + x] = color;
        }
        return image;
    }

    static int[] createFilledRectangle(int width, int height, int color)
    {
        int[] image = new int[width * height];
        Arrays.fill(image, color);
        return image;
    }

    static int[] createCircle(int diameter, int color)
    {
        int radius = diameter / 2;
        int[] image = new int[diameter * diameter];
        for (int i = 0; i < image.length; i++)
        {
            int x = i % diameter;
            int y = i / diameter;
            if ((x - radius) * (x - radius) + (y - radius) * (y - radius) < radius)
            {
                image[i] = color;
            }
        }
        return image;
    }
}
