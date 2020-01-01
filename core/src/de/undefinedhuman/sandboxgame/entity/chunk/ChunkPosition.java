package de.undefinedhuman.sandboxgame.entity.chunk;

public class ChunkPosition {

    public int x = 0, y = 0;

    public ChunkPosition() { }

    public ChunkPosition(int x, int y) {
        this.x = x; this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
