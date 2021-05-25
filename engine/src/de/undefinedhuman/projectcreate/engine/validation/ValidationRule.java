package de.undefinedhuman.projectcreate.engine.validation;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ValidationRule<T> {

    private Predicate<T> condition;
    private String errorMessage = "NOT_IMPLEMENTED";
    private Function<T, String> errorFunction;
    private Consumer<T> correction;

    public ValidationRule(Predicate<T> condition, String errorMessage, Consumer<T> correction) {
        this.condition = condition;
        this.errorMessage = errorMessage;
        this.errorFunction = null;
        this.correction = correction;
    }

    public ValidationRule(Predicate<T> condition, Function<T, String> errorFunction, Consumer<T> correction) {
        this.condition = condition;
        this.errorFunction = errorFunction;
        this.correction = correction;
    }

    public boolean isValid(T t) {
        return condition.test(t);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorMessage(T t) {
        if(errorFunction == null)
            return errorMessage;
        return errorFunction.apply(t);
    }

    public void correct(T t) {
        correction.accept(t);
    }

}
