package de.undefinedhuman.sandboxgame.entity;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.chunk.ChunkPosition;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentList;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.Blueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.transform.TransformComponent;

public class Entity {

    private int blueprintID, worldID;
    public boolean mainPlayer = false;
    private EntityType type;
    private Blueprint blueprint;
    private ComponentList components;

    public TransformComponent transform;

    public Entity(EntityType type, Vector2 size) {
        this(type, size,0);
    }

    public Entity(EntityType type, Vector2 size, int id) {
        this.blueprintID = id;
        this.type = type;
        components = new ComponentList();
        transform = new TransformComponent(this, size);
        components.addComponent(transform);
    }

    public Entity(Blueprint blueprint, Vector2 size) {

        this.blueprint = blueprint;
        this.type = blueprint.getType();
        this.blueprintID = blueprint.getID();
        components = new ComponentList();
        transform = new TransformComponent(this, size);
        components.addComponent(transform);

    }

    public void init() {
        for (Component component : components.getComponents()) component.init();
    }

    public void setComponents(ComponentList list) {
        this.components = list;
    }

    public Blueprint getBlueprint() {
        return blueprint;
    }

    public EntityType getType() {
        return type;
    }

    public void addComponent(Component component) {
        this.components.addComponent(component);
    }

    public boolean hasComponent(ComponentType type) {
        return components.hasComponent(type);
    }

    public boolean hasComponents(ComponentType... types) {
        boolean hasComponents = true;
        for (ComponentType type : types) if (!hasComponent(type)) hasComponents = false;
        return hasComponents;
    }

    public Component getComponent(ComponentType type) {
        if (hasComponent(type)) return components.getComponent(type);
        return null;
    }

    public void loadEntityInfo(String s) {
        LineSplitter lineSplitter = new LineSplitter(s,true,";");
        int componentsNumber = lineSplitter.getNextInt();
        for(int i = 0; i < componentsNumber; i++) { components.getComponent(ComponentType.valueOf(lineSplitter.getNextString())).setNetworkData(lineSplitter); }
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

    public String getNetworkData(ComponentType... types) {
        LineWriter writer = new LineWriter(true);
        writer.writeInt(types.length);
        for(ComponentType type : types) { writer.writeString(type.name()); getComponent(type).getNetworkData(writer); }
        return writer.getData();
    }

    /* Transform Methods */

    public void setPosition(Vector2 pos) {
        transform.setPosition(pos);
    }
    public void setPosition(float x, float y) {
        transform.setPosition(new Vector2(x, y));
    }
    public void setPositionNoCheck(float x, float y) { transform.setPositionNoCheck(new Vector2(x, y)); }
    public Vector2 getPosition() {
        return transform.getPosition();
    }
    public float getX() {
        return transform.getPosition().x;
    }
    public float getY() {
        return transform.getPosition().y;
    }
    public Vector2 getCenteredPosition() {
        return new Vector2(transform.getPosition()).add(getCenter());
    }
    public Vector2 getCenter() {
        return transform.getCenter();
    }
    public float getWidth() { return transform.getWidth(); }
    public float getHeight() { return transform.getHeight(); }
    public ChunkPosition getChunkPosition() { return transform.getChunkPosition(); }
    public int getChunkID() { return transform.getChunkID(); }

}
