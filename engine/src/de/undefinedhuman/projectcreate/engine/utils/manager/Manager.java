package de.undefinedhuman.projectcreate.engine.utils.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Manager {
    default void init() {}
    default void resize(int width, int height) {}
    default void update(float delta) {}
    default void render(SpriteBatch batch, OrthographicCamera camera) {}
    default void renderGui(SpriteBatch batch, OrthographicCamera camera) {}
    default void delete() {}
}
