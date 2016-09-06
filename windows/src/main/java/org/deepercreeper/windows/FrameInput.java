package org.deepercreeper.windows;

import org.deepercreeper.engine.input.AbstractInput;
import org.deepercreeper.engine.input.Key;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FrameInput extends AbstractInput
{
    public FrameInput(Frame frame)
    {
        frame.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                FrameInput.this.keyPressed(getKey(e.getExtendedKeyCode()));
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                FrameInput.this.keyReleased(getKey(e.getExtendedKeyCode()));
            }
        });
    }

    private Key getKey(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.VK_A:
                return Key.LEFT;
            case KeyEvent.VK_D:
                return Key.RIGHT;
            case KeyEvent.VK_SPACE:
                return Key.JUMP;
            case KeyEvent.VK_CONTROL:
                return Key.CROUCH;
            case KeyEvent.VK_ESCAPE:
                return Key.PAUSE;
            case KeyEvent.VK_LEFT:
                return Key.CAMERA_LEFT;
            case KeyEvent.VK_RIGHT:
                return Key.CAMERA_RIGHT;
            case KeyEvent.VK_UP:
                return Key.CAMERA_UP;
            case KeyEvent.VK_DOWN:
                return Key.CAMERA_DOWN;
            case KeyEvent.VK_F:
                return Key.FRAME_RATE;
        }
        return null;
    }
}
