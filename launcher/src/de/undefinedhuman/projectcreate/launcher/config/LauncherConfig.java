package de.undefinedhuman.projectcreate.launcher.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.FilePathSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.SliderSetting;
import de.undefinedhuman.projectcreate.launcher.Launcher;
import de.undefinedhuman.projectcreate.launcher.utils.Tools;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

public class LauncherConfig extends Config {

    public static LauncherConfig instance;

    public Setting
            gameInstallationPath = new FilePathSetting("gameInstallationPath", Launcher.DEFAULT_INSTALLATION_DIRECTORY) {
                @Override
                public String chooseFilePath(FsFile defaultFile) {
                    return InstallationUtils.chooseInstallationDirectory(defaultFile);
                }
            },
            includeSnapshots = new BooleanSetting("Include Snapshots", false),
            maximumMemory = new SliderSetting("Maximum Game Memory (Xmx)", 0, Tools.AVAILABLE_MAX_MEMORY_IN_MB_HALVED, 0, 500, 1000f),
            initialMemory = new SliderSetting("Initial Game Memory (Xms)", 0, Tools.AVAILABLE_MAX_MEMORY_IN_MB_HALVED, 0, 500, 1000f);

    private LauncherConfigValidator validator;

    public LauncherConfig() {
        super("launcher");
        if(instance == null)
            instance = this;
        addSettings(gameInstallationPath, includeSnapshots, maximumMemory, initialMemory);
        validator = new LauncherConfigValidator();
    }

    @Override
    public void validate() {
        validator.validate(this);
    }
}
