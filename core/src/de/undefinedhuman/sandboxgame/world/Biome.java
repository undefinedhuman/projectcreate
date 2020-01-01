package de.undefinedhuman.sandboxgame.world;

import de.undefinedhuman.sandboxgame.world.layer.LayerList;
import de.undefinedhuman.sandboxgame.world.layer.LayerTransition;

import java.util.Random;

public enum Biome {

    // Bei Transition TOP, maxY = maximales Y des Layers, sondern das erste Y ab dem der Top Layer da sein soll

    GRASSLAND(6,1.7f,0.4f, new String[] { "DESERT" },
            new LayerList().addLayer(1200, (byte) 1, LayerTransition.LINEAR).addLayer(1000, (byte) 3, LayerTransition.TOP).addLayer(1000, (byte) 2, LayerTransition.CAVE)),


    DESERT(8,1.2f,0.1f,new String[] { "GRASSLAND" },
            new LayerList().addLayer(1200, (byte) 4, LayerTransition.LINEAR).addLayer(1000, (byte) 2, LayerTransition.CAVE)),


    CAVE(7,1.4f,0.4f, new String[0], new LayerList());

    private String[] possibleNeighbors;
    public int octaves;
    public float amplitude, roughness;
    public LayerList layerList;

    private Random random = new Random();

    Biome(int octaves, float amplitude, float roughness, String[] possibleNeighbors, LayerList layerList) {

        this.octaves = octaves; this.amplitude = amplitude; this.roughness = roughness;
        this.possibleNeighbors = possibleNeighbors;
        this.layerList = layerList;

    }

    public String[] getPossibleNeighbors() {
        return possibleNeighbors;
    }

    public Noise getNoise(int seed) {
        return new Noise(octaves, amplitude, roughness).setSeed(seed);
    }

    public Biome getRandomNeighbor() {
        return Biome.valueOf(possibleNeighbors[random.nextInt(possibleNeighbors.length)]);
    }

    public LayerList getLayerList() {
        return layerList;
    }

}
