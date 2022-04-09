package de.undefinedhuman.projectcreate.kamino.config;

import de.undefinedhuman.projectcreate.engine.config.ConfigValidator;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.validation.ValidationRule;

public class KaminoConfigValidator extends ConfigValidator<KaminoConfig> {

    public KaminoConfigValidator() {
        super(
                new ValidationRule<>(
                        kaminoConfig -> Utils.isInRange(kaminoConfig.numberOfThreads.getValue(), KaminoConfig.DEFAULT_NUMBER_OF_THREADS, Integer.MAX_VALUE),
                        "Number of threads need to be in range [1, " + Integer.MAX_VALUE + "], automatically set back to " + KaminoConfig.DEFAULT_NUMBER_OF_THREADS,
                        kaminoConfig -> kaminoConfig.numberOfThreads.setValue(KaminoConfig.DEFAULT_NUMBER_OF_THREADS)
                )
        );
    }

}
