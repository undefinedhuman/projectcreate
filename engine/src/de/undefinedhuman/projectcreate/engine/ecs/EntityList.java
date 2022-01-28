package de.undefinedhuman.projectcreate.engine.ecs;

import de.undefinedhuman.projectcreate.engine.log.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

class EntityList {

    private final HashMap<Long, Entity> entitiesByID = new HashMap<>();
    private final Collection<Entity> unmodifiableEntities = Collections.unmodifiableCollection(entitiesByID.values());

    // private final List<EntityListener> entityListeners = Collections.synchronizedList(new ArrayList<>());

    public void addEntity(Entity entity) {
        this.addEntity(entity.getWorldID(), entity);
    }

    public void addEntity(long worldID, Entity entity) {
        if(entitiesByID.containsKey(worldID))
            Log.warn("Entity List already contains entity with world id: " + worldID + ". OVERRIDE!");
        entitiesByID.put(worldID, entity);
        // notifyListener(EntityListener.Type.ADD, entity);
    }

    public boolean removeEntity(long worldID) {
        Entity entity = getEntity(worldID);
        if(entity == null)
            return false;
        entity.scheduledForRemoval = false;
        entitiesByID.remove(worldID);
        //notifyListener(EntityListener.Type.REMOVE, entity);
        return true;
    }

    public void removeAllEntities() {
        entitiesByID.clear();
        // notifyListener(EntityListener.Type.REMOVE_ALL, unmodifiableEntities.toArray(new Entity[0]));
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

/*    public void subscribe(EntityListener listener) {
        this.entityListeners.add(listener);
    }

    public void unsubscribe(EntityListener listener) {
        this.entityListeners.remove(listener);
    }

    private void notifyListener(EntityListener.Type type, Entity... entities) {
        synchronized (entityListeners) {
            this.entityListeners.forEach(listener -> listener.handle(type, entities));
        }
    }*/

}
