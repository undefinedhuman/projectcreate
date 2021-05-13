package de.undefinedhuman.projectcreate.updater.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.FilePathSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.BooleanSetting;
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
                public String chooseFilePath(FsFile defaultFile) {
                    return InstallationUtils.chooseInstallationDirectory(defaultFile);
                }
            };
    public Setting<Version>
            version = new Setting<>("version", new Version(Stage.INDEV, 0, 0, 0, 0).toString(), value -> Version.parse(String.valueOf(value)));

    private UpdaterConfig() {
        super("updater");
        if(instance == null)
            instance = this;
        addSettings(installationPath, version, firstRun);
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
