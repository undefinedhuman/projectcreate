package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.utils.Manager;
import de.undefinedhuman.sandboxgame.utils.Variables;

public class BackgroundManager extends Manager {

    public static BackgroundManager instance;

    private BackgroundLayer background;

    public BackgroundManager() {
        if (instance == null) instance = this;
        background = new BackgroundLayer(new Vector2(320, 180));
    }

    @Override
    public void init() {
        super.init();
        Time.load();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background.resize(width, height);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        background.update(delta, Variables.HOUR_LENGTH);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        super.render(batch, camera);
        background.render(batch, camera);
    }

    @Override
    public void delete() {
        super.delete();
        Time.delete();
    }

}
