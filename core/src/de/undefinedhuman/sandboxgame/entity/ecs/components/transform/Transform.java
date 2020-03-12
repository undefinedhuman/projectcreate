package de.undefinedhuman.sandboxgame.entity.ecs.components.transform;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.chunk.ChunkPosition;
import de.undefinedhuman.sandboxgame.network.components.NetworkComponent;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.world.World;

public class Transform implements NetworkComponent {

    private Entity entity;
    private Vector2 position = new Vector2(), size = new Vector2();
    private ChunkPosition currentChunk = new ChunkPosition(), tempChunk = new ChunkPosition();
    private int chunkID = 0;

    public Transform(Entity entity, Vector2 size) {
        this.entity = entity;
        this.size.set(size);
    }

    // Profile Image Position set No chunk and world edge check necessary
    public void setPositionNoCheck(Vector2 position) {
        this.position.set(position);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        setPosition(position.x, position.y);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
        checkWorld();
    }

    private void checkWorld() {

        if (position.x < 0.0F || position.x >= World.instance.blockWidth) {
            float b = (position.x < 0f ? 1f : -1f);
            position.x = position.x + b * World.instance.blockWidth;
            if (entity.mainPlayer) {
                GameManager.gameCamera.position.set(GameManager.gameCamera.position.x + b * World.instance.blockWidth, GameManager.gameCamera.position.y, GameManager.gameCamera.position.z);
                GameManager.gameCamera.update();
            }
        }
        tempChunk.setPosition(((int) position.x / Variables.BLOCK_SIZE) / Variables.CHUNK_SIZE, ((int) position.y / Variables.BLOCK_SIZE) / Variables.CHUNK_SIZE);
        if (tempChunk.x != currentChunk.x || tempChunk.y != currentChunk.y) {
            EntityManager.instance.getChunk(currentChunk.x, currentChunk.y).removeEntity(this.entity.getWorldID());
            EntityManager.instance.getChunk(tempChunk.x, tempChunk.y).addEntity(this.entity.getWorldID(), this.entity);
            currentChunk.setPosition(tempChunk.x, tempChunk.y);
        }
    }

    public void addPosition(float x, float y) {
        this.position.add(x, y);
    }

    public void addPosition(Vector2 position) {
        this.position.add(position);
    }

    public Vector2 getSize() { return size; }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getCenter() {
        return new Vector2().mulAdd(size, 0.5f);
    }

    public Vector2 getCenterPosition() { return new Vector2(position).mulAdd(size, 0.5f); }

    public ChunkPosition getChunkPosition() {
        return currentChunk;
    }

    public int getChunkID() {
        return tempChunk.x + EntityManager.instance.chunkSize.x * tempChunk.y;
    }

    public float getWidth() { return size.x; }

    public float getHeight() { return size.y; }

    public float getX() { return position.x; }

    public float getY() { return position.y; }

    @Override
    public void send(LineWriter writer) {
        writer.writeVector2(position);
    }

    @Override
    public void receive(LineSplitter splitter) {
        position = splitter.getNextVector2();
    }

}
