package de.undefinedhuman.sandboxgame.entity;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityList {

    private HashMap<Integer, Entity> entities;
    private ArrayList<Integer> entitiesForCollision;

    public EntityList() {
        entities = new HashMap<>();
        entitiesForCollision = new ArrayList<>();
    }

    public void addEntity(int id, Entity entity) {
        this.entities.put(id, entity);
        if (entity.hasComponent(ComponentType.COLLISION)) entitiesForCollision.add(id);
    }

    public Entity getEntity(int id) {
        return entities.get(id);
    }

    public void removeEntity(int id) {

        Entity entity = entities.get(id);
        if (entity.hasComponent(ComponentType.COLLISION)) entitiesForCollision.remove(id);
        this.entities.remove(id);

    }

    public boolean hasEntity(int id) {
        return entities.containsKey(id);
    }

    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }

    public ArrayList<Entity> getPlayers() {
        ArrayList<Entity> players = new ArrayList<>();
        for (Entity entity : entities.values()) if (entity.getType() == EntityType.PLAYER) players.add(entity);
        return players;
    }

    public ArrayList<Integer> getPlayersID() {
        ArrayList<Integer> players = new ArrayList<>();
        for (int id : entities.keySet()) if (entities.get(id).getType() == EntityType.PLAYER) players.add(id);
        return players;
    }

    public ArrayList<Entity> getEntityInRangeForCollision(Vector2 pos, float range) {

        ArrayList<Entity> entitiesInRange = new ArrayList<>();

        for (Integer id : entitiesForCollision) {
            Entity entity = entities.get(id);
            float entityDis = new Vector2(entity.transform.getPosition()).sub(pos).len();
            if (entityDis <= range) entitiesInRange.add(entity);
        }

        return entitiesInRange;

    }

    public ArrayList<Integer> getEntitiesForCollision() {
        return entitiesForCollision;
    }

    public void delete() {
        entitiesForCollision.clear();
        entities.clear();
    }

}
