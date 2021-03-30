package de.undefinedhuman.projectcreate.launcher.config;

import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;

public class LauncherConfig {

    public static final String PROPERTY_MAX_HEAP_SIZE = "maxHeapSize";

    public Setting
            gameInstallationPath = new Setting(SettingType.String, "gameInstallationPath", Paths.GAME_PATH + "versions/");

    protected void initMaxHeapSize() {
        final String maxHeapSizeStr = properties.getProperty(PROPERTY_MAX_HEAP_SIZE);
        JavaHeapSize maxJavaHeapSize = MAX_HEAP_SIZE_DEFAULT;
        if (maxHeapSizeStr != null) {
            try {
                maxJavaHeapSize = JavaHeapSize.valueOf(maxHeapSizeStr);
            } catch (IllegalArgumentException e) {
                logger.warn(WARN_MSG_INVALID_VALUE, maxHeapSizeStr, PROPERTY_MAX_HEAP_SIZE);
            }
        }
        properties.setProperty(PROPERTY_MAX_HEAP_SIZE, maxJavaHeapSize.name());
    }

    protected void initInitialHeapSize() {
        final String initialHeapSizeStr = properties.getProperty(PROPERTY_INITIAL_HEAP_SIZE);
        JavaHeapSize initialJavaHeapSize = INITIAL_HEAP_SIZE_DEFAULT;
        if (initialHeapSizeStr != null) {
            try {
                initialJavaHeapSize = JavaHeapSize.valueOf(initialHeapSizeStr);
            } catch (IllegalArgumentException e) {
                logger.warn(WARN_MSG_INVALID_VALUE, initialHeapSizeStr, PROPERTY_INITIAL_HEAP_SIZE);
            }
        }
        properties.setProperty(PROPERTY_INITIAL_HEAP_SIZE, initialJavaHeapSize.name());
    }

}
