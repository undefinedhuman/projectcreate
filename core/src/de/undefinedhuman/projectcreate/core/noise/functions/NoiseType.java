package de.undefinedhuman.projectcreate.core.noise.functions;

import de.undefinedhuman.projectcreate.core.noise.generator.NoiseGenerator;
import de.undefinedhuman.projectcreate.core.noise.generator.OpenSimplex2F;
import de.undefinedhuman.projectcreate.core.noise.generator.OpenSimplex2S;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.lang.reflect.InvocationTargetException;

public enum NoiseType {
    OPEN_SIMPLEX_2_SMOOTH(OpenSimplex2S.class),
    OPEN_SIMPLEX_2_FAST(OpenSimplex2F.class);

    private Class<? extends NoiseGenerator> noiseGenerator;

    NoiseType(Class<? extends NoiseGenerator> noiseGenerator) {
        this.noiseGenerator = noiseGenerator;
    }

    public NoiseGenerator newInstance(long seed) {
        NoiseGenerator noiseGenerator = null;
        try {
            noiseGenerator = this.noiseGenerator.getConstructor(long.class).newInstance(seed);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Log.error("Error", ex);
            Log.showErrorDialog("Error while creating noise generator instance", true);
        }
        return noiseGenerator;
    }

}
