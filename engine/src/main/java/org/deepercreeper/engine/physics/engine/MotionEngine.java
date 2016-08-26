package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.physics.engine.motion.components.motion.StepMotion;
import org.deepercreeper.engine.physics.engine.motion.colliders.MinimumCollider;
import org.deepercreeper.engine.physics.engine.motion.components.DistanceMotionComponent;
import org.deepercreeper.engine.physics.engine.motion.splitters.AxialSplitter;
import org.deepercreeper.engine.physics.engine.motion.strategies.ComponentStepsStrategy;
import org.deepercreeper.engine.physics.engine.motion.strategies.MotionStrategy;
import org.deepercreeper.engine.util.Updatable;

public class MotionEngine extends AbstractEngine implements Updatable
{
    private MotionStrategy strategy;

    private double delta;

    private boolean pause = false;

    public MotionEngine(Engine engine)
    {
        super(engine);
        setStrategy(new ComponentStepsStrategy<>(DistanceMotionComponent::new, () -> new StepMotion(new AxialSplitter(10E-5), new MinimumCollider())));
    }

    public void setPause(boolean pause)
    {
        this.pause = pause;
    }

    public void togglePause()
    {
        pause = !pause;
    }

    public void setStrategy(MotionStrategy strategy)
    {
        this.strategy = strategy;
        strategy.init(getEngine().getEntityEngine().getSolidEntities());
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
        getEngine().getEntityEngine().getNonSolidEntities().forEach(entity -> entity.updateAll(delta));
    }

    private void updateSolidMotion()
    {
        strategy.update(delta);
    }
}
