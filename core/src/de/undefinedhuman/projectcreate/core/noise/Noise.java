package de.undefinedhuman.projectcreate.core.noise;

import de.undefinedhuman.projectcreate.core.noise.generator.NoiseGenerator;

public class Noise {

    private int octaves = 8;
    private float lacunarity = 2f;
    private float gain = 0.5f;
    private float frequency = 0.005f;

    // Range [0...1]
    private float amplitudeFactor = 0;
    private float fractalBounding = 1 / 1.75f;

    private NoiseGenerator noiseGenerator;

    public Noise(NoiseGenerator noiseGenerator) {
        this.noiseGenerator = noiseGenerator;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public void setOctaves(int octaves) {
        this.octaves = octaves;
        calculateFractalBounding();
    }

    public void setLacunarity(float lacunarity) {
        this.lacunarity = lacunarity;
    }

    public void setGain(float gain) {
        this.gain = gain;
        calculateFractalBounding();
    }

    public void setAmplitudeFactor(float amplitudeFactor) {
        this.amplitudeFactor = amplitudeFactor;
    }

    public float calculateFBm(float x, float y) {
        float value = 0, amplitude = fractalBounding;
        x *= frequency;
        y *= frequency;
        for(int i = 0; i < octaves; i++) {
            double noise =  noiseGenerator.noise2(x, y);
            value += noise * amplitude;
            amplitude *= lerp(1f, (noise + 1) * 0.5f, amplitudeFactor);

            x *= lacunarity;
            y *= lacunarity;
            amplitude *= gain;
        }
        return value;
    }

    private void calculateFractalBounding() {
        float gain = abs(this.gain);
        float amplitude = gain;
        float amplitudeFractal = 1f;
        for (int i = 1; i < octaves; i++) {
            amplitudeFractal += amplitude;
            amplitude *= gain;
        }
        fractalBounding = 1 / amplitudeFractal;
    }

    public float gradient(float y, float height) {
        float value = y / height;
        return linInterpolation(0, 1, value);
    }

    public float linInterpolation(float a, float b, float v) {
        return (a * (b - v) + b * v);
    }

    public double scale(float scale, float offset, double value) {
        return value * scale + offset;
    }

    public float select(float low, float high, float threshold, double value) {
        return select(threshold, value) ? high : low;
    }

    public boolean select(float threshold, double value) {
        return value >= threshold;
    }

    private float abs(float v) {
        return v < 0 ? -v : v;
    }

    private double lerp(float a, double b, float f) {
        return a + f * (b - a);
    }

}
