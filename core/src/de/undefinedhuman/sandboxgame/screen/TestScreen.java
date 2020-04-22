package de.undefinedhuman.sandboxgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameScreen;
import de.undefinedhuman.sandboxgame.world.World;
import de.undefinedhuman.sandboxgame.world.WorldGenerator;
import de.undefinedhuman.sandboxgame.world.settings.BiomeSetting;
import de.undefinedhuman.sandboxgame.world.settings.WorldPreset;
import de.undefinedhuman.sandboxgame.world.settings.WorldSetting;

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

        World.instance = WorldGenerator.instance.generateTestWorld(new WorldPreset("Main", WorldSetting.DEV, BiomeSetting.DEV));
        EntityManager.instance.init();

        Entity player = BlueprintManager.instance.getBlueprint(0).createInstance();
        player.mainPlayer = true;
        player.setPosition(1600, 800);
        player.setWorldID(0);
        GameManager.instance.player = player;
        EntityManager.instance.addEntity(0, player);

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
