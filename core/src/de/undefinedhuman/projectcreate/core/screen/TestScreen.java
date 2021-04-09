package de.undefinedhuman.projectcreate.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.undefinedhuman.projectcreate.core.Main;
import de.undefinedhuman.projectcreate.core.entity.Entity;
import de.undefinedhuman.projectcreate.core.entity.EntityManager;
import de.undefinedhuman.projectcreate.core.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.core.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.core.screen.gamescreen.GameScreen;
import de.undefinedhuman.projectcreate.core.world.WorldGenerator;
import de.undefinedhuman.projectcreate.core.world.settings.BiomeSetting;
import de.undefinedhuman.projectcreate.core.world.settings.WorldSetting;

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
        EntityManager.getInstance().init();

        Entity player = BlueprintManager.getInstance().getBlueprint(0).createInstance();
        player.mainPlayer = true;
        player.setPosition(160, 800);
        player.setWorldID(0);
        GameManager.instance.player = player;
        EntityManager.getInstance().addEntity(0, player);

        Entity furnace = BlueprintManager.getInstance().getBlueprint(1).createInstance();
        furnace.setPosition(3100, 800);
        furnace.setWorldID(1);
        EntityManager.getInstance().addEntity(1, furnace);

        Entity furnace2 = BlueprintManager.getInstance().getBlueprint(1).createInstance();
        furnace2.setPosition(100, 800);
        furnace2.setWorldID(2);
        EntityManager.getInstance().addEntity(2, furnace2);

        Main.instance.setScreen(GameScreen.instance);

        /*ClientManager.instance.connect();

        LoginPacket packet = new LoginPacket();
        packet.name = "GentleXD " + new Random().nextInt(100);
        ClientManager.instance.sendTCP(packet);*/

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
