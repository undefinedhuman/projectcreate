package de.undefinedhuman.projectcreate.core.noise.functions.fractal;

import de.undefinedhuman.projectcreate.core.noise.functions.BaseFunction;
import de.undefinedhuman.projectcreate.core.noise.functions.NoiseType;
import de.undefinedhuman.projectcreate.core.noise.generator.NoiseGenerator;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.slider.SliderSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.slider.SliderSettingDirector;

public class FractalFunction extends BaseFunction {

    private NoiseGenerator noiseGenerator = NoiseType.values()[0].newInstance(1337);

    public SelectionSetting<NoiseType>
            noiseType = (SelectionSetting<NoiseType>) new SelectionSetting<>("Noise Type", NoiseType.values(), NoiseType::valueOf, Enum::name)
            .addValueListener(value -> {
                noiseGenerator = value.newInstance(1337);
                if(noiseGenerator == null)
                    NoiseType.OPEN_SIMPLEX_2_SMOOTH.newInstance(1337);
            });

    public SelectionSetting<FractalType>
            fractalType = new SelectionSetting<>("Fractal Type", FractalType.values(), value -> FractalType.valueOf(String.valueOf(value)), Enum::name);

    public SliderSetting
            frequency = SliderSetting.newInstance("Frequency")
                    .with(builder -> {
                        builder.bounds.set(10, 1500);
                        builder.defaultValue = 1000;
                        builder.tickSpeed = 5;
                        builder.scale = 100000;
                        builder.numberOfDecimals = 4;
                    })
                    .build(),
            octaves = (SliderSetting) SliderSettingDirector.createIntegerSlider(SliderSetting.newInstance("Octaves"))
                    .with(builder -> {
                        builder.scale = 1f;
                        builder.bounds.set(1, 10);
                        builder.defaultValue = 3;
                    })
                    .build()
                    .addValueListener(value -> fractalBounding = calculateFractalBounding()),
            lacunarity = SliderSettingDirector.createFloatSlider(SliderSetting.newInstance("Lacunarity"))
                    .with(builder -> {
                        builder.bounds.set(0, 400);
                        builder.defaultValue = 200;
                    })
                    .build(),
            amplitudeFactor = SliderSettingDirector.createFloatSlider(SliderSetting.newInstance("Amplitude Factor"))
                    .with(builder -> {
                        builder.bounds.set(0, 100);
                        builder.tickSpeed = 1;
                    })
                    .build(),
            gain = (SliderSetting) SliderSettingDirector.createFloatSlider(SliderSetting.newInstance("Gain"))
                    .with(builder -> builder.defaultValue = 50)
                    .build()
                    .addValueListener(value -> fractalBounding = calculateFractalBounding());

    private float fractalBounding = calculateFractalBounding();

    public FractalFunction() {
        super("Fractal");
        addSettings(noiseType, fractalType, frequency, octaves, lacunarity, gain, amplitudeFactor);
    }

    @Override
    public double calculateValue(double x, double y, double value) {
        float fractalValue = 0;
        float amplitude = fractalBounding;
        x *= frequency.getValue();
        y *= frequency.getValue();
        for(int i = 0; i < octaves.getValue(); i++) {
            double noise = noiseGenerator.noise2(x, y);
            fractalValue += noise * amplitude;
            amplitude *= lerp(1f, (noise + 1) * 0.5f, amplitudeFactor.getValue());

            x *= lacunarity.getValue();
            y *= lacunarity.getValue();
            amplitude *= gain.getValue();
        }
        return value + fractalValue;
    }

    private float calculateFractalBounding() {
        float gainAbs = abs(gain.getValue());
        float amplitude = gainAbs;
        float amplitudeFractal = 1f;
        for (int i = 1; i < octaves.getValue(); i++) {
            amplitudeFractal += amplitude;
            amplitude *= gainAbs;
        }
        return 1 / amplitudeFractal;
    }

    private float abs(float v) {
        return v < 0 ? -v : v;
    }

    private double lerp(float a, double b, float f) {
        return a + f * (b - a);
    }

}
