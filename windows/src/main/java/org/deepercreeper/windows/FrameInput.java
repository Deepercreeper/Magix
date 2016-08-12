package org.deepercreeper.windows;

import org.deepercreeper.engine.input.Input;
import org.deepercreeper.engine.input.Key;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FrameInput implements Input
{
    private final boolean[] activeKeys = new boolean[Key.values().length];

    public FrameInput(Frame frame)
    {
        frame.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                FrameInput.this.keyPressed(e.getExtendedKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                FrameInput.this.keyReleased(e.getExtendedKeyCode());
            }
        });
    }

    private void keyPressed(int keyCode)
    {
        int index = getKey(keyCode);
        if (index >= 0)
        {
            activeKeys[index] = true;
        }
    }

    private void keyReleased(int keyCode)
    {
        int index = getKey(keyCode);
        if (index >= 0)
        {
            activeKeys[index] = false;
        }
    }

    private int getKey(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.VK_A:
                return Key.LEFT.ordinal();
            case KeyEvent.VK_D:
                return Key.RIGHT.ordinal();
            case KeyEvent.VK_SPACE:
                return Key.JUMP.ordinal();
            case KeyEvent.VK_CONTROL:
                return Key.CROUCH.ordinal();
        }
        return -1;
    }

    @Override
    public boolean isActive(Key key)
    {
        return activeKeys[key.ordinal()];
    }
}
