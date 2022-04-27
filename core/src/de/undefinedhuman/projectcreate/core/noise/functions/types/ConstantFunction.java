package de.undefinedhuman.projectcreate.core.noise.functions.types;

import de.undefinedhuman.projectcreate.core.noise.functions.BaseFunction;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;

public class ConstantFunction extends BaseFunction {

    public FloatSetting value = new FloatSetting("Value", 0f);

    public ConstantFunction() {
        super("Constant");
        addSettings(value);
    }

    @Override
    public double calculateValue(double x, double y, double value) {
        return value + this.value.getValue();
    }
}
