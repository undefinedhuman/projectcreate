package de.undefinedhuman.sandboxgameserver.entity;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.name.NameComponent;
import de.undefinedhuman.sandboxgameserver.utils.IDManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class EntityList {

    private IDManager idManager;
    private HashMap<Integer, Entity> entities, players;
    private ArrayList<Entity> entitiesForCollision;

    public EntityList(IDManager idManager) {

        this.idManager = idManager;
        entities = new HashMap<>();
        players = new HashMap<>();
        entitiesForCollision = new ArrayList<>();

    }

    public void addEntity(int id, Entity entity) {

        this.entities.put(id, entity);
        if (entity.hasComponent(ComponentType.COLLISION)) entitiesForCollision.add(entity);
        if (entity.getType() == EntityType.PLAYER) players.put(id, entity);

    }

    public boolean hasPlayer(int id) {
        return players.containsKey(id);
    }

    public Entity getPlayer(int id) {
        return players.get(id);
    }

    public Entity getEntity(int id) {
        return entities.get(id);
    }

    public void removeEntity(int id) {

        if (hasEntity(id)) {

            Entity entity = entities.get(id);
            if (entity.hasComponent(ComponentType.COLLISION)) entitiesForCollision.remove(entity);
            if (entity.getType() == EntityType.PLAYER) players.remove(id);
            this.entities.remove(id);
            this.idManager.addID(id);

        }

    }

    public boolean hasEntity(int id) {
        return entities.containsKey(id);
    }

    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }

    public Collection<Integer> getPlayersID() {
        return players.keySet();
    }

    public boolean isPlayerOnline(String playerName) {

        for (Entity entity : getPlayers())
            if (((NameComponent) entity.getComponent(ComponentType.NAME)).getName().equalsIgnoreCase(playerName))
                return true;
        return false;

    }

    public Collection<Entity> getPlayers() {
        return players.values();
    }

    public ArrayList<Entity> getEntityInRangeForCollision(Vector2 pos, float range) {

        ArrayList<Entity> entitiesinRange = new ArrayList<>();

        for (Entity entity : entitiesForCollision) {

            float entityDis = new Vector2(entity.getX() - pos.x, entity.getY() - pos.y).len();
            if (entityDis <= range) entitiesinRange.add(entity);

        }

        return entitiesinRange;

    }

    public ArrayList<Entity> getEntitiesForCollision() {
        return entitiesForCollision;
    }

    public void delete() {

        idManager.delete();
        entitiesForCollision.clear();
        players.clear();
        entities.clear();

    }

}
