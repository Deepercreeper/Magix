package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.input.Input;
import org.deepercreeper.engine.input.Key;
import org.deepercreeper.engine.util.Updatable;
import org.deepercreeper.engine.util.Vector;

public class InputEngine extends AbstractEngine implements Updatable
{
    private Input input;

    public InputEngine(Engine engine)
    {
        super(engine);
    }

    public void setInput(Input input)
    {
        this.input = input;
    }

    public Input getInput()
    {
        return input;
    }

    @Override
    public void update(double delta)
    {
        if (input != null)
        {
            updateInput();
        }
    }

    private void updateInput()
    {
        if (input.checkHit(Key.PAUSE))
        {
            getEngine().getMotionEngine().togglePause();
        }
        Vector position = getEngine().getRenderingEngine().getPosition();
        if (input.isActive(Key.CAMERA_RIGHT))
        {
            position.add(1, 0);
        }
        if (input.isActive(Key.CAMERA_LEFT))
        {
            position.add(-1, 0);
        }
        if (input.isActive(Key.CAMERA_UP))
        {
            position.add(0, -1);
        }
        if (input.isActive(Key.CAMERA_DOWN))
        {
            position.add(0, 1);
        }
        getEngine().getRenderingEngine().setPosition(position);
    }
}
