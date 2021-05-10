package de.undefinedhuman.projectcreate.core.entity.ecs.blueprint;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.core.utils.Tools;
import de.undefinedhuman.projectcreate.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.utils.Manager;

import java.util.Arrays;
import java.util.HashMap;

public class BlueprintManager extends Manager {

    private static volatile BlueprintManager instance;

    private HashMap<Integer, Blueprint> blueprints;

    private BlueprintManager() {
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
            if (!hasBlueprint(id))
                blueprints.put(id, loadBlueprint(new FsFile(Paths.ENTITY_PATH, id + "/settings.entity", Files.FileType.Internal)));
            loaded |= hasBlueprint(id);
        }
        Log.debug(() -> {
            Object[] loadedBlueprints = Arrays.stream(ids).filter(id -> blueprints.containsKey(id)).toArray();
            return "Loaded blueprint" + Tools.appendSToString(loadedBlueprints.length) + ": " + Tools.convertArrayToPrintableString(loadedBlueprints);
        });
        return loaded;
    }

    public boolean hasBlueprint(int id) {
        return blueprints.containsKey(id);
    }

    @Override
    public void delete() {
        for (Blueprint blueprint : blueprints.values())
            blueprint.delete();
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
            setting.loadSetting(reader.parent(), object);

        for(ComponentType type : ComponentType.values()) {
            if(!object.containsKey(type.name())) continue;
            Object componentObject = object.get(type.name());
            if(!(componentObject instanceof SettingsObject)) continue;
            blueprint.addComponentBlueprint(type.createInstance(reader.parent(), (SettingsObject) object.get(type.name())));
        }

        reader.close();
        return blueprint;
    }

    public static BlueprintManager getInstance() {
        if (instance == null) {
            synchronized (BlueprintManager.class) {
                if (instance == null)
                    instance = new BlueprintManager();
            }
        }
        return instance;
    }

}
