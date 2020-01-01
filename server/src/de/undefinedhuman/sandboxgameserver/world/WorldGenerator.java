package de.undefinedhuman.sandboxgameserver.world;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.world.settings.BiomeSetting;
import de.undefinedhuman.sandboxgameserver.world.settings.WorldPreset;
import de.undefinedhuman.sandboxgameserver.world.settings.WorldSetting;

import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator {

    public static WorldGenerator instance;

    private int maxHeight = 1200;
    private Random random;

    public WorldGenerator() {
        this.random = new Random();
    }

    public World generateWorld(WorldPreset preset) {
        return generateWorld(preset.getName(), preset.getWorldSetting(), preset.getBiomeSetting());
    }

    private boolean value;

    public World generateWorld(String name, WorldSetting worldSetting, BiomeSetting biomeSetting) {

        World world = new World(name,worldSetting.getBiomeSize() * (biomeSetting.getSize() + biomeSetting.getTransition()),2000, WorldManager.instance.getIDManager(), new Vector2(50,1000));
        Biome[] biomes = generateBiomes(worldSetting);

        for (int i = 0; i < biomes.length; i++) {

            int height = 400;

            Noise biomeNoise = biomes[i].getNoise().setSeed(world.seed), neighborNoise = biomes[i + 1 > biomes.length - 1 ? 0 : i + 1].getNoise().setSeed(world.seed), caveNoise = Biome.CAVE.getNoise().setSeed(world.seed);

            //for (int y = maxHeight - 400; y <= maxHeight; y++) {
            for(int y = 0; y <= height; y++) {
                //float caveHeight = 0.15f - (1 - caveNoise.linInterpolation(0,0.75f,(float) 1 / maxHeight * (maxHeight - y))) / 7; //0.15f - (1 - caveNoise.linInterpolation(0,0.75f,(float) 1 / maxHeight * (y - maxHeight))) / 7;

                float caveHeight = 0.15f - (1f - caveNoise.linInterpolation(0,0.5f,1f / height * y)) / 6;

                //if(i == 0) System.out.println(caveHeight + ", " + (float) 1 / maxHeight * (maxHeight - y));

                for (int x = 0; x < biomeSetting.getSize(); x++) {
                    //value = biomeNoise.select(0.5f, biomeNoise.gradient((biomeSetting.getSize() + biomeSetting.getTransition()) * i + x, y, height));
                    //value = biomeNoise.select(0,1,0.5f, Tools.lerp(biomeNoise.gradient((biomeSetting.getSize() + biomeSetting.getTransition()) * i + x,maxHeight-y, maxHeight), neighborNoise.gradient((biomeSetting.getSize() + biomeSetting.getTransition()) * i + x,maxHeight-y, maxHeight),(float) (x - biomeSetting.getSize()) / biomeSetting.getTransition()));
                    //if (value)
                    value = caveNoise.select(caveHeight, caveNoise.fractal((biomeSetting.getSize() + biomeSetting.getTransition()) * i + x,y));

                    if(value) world.mainLayer.blocks[(biomeSetting.getSize() + biomeSetting.getTransition()) * i + x][maxHeight - y] = (byte) biomes[i].blockID;

                }

                for(int x = 0; x < biomeSetting.getTransition(); x++) {

                    //if(biomeNoise.select(0.5f, Tools.lerp(biomeNoise.gradient((biomeSetting.getSize() + biomeSetting.getTransition()) * i + biomeSetting.getSize() + x, y, height), neighborNoise.gradient((biomeSetting.getSize() + biomeSetting.getTransition()) * i + biomeSetting.getSize() + x,y, height),(float) x / biomeSetting.getTransition()))) {
                        if(!caveNoise.select(caveHeight, caveNoise.fractal((biomeSetting.getSize() + biomeSetting.getTransition()) * i + biomeSetting.getSize() + x,y))) continue;
                        /*byte blockID = 0;
                        if(biomeNoise.select(0.5f, Tools.lerp(0,1,(float) x/biomeSetting.getTransition()) + biomeNoise.getInterpolatedNoise(x,y)))
                            blockID = (byte) biomes[i + 1 > biomes.length - 1 ? 0 : i+1].blockID;
                        else
                            blockID = (byte) biomes[i].blockID;*/
                        //world.mainLayer.blocks[(biomeSetting.getSize() + biomeSetting.getTransition()) * i + biomeSetting.getSize() + x][maxHeight - y] = blockID;
                        world.mainLayer.blocks[(biomeSetting.getSize() + biomeSetting.getTransition()) * i + biomeSetting.getSize() + x][maxHeight - y] = 1;
                    //}

                }

            }

        }

        /*for(int x = 0; x < 3; x++) {

            for(int i = 0; i < world.width; i++) {

                for(int j = 0; j < world.height; j++) {

                    checkWorldTile(world, i, j);

                }

            }

        }*/

        return world;

    }

    public boolean checkWorldTile(World world, int x, int y) {

        byte air = 0, solid = 0, current = world.mainLayer.getBlock(x,y);

        for(int i = 0; i < 2; i++) {

            for(int j = -1; j < 2; j++) {

                if(j == 0) continue;
                if(getTileNeighbor(world, x, y, i == 0 ? j : 0, i == 1 ? j : 0)) solid++; else air++;

            }

        }

        return (air < 3 || current == 0) && (solid == 4 || current != 0);

    }

    public boolean getTileNeighbor(World world, int x, int y, int xOffset, int yOffset) {
        return world.mainLayer.getBlock(x + xOffset,y + yOffset) != 0;
    }

    public World generateWorldTest(WorldPreset preset) {

        World world = new World(preset.getName(),400,2000, WorldManager.instance.getIDManager(), new Vector2(50,420));

        for(int i = 0; i < world.width; i++) {

            for(int j = 0; j < 400; j++) world.mainLayer.blocks[i][j] = (byte) 2;
            for(int j = 400; j < 410; j++) world.mainLayer.blocks[i][j] = (byte) 1;

            for(int j = 0; j < 400; j++) world.backLayer.blocks[i][j] = (byte) 2;
            for(int j = 400; j < 410; j++) world.backLayer.blocks[i][j] = (byte) 1;

        }

        return world;

    }

    private Biome[] generateBiomes(WorldSetting worldSetting) {

        Biome[] biomes = new Biome[worldSetting.getBiomeSize()];

        ArrayList<Biome> neededBiomes = new ArrayList<>(worldSetting.getNeededBiomes());
        biomes[0] = worldSetting.getStartBiome();

        for (int i = 1; i < biomes.length; i++) {

            Biome lastBiome = biomes[Math.max(i - 1, 0)];

            if (neededBiomes.size() > 0) {

                ArrayList<Biome> overlapBiomes = getOverlapBiomes(neededBiomes, lastBiome.getPossibleNeighbors());
                int size = overlapBiomes.size();
                if (size > 0) biomes[i] = overlapBiomes.get(random.nextInt(size));
                else biomes[i] = neededBiomes.get(random.nextInt(neededBiomes.size()));

            } else biomes[i] = lastBiome.getRandomNeighbor();

        }

        return biomes;

    }

    private ArrayList<Biome> getOverlapBiomes(ArrayList<Biome> biomes1, String[] biomes2) {

        ArrayList<Biome> biomes = new ArrayList<>();
        for (String biome : biomes2) if (biomes1.contains(Biome.valueOf(biome))) biomes.add(Biome.valueOf(biome));
        return biomes;

    }

}
