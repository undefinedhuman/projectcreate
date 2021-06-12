package de.undefinedhuman.projectcreate.core.noise.functions.types;

import de.undefinedhuman.projectcreate.core.noise.functions.BaseFunction;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.FloatSetting;

public class ScaleFunction extends BaseFunction {

    public FloatSetting
            scaleSetting = new FloatSetting("Scale", 0.5f),
            offsetSetting = new FloatSetting("Offset", 0.5f);

    public ScaleFunction() {
        super("Scale Function");
        settings.addSettings(scaleSetting, offsetSetting);
    }

    @Override
    public double calculateValue(double x, double y, double value) {
        return scaleSetting.getValue() * value + offsetSetting.getValue();
    }
}
