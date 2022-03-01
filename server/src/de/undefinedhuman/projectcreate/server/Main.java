package de.undefinedhuman.projectcreate.server;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.gl.HeadlessApplicationAdapter;
import de.undefinedhuman.projectcreate.engine.log.Level;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.server.network.ServerEncryption;

import java.util.Locale;
import java.util.regex.Pattern;

public class Main {

    public static float delta;

    private static final Pattern LOG_LEVEL_REGEX = Pattern.compile("(debug)|(info)|(error)|(warn)|(none)", Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        config.updatesPerSecond = Variables.SERVER_TICK_RATE;
        Paths.getInstance(args.length >= 1 ? args[0] : ".", args.length >= 2 ? Files.FileType.valueOf(args[1]) : Files.FileType.Local);
        Variables.LOG_LEVEL = args.length >= 3 && LOG_LEVEL_REGEX.matcher(args[2]).matches() ? Level.valueOf(args[2].toUpperCase(Locale.ROOT)) : Level.INFO;
        Variables.DONT_LOAD_TEXTURES = true;
        ServerEncryption.getInstance();
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
