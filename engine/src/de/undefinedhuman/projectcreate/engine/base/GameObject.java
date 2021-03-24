package de.undefinedhuman.projectcreate.engine.base;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameObject extends Transform {

    public GameObject() {
        super();
    }

    public GameObject(Vector2 size) {
        super(size);
    }

    public void init() {}
    public void resize(int width, int height) {}
    public void update(float delta) {}
    public void render(SpriteBatch batch, OrthographicCamera camera) {}
    public void delete() {}

}
