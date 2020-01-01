package de.undefinedhuman.sandboxgame.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.entity.chunk.Chunk;
import de.undefinedhuman.sandboxgame.entity.chunk.ChunkPosition;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.entity.ecs.system.*;
import de.undefinedhuman.sandboxgame.utils.Variables;
import de.undefinedhuman.sandboxgame.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class EntityManager {

    public static EntityManager instance;

    private Chunk[][] chunks;
    public ChunkPosition chunkSize = new ChunkPosition();

    private HashMap<Integer, Entity> entities;
    private ArrayList<Entity> players;
    private ArrayList<Integer> entitiesToRemove;
    private ArrayList<System> systems;

    public EntityManager() {

        players = new ArrayList<>();
        entities = new HashMap<>();
        entitiesToRemove = new ArrayList<>();
        systems = new ArrayList<>();
        AngleSystem.instance = new AngleSystem();
        systems.add(AngleSystem.instance);
        AnimationSystem.instance = new AnimationSystem();
        systems.add(AnimationSystem.instance);
        ArmSystem.instance = new ArmSystem();
        systems.add(ArmSystem.instance);
        InteractionSystem.instance = new InteractionSystem();
        systems.add(InteractionSystem.instance);
        EquipSystem.instance = new EquipSystem();
        systems.add(EquipSystem.instance);
        MovementSystem.instance = new MovementSystem();
        systems.add(MovementSystem.instance);
        RenderSystem.instance = new RenderSystem();
        systems.add(RenderSystem.instance);

    }

    public void init() {
        clearEntities();
        chunkSize.setPosition(World.instance.width/ Variables.CHUNK_SIZE,World.instance.height/Variables.CHUNK_SIZE);
        chunks = new Chunk[chunkSize.x][chunkSize.y];
        for(int i = 0; i < chunkSize.x; i++) {
            for(int j = 0; j < chunkSize.y; j++) {
                chunks[i][j] = new Chunk();
            }
        }
    }

    public void addEntity(int id, Entity entity) {
        RenderSystem.dirty = true;
        this.entities.put(id, entity);
        this.chunks[entity.getChunkPosition().x][entity.getChunkPosition().y].addEntity(id, entity);
        initSystems(entity);
    }

    public Entity getEntity(int id) {
        return entities.get(id);
    }

    public void removeEntity(int id) {
        this.entitiesToRemove.add(id);
    }

    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }

    public ArrayList<Entity> getPlayers() {
        return players;
    }

    public Chunk getChunk(int x, int y) {
        return chunks[x][y];
    }

    public void update(float delta) {
        for (Entity entity : this.entities.values()) for (System system : systems) system.update(delta, entity);
        if(entitiesToRemove.size() > 0) {
            RenderSystem.dirty = true;
            for (int i : entitiesToRemove) {
                Entity entity = entities.get(i);
                chunks[entity.getChunkPosition().x][entity.getChunkPosition().y].removeEntity(i);
                entities.remove(i);
            }
            entitiesToRemove.clear();
        }
    }

    public void render(SpriteBatch batch) {
        for (System system : systems) system.render(batch);
    }
    public void render(SpriteBatch batch, Entity entity) {
        for (System system : systems) system.render(batch, entity);
    }

    public void delete() {
        systems.clear();
        clearEntities();
    }

    private void clearEntities() {
        entitiesToRemove.clear();
        players.clear();
        entities.clear();
    }

    private void initSystems(Entity entity) {
        for (System system : systems) system.init(entity);
    }

    public ArrayList<Entity> getEntityInRangeForCollision(Vector2 pos, float range) {
        ArrayList<Entity> entitiesInRange = new ArrayList<>();
        for (Entity entity : entities.values()) if (new Vector2().set(entity.getX() - pos.x, entity.getY() - pos.y).len() <= range) entitiesInRange.add(entity);
        return entitiesInRange;
    }

    public Collection<Entity> getEntitiesForCollision() {
        return entities.values();
    }

}
