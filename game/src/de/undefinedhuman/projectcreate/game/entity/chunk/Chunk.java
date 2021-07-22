package de.undefinedhuman.projectcreate.game.entity.chunk;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.ecs.collision.CollisionComponent;
import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector4;
import de.undefinedhuman.projectcreate.game.collision.CollisionManager;

import java.util.ArrayList;
import java.util.Collection;

public class Chunk {

    private ArrayList<Entity> entitiesForCollision = new ArrayList<>();
    private MultiMap<EntityType, Entity> entitiesByType = new MultiMap<>();

    public void render(SpriteBatch batch, int renderOffset) {
        for(EntityType type : EntityType.values()) {
            ArrayList<Entity> entityList = entitiesByType.getValuesWithKey(type);
            if(entityList == null) continue;
            for(Entity entity : entityList)
                EntityManager.getInstance().renderSystem.render(batch, entity, renderOffset);
        }
    }

    public void addEntity(Entity entity) {
        if(entity == null || entitiesByType.hasValue(entity.getType(), entity)) return;
        this.entitiesByType.add(entity.getType(), entity);
        if (entity.hasComponent(CollisionComponent.class)) entitiesForCollision.add(entity);
    }

    public void removeEntity(Entity entity) {
        if(entity == null || !entitiesByType.hasValue(entity.getType(), entity)) return;
        if (entity.hasComponent(CollisionComponent.class)) entitiesForCollision.remove(entity);
        entitiesByType.removeValue(entity.getType(), entity);
    }

    public Collection<Entity> getEntitiesByType(EntityType type) {
        return entitiesByType.getValuesWithKey(type);
    }

    public ArrayList<Entity> getEntitiesInRangeForCollision(Vector4 bounds) {
        ArrayList<Entity> entitiesInRange = new ArrayList<>();
        for (Entity entity : entitiesForCollision)
            if(CollisionManager.collideAABB(bounds,
                    ((CollisionComponent) entity.getComponent(CollisionComponent.class)).getBounds(entity.getPosition())))
                entitiesInRange.add(entity);
        return entitiesInRange;
    }

    public void delete() {
        entitiesForCollision.clear();
        entitiesByType.clear();
    }

}
