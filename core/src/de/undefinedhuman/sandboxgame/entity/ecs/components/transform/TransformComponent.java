package de.undefinedhuman.sandboxgame.entity.ecs.components.transform;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.chunk.ChunkPosition;
import de.undefinedhuman.sandboxgame.entity.ecs.Component;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Variables;
import de.undefinedhuman.sandboxgame.world.World;

public class TransformComponent extends Component {

    private Vector2 position = new Vector2(), size;
    private ChunkPosition currentChunk = new ChunkPosition(), tempChunk = new ChunkPosition();
    private int chunkID = 0;

    public TransformComponent(Entity entity, Vector2 size) {
        super(entity);
        this.size = new Vector2(size);
        this.type = ComponentType.TRANSFORM;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
        checkWorld();
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
        checkWorld();
    }

    // Profile Image Position set No chunk and world edge check necessary
    public void setPositionNoCheck(Vector2 position) {
        this.position.set(position);
    }

    public Vector2 getPosition() {
        return position;
    }
    public float getWidth() {
        return size.x;
    }
    public void setWidth(float width) {
        size.x = width;
    }
    public float getHeight() {
        return size.y;
    }
    public void setHeight(float height) {
        size.y = height;
    }
    public Vector2 getSize() { return size; }
    public void setSize(Vector2 size) {
        this.size = size;
    }
    public void setX(float x) {
        position.x = x;
    }
    public void setY(float y) {
        position.y = y;
    }
    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }
    public Vector2 getCenter() {
        return new Vector2(size.x/2,size.y/2);
    }
    public ChunkPosition getChunkPosition() {
        return currentChunk;
    }
    public int getChunkID() {
        return tempChunk.x + EntityManager.instance.chunkSize.x * tempChunk.y;
    }

    private void checkWorld() {

        if (position.x < 0.0F || position.x >= World.instance.blockWidth) {
            float b = (position.x < 0f ? 1f : -1f);
            position.x = position.x + b * World.instance.blockWidth;
            if(entity.mainPlayer) {
                GameManager.gameCamera.position.set(GameManager.gameCamera.position.x + b * World.instance.blockWidth, GameManager.gameCamera.position.y, GameManager.gameCamera.position.z);
                GameManager.gameCamera.update();
            }
        }
        tempChunk.setPosition(((int) position.x/Variables.BLOCK_SIZE)/Variables.CHUNK_SIZE, ((int) position.y/ Variables.BLOCK_SIZE)/Variables.CHUNK_SIZE);
        if (tempChunk.x != currentChunk.x || tempChunk.y != currentChunk.y) {
            EntityManager.instance.getChunk(currentChunk.x, currentChunk.y).removeEntity(this.entity.getWorldID());
            EntityManager.instance.getChunk(tempChunk.x, tempChunk.y).addEntity(this.entity.getWorldID(), this.entity);
            currentChunk.setPosition(tempChunk.x, tempChunk.y);
        }
    }

    @Override
    public void setNetworkData(LineSplitter s) {
        position.set(s.getNextVector2());
    }

    @Override
    public void getNetworkData(LineWriter w) {
        w.writeVector2(position);
    }

}
