package de.undefinedhuman.sandboxgameserver.entity;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentList;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.entity.ecs.blueprint.Blueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.transform.TransformComponent;
import de.undefinedhuman.sandboxgameserver.file.LineWriter;

public class Entity {

    public Connection c;
    public TransformComponent transform;

    private int blueprintID, worldID;
    private String worldName;
    private EntityType type;
    private Blueprint blueprint;
    private ComponentList components;

    public Entity(EntityType type, Vector2 size) {

        this.blueprintID = 0;
        this.type = type;
        components = new ComponentList();
        transform = new TransformComponent(this, size);
        components.addComponent(transform);

    }

    public Entity(EntityType type, Vector2 size, int blueprintID) {

        this.blueprintID = blueprintID;
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

    public ComponentList getComponents() {
        return components;
    }

    public void setComponents(ComponentList list) {
        this.components = list;
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

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getEntityInfo() {

        LineWriter writer = new LineWriter(true);
        writer.writeInt(components.getComponents().size());
        for(Component component : components.getComponents()) { writer.writeString(component.getType().toString()); component.getNetworkData(writer); }
        return writer.getData();

    }

    public String getNetworkData(ComponentType... types) {

        LineWriter writer = new LineWriter(true);
        writer.writeInt(types.length);
        for(ComponentType type : types) { writer.writeString(type.toString()); getComponent(type).getNetworkData(writer); }
        return writer.getData();

    }

    /* Transform Methods */

    public void setPosition(float x, float y) {
        transform.setPosition(new Vector2(x, y));
    }

    public Vector2 getPosition() {
        return transform.getPosition();
    }

    public void setPosition(Vector2 pos) {
        transform.setPosition(pos);
    }

    public float getX() {
        return transform.getPosition().x;
    }

    public void setX(float x) {
        transform.setX(x);
    }

    public float getY() {
        return transform.getPosition().y;
    }

    public void setY(float y) {
        transform.setY(y);
    }

    public Vector2 getCenter() {
        return transform.getCenter();
    }

}
