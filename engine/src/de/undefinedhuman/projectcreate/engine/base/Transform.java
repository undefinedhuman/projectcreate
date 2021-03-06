package de.undefinedhuman.projectcreate.engine.base;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;

public class Transform implements NetworkSerializable {

    protected Vector2 position = new Vector2();
    protected Vector2 size = new Vector2();

    public Transform() {}

    public Transform(Vector2 size) {
        this.size.set(size);
    }

    public Transform(Transform transform) {
        this.position.set(transform.position);
        this.size.set(transform.size);
    }

    public Vector2 getPosition() {
        return new Vector2(position);
    }
    public void setPosition(Vector2 position) {
        setPosition(position.x, position.y);
    }
    public void setPosition(float x, float y) { this.position.set(x, y); }
    public void addPosition(float x, float y) {
        this.position.add(x, y);
    }
    public void addPosition(Vector2 position) {
        this.addPosition(position.x, position.y);
    }
    public Vector2 getSize() { return size; }
    public void setSize(Vector2 size) {
        this.size = size;
    }
    public void setSize(float width, float height) {
        this.size.set(width, height);
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
    public void serialize(LineWriter writer) {
        writer.writeVector2(position);
    }

    @Override
    public void parse(LineSplitter splitter) {
        position = splitter.getNextVector2();
    }

    @Override
    public String toString() {
        return "(X: " + position.x + ", Y: " + position.y + "), (WIDTH: " + size.x + ", HEIGHT: " + size.y + ")";
    }

}
