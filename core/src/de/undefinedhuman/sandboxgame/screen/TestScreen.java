package de.undefinedhuman.sandboxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.GuiComponent;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.gui.elements.Slider;
import de.undefinedhuman.sandboxgame.gui.event.ChangeEvent;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTexture;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.Constraints;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;

public class TestScreen implements Screen {

    public static TestScreen instance;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    private Gui gui;
    private GuiComponent text;

    @Override
    public void show() {

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        batch = new SpriteBatch();

        Gdx.graphics.setResizable(true);

        GuiManager.instance = new GuiManager();
        GuiManager.instance.init();

        Slider slider = new Slider(new GuiTexture(GuiTemplate.SCROLL_PANEL),"gui/sound bar.png","gui/pointer.png",true);
        slider.setConstraints(new Constraints().set(new PixelConstraint(25), new RelativeConstraint(0.5f), new PixelConstraint(128), new PixelConstraint(4)).setCenteredY());
        GuiComponent textSlider = new Text("1.0f").setColor(Color.ORANGE).setConstraints(new Constraints().setCenteredY().setPosition(new RelativeConstraint(1.075f), new RelativeConstraint(0.5f)));
        slider.addChild(textSlider);
        slider.addChangeListener(new ChangeEvent() {
            @Override
            public void notify(float progress) {
                ((Text) textSlider).setText((int) (progress * 100));
            }
        });
        GuiManager.instance.addGui(slider);

        gui = new Gui(new GuiTexture(GuiTemplate.SMALL_PANEL));
        gui.setScale(0.5f);
        Constraints constraints = new Constraints().set(new RelativeConstraint(0.5f), new PixelConstraint(150), new PixelConstraint(128), new PixelConstraint(128)).setCenteredX();
        gui.setConstraints(constraints);
        text = new Text("Hallo").setConstraints(new Constraints()
                .setPosition(new RelativeConstraint(0.5f), new RelativeConstraint(0.5f)).setCentered());
        gui.addChild(text);
        GuiManager.instance.addGui(gui);

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

        ((Text) text).setText("Hallo");

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
