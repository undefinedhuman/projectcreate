package de.undefinedhuman.projectcreate.core.noise.generator;

public abstract class NoiseGenerator {

    protected long seed = 1337;

    public NoiseGenerator(long seed) {
        this.seed = seed;
    }

    public abstract double noise2(double x, double y);
}
