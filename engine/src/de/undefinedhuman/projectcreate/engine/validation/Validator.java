package de.undefinedhuman.projectcreate.engine.validation;

import java.util.Arrays;
import java.util.List;

public abstract class Validator<T> {

    // Test

    protected List<ValidationRule<T>> rules;

    @SafeVarargs
    public Validator(ValidationRule<T>... rules) {
        this.rules = Arrays.asList(rules);
    }

    public abstract void validate(T t);
}
