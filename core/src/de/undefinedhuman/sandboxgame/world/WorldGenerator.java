package de.undefinedhuman.sandboxgame.world;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.layer.Layer;
import de.undefinedhuman.sandboxgame.world.settings.BiomeSetting;
import de.undefinedhuman.sandboxgame.world.settings.WorldPreset;
import de.undefinedhuman.sandboxgame.world.settings.WorldSetting;
import de.undefinedhuman.sandboxgame.world.shift.Shift;
import de.undefinedhuman.sandboxgame.world.shift.ShiftList;

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

    public World generateTestWorld(WorldPreset preset) {
        return generateTestWorld(preset.getName(), preset.getWorldSetting(), preset.getBiomeSetting());
    }

    private World generateTestWorld(String name, WorldSetting worldSetting, BiomeSetting biomeSetting) {
        World world = new World(name, 50, worldSetting.getBiomeSize() * (biomeSetting.getSize() + biomeSetting.getTransition()), 2000, 23479356);
        for (int x = 0; x < world.width; x++) for (int y = 0; y < 50; y++) world.mainLayer.setBlock(x, y, (byte) 1);
        WorldManager.instance.checkMap(world.backLayer);
        WorldManager.instance.checkMap(world.mainLayer);
        return world;
    }

    public World generateWorld(WorldPreset preset) {
        return generateWorld(preset.getName(), preset.getWorldSetting(), preset.getBiomeSetting());
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

        float[] caveHeight = new float[400], borderSizes = new float[world.width], biomeTransitions = new float[biomeSetting.getTransition()];
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
        for (int x = 0; x < world.width; x++) for (int y = 0; y < maxHeight; y++) checkWorldTile(world, x, y);

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
                if (currentNoise.select(0.5f, isTransition ? Tools.lerp(currentNoise.gradient(currentX + biomeSetting.getTransition(), y, currentHeight), nextNoise.gradient(nextBiomeID == 0 ? currentX + biomeSetting.getTransition() - world.width : currentX + biomeSetting.getTransition(), y, currentHeight), biomeTransitions[transitionX]) : currentNoise.gradient(currentX + biomeSetting.getTransition(), y, currentHeight)) && caveNoise.select(borderSizes[currentX] * caveHeight[y], caveNoise.fractal(currentX, currentY)))
                    world.mainLayer.blocks[currentX][currentY] = getBlockID(world.mainLayer, currentX, currentY, borderSizes, biomes[isTransition && currentNoise.select(0.5f, Tools.lerp(0, 1, biomeTransitions[transitionX] + currentNoise.getInterpolatedNoise(currentX, y))) ? nextBiomeID : biomeID], list);
            }

            currentHeight = 800;
            for (int y = 0; y <= currentHeight; y++) {
                if (caveNoise.select(borderSizes[currentX] * 0.06f, caveNoise.fractal(currentX, y)))
                    world.mainLayer.blocks[currentX][y] = getBlockID(world.mainLayer, currentX, y, borderSizes, biomes[isTransition && currentNoise.select(0.5f, Tools.lerp(0, 1, biomeTransitions[transitionX] + currentNoise.getInterpolatedNoise(currentX, y))) ? nextBiomeID : biomeID], list);
            }

        }

    }

    private void checkWorldTile(World world, int x, int y) {
        byte[] neighborIDs = new byte[4];
        byte air = 0, current = world.mainLayer.getBlock(x, y);
        for (int i = 0; i < neighborIDs.length; i++) {
            neighborIDs[i] = world.mainLayer.getBlock(tempPosition.set(x, y).add(neighbors[i]));
            air += neighborIDs[i] == 0 ? 1 : 0;
        }
        if (air >= 3 && current != 0) world.mainLayer.setBlock(x, y, (byte) 0);
        if (air == 0 && current == 0) world.mainLayer.setBlock(x, y, Tools.getMostFrequentArrayElement(neighborIDs));
    }

    private byte getBlockID(WorldLayer worldLayer, int x, int y, float[] borderSizes, Biome biome, ShiftList shiftList) {
        Layer maxLayer = null;
        for (Layer layer : biome.getLayerList().getLayers()) if (layer.isMaxY(worldLayer, x, y)) maxLayer = layer;
        byte blockID = maxLayer != null ? maxLayer.blockID : 0;
        if (blockID != 0) for (Shift shift : shiftList.getShifts())
            if (Tools.contains(blockID, shift.blockFilter) && shift.getNoise().select(shift.threshold, borderSizes[x] * shift.getNoise().fractal(x, y)))
                blockID = shift.blockID;
        return blockID;
    }

}
