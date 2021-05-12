package de.undefinedhuman.projectcreate.game.world;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.game.utils.Tools;
import de.undefinedhuman.projectcreate.game.world.layer.Layer;
import de.undefinedhuman.projectcreate.game.world.settings.BiomeSetting;
import de.undefinedhuman.projectcreate.game.world.settings.WorldSetting;
import de.undefinedhuman.projectcreate.game.world.shift.Shift;
import de.undefinedhuman.projectcreate.game.world.shift.ShiftList;

import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator {

    public static WorldGenerator instance;
    private static Vector2[] neighbors = new Vector2[] {new Vector2(-1, 0), new Vector2(0, 1), new Vector2(0, -1), new Vector2(1, 0)};
    private int maxHeight = 1200;
    private Random random;
    private Vector2 tempPosition = new Vector2();

    public WorldGenerator() {
        this.random = new Random();
    }

    public void generateTestWorld(String name, WorldSetting worldSetting, BiomeSetting biomeSetting) {
        World.instance = new World(name, 50, worldSetting.getBiomeSize() * (biomeSetting.getSize() + biomeSetting.getTransition()), 2000, 432);
        for (int x = 0; x < World.instance.size.x; x++) for (int y = 0; y < 50; y++) World.instance.setBlock(x, y, World.MAIN_LAYER, (byte) 1);
        World.instance.setBlock(-1, 51, World.MAIN_LAYER, (byte) 1);
        WorldManager.instance.checkMap(World.MAIN_LAYER);
    }

    public World generateWorld(String name, WorldSetting worldSetting, BiomeSetting biomeSetting) {
        World world = new World(name, maxHeight, worldSetting.getBiomeSize() * (biomeSetting.getSize() + biomeSetting.getTransition()), 2000, 23479356);
        Biome[] biomes = generateBiomes(worldSetting);
        Noise[] biomeNoise = new Noise[biomes.length];
        Noise caveNoise = Biome.CAVE.getNoise(world.seed);
        for (int i = 0; i < biomeNoise.length; i++) biomeNoise[i] = biomes[i].getNoise(world.seed);
        generateWorldBiomes(world, biomeSetting, biomes, biomeNoise, caveNoise);
        return world;
    }

    private Biome[] generateBiomes(WorldSetting worldSetting) {

        Biome[] biomes = new Biome[worldSetting.getBiomeSize()];
        ArrayList<Biome> neededBiomes = new ArrayList<>(worldSetting.getNeededBiomes());
        biomes[0] = worldSetting.getStartBiome();

        for (int i = 1; i < biomes.length; i++) {

            Biome lastBiome = biomes[Math.max(i - 1, 0)];
            if (neededBiomes.size() > 0) {
                Biome biome;
                ArrayList<Biome> overlapBiomes = getOverlapBiomes(neededBiomes, lastBiome.getPossibleNeighbors());
                int size = overlapBiomes.size();
                biome = size > 0 ? overlapBiomes.get(random.nextInt(size)) : neededBiomes.get(random.nextInt(neededBiomes.size()));
                neededBiomes.remove(biome);
                biomes[i] = biome;
            } else biomes[i] = lastBiome.getRandomNeighbor();

        }

        return biomes;

    }

    private void generateWorldBiomes(World world, BiomeSetting biomeSetting, Biome[] biomes, Noise[] noise, Noise caveNoise) {

        float[] caveHeight = new float[400], borderSizes = new float[world.size.x], biomeTransitions = new float[biomeSetting.getTransition()];
        int[] tempY = new int[400];
        int borderSize = biomeSetting.getTransition() / 4;

        ShiftList list = new ShiftList()
                .addShift(new Shift((byte) 2, 6, 0.75f, 0.4f, world.seed, 0.25f, (byte) 4))
                .addShift(new Shift((byte) 1, 5, 0.75f, 0.3f, world.seed, 0.2f, (byte) 4));

        for (int y = 399; y >= 0; y--) {
            caveHeight[y] = 0.15f - (1f - caveNoise.linInterpolation(0, 0.5f, (float) y / 399)) / 6;
            tempY[y] = maxHeight - y;
        }
        for (int x = 0; x < biomeTransitions.length; x++)
            biomeTransitions[x] = (float) x / biomeSetting.getTransition();
        for (int x = 0; x < borderSizes.length; x++) borderSizes[x] = calcBorders(x, borderSizes.length, borderSize);
        for (int i = 0; i < biomes.length; i++)
            generateBiome(world, tempY, biomeSetting, i, biomes, noise, caveNoise, list, borderSizes, caveHeight, biomeTransitions);
        for (int x = 0; x < world.size.x; x++) for (int y = 0; y < maxHeight; y++) checkWorldTile(world, x, y);

    }

    private ArrayList<Biome> getOverlapBiomes(ArrayList<Biome> biomes1, String[] biomes2) {
        ArrayList<Biome> biomes = new ArrayList<>();
        for (String biome : biomes2) if (biomes1.contains(Biome.valueOf(biome))) biomes.add(Biome.valueOf(biome));
        return biomes;
    }

    private float calcBorders(float x, float width, float borderSize) {
        return (x < borderSize ? Tools.lerp(0, 1, (x % borderSize) / borderSize) : x > (width - borderSize) ? 1f - Tools.lerp(0, 1, (x % borderSize) / borderSize) : 1f);
    }

    private void generateBiome(World world, int[] tempY, BiomeSetting biomeSetting, int biomeID, Biome[] biomes, Noise[] noise, Noise caveNoise, ShiftList list, float[] borderSizes, float[] caveHeight, float[] biomeTransitions) {

        int tempX = (biomeSetting.getWidth()) * biomeID,
                nextBiomeID = biomeID + 1 >= biomes.length ? 0 : biomeID + 1,
                currentHeight, currentX, currentY, transitionX;

        Noise currentNoise = noise[biomeID], nextNoise = noise[nextBiomeID];

        boolean isTransition;

        for (int x = 0; x < biomeSetting.getWidth(); x++) {
            currentX = tempX + x;
            transitionX = x % biomeSetting.getTransition();
            isTransition = x > biomeSetting.getSize();

            currentHeight = 400;
            for (int y = 0; y < currentHeight; y++) {
                currentY = tempY[y];
                if (currentNoise.select(0.5f, isTransition ? Tools.lerp(currentNoise.gradient(currentX + biomeSetting.getTransition(), y, currentHeight), nextNoise.gradient(nextBiomeID == 0 ? currentX + biomeSetting.getTransition() - world.size.x : currentX + biomeSetting.getTransition(), y, currentHeight), biomeTransitions[transitionX]) : currentNoise.gradient(currentX + biomeSetting.getTransition(), y, currentHeight)) && caveNoise.select(borderSizes[currentX] * caveHeight[y], caveNoise.calculateFractalNoise(currentX, currentY)))
                    world.setBlock(currentX, currentY, World.MAIN_LAYER, getBlockID(World.MAIN_LAYER, currentX, currentY, borderSizes, biomes[isTransition && currentNoise.select(0.5f, Tools.lerp(0, 1, biomeTransitions[transitionX] + currentNoise.calculateInterpolatedNoise(currentX, y))) ? nextBiomeID : biomeID], list));
            }

            currentHeight = 800;
            for (int y = 0; y <= currentHeight; y++) {
                if (caveNoise.select(borderSizes[currentX] * 0.06f, caveNoise.calculateFractalNoise(currentX, y)))
                    world.setBlock(currentX, y, World.MAIN_LAYER, getBlockID((byte) currentX, y, World.MAIN_LAYER, borderSizes, biomes[isTransition && currentNoise.select(0.5f, Tools.lerp(0, 1, biomeTransitions[transitionX] + currentNoise.calculateInterpolatedNoise(currentX, y))) ? nextBiomeID : biomeID], list));
            }

        }

    }

    // TODO REFACTOR THE MOST FREQUENT ARRAY ELEMENT TO ALSO CONSIDER THE BLOCK ID AT THE POSITION

    private void checkWorldTile(World world, int x, int y) {
        byte[] neighborIDs = new byte[4];
        byte air = 0, current = world.getBlock(x, y, World.MAIN_LAYER);
        for (int i = 0; i < neighborIDs.length; i++) {
            tempPosition.set(x, y).add(neighbors[i]);
            neighborIDs[i] = world.getBlock((int) tempPosition.x, (int) tempPosition.y, World.MAIN_LAYER);
            air += neighborIDs[i] == 0 ? 1 : 0;
        }
        if (air >= 3 && current != 0) world.setBlock(x, y, World.MAIN_LAYER, (byte) 0);
        if (air == 0 && current == 0) world.setBlock(x, y, World.MAIN_LAYER, Tools.getMostFrequentArrayElement(neighborIDs));
    }

    private byte getBlockID(byte worldLayer, int x, int y, float[] borderSizes, Biome biome, ShiftList shiftList) {
        Layer maxLayer = null;
        for (Layer layer : biome.getLayerList().getLayers()) if (layer.isMaxY(worldLayer, x, y)) maxLayer = layer;
        byte blockID = maxLayer != null ? maxLayer.blockID : 0;
        if (blockID != 0) for (Shift shift : shiftList.getShifts())
            if (Tools.contains(blockID, shift.blockFilter) && shift.getNoise().select(shift.threshold, borderSizes[x] * shift.getNoise().calculateFractalNoise(x, y)))
                blockID = shift.blockID;
        return blockID;
    }

}
