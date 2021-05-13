package de.undefinedhuman.projectcreate.updater.config;

import de.undefinedhuman.projectcreate.engine.config.ConfigValidator;
import de.undefinedhuman.projectcreate.engine.file.FileError;
import de.undefinedhuman.projectcreate.engine.validation.ValidationRule;
import de.undefinedhuman.projectcreate.updater.Updater;
import de.undefinedhuman.projectcreate.updater.utils.InstallationUtils;

public class UpdaterConfigValidator extends ConfigValidator<UpdaterConfig> {

    public UpdaterConfigValidator() {
        super(
                new ValidationRule<>(
                        config -> !FileError.checkFileForErrors("checking installation directory", config.installationPath.getValue(), FileError.NULL, FileError.NO_DIRECTORY, FileError.NOT_WRITEABLE),
                        "Error validating the installation directory!",
                        config -> config.installationPath.setValue(InstallationUtils.chooseInstallationDirectory(Updater.DEFAULT_INSTALLATION_DIRECTORY))
                )
        );
    }

}
