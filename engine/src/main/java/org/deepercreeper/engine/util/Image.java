package org.deepercreeper.engine.util;

public class Image extends Rectangle
{
    private int[] data;

    public Image(GenericImageBuilder<?> builder)
    {
        super(builder);
        data = builder.data;
    }

    public final void setData(int[] data)
    {
        this.data = data;
    }

    public final int[] getData()
    {
        return data;
    }

    public final void validate()
    {
        if (data == null || data.length != getWidth() * getHeight())
        {
            throw new IllegalStateException("Data length of image has to be width times height");
        }
    }

    public final void drawOver(Image image)
    {
        validate();
        image.validate();
        Rectangle cut = getCut(image);
        if (cut.isEmpty())
        {
            return;
        }
        int[] imageData = image.getData();
        for (int y = 0; y < cut.getHeight(); y++)
        {
            int dataIndex = (y + cut.getY() - getY()) * getWidth() + cut.getX() - getX();
            int imageDataIndex = (y + cut.getY() - image.getY()) * image.getWidth() + cut.getX() - image.getX();
            System.arraycopy(data, dataIndex, imageData, imageDataIndex, cut.getWidth());
        }
    }

    public static abstract class GenericImageBuilder<T extends GenericImageBuilder<T>> extends GenericRectangleBuilder<T>
    {
        protected int[] data = null;

        public T setData(int[] data)
        {
            this.data = data;
            return getThis();
        }

        public T set(Image image)
        {
            set((Rectangle) image);
            data = image.getData();
            return getThis();
        }

        public Image build()
        {
            return new Image(this);
        }
    }

    public static final class ImageBuilder extends GenericImageBuilder<ImageBuilder>
    {
        @Override
        protected ImageBuilder getThis()
        {
            return this;
        }
    }
}
