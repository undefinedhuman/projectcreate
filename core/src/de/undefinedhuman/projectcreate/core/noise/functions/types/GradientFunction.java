package de.undefinedhuman.projectcreate.core.noise.functions.types;

import de.undefinedhuman.projectcreate.core.noise.functions.BaseFunction;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class GradientFunction extends BaseFunction {

    public IntSetting
            height = new IntSetting("Height", 1, 1, Integer.MAX_VALUE);

    public GradientFunction() {
        super("Gradient");
        settings.addSettings(height);
    }

    @Override
    public double calculateValue(double x, double y, double value) {
        return linInterpolation(0, 1, y / height.getValue()) * value;
    }

    public double linInterpolation(double a, double b, double v) {
        return (a * (b - v) + b * v);
    }

}
