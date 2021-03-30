package de.undefinedhuman.projectcreate.core.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.camera.CameraManager;
import de.undefinedhuman.projectcreate.engine.entity.components.collision.CollisionComponent;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector4;
import de.undefinedhuman.projectcreate.core.entity.chunk.Chunk;
import de.undefinedhuman.projectcreate.core.entity.ecs.System;
import de.undefinedhuman.projectcreate.core.entity.ecs.system.*;
import de.undefinedhuman.projectcreate.core.utils.Tools;
import de.undefinedhuman.projectcreate.core.world.World;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.entity.EntityType;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.entity.ecs.system.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class EntityManager extends Manager {

    public static EntityManager instance;

    private Vector2i chunkSize = new Vector2i();
    private Chunk[][] chunks;
    private HashMap<Integer, Entity> entities = new HashMap<>();
    private ArrayList<Integer> entitiesToRemove = new ArrayList<>();
    private ArrayList<System> systems = new ArrayList<>();

    public EntityManager() {
        if (instance == null) instance = this;
        addSystems(new AngleSystem(), new AnimationSystem(), new ArmSystem(), new InteractionSystem(), new EquipSystem(), new MovementSystem(), new RenderSystem());
    }

    @Override
    public void init() {
        clearEntities();
        chunkSize.set(World.instance.size.x / Variables.CHUNK_SIZE, World.instance.size.y / Variables.CHUNK_SIZE);
        chunks = new Chunk[chunkSize.x][chunkSize.y];
        for (int i = 0; i < chunkSize.x; i++)
            for (int j = 0; j < chunkSize.y; j++)
                chunks[i][j] = new Chunk();
    }

    @Override
    public void update(float delta) {
        for (Entity entity : this.entities.values()) for (System system : systems) system.update(delta, entity);
        if (entitiesToRemove.size() == 0) return;
        for (int worldID : entitiesToRemove) {
            Entity entity = entities.get(worldID);
            getChunk(entity.getChunkPosition()).removeEntity(entity);
            entities.remove(worldID);
        }
        entitiesToRemove.clear();
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for (int x = CameraManager.instance.chunkBounds.x; x <= CameraManager.instance.chunkBounds.z; x++)
            for (int y = CameraManager.instance.chunkBounds.y; y <= CameraManager.instance.chunkBounds.w; y++)
                getChunk(x, y).render(batch, World.instance.pixelSize.x * (x < 0 ? -1 : x >= chunkSize.x ? 1 : 0));
    }

    public void render(SpriteBatch batch, Entity entity) {
        RenderSystem.instance.render(batch, entity);
    }

    public void addEntity(int worldID, Entity entity) {
        if(entity == null) return;
        this.entities.put(worldID, entity);
        entity.updateChunkPosition();
        for (System system : systems) system.init(entity);
    }

    public Entity getEntity(int worldID) {
        if(!entities.containsKey(worldID)) return null;
        return entities.get(worldID);
    }

    public void removeEntity(int worldID) {
        if(!entities.containsKey(worldID)) return;
        this.entitiesToRemove.add(worldID);
    }

    public Chunk getChunk(Vector2i position) {
        return getChunk(position.x, position.y);
    }

    public Chunk getChunk(int x, int y) {
        y = Tools.clamp(y, 0, chunks[0].length-1);
        return chunks[(chunkSize.x + x) % chunkSize.x][y];
    }

    public Vector2i getChunkSize() {
        return chunkSize;
    }

    public ArrayList<Entity> getEntityByType(EntityType type) {
        ArrayList<Entity> entities = new ArrayList<>();
        for(int chunkX = CameraManager.instance.chunkBounds.x; chunkX <= CameraManager.instance.chunkBounds.z; chunkX++)
            for(int chunkY = CameraManager.instance.chunkBounds.y; chunkY <= CameraManager.instance.chunkBounds.w; chunkY++) {
                Collection<Entity> entitiesOfChunk = chunks[Tools.getWorldPositionX(chunkX, chunkSize.x)][chunkY].getEntitiesByType(type);
                if(entitiesOfChunk != null) entities.addAll(entitiesOfChunk);
            }
        return entities;
    }

    public ArrayList<Entity> getEntitiesWithCollision(Entity entity) {
        CollisionComponent collisionComponent;
        if((collisionComponent = (CollisionComponent) entity.getComponent(ComponentType.COLLISION)) == null) return new ArrayList<>();
        return getEntitiesWithCollision(collisionComponent.getBounds(entity.getPosition()));
    }

    public ArrayList<Entity> getEntitiesWithCollision(Vector4 bounds) {
        ArrayList<Entity> entitiesInRange = new ArrayList<>();
        for(int chunkX = CameraManager.instance.chunkBounds.x; chunkX <= CameraManager.instance.chunkBounds.z; chunkX++)
            for(int chunkY = CameraManager.instance.chunkBounds.y; chunkY <= CameraManager.instance.chunkBounds.w; chunkY++)
                entitiesInRange.addAll(getChunk(chunkX, chunkY).getEntitiesInRangeForCollision(bounds));
        return entitiesInRange;
    }

    public void delete() {
        systems.clear();
        clearEntities();
    }

    private void addSystems(System... systems) {
        this.systems.addAll(Arrays.asList(systems));
    }

    private void clearEntities() {
        chunks = new Chunk[0][0];
        entitiesToRemove.clear();
        entities.clear();
    }

}
