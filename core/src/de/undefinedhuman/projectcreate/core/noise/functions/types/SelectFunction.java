package de.undefinedhuman.projectcreate.core.noise.functions.types;

import de.undefinedhuman.projectcreate.core.noise.functions.BaseFunction;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;

public class SelectFunction extends BaseFunction {

    private FloatSetting
            low = new FloatSetting("Lower Bound", 0f),
            upper = new FloatSetting("Upper Bound", 1f),
            threshold = new FloatSetting("Threshold", 0f);

    public SelectFunction() {
        super("SelectFunction");
        settings.addSettings(low, upper, threshold);
    }

    @Override
    public double calculateValue(double x, double y, double value) {
        return value >= threshold.getValue() ? upper.getValue() : low.getValue();
    }

}
