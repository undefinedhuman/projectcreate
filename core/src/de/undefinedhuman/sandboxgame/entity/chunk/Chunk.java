package de.undefinedhuman.sandboxgame.entity.chunk;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;

public class Chunk {

    private HashMap<Integer, Entity> entities = new HashMap<>();
    private ArrayList<Entity> entitiesForCollision = new ArrayList<>(), players = new ArrayList<>();
    private Vector2 distanceVector = new Vector2();

    public Chunk() { }

    public void addEntity(int id, Entity entity) {
        this.entities.put(id, entity);
        if (entity.hasComponent(ComponentType.COLLISION)) entitiesForCollision.add(entity);
        if (entity.getType() == EntityType.PLAYER) players.add(entity);
    }

    public void removeEntity(int id) {
        if (!entities.containsKey(id)) return;
        Entity entity = entities.get(id);
        if (entity.hasComponent(ComponentType.COLLISION)) entitiesForCollision.remove(entity);
        if (entity.getType() == EntityType.PLAYER) players.remove(entity);
        this.entities.remove(id);
    }

    public ArrayList<Entity> getPlayers() {
        return this.players;
    }

    public ArrayList<Entity> getEntityInRangeForCollision(Vector2 pos, float range) {
        ArrayList<Entity> entitiesInRange = new ArrayList<>();
        for (Entity entity : entitiesForCollision)
            if (distanceVector.set(entity.getPosition()).sub(pos).len() <= range)
                entitiesInRange.add(entity);
        return entitiesInRange;
    }

    public ArrayList<Entity> getEntitiesForCollision() {
        return entitiesForCollision;
    }

    public void delete() {
        entitiesForCollision.clear();
        players.clear();
        entities.clear();
    }

}
