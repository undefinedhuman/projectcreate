package de.undefinedhuman.projectcreate.engine.ecs.systems;

import com.badlogic.gdx.utils.Array;
import de.undefinedhuman.projectcreate.engine.ecs.*;
import de.undefinedhuman.projectcreate.engine.ecs.System;
import de.undefinedhuman.projectcreate.engine.event.Observer;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;

import java.util.Comparator;

public abstract class SortedIteratingSystem extends System {

    private final Array<Entity> sortedEntities;
    private boolean sorted = true;
    private final Comparator<Entity> comparator;

    private Observer<FamilyEvent> familyUpdates;

    public SortedIteratingSystem (Comparator<Entity> comparator) {
        this(comparator, 0);
    }

    public SortedIteratingSystem (Comparator<Entity> comparator, int priority) {
        super(priority);
        this.comparator = comparator;
        sortedEntities = new Array<>(false, 16);
        entities = new ImmutableArray<>(sortedEntities);

        familyUpdates = familyEvent -> {
            if(familyEvent.familyIndex != getFamily().getIndex()) return;
            switch (familyEvent.type) {
                case ADD -> {
                    sortedEntities.addAll(familyEvent.entities);
                    sorted = false;
                }
                case REMOVE -> {
                    for(Entity entity : familyEvent.entities)
                        sortedEntities.removeValue(entity, true);
                    sorted = false;
                }
                case UPDATE -> sorted = false;
            }
        };
    }

    @Override
    public void init(EntityManager entityManager) {
        ImmutableArray<Entity> newEntities = entityManager.getEntitiesFor(getFamily());
        sortedEntities.clear();
        if(newEntities.size() > 0) {
            for(Entity entity : newEntities)
                sortedEntities.add(entity);
            sortedEntities.sort(comparator);
        }
        entityManager.subscribe(FamilyEvent.class, familyUpdates);
    }

    @Override
    public void delete(EntityManager entityManager) {
        entityManager.unsubscribe(FamilyEvent.class, familyUpdates);
        sortedEntities.clear();
        sorted = true;
    }

    private void sort() {
        if(sorted) return;
        sortedEntities.sort(comparator);
        sorted = true;
    }

    @Override
    protected void process(float delta) {
        sort();
        for(Entity entity : sortedEntities)
            processEntity(delta, entity);
    }

    @Override
    protected void start() {}

    protected abstract void processEntity(float delta, Entity entity);

    @Override
    protected void end() {}
}
