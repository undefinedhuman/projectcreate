package de.undefinedhuman.projectcreate.game.entity;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.base.GameObject;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentList;
import de.undefinedhuman.projectcreate.engine.ecs.EntityType;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.utils.Tools;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.game.entity.ecs.blueprint.Blueprint;
import de.undefinedhuman.projectcreate.game.network.components.NetworkComponent;
import de.undefinedhuman.projectcreate.game.world.World;

public class Entity extends GameObject implements NetworkComponent {

    public boolean mainPlayer = false;

    private int blueprintID, worldID, chunkID;
    private EntityType type;
    private ComponentList components;

    private Vector2i chunkPosition = new Vector2i(), tempChunkPosition = new Vector2i();

    public Entity(Blueprint blueprint, Vector2 size) {
        this(size, blueprint.getType(), blueprint.getID());
    }

    public Entity(Vector2 size, EntityType type, int id) {
        super(size);
        this.blueprintID = id;
        this.type = type;
        components = new ComponentList();
        updateChunkPosition();
    }

    @Override
    public void init() {
        for (Component component : components.getComponents()) component.init();
    }

    @Override
    public void delete() {
        components.delete();
    }

    public void setComponents(ComponentList list) {
        this.components = list;
    }

    public void addComponents(Component... components) {
        for(Component component : components) this.components.addComponent(component);
    }

    public Component getComponent(Class<? extends Component> type) {
        if(!hasComponent(type)) return null;
        return components.getComponent(type);
    }

    public boolean hasComponent(Class<? extends Component>... types) {
        boolean hasComponents = true;
        for (Class<? extends Component> type : types) if (!components.hasComponent(type)) hasComponents = false;
        return hasComponents;
    }

    public EntityType getType() {
        return type;
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
        position.x = (World.instance.pixelSize.x + position.x) % World.instance.pixelSize.x;
        updateChunkPosition();
    }

    public void updateChunkPosition() {
        tempChunkPosition.set(position).div(Variables.BLOCK_SIZE).div(Variables.CHUNK_SIZE);
        tempChunkPosition.x = Tools.getWorldPositionX(tempChunkPosition.x, EntityManager.getInstance().getChunkSize().x);
        int newChunkID = tempChunkPosition.x + EntityManager.getInstance().getChunkSize().x * tempChunkPosition.y;
        if(newChunkID == chunkID) return;
        EntityManager.getInstance().getChunk(chunkPosition.x, chunkPosition.y).removeEntity(this);
        EntityManager.getInstance().getChunk(tempChunkPosition.x, tempChunkPosition.y).addEntity(this);
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
