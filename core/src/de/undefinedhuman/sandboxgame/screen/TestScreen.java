package de.undefinedhuman.sandboxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.ConstraintFactory;

public class TestScreen implements Screen {

    public static TestScreen instance;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    @Override
    public void show() {

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        batch = new SpriteBatch();

        Gdx.graphics.setResizable(true);

        GuiManager.instance = new GuiManager();
        GuiManager.instance.init();

        GuiManager.instance.addGui(new Gui(new GuiTexture(GuiTemplate.SMALL_PANEL), ConstraintFactory.setRelativeConstraints(0.1f,0.1f,0.1f,0.1f)).setGuiScale(0.5f));

    }

    @Override
    public void resize(int width, int height) {

        camera.setToOrtho(false, width, height);
        viewport.update(width, height);
        camera.update();

        GuiManager.instance.resize(width, height);

    }

    @Override
    public void render(float delta) {

        GuiManager.instance.update(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        GuiManager.instance.render(batch, camera);
        batch.end();

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

}
