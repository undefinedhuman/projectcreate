package de.undefinedhuman.sandboxgameserver.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgameserver.entity.EntityType;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgameserver.entity.ecs.blueprint.Blueprint;

public class ResourceManager {

    public static ResourceManager instance;

    public static FileHandle loadFile(Paths path, String name) {
        return new FileHandle(path.getPath() + name);
    }

    public Blueprint loadBlueprint(int id) {

        FsFile file = new FsFile(Paths.ENTITY_FOLDER, id + "/settings.txt", false);
        FileReader reader = new FileReader(file, true);
        reader.nextLine();
        EntityType type = EntityType.valueOf(reader.getNextString());
        reader.getNextString();
        Vector2 size = reader.getNextVector2();
        int componentSize = reader.getNextInt();
        Blueprint blueprint = new Blueprint(id, type, size);

        for (int i = 0; i < componentSize; i++) {

            reader.nextLine();
            String componentName = reader.getNextString();

            if (ComponentType.hasType(componentName)) {
                ComponentBlueprint componentBlueprint = ComponentType.load(componentName, reader, id);
                blueprint.addComponentBlueprint(componentBlueprint);
            }

        }

        reader.close();

        Log.info("Loaded Entity Blueprint ID: " + id);

        return blueprint;

    }

}
