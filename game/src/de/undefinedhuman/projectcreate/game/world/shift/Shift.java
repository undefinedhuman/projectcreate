package de.undefinedhuman.projectcreate.game.world.shift;

import de.undefinedhuman.projectcreate.core.noise.Noise;
import de.undefinedhuman.projectcreate.core.noise.generator.OpenSimplex2F;

public class Shift {

    public byte blockID;
    public float threshold;
    public byte[] blockFilter;
    private Noise noise;

    public Shift(byte blockID, int octaves, float amplitude, float roughness, int seed, float threshold, byte... blockFilter) {
        this.blockID = blockID;
        this.noise = new Noise(new OpenSimplex2F(seed));
        // noise.setSeed(seed);
        this.threshold = threshold;
        this.blockFilter = blockFilter;
    }

    public Noise getNoise() {
        return noise;
    }

}
