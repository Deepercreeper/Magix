package org.deepercreeper.magix.windows;

import org.deepercreeper.engine.display.Display;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@Component
public class FrameDisplay implements Display, InitializingBean {
    private final Frame frame;

    @Autowired
    public FrameDisplay(Frame frame) {
        this.frame = frame;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                clear(0, 0, getWidth(), getHeight());
            }
        });
    }

    @Override
    public void render(int x, int y, int width, int height, int[] image) {
        try {
            frame.getImage().setRGB(x, y, width, height, image, 0, width);
        }
        catch (ArrayIndexOutOfBoundsException ignored) {
        }
        frame.repaint(x, y, width, height);
    }

    @Override
    public void clear(int x, int y, int width, int height) {
        try {
            frame.getImage().setRGB(x, y, width, height, Display.createFilledRectangle(width, height, 0xff000000), 0, width);
        }
        catch (ArrayIndexOutOfBoundsException ignored) {
        }
        frame.repaint(x, y, width, height);
    }

    @Override
    public int getWidth() {
        return frame.getWidth();
    }

    @Override
    public int getHeight() {
        return frame.getHeight();
    }
}
