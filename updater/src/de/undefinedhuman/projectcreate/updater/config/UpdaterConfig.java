package de.undefinedhuman.projectcreate.updater.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.types.BaseSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.FilePathSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.ui.accordion.Accordion;
import de.undefinedhuman.projectcreate.engine.utils.Stage;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.updater.Updater;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

public class UpdaterConfig extends Config {

    private static volatile UpdaterConfig instance;

    public BooleanSetting
            firstRun = new BooleanSetting("firstRun", true);
    public FilePathSetting
            installationPath = new FilePathSetting("installationPath", Updater.DEFAULT_INSTALLATION_DIRECTORY) {
                @Override
                public FsFile chooseFilePath(FsFile defaultFile) {
                    return InstallationUtils.chooseInstallationDirectory(defaultFile);
                }
            };
    public BaseSetting<Version> version = new BaseSetting<Version>("version", new Version(Stage.INDEV, 0, 0, 0, 0), value -> Version.parse(String.valueOf(value)), Version::toString) {
        @Override
        public void createSettingUI(Accordion accordion) {}

        @Override
        protected void updateMenu(Version value) {}
    };

    private UpdaterConfig() {
        super("updater");
        if(instance == null)
            instance = this;
        addSettings(installationPath, firstRun);
    }

    @Override
    public void save() {
        firstRun.setValue(false);
        super.save();
    }

    @Override
    public void validate() {}

    public static UpdaterConfig getInstance() {
        if (instance == null) {
            synchronized (UpdaterConfig.class) {
                if (instance == null)
                    instance = new UpdaterConfig();
            }
        }
        return instance;
    }
}
