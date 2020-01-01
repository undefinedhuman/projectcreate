package de.undefinedhuman.sandboxgame.world.shift;

import de.undefinedhuman.sandboxgame.world.Noise;

public class Shift {

    public byte blockID;
    private Noise noise;
    public float threshold;
    public byte[] blockFilter;

    public Shift(byte blockID, int octaves, float amplitude, float roughness, int seed, float threshold, byte... blockFilter) {
        
        this.blockID = blockID;
        this.noise = new Noise(octaves, amplitude, roughness);
        noise.setSeed(seed);
        this.threshold = threshold;
        this.blockFilter = blockFilter;

    }

    public Noise getNoise() {
        return noise;
    }

}
