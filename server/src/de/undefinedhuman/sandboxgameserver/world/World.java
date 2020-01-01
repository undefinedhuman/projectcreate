package de.undefinedhuman.sandboxgameserver.world;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgameserver.entity.Entity;
import de.undefinedhuman.sandboxgameserver.entity.EntityList;
import de.undefinedhuman.sandboxgameserver.entity.EntityManager;
import de.undefinedhuman.sandboxgameserver.entity.EntityType;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.FsFile;
import de.undefinedhuman.sandboxgameserver.file.Paths;
import de.undefinedhuman.sandboxgameserver.utils.IDManager;

import java.util.Collection;
import java.util.Random;

public class World {

    public String name;
    public Vector2 spawnPoint;
    public int width, height, seed;
    public WorldLayer mainLayer, backLayer;

    public boolean alwaysLoaded = false;

    private IDManager idManager;

    private EntityList list;
    private int playersOnline = 0;

    public World(String name, int width, int height, IDManager idManager, Vector2 spawnPoint) {
        this(name, width, height, idManager, spawnPoint, new Random().nextInt(100000000));
    }

    public World(String name, int width, int height, IDManager idManager, Vector2 spawnPoint, int seed) {

        this.name = name;
        this.width = width;
        this.height = height;
        this.spawnPoint = spawnPoint;
        this.mainLayer = new WorldLayer(true, width, height);
        this.backLayer = new WorldLayer(false, width, height);

        this.idManager = idManager;

        this.list = new EntityList(idManager);

        // TODO : Irgendwie das Task System überarbeiten
        if(name.equalsIgnoreCase("Main")) alwaysLoaded = true;

        this.seed = seed;

    }

    public void addEntity(int id, Entity entity) {

        if(entity.getType() == EntityType.PLAYER) { playersOnline++; WorldManager.instance.removeWorldToDelete(name); }
        entity.setWorldName(name);
        entity.setWorldID(id);
        this.list.addEntity(id, entity);

    }

    public void removeEntity(int id) {

        if(list.hasEntity(id)) {
            if(list.getEntity(id).getType() == EntityType.PLAYER) playersOnline--;
            this.list.removeEntity(id);
            if(playersOnline == 0) WorldManager.instance.addWorldToDelete(name);
        }

    }

    public boolean hasEntity(int id) {
        return list.hasEntity(id);
    }

    public Entity getEntity(int id) {
        return this.list.getEntity(id);
    }

    public Collection<Integer> getPlayersID() {
        return list.getPlayersID();
    }

    public Collection<Entity> getPlayers() {
        return list.getPlayers();
    }

    public boolean isPlayerOnline(String playerName) {
        return list.isPlayerOnline(playerName);
    }

    public void saveEntities() {

        FsFile entityFile = new FsFile(Paths.WORLD_PATH, name + "/entities.world",false);
        FileWriter entityWriter = entityFile.getFileWriter(true);
        entityWriter.writeInt(list.getEntities().size() - list.getPlayers().size());
        entityWriter.nextLine();

        for (Entity entity : list.getEntities().values()) {

            if (entity.getType() != EntityType.PLAYER) {

                EntityManager.instance.saveEntity(entityWriter, entity);
                entityWriter.nextLine();

            } else EntityManager.instance.savePlayer(name, entity);

        }

        entityWriter.close();

    }

    public void loadEntities(String worldName) {

        FsFile entityFile = new FsFile(Paths.WORLD_PATH, worldName + "/entities.world",false,false);

        if (entityFile.exists()) {

            FileReader reader = entityFile.getFileReader(true);
            reader.nextLine();
            int entitySize = reader.getNextInt();
            reader.nextLine();

            for (int i = 0; i < entitySize; i++) {

                addEntity(idManager.getID(), EntityManager.instance.loadEntity(reader));
                reader.nextLine();

            }

            reader.close();

        }

    }

    public Vector2 getSpawnPoint() {
        return new Vector2(spawnPoint.x * 16,spawnPoint.y * 16);
    }

    public int getPlayersOnline() {
        return playersOnline;
    }

}
