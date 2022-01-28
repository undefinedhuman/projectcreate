package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.utils.Array;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.ds.ImmutableArray;

import java.util.HashMap;

class FamilyManager {

    private EntityList entities;
    private HashMap<Family, Array<Entity>> familyEntities = new HashMap<>();
    private HashMap<Family, ImmutableArray<Entity>> immutableFamilyEntities = new HashMap<>();

    FamilyManager(EntityList entities) {
        this.entities = entities;
    }

    public ImmutableArray<Entity> getEntitiesFor(Family family) {
        return addFamily(family);
    }

    public void addEntities(Entity... entities) {
        for(Entity entity : entities)
            familyEntities.keySet().forEach(family -> addEntity(entity, family));
    }

    private void addEntity(Entity entity, Family family) {
        this.addEntity(entity, family, familyEntities.get(family));
    }

    private void addEntity(Entity entity, Family family, Array<Entity> familyEntities) {
        if(!family.matches(entity) || entity.isScheduledForRemoval()) return;
        familyEntities.add(entity);
        entity.getFamilyBits().set(family.getIndex());
    }

    public void removeEntities(Entity... entities) {
        for(Entity entity : entities)
            familyEntities.keySet().forEach(family -> removeEntity(entity, family));
    }

    private void removeEntity(Entity entity, Family family) {
        if(!family.matches(entity)) return;
        familyEntities.get(family).removeValue(entity, true);
        entity.getFamilyBits().clear(family.getIndex());
    }

    public void removeAllEntities() {
        familyEntities.values().forEach(Array::clear);
        entities.getUnmodifiableEntities().forEach(entity -> entity.getFamilyBits().clear());
    }

    void updateFamiliesForEntities(Entity... entities) {
        for (Entity entity : entities)
            updateFamiliesForEntity(entity);
    }

    void updateFamiliesForEntity(Entity entity) {
        familyEntities.keySet().forEach(family -> {
            boolean matches = family.matches(entity) && !entity.isScheduledForRemoval();
            boolean isInFamily = entity.getFamilyBits().get(family.getIndex());
            if(matches == isInFamily)
                return;
            if(matches) addEntity(entity, family);
            else removeEntity(entity, family);
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
            this.familyEntities.put(family, familyEntities);
            this.immutableFamilyEntities.put(family, immutableFamilyEntities);
            this.entities.getUnmodifiableEntities().forEach(entity -> addEntity(entity, family, familyEntities));
        }
        return immutableFamilyEntities;
    }

    void removeFamily(Family family) {
        familyEntities.remove(family);
        immutableFamilyEntities.remove(family);
    }

}
