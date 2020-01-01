package de.undefinedhuman.sandboxgameserver.entity;

import de.undefinedhuman.sandboxgameserver.entity.ecs.Component;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.name.NameComponent;
import de.undefinedhuman.sandboxgameserver.file.FileReader;
import de.undefinedhuman.sandboxgameserver.file.FileWriter;
import de.undefinedhuman.sandboxgameserver.file.FsFile;
import de.undefinedhuman.sandboxgameserver.file.Paths;
import de.undefinedhuman.sandboxgameserver.world.WorldManager;

public class EntityManager {

    public static EntityManager instance;

    public EntityManager() {}

    public boolean playerExists(String playerName) {

        FsFile file = new FsFile(Paths.PLAYER_PATH, true);
        return file.hasChildren(playerName + ".player");

    }

    public Entity loadPlayer(FileReader reader) {

        reader.nextLine();
        return loadEntity(reader);

    }

    public void savePlayer(String worldName, Entity entity) {

        String name = ((NameComponent) entity.getComponent(ComponentType.NAME)).getName();

        FsFile file = new FsFile(Paths.PLAYER_PATH, name + ".player", false);
        FileWriter writer = file.getFileWriter(true);
        writer.writeString(worldName);
        writer.nextLine();
        saveEntity(writer, entity);
        writer.close();

    }

    public Entity generatePlayer(String playerName) {

        Entity player = BlueprintManager.instance.getBlueprint(0).createInstance();
        ((NameComponent) player.getComponent(ComponentType.NAME)).setName(playerName);
        player.setPosition(WorldManager.instance.getWorld("Main").getSpawnPoint());
        savePlayer("Main", player);

        return player;

    }

    public Entity loadEntity(FileReader reader) {

        reader.nextLine();
        int entityID = reader.getNextInt();
        Entity entity = BlueprintManager.instance.getBlueprint(entityID).createInstance();

        int componentSize = reader.getNextInt();

        for (int i = 0; i < componentSize; i++) {

            ComponentType type = ComponentType.valueOf(reader.getNextString());
            entity.getComponent(type).load(reader);

        }

        return entity;

    }

    public void saveEntity(FileWriter writer, Entity entity) {

        writer.writeInt(entity.getBlueprintID());
        writer.writeInt(entity.getComponents().getComponents().size());

        for (Component component : entity.getComponents().getComponents()) {

            writer.writeString(component.getType().name());
            component.save(writer);

        }

    }

}
