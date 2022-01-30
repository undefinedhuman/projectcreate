package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.log.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

class EntityList {

    private final HashMap<Long, Entity> entitiesByID = new HashMap<>();
    private final Collection<Entity> unmodifiableEntities = Collections.unmodifiableCollection(entitiesByID.values());

    public void addEntities(Entity... entities) {
        for(Entity entity : entities)
            this.addEntity(entity.getWorldID(), entity);
    }

    void addEntity(long worldID, Entity entity) {
        if(entitiesByID.containsKey(worldID))
            Log.warn("Entity List already contains entity with world id: " + worldID + ". OVERRIDE!");
        entitiesByID.put(worldID, entity);
    }

    public void removeEntities(Entity... entities) {
        for(Entity entity : entities) {
            entity.scheduledForRemoval = false;
            entitiesByID.remove(entity.getWorldID());
        }
    }

    public void removeAllEntities() {
        entitiesByID.clear();
    }

    public Entity getEntity(long worldID) {
        return entitiesByID.get(worldID);
    }

    public boolean hasEntity(long worldID) {
        return this.entitiesByID.containsKey(worldID);
    }

    public Collection<Entity> getUnmodifiableEntities() {
        return unmodifiableEntities;
    }

}
