package de.undefinedhuman.sandboxgame.world;

import java.util.Random;

public class Noise {

    private Random random = new Random();

    private int seed, octaves;
    private float amplitude, roughness;

    public Noise(int octaves, float amplitude, float roughness) {
        this.seed = random.nextInt(10000);
        this.octaves = octaves;
        this.amplitude = amplitude;
        this.roughness = roughness;
    }

    public float gradient(float x, float y, float maxY) {
        float value = y / maxY;
        value += scale(0.5f, 0, calculateFractalNoise(x, y));
        return linInterpolation(0, 1, value);
    }

    public float linInterpolation(float a, float b, float v) {
        return (a * (b - v) + b * v);
    }

    public float scale(float scale, float offset, float value) {
        return value * scale + offset;
    }

    public float calculateFractalNoise(float x, float y) {
        float value = 0;
        float d = (float) Math.pow(2, octaves - 1);
        for (int i = 0; i < octaves; i++) {
            float freq = (float) (Math.pow(2, i) / d), amp = (float) Math.pow(roughness, i) * amplitude;
            value += calculateInterpolatedNoise(x * freq, y * freq) * amp;
        }
        return Math.abs(value);
    }

    public float calculateInterpolatedNoise(float x, float y) {
        int intX = (int) x, intY = (int) y;
        float fracX = x - intX, fracY = y - intY;
        float   i1 = calculateCosInterpolation(calculateSmoothNoise(intX, intY), calculateSmoothNoise(intX + 1, intY), fracX),
                i2 = calculateCosInterpolation(calculateSmoothNoise(intX, intY + 1), calculateSmoothNoise(intX + 1, intY + 1), fracX);
        return calculateCosInterpolation(i1, i2, fracY);
    }

    private float calculateCosInterpolation(float a, float b, float blend) {
        float c = (float) ((1f - Math.cos((blend * Math.PI))) * 0.5f);
        return a * (1 - c) + b * c;
    }

    private float calculateSmoothNoise(int x, int y) {
        float corners = (calculateNoise(x - 1, y - 1) + calculateNoise(x + 1, y - 1) + calculateNoise(x - 1, y + 1) + calculateNoise(x + 1, y + 1)) / 16f;
        float sides = (calculateNoise(x - 1, y) + calculateNoise(x + 1, y) + calculateNoise(x, y - 1) + calculateNoise(x, y + 1)) / 8f;
        float center = calculateNoise(x, y) / 4f;
        return corners + sides + center;
    }

    private float calculateNoise(int x, int y) {
        random.setSeed(x * 49632 + y * 325176 + seed);
        return random.nextFloat() * 2f - 1f;
    }

    public boolean select(float threshold, float value) { return value >= threshold; }

    public Noise setSeed(int seed) {
        this.seed = seed;
        return this;
    }

}
