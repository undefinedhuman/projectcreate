package de.undefinedhuman.sandboxgameserver.world;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.Main;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.file.*;
import de.undefinedhuman.sandboxgameserver.log.Log;
import de.undefinedhuman.sandboxgameserver.utils.IDManager;
import de.undefinedhuman.sandboxgameserver.utils.threading.TaskType;
import de.undefinedhuman.sandboxgameserver.utils.threading.ThreadManager;
import de.undefinedhuman.sandboxgameserver.utils.threading.ThreadTask;
import de.undefinedhuman.sandboxgameserver.world.settings.WorldPreset;

import java.util.*;

public class WorldManager {

    public static WorldManager instance;

    private IDManager idManager;
    private HashMap<String, World> worlds;
    private ArrayList<String> worldsToDelete;
    private Timer saveTimer, deleteTimer;
    private ArrayList<WorldPreset> worldsToGenerate;

    public WorldManager() {

        worlds = new HashMap<>();
        worldsToGenerate = new ArrayList<>();
        worldsToDelete = new ArrayList<>();

        idManager = new IDManager();

        saveTimer = new Timer();
        saveTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                for (String name : worlds.keySet()) save(worlds.get(name));
            }

        },900000,900000);

        deleteTimer = new Timer();
        deleteTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for(String name : worldsToDelete) deleteWorld(name);
                worldsToDelete.clear();
            }
        },120000,120000);

        loadGeneratedWorlds();

    }

    public void saveWorld(String name) {

        if (worlds.containsKey(name)) ThreadManager.instance.addTask(TaskType.WORLDSAVE, new ThreadTask() {

            @Override
            public void runTask() {
                save(worlds.get(name));
            }

            @Override
            public void endTask() {
                Log.instance.info("World " + name + " saved!");
            }

        });

    }

    public void loadWorld(String name) {

        if (!worlds.containsKey(name)) ThreadManager.instance.addTask(TaskType.WORLDLOAD, new ThreadTask() {

            @Override
            public void runTask() {
                load(name);
            }

            @Override
            public void endTask() {
                Log.instance.info("World " + name + " loaded!");
            }

        });

    }

    public void addWorldToDelete(String worldName) {

        World world = getWorld(worldName);
        if(world != null && !world.alwaysLoaded) worldsToDelete.add(worldName);

    }

    public void removeWorldToDelete(String worldName) {
        worldsToDelete.remove(worldName);
    }

    public void deleteWorld(String name) {

        if (worlds.containsKey(name)) ThreadManager.instance.addTask(TaskType.WORLDSAVE, new ThreadTask() {
            @Override
            public void runTask() {
                World world = worlds.get(name);
                Collection<Integer> players = world.getPlayersID();
                if(players.size() > 0 && !Main.closing) {

                    if(!hasWorld("Main") && worldExists("Main")) load("Main");

                    World main = getWorld("Main");

                    for(int id : players) {

                        Entity player = world.getEntity(id);
                        world.removeEntity(id);
                        main.addEntity(id, player);

                    }

                }

                save(worlds.get(name));
                worlds.remove(name);

            }

            @Override
            public void endTask() {
                Log.instance.info("World " + name + " deleted!");
            }

        });

    }

    public void generateWorld(WorldPreset preset) {

        worldsToGenerate.add(preset);

        if (!worldExists(preset.getName())) ThreadManager.instance.addTask(TaskType.WORLDGENERATE, new ThreadTask() {

            @Override
            public void runTask() {
                worlds.put(preset.getName(), WorldGenerator.instance.generateWorld(preset));
                save(worlds.get(preset.getName()));
                worldsToGenerate.remove(preset);
            }

            @Override
            public void endTask() {
                Log.instance.info("World " + preset.getName() + " generated!");
            }

        });

    }

    // TODO Remove
    public void generateWorldTest(WorldPreset preset) {

        worldsToGenerate.add(preset);

        if (!worldExists(preset.getName())) ThreadManager.instance.addTask(TaskType.WORLDGENERATE, new ThreadTask() {

            @Override
            public void runTask() {
                worlds.put(preset.getName(), WorldGenerator.instance.generateWorldTest(preset));
                save(worlds.get(preset.getName()));
                worldsToGenerate.remove(preset);
            }

            @Override
            public void endTask() {
                Log.instance.info("World " + preset.getName() + " generated!");
            }

        });

    }

    public boolean hasWorld(String name) {
        return worlds.containsKey(name);
    }

    public World getWorld(String name) {
        return worlds.get(name);
    }

    private void deleteWorlds(String name) {

        save(worlds.get(name));
        worlds.remove(name);
        Log.instance.info("World " + name + " saved!");

    }

    public Entity getEntity(int id) {

        for(World world : worlds.values()) if(world.hasEntity(id)) return world.getEntity(id);
        return null;

    }

    public ArrayList<Entity> getPlayers() {

        ArrayList<Entity> players = new ArrayList<>();
        for(World world : worlds.values()) players.addAll(world.getPlayers());
        return players;

    }

    public boolean isPlayerOnline(String playerName) {

        for (World world : worlds.values()) if (world.isPlayerOnline(playerName)) return true;
        return false;

    }

    public boolean worldExists(String name) {

        FsFile file = new FsFile(Paths.WORLD_PATH, name + "/", true, false);
        return file.hasChildren("settings.world") || worlds.containsKey(name);

    }

    private void loadGeneratedWorlds() {

        FsFile file = new FsFile(Paths.WORLD_PATH, "generatedWorlds.txt", false, false);
        if (file.exists()) {

            FileReader reader = file.getFileReader(true);
            reader.nextLine();
            int size = reader.getNextInt();
            for (int i = 0; i < size; i++) {
                WorldPreset preset = new WorldPreset("", null, null);
                preset.load(reader);
                generateWorld(preset);
                worldsToGenerate.add(preset);
            }

            reader.close();

        }

        file.delete();

    }

    private void saveGeneratedWorlds() {

        FsFile file = new FsFile(Paths.WORLD_PATH, "generatedWorlds.txt", false, false);
        if (file.exists()) FileManager.deleteFile(file.getFile());
        file.createFile(false);
        FileWriter writer = file.getFileWriter(true);
        writer.writeInt(worldsToGenerate.size());
        for (WorldPreset preset : worldsToGenerate) preset.save(writer);
        writer.close();

    }

    private void save(World world) {

        FsFile worldFile = new FsFile(Paths.WORLD_PATH, world.name + "/settings.world", false);
        FileWriter worldWriter = worldFile.getFileWriter(true);
        worldWriter.writeVector2(world.spawnPoint);
        worldWriter.writeInt(world.width);
        worldWriter.writeInt(world.height);
        worldWriter.writeInt(world.seed);
        worldWriter.nextLine();

        worldWriter.writeString(world.mainLayer.getLayer());
        worldWriter.nextLine();
        worldWriter.writeString(world.backLayer.getLayer());

        worldWriter.close();

        world.saveEntities();

    }

    public void load(String name) {

        FsFile file = new FsFile(Paths.WORLD_PATH, name + "/settings.world", false, false);

        if (file.exists()) {

            FileReader reader = file.getFileReader(true);
            reader.nextLine();
            Vector2 spawnPoint = reader.getNextVector2();
            int width = reader.getNextInt(), height = reader.getNextInt(), seed = reader.getNextInt();
            reader.nextLine();

            World world = new World(name, width, height, idManager, spawnPoint, seed);

            world.mainLayer.setLayer(reader.getNextString());
            reader.nextLine();
            world.backLayer.setLayer(reader.getNextString());

            reader.close();
            worlds.put(name, world);

            world.loadEntities(name);

        }

    }

    public IDManager getIDManager() {
        return idManager;
    }

    public int getNewID() {
        return idManager.getID();
    }

    public void delete() {

        deleteTimer.cancel();
        saveTimer.cancel();

        if (worldsToGenerate.size() > 0) saveGeneratedWorlds();
        worldsToDelete.clear();
        worldsToGenerate.clear();
        for (String name : worlds.keySet()) deleteWorlds(name);
        worlds.clear();

    }

}
