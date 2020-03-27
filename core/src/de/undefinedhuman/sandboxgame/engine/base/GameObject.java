package de.undefinedhuman.sandboxgame.engine.base;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.entity.ecs.components.transform.Transform;

public abstract class GameObject extends Transform {

    public GameObject(Vector2 size) {
        super(size);
    }

    public abstract void init();
    public abstract void resize(int width, int height);
    public abstract void update();
    public abstract void render(SpriteBatch batch, OrthographicCamera camera);
    public abstract void renderUI(SpriteBatch batch, OrthographicCamera camera);
    public abstract void delete();

}
