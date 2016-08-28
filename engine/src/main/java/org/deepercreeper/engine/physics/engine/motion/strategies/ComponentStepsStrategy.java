package org.deepercreeper.engine.physics.engine.motion.strategies;

import org.deepercreeper.common.util.Util;
import org.deepercreeper.engine.physics.Entity;
import org.deepercreeper.engine.physics.engine.motion.components.MotionComponent;
import org.deepercreeper.engine.physics.engine.motion.components.MotionComponentFactory;
import org.deepercreeper.engine.physics.engine.motion.components.motion.ComponentMotionFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ComponentStepsStrategy<T extends MotionComponent<T>> extends AbstractMotionStrategy
{
    private final ExecutorService executorService = Executors.newFixedThreadPool(24);

    private final Set<T> motionComponents = new HashSet<>();

    private final AtomicInteger runningComponents = new AtomicInteger();

    private final MotionComponentFactory<T> motionComponentFactory;

    private final ComponentMotionFactory componentMotionFactory;

    public ComponentStepsStrategy(MotionComponentFactory<T> motionComponentFactory, ComponentMotionFactory componentMotionFactory)
    {
        this.motionComponentFactory = motionComponentFactory;
        this.componentMotionFactory = componentMotionFactory;
    }

    @Override
    public void update()
    {
        computeMotionComponents();
        moveMotionComponents();
    }

    private void computeMotionComponents()
    {
        long timeStamp = System.currentTimeMillis();
        motionComponents.clear();
        getEntities().forEach(this::findMotionComponent);
        System.out.println("Computing motion components took " + (System.currentTimeMillis() - timeStamp) + " ms");
    }

    private void findMotionComponent(Entity entity)
    {
        T component = motionComponentFactory.create(componentMotionFactory.create(), getDelta());
        component.add(entity);
        boolean changed;
        do
        {
            changed = addComponents(component);
        } while (changed);
        motionComponents.add(component);
    }

    private boolean addComponents(T motionComponent)
    {
        boolean changed = false;
        Iterator<T> iterator = motionComponents.iterator();
        while (iterator.hasNext())
        {
            T component = iterator.next();
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
        runningComponents.set(motionComponents.size());
        motionComponents.forEach(component -> component.init(runningComponents::decrementAndGet));
        motionComponents.forEach(executorService::execute);
        while (runningComponents.get() > 0)
        {
            Util.sleep(1);
        }
    }
}
