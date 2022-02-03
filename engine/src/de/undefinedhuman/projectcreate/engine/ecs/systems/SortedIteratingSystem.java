package de.undefinedhuman.projectcreate.engine.ecs.systems;

import com.badlogic.gdx.utils.Array;
import de.undefinedhuman.projectcreate.engine.ecs.System;
import de.undefinedhuman.projectcreate.engine.ecs.*;
import de.undefinedhuman.projectcreate.engine.observer.Observer;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;

import java.util.Comparator;

public abstract class SortedIteratingSystem extends System {

    private Array<Entity> sortedEntities;
    private boolean sorted = true;
    private Comparator<Entity> comparator;

    private Observer<Entity[]> addEntitiesToSystem, removeEntitiesFromSystem, updateEntitiesOfSystem;

    public SortedIteratingSystem (Comparator<Entity> comparator) {
        this(comparator, 0);
    }

    public SortedIteratingSystem (Comparator<Entity> comparator, int priority) {
        super(priority);
        this.comparator = comparator;
        sortedEntities = new Array<>(false, 16);
        entities = new ImmutableArray<>(sortedEntities);
        addEntitiesToSystem = entities -> {
            sortedEntities.addAll(entities);
            sorted = false;
        };
        removeEntitiesFromSystem = entities -> {
            for (Entity entity : entities)
                sortedEntities.removeValue(entity, true);
            sorted = false;
        };
        updateEntitiesOfSystem = entities -> sorted = false;
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
        FamilyEvent.subscribe(entityManager, getFamily(), FamilyEvent.Type.ADD, addEntitiesToSystem);
        FamilyEvent.subscribe(entityManager, getFamily(), FamilyEvent.Type.REMOVE, removeEntitiesFromSystem);
        FamilyEvent.subscribe(entityManager, getFamily(), FamilyEvent.Type.UPDATE, updateEntitiesOfSystem);
    }

    @Override
    public void delete(EntityManager entityManager) {
        FamilyEvent.unsubscribe(entityManager, getFamily(), FamilyEvent.Type.ADD, addEntitiesToSystem);
        FamilyEvent.unsubscribe(entityManager, getFamily(), FamilyEvent.Type.REMOVE, removeEntitiesFromSystem);
        FamilyEvent.unsubscribe(entityManager, getFamily(), FamilyEvent.Type.UPDATE, updateEntitiesOfSystem);
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
