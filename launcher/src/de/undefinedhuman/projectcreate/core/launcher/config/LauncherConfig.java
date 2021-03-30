package de.undefinedhuman.projectcreate.core.launcher.config;

import de.undefinedhuman.projectcreate.core.engine.file.Paths;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.core.engine.settings.types.SelectionSetting;

public class LauncherConfig {

    public static final String PROPERTY_MAX_HEAP_SIZE = "maxHeapSize";

    public Setting
            gameInstallationPath = new Setting(SettingType.String, "gameInstallationPath", Paths.GAME_PATH + "versions/"),
            maximalMemory = new SelectionSetting("Maximum Game Memory (Xmx)", new String[0]);

}
