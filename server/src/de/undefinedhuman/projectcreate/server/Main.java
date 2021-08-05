package de.undefinedhuman.projectcreate.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import de.undefinedhuman.projectcreate.engine.gl.HeadlessApplicationAdapter;

public class Main {

    public static void main(String[] args) {
        new HeadlessApplication(new HeadlessApplicationAdapter() {
            @Override
            public void create() {
                ServerManager.getInstance().init();
            }

            @Override
            public void render() {
                ServerManager.getInstance().update(Gdx.graphics.getDeltaTime());
            }

            @Override
            public void dispose() {
                ServerManager.getInstance().delete();
            }
        }, new HeadlessApplicationConfiguration());
    }

}
