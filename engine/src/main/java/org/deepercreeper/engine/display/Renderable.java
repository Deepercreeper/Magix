package org.deepercreeper.engine.display;

import org.deepercreeper.engine.util.Image;

public interface Renderable
{
    Image generateImage(double scale);
}
