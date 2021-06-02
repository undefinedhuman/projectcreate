package de.undefinedhuman.projectcreate.core.noise.functions;

import de.undefinedhuman.projectcreate.engine.settings.types.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.slider.SliderSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.slider.SliderSettingDirector;

public class FractalFunction extends BaseFunction {

    public SelectionSetting<FractalType>
            fractalType = new SelectionSetting<>("Fractal Type", FractalType.values(), value -> FractalType.valueOf(String.valueOf(value)));

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
            octaves = SliderSettingDirector.createIntegerSlider(SliderSetting.newInstance("Octaves"))
                    .with(builder -> {
                        builder.bounds.set(1, 10);
                        builder.defaultValue = 3;
                    })
                    .build(),
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
            gain = SliderSettingDirector.createFloatSlider(SliderSetting.newInstance("Gain"))
                    .with(builder -> {
                        builder.defaultValue = 50;
                    })
                    .build();

    public FractalFunction() {
        super("Fractal");
        settings.addSettings(fractalType, frequency, octaves, lacunarity, amplitudeFactor, gain);
    }

    @Override
    public double calculateValue(double x, double y, double value) {
        float fractalValue = 0;
        switch (fractalType.getValue()) {
            default:
            case FBM:
                float amplitude = calculateFractalBounding();
                x *= frequency.getValue();
                y *= frequency.getValue();
                for(int i = 0; i < octaves.getValue(); i++) {
                    double noise = 0.0; // noiseGenerator.noise2(x, y);
                    fractalValue += noise * amplitude;
                    amplitude *= lerp(1f, (noise + 1) * 0.5f, amplitudeFactor.getValue());

                    x *= lacunarity.getValue();
                    y *= lacunarity.getValue();
                    amplitude *= gain.getValue();
                }
                return value + fractalValue;
        }
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
