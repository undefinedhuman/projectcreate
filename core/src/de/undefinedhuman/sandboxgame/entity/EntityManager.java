package de.undefinedhuman.sandboxgame.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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

public class EntityManager {

    public static EntityManager instance;
    public Vector2i chunkSize = new Vector2i();
    private Chunk[][] chunks;
    private HashMap<Integer, Entity> entities = new HashMap<>();
    private ArrayList<Entity> players = new ArrayList<>();
    private ArrayList<Integer> entitiesToRemove = new ArrayList<>();
    private ArrayList<System> systems = new ArrayList<>();

    public EntityManager() {
        if(instance == null) instance = this;
        addSystems(new AngleSystem(), new AnimationSystem(), new ArmSystem(), new InteractionSystem(), new EquipSystem(), new MovementSystem(), new RenderSystem());
    }

    public void init() {
        clearEntities();
        chunkSize.set(World.instance.width / Variables.CHUNK_SIZE, World.instance.height / Variables.CHUNK_SIZE);
        chunks = new Chunk[chunkSize.x][chunkSize.y];
        for (int i = 0; i < chunkSize.x; i++) {
            for (int j = 0; j < chunkSize.y; j++) {
                chunks[i][j] = new Chunk();
            }
        }
    }

    private void clearEntities() {
        entitiesToRemove.clear();
        players.clear();
        entities.clear();
    }

    public void addEntity(int id, Entity entity) {
        RenderSystem.dirty = true;
        this.entities.put(id, entity);
        this.chunks[entity.getChunkPosition().x][entity.getChunkPosition().y].addEntity(id, entity);
        initSystems(entity);
    }

    private void initSystems(Entity entity) {
        for (System system : systems) system.init(entity);
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
        if (entitiesToRemove.size() > 0) {
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

}
