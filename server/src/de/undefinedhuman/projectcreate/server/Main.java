package de.undefinedhuman.projectcreate.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import de.undefinedhuman.projectcreate.engine.gl.HeadlessApplicationAdapter;
import de.undefinedhuman.projectcreate.engine.utils.Variables;

public class Main {

    public static float delta;

    public static void main(String[] args) {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        config.updatesPerSecond = Variables.SERVER_TICK_RATE;
        Variables.DONT_LOAD_TEXTURES = true;
        new HeadlessApplication(new HeadlessApplicationAdapter() {
            @Override
            public void create() {
                ServerManager.getInstance().init();
            }

            @Override
            public void render() {
                delta = Gdx.graphics.getDeltaTime();
                ServerManager.getInstance().update(delta);
            }

            @Override
            public void dispose() {
                ServerManager.getInstance().delete();
            }
        }, config);
    }

}
