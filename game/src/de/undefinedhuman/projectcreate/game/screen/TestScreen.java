package de.undefinedhuman.projectcreate.game.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameScreen;
import de.undefinedhuman.projectcreate.game.world.WorldGenerator;
import de.undefinedhuman.projectcreate.game.world.settings.BiomeSetting;
import de.undefinedhuman.projectcreate.game.world.settings.WorldSetting;

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

        WorldGenerator.instance = new WorldGenerator();
        WorldGenerator.instance.generateTestWorld("Main", WorldSetting.DEV, BiomeSetting.DEV);

        Entity player = BlueprintManager.getInstance().getBlueprint(0).createInstance();
        player.getComponent(TransformComponent.class).setPosition(160, 800);
        player.flags = 0;
        GameManager.instance.player = player;
        GameManager.instance.getEngine().addEntity(player);

        Entity furnace = BlueprintManager.getInstance().getBlueprint(1).createInstance();
        furnace.getComponent(TransformComponent.class).setPosition(3100, 800);
        furnace.flags = 1;
        GameManager.instance.getEngine().addEntity(furnace);

        Entity furnace2 = BlueprintManager.getInstance().getBlueprint(1).createInstance();
        furnace2.getComponent(TransformComponent.class).setPosition(100, 800);
        furnace2.flags = 2;
        GameManager.instance.getEngine().addEntity(furnace2);

        Main.instance.setScreen(GameScreen.instance);

        /*ClientManager.getInstance().connect();

        LoginPacket packet = new LoginPacket();
        packet.name = "GentleXD " + new Random().nextInt(100);
        ClientManager.getInstance().sendTCP(packet);*/

        Gdx.graphics.setResizable(true);

    }

    @Override
    public void render(float delta) {

        clear();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

        camera.setToOrtho(false, width, height);
        viewport.update(width, height);
        camera.update();

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    private void clear() {

        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

    }

}
