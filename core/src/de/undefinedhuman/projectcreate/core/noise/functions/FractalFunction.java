package de.undefinedhuman.projectcreate.core.noise.functions;

import de.undefinedhuman.projectcreate.engine.settings.types.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class FractalFunction extends BaseFunction {

    public SelectionSetting<FractalType> fractalType = new SelectionSetting<>("Fractal Type", FractalType.values(), value -> FractalType.valueOf(String.valueOf(value)));
    public IntSetting octaves = new IntSetting("Octaves", 0);
    /*private SliderSetting
            frequency = new SliderSetting(), octaves, lacunarity, amplitudeFactor, gain;
    public FloatSetting
            frequency = new FloatSetting("Frequency", 0f),
            lacunarity = new FloatSetting("Lacunarity", 0f),
            gain = new FloatSetting("Gain", 0f),
            amplitudeFactor = new FloatSetting("Amplitude Factor", 0f);

    private float frequency = 0.01f;
    private float lacunarity = 2f;
    private float gain = 0.5f;
    private float amplitudeFactor = 0;*/

    @Override
    public double calculateValue(double x, double y, double value) {

        return 0;
    }

}
