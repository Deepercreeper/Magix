package org.deepercreeper.engine.physics;

public class TestEntity extends Entity
{
    public TestEntity(double x, double y, double width, double height)
    {
        super(new EntityBuilder().setX(x).setY(y).setWidth(width).setHeight(height));
    }
}
