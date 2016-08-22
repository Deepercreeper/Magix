package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.engine.motion.MotionComponent;
import org.deepercreeper.engine.util.Updatable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MotionEngine extends AbstractEngine implements Updatable
{
    private final List<MotionComponent> motionComponents = new ArrayList<>();

    private double delta;

    private boolean pause = false;

    public MotionEngine(Engine engine)
    {
        super(engine);
    }

    public void setPause(boolean pause)
    {
        this.pause = pause;
    }

    public void togglePause()
    {
        pause = !pause;
    }

    @Override
    public void update(double delta)
    {
        if (!pause)
        {
            this.delta = delta;
            updateNonSolidMotion();
            updateSolidMotion();
        }
    }

    private void updateNonSolidMotion()
    {
        //TODO Compute appropriate
        getEngine().getEntityEngine().getNonSolidEntities().forEach(entity -> entity.updateAll(delta));
    }

    private void updateSolidMotion()
    {
        computeMotionComponents();
        moveMotionComponents();
    }

    private void computeMotionComponents()
    {
        motionComponents.clear();
        getEngine().getEntityEngine().getSolidEntities().forEach(this::findMotionComponent);
    }

    private void findMotionComponent(Entity entity)
    {
        MotionComponent component = new MotionComponent(delta);
        component.add(entity);
        boolean changed;
        do
        {
            changed = addComponents(component);
        } while (changed);
        motionComponents.add(component);
    }

    private boolean addComponents(MotionComponent motionComponent)
    {
        boolean changed = false;
        Iterator<MotionComponent> iterator = motionComponents.iterator();
        while (iterator.hasNext())
        {
            MotionComponent component = iterator.next();
            if (motionComponent.isTouching(component))
            {
                motionComponent.consume(component);
                iterator.remove();
                changed = true;
            }
        }
        return changed;
    }

    private void moveMotionComponents()
    {
        //TODO This may be executed in parallel
        motionComponents.forEach(MotionComponent::move);
    }
}
