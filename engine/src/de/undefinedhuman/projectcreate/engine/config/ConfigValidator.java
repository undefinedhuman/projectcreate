package de.undefinedhuman.projectcreate.engine.config;

import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.validation.ValidationRule;
import de.undefinedhuman.projectcreate.engine.validation.Validator;

public class ConfigValidator<T extends Config> extends Validator<T> {

    @SafeVarargs
    public ConfigValidator(ValidationRule<T>... rules) {
        super(rules);
    }

    public void validate(T t) {
        for(ValidationRule<T> rule : rules) {
            if(rule.isValid(t))
                continue;
            Log.error(rule.getErrorMessage(t));
            if(rule.shouldCrash())
                Log.crash();
            rule.correct(t);
        }
    }

}
