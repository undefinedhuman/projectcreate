package de.undefinedhuman.projectcreate.server.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

public class ServerConfig extends Config {

    private static volatile ServerConfig instance;

    public IntSetting
            displayWidth = new IntSetting("displayWidth", 1280),
            displayHeight = new IntSetting("displayHeight", 720),
            maxFps = new IntSetting("maxFPS", 0),
            guiScale = new IntSetting("guiScale", 5);
    public BooleanSetting
            vSync = new BooleanSetting("vSync", false),
            fullScreen = new BooleanSetting("fullScreen", false),
            renderHitboxes = new BooleanSetting("renderHitboxes", false),
            firstRun = new BooleanSetting("firstRun", false);
    public StringSetting
            language = new StringSetting("language", "eu_DE");

    private ServerConfig() {
        super("server");
        addSettings(displayWidth, displayHeight, vSync, maxFps, fullScreen, language, renderHitboxes, firstRun, guiScale);
    }

    public static ServerConfig getInstance() {
        if(instance != null)
            return instance;
        if (instance == null) {
            synchronized (ServerConfig.class) {
                if (instance == null)
                    instance = new ServerConfig();
            }
        }
        return instance;
    }

    @Override
    public void validate() {}

    @Override
    public void validate() {}
}
