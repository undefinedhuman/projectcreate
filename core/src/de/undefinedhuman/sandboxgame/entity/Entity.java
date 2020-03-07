package de.undefinedhuman.sandboxgame.entity;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentList;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.Blueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.transform.Transform;
import de.undefinedhuman.sandboxgame.network.components.NetworkComponent;

public class Entity implements NetworkComponent {

    public boolean mainPlayer = false;
    public Transform transform;
    private int blueprintID, worldID;
    private EntityType type;
    private Blueprint blueprint;
    private ComponentList components;

    public Entity(EntityType type, Vector2 size) {
        this(type, size, 0);
    }

    public Entity(EntityType type, Vector2 size, int id) {
        this.blueprintID = id;
        this.type = type;
        components = new ComponentList();
        transform = new Transform(this, size);
    }

    public Entity(Blueprint blueprint, Vector2 size) {
        this.blueprint = blueprint;
        this.type = blueprint.getType();
        this.blueprintID = blueprint.getID();
        components = new ComponentList();
        transform = new Transform(this, size);
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

    public boolean hasComponents(ComponentType... types) {
        boolean hasComponents = true;
        for (ComponentType type : types) if (!hasComponent(type)) hasComponents = false;
        return hasComponents;
    }

    public boolean hasComponent(ComponentType type) {
        return components.hasComponent(type);
    }

    // TODO Update to network components methods

    public void receive(String s) {
        LineSplitter lineSplitter = new LineSplitter(s, true, ";");
        int componentsNumber = lineSplitter.getNextInt();
        for (int i = 0; i < componentsNumber; i++) {
            components.getComponent(ComponentType.valueOf(lineSplitter.getNextString())).receive(lineSplitter);
        }
    }

    public String send(ComponentType... types) {
        LineWriter writer = new LineWriter(true);
        writer.writeInt(types.length);
        for (ComponentType type : types) {
            writer.writeString(type.name());
            getComponent(type).send(writer);
        }
        return writer.getData();
    }

    public Component getComponent(ComponentType type) {
        if (hasComponent(type)) return components.getComponent(type);
        return null;
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

    public Transform getTransform() {
        return transform;
    }

    @Override
    public void send(LineWriter writer) {}

    @Override
    public void receive(LineSplitter splitter) {}

}
