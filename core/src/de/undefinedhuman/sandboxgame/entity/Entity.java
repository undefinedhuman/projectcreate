package de.undefinedhuman.sandboxgame.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.base.GameObject;
import de.undefinedhuman.sandboxgame.engine.entity.Component;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentList;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.EntityType;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.engine.utils.math.Vector2i;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.Blueprint;
import de.undefinedhuman.sandboxgame.network.components.NetworkComponent;
import de.undefinedhuman.sandboxgame.world.World;

public class Entity extends GameObject implements NetworkComponent {

    public boolean mainPlayer = false;
    private int blueprintID, worldID, chunkID;
    private EntityType type;
    private ComponentList components;

    private Vector2i chunkPosition = new Vector2i(), tempChunkPosition = new Vector2i();

    public Entity(Vector2 size, EntityType type) {
        this(size, type, 0);
    }

    public Entity(Blueprint blueprint, Vector2 size) {
        this(size, blueprint.getType(), blueprint.getID());
    }

    public Entity(Vector2 size, EntityType type, int id) {
        super(size);
        this.blueprintID = id;
        this.type = type;
        components = new ComponentList();
    }

    @Override
    public void init() {
        for (Component component : components.getComponents()) component.init();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {}

    @Override
    public void delete() {
        components.delete();
    }

    public void setComponents(ComponentList list) {
        this.components = list;
    }

    public void addComponent(Component component) {
        this.components.addComponent(component);
    }

    public Component getComponent(ComponentType type) {
        if (hasComponent(type)) return components.getComponent(type);
        return null;
    }

    public boolean hasComponents(ComponentType... types) {
        boolean hasComponents = true;
        for (ComponentType type : types) if (!hasComponent(type)) hasComponents = false;
        return hasComponents;
    }

    public boolean hasComponent(ComponentType type) {
        return components.hasComponent(type);
    }

    public EntityType getType() {
        return type;
    }

    public int getBlueprintID() {
        return blueprintID;
    }

    public int getWorldID() {
        return worldID;
    }

    public void setWorldID(int worldID) {
        this.worldID = worldID;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        updateWorldPosition();
        updateChunkPosition();
    }

    private void updateWorldPosition() {
        if(position.x > 0 && position.x < World.instance.blockWidth) return;
        position.x = position.x + (position.x < 0f ? 1f : -1f) * World.instance.blockWidth;
    }

    private void updateChunkPosition() {
        tempChunkPosition.set(position).div(Variables.BLOCK_SIZE).div(Variables.CHUNK_SIZE);
        int newChunkID = tempChunkPosition.x + EntityManager.instance.chunkSize.x * tempChunkPosition.y;
        if(newChunkID == chunkID) return;
        EntityManager.instance.getChunk(tempChunkPosition.x, tempChunkPosition.y).removeEntity(worldID);
        EntityManager.instance.getChunk(tempChunkPosition.x, tempChunkPosition.y).addEntity(worldID, this);
        chunkPosition.set(tempChunkPosition);
        chunkID = newChunkID;
    }

    public Vector2i getChunkPosition() {
        return chunkPosition;
    }

    @Override
    public void send(LineWriter writer) {}

    @Override
    public void receive(LineSplitter splitter) {}

}
