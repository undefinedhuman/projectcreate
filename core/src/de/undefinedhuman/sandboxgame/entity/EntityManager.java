package de.undefinedhuman.sandboxgame.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.EntityType;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector2i;
import de.undefinedhuman.sandboxgame.entity.chunk.Chunk;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.entity.ecs.system.*;
import de.undefinedhuman.sandboxgame.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class EntityManager extends Manager {

    public static EntityManager instance;
    public Vector2i chunkSize = new Vector2i();
    private Chunk[][] chunks;
    private HashMap<Integer, Entity> entities = new HashMap<>();
    private HashMap<EntityType, ArrayList<Entity>> entitiesByType = new HashMap<>();
    private ArrayList<Integer> entitiesToRemove = new ArrayList<>();
    private ArrayList<System> systems = new ArrayList<>();

    public EntityManager() {
        if(instance == null) instance = this;
        addSystems(new AngleSystem(), new AnimationSystem(), new ArmSystem(), new InteractionSystem(), new EquipSystem(), new MovementSystem(), new RenderSystem());
        for(EntityType type : EntityType.values()) entitiesByType.put(type, new ArrayList<>());
    }

    public void init() {
        clearEntities();
        chunkSize.set(World.instance.width / Variables.CHUNK_SIZE, World.instance.height / Variables.CHUNK_SIZE);
        chunks = new Chunk[chunkSize.x][chunkSize.y];
        for (int i = 0; i < chunkSize.x; i++)
            for (int j = 0; j < chunkSize.y; j++)
                chunks[i][j] = new Chunk();
    }

    public void addEntity(int worldID, Entity entity) {
        if(entity == null) return;
        this.entities.put(worldID, entity);
        this.entitiesByType.get(entity.getType()).add(entity);
        this.chunks[entity.getChunkPosition().x][entity.getChunkPosition().y].addEntity(worldID, entity);
        initSystems(entity);
    }

    private void initSystems(Entity entity) {
        for (System system : systems) system.init(entity);
    }

    public Entity getEntity(int worldID) {
        return entities.get(worldID);
    }

    public void removeEntity(int worldID) {
        this.entitiesToRemove.add(worldID);
    }

    public ArrayList<Entity> getPlayers() {
        return entitiesByType.get(EntityType.Player);
    }

    public Chunk getChunk(int x, int y) {
        return chunks[x][y];
    }

    public void update(float delta) {
        for (Entity entity : this.entities.values()) for (System system : systems) system.update(delta, entity);
        if (entitiesToRemove.size() > 0) {
            for (int worldID : entitiesToRemove) {
                Entity entity = entities.get(worldID);
                entitiesByType.get(entity.getType()).remove(entity);
                chunks[entity.getChunkPosition().x][entity.getChunkPosition().y].removeEntity(worldID);
                entities.remove(worldID);
            }
            entitiesToRemove.clear();
        }
    }

    public void render(SpriteBatch batch) {
        for(EntityType type : EntityType.values()) {
            ArrayList<Entity> entityList = entitiesByType.get(type);
            for(Entity entity : entityList)
                render(batch, entity);
        }
    }

    public void render(SpriteBatch batch, Entity entity) {
        RenderSystem.instance.render(batch, entity);
    }

    public void delete() {
        systems.clear();
        clearEntities();
        entitiesByType.clear();
    }

    public ArrayList<Entity> getEntityInRangeForCollision(Vector2 pos, float range) {
        ArrayList<Entity> entitiesInRange = new ArrayList<>();
        for (Entity entity : entities.values())
            if (new Vector2().set(entity.getPosition()).sub(pos).len() <= range)
                entitiesInRange.add(entity);
        return entitiesInRange;
    }

    public Collection<Entity> getEntitiesForCollision() {
        return entities.values();
    }

    private void addSystems(System... systems) {
        this.systems.addAll(Arrays.asList(systems));
    }

    private void clearEntities() {
        for(ArrayList<Entity> entityTypeList : entitiesByType.values()) entityTypeList.clear();
        entitiesToRemove.clear();
        entities.clear();
    }

}
