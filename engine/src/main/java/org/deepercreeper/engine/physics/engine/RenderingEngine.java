package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.display.AbstractRenderer;
import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.display.Renderer;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.util.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RenderingEngine implements Updatable
{
    private final Map<Entity, Image> images = new HashMap<>();

    private final Map<Entity, Rectangle> clearBoxes = new HashMap<>();

    private final Vector position = new Vector();

    private final EntityEngine entityEngine;

    private final UpdateEngine updateEngine;

    private Renderer renderer = new AbstractRenderer();

    private boolean frameRate = false;

    private double scale = 48;

    private int counter = 0;

    public RenderingEngine(EntityEngine entityEngine, UpdateEngine updateEngine)
    {
        this.entityEngine = entityEngine;
        this.updateEngine = updateEngine;
    }

    public void setPosition(Vector position)
    {
        if (this.position.equals(position))
        {
            return;
        }
        this.position.set(position);
        renderer.setPosition(position.asPoint());
        renderer.clear();
    }

    public Vector getPosition()
    {
        return new Vector(position);
    }

    public void setDisplay(Display display)
    {
        renderer.setDisplay(display);
    }

    public void setScale(double scale)
    {
        this.scale = scale;
    }

    public void setFrameRate(boolean frameRate)
    {
        this.frameRate = frameRate;
    }

    public void toggleFrameRate()
    {
        frameRate = !frameRate;
    }

    public boolean isVisible(Box box)
    {
        return renderer.isVisible(box.asScaledRectangle(scale));
    }

    public double getScale()
    {
        return scale;
    }

    @Override
    public void update(double delta)
    {
        if (renderer != null)
        {
            render();
        }
    }

    private void render()
    {
        computeImages();
        clearEntities();
        renderEntities();
        renderFrameRate();
    }

    private void computeImages()
    {
        images.clear();
        for (Entity entity : entityEngine.getEntities())
        {
            images.put(entity, entity.generateImage(scale));
        }
    }

    private void clearEntities()
    {
        Iterator<Map.Entry<Entity, Rectangle>> iterator = clearBoxes.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Entity, Rectangle> entry = iterator.next();
            Entity entity = entry.getKey();
            Rectangle renderBox = images.get(entity);
            if (renderBox != null)
            {
                for (Rectangle subtraction : entry.getValue().getSubtraction(renderBox))
                {
                    renderer.clear(subtraction);
                }
            }
            else
            {
                renderer.clear(entry.getValue());
            }
            iterator.remove();
        }
        clearBoxes.putAll(images);
    }

    private void renderEntities()
    {
        for (Image image : images.values())
        {
            renderer.render(image);
        }
    }

    private void renderFrameRate()
    {
        if (!frameRate)
        {
            return;
        }
        int difference = (int) updateEngine.getDifference();
        if (difference > 0)
        {
            Image image = new Image.ImageBuilder().setX(counter).setY(100 - difference).setWidth(1).setHeight(difference)
                                                  .setData(Display.createRectangle(1, difference, 0xffffffff)).build();
            image.moveBy(position.asPoint());
            renderer.clear(new Rectangle.RectangleBuilder().set(image).setY(position.asPoint().getY()).setHeight(100).build());
            renderer.render(image);
        }
        counter = (counter + 1) % 100;
    }
}
