package de.undefinedhuman.projectcreate.updater.config;

import de.undefinedhuman.projectcreate.engine.config.Config;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingType;
import de.undefinedhuman.projectcreate.engine.settings.types.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.FilePathSetting;
import de.undefinedhuman.projectcreate.engine.utils.Version;
import de.undefinedhuman.projectcreate.updater.Updater;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

public class UpdaterConfig extends Config {

    public static UpdaterConfig instance;

    public Setting
            firstRun = new BooleanSetting("firstRun", true),
            installationPath = new FilePathSetting("installationPath", Updater.DEFAULT_INSTALLATION_DIRECTORY) {
                @Override
                public String chooseFilePath(FsFile defaultFile) {
                    return InstallationUtils.chooseInstallationDirectory(defaultFile);
                }
            },
            version = new Setting(SettingType.Version, "version", new Version(0, 0, 0).toString());

    public UpdaterConfig() {
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
}