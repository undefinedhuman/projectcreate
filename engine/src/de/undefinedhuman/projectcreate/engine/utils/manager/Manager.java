package de.undefinedhuman.projectcreate.engine.utils.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Manager {
    public void init() {}
    public void resize(int width, int height) {}
    public void update(float delta) {}
    public void render(SpriteBatch batch, OrthographicCamera camera) {}
    public void renderGui(SpriteBatch batch, OrthographicCamera camera) {}
    public void delete() {}
}
