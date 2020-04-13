package de.undefinedhuman.sandboxgame.entity.ecs.blueprint;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.ressources.ResourceManager;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.entity.EntityType;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.Arrays;
import java.util.HashMap;

public class BlueprintManager extends Manager {

    public static BlueprintManager instance;

    private HashMap<Integer, Blueprint> blueprints;

    public BlueprintManager() {
        if (instance == null) instance = this;
        blueprints = new HashMap<>();
    }

    @Override
    public void init() {
        super.init();
        loadBlueprints(0);
    }

    public boolean loadBlueprints(Integer... ids) {
        boolean loaded = false;
        for (int id : ids) {
            if (!hasBlueprint(id)) blueprints.put(id, ResourceManager.loadBlueprint(id));
            loaded |= hasBlueprint(id);
        }
        if (loaded)
            Log.info("Blueprint" + Tools.appendSToString(ids.length) + " loaded successfully: " + Arrays.toString(ids));
        return loaded;
    }

    public boolean hasBlueprint(int id) {
        return blueprints.containsKey(id);
    }

    @Override
    public void delete() {
        for (Blueprint blueprint : blueprints.values()) blueprint.delete();
        blueprints.clear();
    }

    public void addBlueprint(Blueprint blueprint) {
        this.blueprints.put(blueprint.getID(), blueprint);
    }

    public void removeBlueprints(int... ids) {
        for (int id : ids) {
            if (!hasBlueprint(id)) continue;
            blueprints.get(id).delete();
            blueprints.remove(id);
        }
    }

    public Blueprint getBlueprint(int id) {
        if (hasBlueprint(id) || loadBlueprints(id)) return blueprints.get(id);
        return hasBlueprint(0) ? getBlueprint(0) : null;
    }

    public static Blueprint loadBlueprint(int id) {

        FileHandle file = ResourceManager.loadFile(Paths.ENTITY_FOLDER, id + "/settings.txt");
        FileReader reader = new FileReader(file, true);
        reader.nextLine();
        EntityType type = EntityType.valueOf(reader.getNextString());
        reader.getNextString();
        Vector2 size = reader.getNextVector2();
        int componentSize = reader.getNextInt();
        Blueprint blueprint = new Blueprint(id, type, size);
        for (int i = 0; i < componentSize; i++) {
            reader.nextLine();
            blueprint.addComponentBlueprint(ComponentType.load(reader.getNextString(), reader));
        }
        reader.close();
        return blueprint;

    }

}
