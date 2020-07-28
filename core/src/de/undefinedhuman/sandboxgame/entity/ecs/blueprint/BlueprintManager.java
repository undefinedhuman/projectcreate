package de.undefinedhuman.sandboxgame.entity.ecs.blueprint;

import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsObject;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
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
        loadBlueprints(0, 1);
    }

    public boolean loadBlueprints(Integer... ids) {
        boolean loaded = false;
        for (int id : ids) {
            if (!hasBlueprint(id)) blueprints.put(id, loadBlueprint(new FsFile(Paths.ENTITY_FOLDER, id + "/settings.entity", false)));
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

    public static Blueprint loadBlueprint(FsFile file) {
        Blueprint blueprint = new Blueprint();
        FileReader reader = file.getFileReader(true);
        SettingsObject object = Tools.loadSettings(reader);

        for(Setting setting : blueprint.settings.getSettings())
            setting.loadSetting(reader.getParentDirectory(), object);

        for(ComponentType type : ComponentType.values()) {
            if(!object.containsKey(type.name())) continue;
            Object componentObject = object.get(type.name());
            if(!(componentObject instanceof SettingsObject)) continue;
            blueprint.addComponentBlueprint(type.load(reader.getParentDirectory(), (SettingsObject) object.get(type.name())));
        }

        reader.close();
        return blueprint;
    }

}
