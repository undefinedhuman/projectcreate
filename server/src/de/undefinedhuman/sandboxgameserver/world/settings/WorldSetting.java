package de.undefinedhuman.sandboxgameserver.world.settings;

import de.undefinedhuman.sandboxgameserver.world.Biome;

import java.util.ArrayList;
import java.util.Arrays;

public enum WorldSetting {

    DEV(4, Biome.GRASSLAND, Biome.DESERT), SMALL(10, Biome.GRASSLAND, Biome.DESERT), NORMAL(15, Biome.GRASSLAND, Biome.DESERT), BIG(25, Biome.GRASSLAND, Biome.DESERT);

    private int biomeSize;
    private ArrayList<Biome> neededBiomes;
    private Biome startBiome;

    WorldSetting(int biomeSize, Biome startBiome, Biome... neededBiomes) {

        this.biomeSize = biomeSize;
        this.startBiome = startBiome;
        this.neededBiomes = new ArrayList<>();
        this.neededBiomes.addAll(Arrays.asList(neededBiomes));

    }

    public int getBiomeSize() {
        return biomeSize;
    }

    public ArrayList<Biome> getNeededBiomes() {
        return neededBiomes;
    }

    public Biome getStartBiome() {
        return startBiome;
    }

}
