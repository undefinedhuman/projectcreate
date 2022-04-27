package de.undefinedhuman.projectcreate.launcher.config;

import de.undefinedhuman.projectcreate.engine.config.ConfigValidator;
import de.undefinedhuman.projectcreate.engine.validation.ValidationRule;

public class LauncherConfigValidator extends ConfigValidator<LauncherConfig> {

    public LauncherConfigValidator() {
        super(
                new ValidationRule<>(
                        l -> !(System.getProperty("os.arch").equals("x86") && l.maximumMemory.getValue() > 1.5f),
                        "Maximum heap size cannot exceed 1.5Gb for a 32-bit JVM.",
                        l -> l.maximumMemory.setValue(1.5f)
                ),
                new ValidationRule<>(
                        l -> l.initialMemory.getValue() <= l.maximumMemory.getValue(),
                        "Initial heap size cannot be larger then the maximum heap size!",
                        l -> l.initialMemory.setValue(l.maximumMemory.getValue())
                )
        );
    }

}
