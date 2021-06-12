package de.undefinedhuman.projectcreate.launcher.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.FilePathSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.slider.SliderSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.utils.Stage;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.launcher.Launcher;
import de.undefinedhuman.projectcreate.launcher.utils.Tools;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

public class LauncherConfig extends Config {

    private static LauncherConfig instance;

    public FilePathSetting
            gameInstallationPath = new FilePathSetting("Game installation path", Launcher.DEFAULT_INSTALLATION_DIRECTORY) {
                @Override
                public FsFile chooseFilePath(FsFile defaultFile) {
                    return InstallationUtils.chooseInstallationDirectory(defaultFile);
                }
            };
    public BooleanSetting
            includeSnapshots = new BooleanSetting("Include Snapshots", false),
            closeLauncherAfterGameStart = new BooleanSetting("Close Launcher", true);
    public SliderSetting
        maximumMemory = new SliderSetting("Xmx", 0, Tools.AVAILABLE_MAX_MEMORY_IN_MB_HALVED, 0, 500, 1000f),
        initialMemory = new SliderSetting("Xms", 0, Tools.AVAILABLE_MAX_MEMORY_IN_MB_HALVED, 0, 500, 1000f);
    public Setting<Version>
            lastPlayedGameVersion = new Setting<>("lastPlayedVersion", new Version(Stage.INDEV, 0, 0, 0, 0).toString(), value -> Version.parse(String.valueOf(value)));

    private LauncherConfigValidator validator;

    private LauncherConfig() {
        super("launcher");
        addSettings(gameInstallationPath, includeSnapshots, maximumMemory, initialMemory, closeLauncherAfterGameStart, lastPlayedGameVersion);
        validator = new LauncherConfigValidator();
    }


    @Override
    public void validate() {
        validator.validate(this);
    }

    public static LauncherConfig getInstance() {
        if (instance == null) {
            synchronized (LauncherConfig.class) {
                if (instance == null)
                    instance = new LauncherConfig();
            }
        }
        return instance;
    }

}
