package de.undefinedhuman.projectcreate.engine.validation;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ValidationRule<T> {

    private Predicate<T> condition;
    private String errorMessage;
    private Consumer<T> correction;

    public ValidationRule(Predicate<T> condition, String errorMessage, Consumer<T> correction) {
        this.condition = condition;
        this.errorMessage = errorMessage;
        this.correction = correction;
    }

    public boolean isValid(T t) {
        return condition.test(t);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void correct(T t) {
        correction.accept(t);
    }

}
