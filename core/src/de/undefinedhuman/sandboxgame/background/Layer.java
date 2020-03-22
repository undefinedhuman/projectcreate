package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Layer {

    public abstract void init();
    public abstract void resize(int width, int height);
    public abstract void update(float delta, float speed);
    public abstract void render(SpriteBatch batch, OrthographicCamera camera);
    public abstract void delete();

}
