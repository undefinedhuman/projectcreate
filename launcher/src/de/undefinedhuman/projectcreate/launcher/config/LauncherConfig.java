package de.undefinedhuman.projectcreate.launcher.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.types.BaseSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.FilePathSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.slider.SliderSetting;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Stage;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.launcher.Launcher;
import de.undefinedhuman.projectcreate.launcher.utils.Tools;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

public class LauncherConfig extends Config
{
    private static LauncherConfigValidator VALIDATOR = new LauncherConfigValidator();

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
            maximumMemory = SliderSetting.newInstance("Xmx").with(
                    builder -> {
                        builder.defaultValue = 0;
                        builder.bounds.set(0, Tools.AVAILABLE_MAX_MEMORY_IN_MB_HALVED);
                        builder.scale = 1000f;
                        builder.tickSpeed = 500;
                    }
            ).build(),
            initialMemory = SliderSetting.newInstance("Xms").with(
                    builder -> {
                        builder.tickSpeed = 500;
                        builder.bounds.set(0, Tools.AVAILABLE_MAX_MEMORY_IN_MB_HALVED);
                        builder.defaultValue = 0;
                        builder.scale = 1000f;
                    }
            ).build();
    public BaseSetting<Version>
            lastPlayedGameVersion = new BaseSetting<Version>("lastPlayedVersion", new Version(Stage.INDEV, 0, 0, 0, 0), value -> Version.parse(String.valueOf(value)), Version::toString) {
        @Override
        public void createSettingUI(Accordion accordion) {}

        @Override
        protected void updateMenu(Version value) {}
    };


    private LauncherConfig() {
        super("launcher");
        addSettings(gameInstallationPath, includeSnapshots,closeLauncherAfterGameStart);
    }


    @Override
    public void validate() {
        VALIDATOR.validate(this);
    }

    public static LauncherConfig getInstance() {
        if(instance != null)
            return instance;
        synchronized (LauncherConfig.class) {
            if (instance == null)
                instance = new LauncherConfig();
        }
        return instance;
    }

}
