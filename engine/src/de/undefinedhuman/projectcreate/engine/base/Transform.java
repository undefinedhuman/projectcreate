package de.undefinedhuman.projectcreate.engine.base;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerialization;

public class Transform implements NetworkSerialization {

    protected Vector2 position = new Vector2(), size = new Vector2();

    public Transform() {}

    public Transform(Vector2 size) {
        this.size.set(size);
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        setPosition(position.x, position.y);
    }
    public void setPosition(float x, float y) { this.position.set(x, y); }
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
