package org.deepercreeper.windows;

import org.deepercreeper.engine.input.Input;
import org.deepercreeper.engine.input.Key;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FrameInput implements Input
{
    private final boolean[] activeKeys = new boolean[Key.values().length];

    private final boolean[] hitKeys = new boolean[Key.values().length];

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
            if (!activeKeys[index])
            {
                hitKeys[index] = true;
            }
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
            case KeyEvent.VK_ESCAPE:
                return Key.PAUSE.ordinal();
            case KeyEvent.VK_LEFT:
                return Key.CAMERA_LEFT.ordinal();
            case KeyEvent.VK_RIGHT:
                return Key.CAMERA_RIGHT.ordinal();
            case KeyEvent.VK_UP:
                return Key.CAMERA_UP.ordinal();
            case KeyEvent.VK_DOWN:
                return Key.CAMERA_DOWN.ordinal();
            case KeyEvent.VK_F:
                return Key.FRAME_RATE.ordinal();
        }
        return -1;
    }

    @Override
    public boolean isActive(Key key)
    {
        return activeKeys[key.ordinal()];
    }

    @Override
    public boolean checkHit(Key key)
    {
        if (hitKeys[key.ordinal()])
        {
            hitKeys[key.ordinal()] = false;
            return true;
        }
        return false;
    }
}
