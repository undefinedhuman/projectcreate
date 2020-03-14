package de.undefinedhuman.sandboxgameserver.world;

import java.util.Random;

public class Noise {

    private Random random;

    private int seed, octaves;
    private float amplitude, roughness;

    public Noise(int octaves, float amplitude, float roughness) {

        this.random = new Random();

        this.seed = 0;
        this.octaves = octaves;
        this.amplitude = amplitude;
        this.roughness = roughness;

    }

    public float gradient(float x, float y, float maxY) {

        float value = (float) 1 / maxY * y;
        value += scale(0.5f, 0, fractal(x, y));
        return linInterpolation(0, 1, value);

    }

    public float scale(float scale, float offset, float value) {
        return value * scale + offset;
    }

    public float fractal(float x, float y) {

        float value = 0;
        float d = (float) Math.pow(2, octaves - 1);

        for (int i = 0; i < octaves; i++) {

            float freq = (float) (Math.pow(2, i) / d);
            float amp = (float) Math.pow(roughness, i) * amplitude;
            value += getInterpolatedNoise(x * freq, y * freq) * amp;

        }

        return Math.abs(value);

    }

    public float linInterpolation(float a, float b, float v) {

        return (a * (b - v) + b * v);

    }

    public float getInterpolatedNoise(float x, float y) {

        int intX = (int) x, intY = (int) y;
        float fracX = x - intX, fracY = y - intY;

        float v1 = getSmoothNoise(intX, intY), v2 = getSmoothNoise(intX + 1, intY), v3 = getSmoothNoise(intX, intY + 1), v4 = getSmoothNoise(intX + 1, intY + 1);
        float i1 = cosInterpolation(v1, v2, fracX), i2 = cosInterpolation(v3, v4, fracX);
        return cosInterpolation(i1, i2, fracY);

    }

    private float getSmoothNoise(int x, int y) {

        float corners = (getNoise(x - 1, y - 1) + getNoise(x + 1, y - 1) + getNoise(x - 1, y + 1) + getNoise(x + 1, y + 1)) / 16f;
        float sides = (getNoise(x - 1, y) + getNoise(x + 1, y) + getNoise(x, y - 1) + getNoise(x, y + 1)) / 8f;
        float center = getNoise(x, y) / 4f;
        return corners + sides + center;

    }

    private float cosInterpolation(float a, float b, float blend) {
        float c = (float) ((1f - Math.cos((blend * Math.PI))) * 0.5f);
        return a * (1 - c) + b * c;
    }

    private float getNoise(int x, int y) {

        random.setSeed(x * 49632 + y * 325176 + seed);
        return random.nextFloat() * 2f - 1f;

    }

    public boolean select(float threshold, float value) {
        return value >= threshold;
    }

    public float select(float min, float max, float threshold, float value) {
        return value >= threshold ? max : min;
    }

    public Noise setSeed(int seed) {
        this.seed = seed;
        return this;
    }

}
