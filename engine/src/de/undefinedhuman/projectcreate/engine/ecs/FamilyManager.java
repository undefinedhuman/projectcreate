package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.utils.Array;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;

import java.util.Arrays;
import java.util.HashMap;

class FamilyManager {

    private static final Entity[] EMPTY_ENTITIES = new Entity[0];

    private EntityManager entityManager;
    private HashMap<Family, Array<Entity>> entitiesInFamilies = new HashMap<>();
    private HashMap<Family, ImmutableArray<Entity>> immutableFamilyEntities = new HashMap<>();

    FamilyManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public ImmutableArray<Entity> getEntitiesFor(Family family) {
        return addFamily(family);
    }

    public void addEntities(Entity... entities) {
        for(Family family : entitiesInFamilies.keySet())
            addEntitiesToFamily(family, entities);
    }

    private void addEntitiesToFamily(Family family, Entity... entities) {
        Array<Entity> entitiesListForFamily = entitiesInFamilies.get(family);
        Entity[] entitiesMatchingFamily = Arrays.stream(entities).filter(entity -> family.matches(entity) && !entity.isScheduledForRemoval()).toArray(Entity[]::new);
        for(Entity entity : entitiesMatchingFamily) {
            entitiesListForFamily.add(entity);
            entity.getFamilyBits().set(family.getIndex());
        }
        entityManager.notify(new FamilyEvent(FamilyEvent.Type.ADD, family.getIndex(), entitiesMatchingFamily));
    }

    public void removeEntities(Entity... entities) {
        for(Family family : entitiesInFamilies.keySet())
            removeEntitiesFromFamily(family, entities);
    }

    private void removeEntitiesFromFamily(Family family, Entity... entities) {
        Array<Entity> entitiesListForFamily = entitiesInFamilies.get(family);
        Entity[] entitiesInFamily = Arrays.stream(entities).filter(entity -> entity.getFamilyBits().get(family.getIndex())).toArray(Entity[]::new);
        for(Entity entity : entitiesInFamily) {
            entitiesListForFamily.removeValue(entity, true);
            entity.getFamilyBits().clear(family.getIndex());
        }
        entityManager.notify(new FamilyEvent(FamilyEvent.Type.REMOVE, family, entitiesInFamily));
    }

    public void removeAllEntities() {
        entitiesInFamilies.keySet().forEach(
                family -> {
                    Entity[] entitiesToRemove = entitiesInFamilies.get(family).toArray(Entity.class);
                    entityManager.notify(new FamilyEvent(FamilyEvent.Type.REMOVE, family, entitiesToRemove));
                }
        );
        entitiesInFamilies.values().forEach(Array::clear);
        entityManager.getEntities().forEach(entity -> entity.getFamilyBits().clear());
    }

    public void clear() {
        removeAllEntities();
        entitiesInFamilies.clear();
        immutableFamilyEntities.clear();
    }

    void updateFamiliesForEntity(Entity entity) {
        entitiesInFamilies.keySet().forEach(family -> {
            boolean matches = family.matches(entity) && !entity.isScheduledForRemoval();
            if(matches && !family.getOptional().isEmpty())
                entityManager.notify(new FamilyEvent(FamilyEvent.Type.UPDATE, family, EMPTY_ENTITIES));
            if(matches == entity.getFamilyBits().get(family.getIndex()))
                return;
            if(matches) addEntitiesToFamily(family, entity);
            else removeEntitiesFromFamily(family, entity);
        });
    }

    ImmutableArray<Entity> addFamily(Family family) {
        ImmutableArray<Entity> immutableFamilyEntities = this.immutableFamilyEntities.get(family);
        if(immutableFamilyEntities == null) {
            if(family == null) {
                Log.error("Family is null, returning null as entity list.");
                return null;
            }
            Array<Entity> familyEntities = new Array<>(false, 16);
            immutableFamilyEntities = new ImmutableArray<>(familyEntities);
            this.entitiesInFamilies.put(family, familyEntities);
            this.immutableFamilyEntities.put(family, immutableFamilyEntities);
            addEntitiesToFamily(family, this.entityManager.getEntities().toArray(new Entity[0]));
        }
        return immutableFamilyEntities;
    }

}
