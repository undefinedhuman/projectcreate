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

    public void init(FamilyManager familyManager) {
        this.entities = familyManager.getEntitiesFor(family);
    }

    public final void update(float delta) {
        if(!checkProcessing())
            return;
        start();
        process(delta);
        end();
    }

    protected void delete() {}

    public int getPriority() {
        return priority;
    }

    public boolean checkProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    Family getFamily() {
        return family;
    }

    protected void start() {}

    protected void end() {}

    protected abstract void process(float delta);

}
