package de.undefinedhuman.sandboxgameserver.world;

import java.util.Random;

public enum Biome {

    GRASSLAND(6,1.7f,0.4f,1,"DESERT"), DESERT(8,1.2f,0.1f,4,"GRASSLAND"),
    CAVE(7,1.4f,0.4f,0);
    //CAVE(8,1.7f, 0.4f,0);

    private Noise noise;
    private String[] possibleNeighbors;
    public int blockID;

    private Random random = new Random();

    Biome(int octaves, float amplitude, float roughness, int blockID, String... possibleNeighbors) {

        this.noise = new Noise(octaves, amplitude, roughness);
        this.possibleNeighbors = possibleNeighbors;
        this.blockID = blockID;

    }

    public String[] getPossibleNeighbors() {
        return possibleNeighbors;
    }

    public Noise getNoise() {
        return noise;
    }

    public Biome getRandomNeighbor() {
        return Biome.valueOf(possibleNeighbors[new Random().nextInt(possibleNeighbors.length-1)]);
    }

}
