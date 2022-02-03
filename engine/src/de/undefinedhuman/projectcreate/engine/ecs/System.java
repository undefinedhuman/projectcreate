package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;

public abstract class System {

    private Family family;
    protected ImmutableArray<Entity> entities;
    private boolean processing;
    private int priority;

    public System() {
        this(0, true);
    }

    public System(int priority) {
        this(priority, true);
    }

    public System(int priority, boolean processing) {
        family = Metadata.parseSystemMetadata(getClass());
        this.priority = priority;
        this.processing = processing;
    }

    public void init(EntityManager entityManager) {
        this.entities = entityManager.getEntitiesFor(family);
    }

    public void delete(EntityManager entityManager) {
        this.entities = null;
    }

    public final void update(float delta) {
        if(!checkProcessing())
            return;
        start();
        process(delta);
        end();
    }

    public int getPriority() {
        return priority;
    }

    public boolean checkProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public Family getFamily() {
        return family;
    }

    protected void start() {}

    protected void end() {}

    protected abstract void process(float delta);

}
