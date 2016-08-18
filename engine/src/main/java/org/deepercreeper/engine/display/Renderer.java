package org.deepercreeper.engine.display;

import org.deepercreeper.engine.util.Image;
import org.deepercreeper.engine.util.Point;
import org.deepercreeper.engine.util.Rectangle;

public interface Renderer
{
    void render(Image image);

    void clear(Rectangle rectangle);

    void clear();

    void setPosition(Point position);

    Point getPosition();

    void setDisplay(Display display);

    boolean isVisible(Rectangle rectangle);
}
