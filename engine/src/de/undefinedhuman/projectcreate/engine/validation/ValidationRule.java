package de.undefinedhuman.projectcreate.engine.validation;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ValidationRule<T> {

    private final Predicate<T> condition;
    private final String errorMessage = "NOT_IMPLEMENTED";
    private final Function<T, String> errorFunction;
    private final Consumer<T> correction;
    private final boolean crashOnError;

    public ValidationRule(Predicate<T> condition, String errorMessage) {
        this(condition, errorMessage, null);
    }

    public ValidationRule(Predicate<T> condition, String errorMessage, Consumer<T> correction) {
        this(condition, t -> errorMessage, correction, false);
    }

    public ValidationRule(Predicate<T> condition, Function<T, String> errorFunction, Consumer<T> correction) {
        this(condition, errorFunction, correction, false);
    }

    public ValidationRule(Predicate<T> condition, Function<T, String> errorFunction, Consumer<T> correction, boolean crashOnError) {
        this.condition = condition;
        this.errorFunction = errorFunction;
        this.correction = correction;
        this.crashOnError = crashOnError;
    }

    public boolean isValid(T t) {
        return condition.test(t);
    }

    public String getErrorMessage(T t) {
        return errorFunction.apply(t);
    }

    public void correct(T t) {
        correction.accept(t);
    }

    public boolean shouldCrash() {
        return crashOnError;
    }

}
