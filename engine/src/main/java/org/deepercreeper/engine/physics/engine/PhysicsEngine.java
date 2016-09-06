package org.deepercreeper.engine.physics.engine;

import org.deepercreeper.engine.physics.Force;
import org.deepercreeper.engine.util.ModificationSet;
import org.deepercreeper.engine.util.Updatable;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class PhysicsEngine implements Updatable
{
    private final ModificationSet<Force> forces = new ModificationSet<>();

    private final Queue<Force> addedForces = new LinkedBlockingQueue<>();

    private final Queue<Force> removedForces = new LinkedBlockingQueue<>();

    public PhysicsEngine()
    {
        forces.setModifiable(false);
    }

    public void add(Force force)
    {
        addedForces.add(force);
    }

    @Override
    public void update(double delta)
    {
        forces.setModifiable(true);
        removeForces();
        addForces();
        forces.setModifiable(false);
    }

    private void removeForces()
    {
        while (!removedForces.isEmpty())
        {
            Force force = removedForces.poll();
            forces.remove(force);
        }
    }

    private void addForces()
    {
        while (!addedForces.isEmpty())
        {
            Force force = addedForces.poll();
            forces.add(force);
        }
    }

    public Set<Force> getForces()
    {
        return forces;
    }
}
