package de.undefinedhuman.projectcreate.engine.ecs;

import com.badlogic.gdx.Files;
import de.undefinedhuman.projectcreate.engine.file.FileReader;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObjectFileReader;
import de.undefinedhuman.projectcreate.engine.utils.Utils;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class BlueprintManager extends Manager {

    public static final int PLAYER_BLUEPRINT_ID = 0;

    private static volatile BlueprintManager instance;

    private final HashMap<Integer, Blueprint> blueprints;
    private HashMap<String, Class<? extends ComponentBlueprint>> componentBlueprintClasses;

    private BlueprintManager() {
        blueprints = new HashMap<>();
        componentBlueprintClasses = new HashMap<>();
    }

    @Override
    public void init() {
        super.init();
        loadBlueprints(0, 1);
    }

    @Override
    public void delete() {
        for (Blueprint blueprint : blueprints.values())
            blueprint.delete();
        blueprints.clear();
    }

    public void addBlueprint(int id, Blueprint blueprint) {
        if(hasBlueprint(id))
            return;
        blueprints.put(id, blueprint);
    }

    public boolean loadBlueprints(int... ids) {
        int[] loadedBlueprintIDs = Arrays.stream(ids)
                .filter(id -> !hasBlueprint(id) && RessourceUtils.existBlueprint(id))
                .peek(id -> {
                    Blueprint blueprint = loadBlueprint(id);
                    addBlueprint(id, blueprint);
                    blueprint.validate();
                })
                .filter(this::hasBlueprint)
                .toArray();
        int[] failedBlueprintIDs = Arrays.stream(ids).filter(id -> {
            if(hasBlueprint(id))
                return false;
            for (int loadedBlueprintID : loadedBlueprintIDs)
                if (loadedBlueprintID == id) return false;
            return true;
        }).toArray();
        if(failedBlueprintIDs.length > 0)
            Log.error("Error while loading blueprint" + Utils.appendSToString(failedBlueprintIDs.length) + ": " + Arrays.toString(Arrays.stream(failedBlueprintIDs).sorted().toArray()));
        if(loadedBlueprintIDs.length > 0)
            Log.debug("Blueprint" + Utils.appendSToString(loadedBlueprintIDs.length) + " loaded: " + Arrays.toString(Arrays.stream(loadedBlueprintIDs).sorted().toArray()));
        return loadedBlueprintIDs.length == ids.length;
    }

    public boolean hasBlueprint(int id) {
        return blueprints.containsKey(id);
    }

    public void registerComponentBlueprints(Stream<Class<? extends ComponentBlueprint>> componentBlueprintClasses) {
        componentBlueprintClasses.forEach(componentBlueprintClass -> this.componentBlueprintClasses.put(ComponentBlueprint.getName(componentBlueprintClass), componentBlueprintClass));
    }

    public ComponentBlueprint getComponentBlueprint(String name, int blueprintID) {
        if(!componentBlueprintClasses.containsKey(name))
            return null;
        ComponentBlueprint componentBlueprint;
        try {
            componentBlueprint = componentBlueprintClasses.get(name).newInstance();
            componentBlueprint.blueprintID = blueprintID;
        } catch (InstantiationException | IllegalAccessException ex) {
            Log.error("Error while creating instance for Component Blueprint: " + name + ", for blueprint ID: " + blueprintID, ex);
            return null;
        }
        return componentBlueprint;
    }

    public void removeBlueprints(int... ids) {
        for (int id : ids) {
            if (!hasBlueprint(id)) continue;
            blueprints.get(id).delete();
            blueprints.remove(id);
        }
    }

    public Set<Integer> getBlueprintIDs() {
        return blueprints.keySet();
    }

    public Entity createEntity(int blueprintID, long worldID) {
        Blueprint blueprint = getBlueprint(blueprintID);
        if(blueprint == null) return null;
        return blueprint.createInstance(worldID);
    }

    public Entity createEntity(int blueprintID, long worldID, int entityFlagMask) {
        Entity entity = createEntity(blueprintID, worldID);
        entity.flags = entityFlagMask;
        return entity;
    }


    public Blueprint getBlueprint(int id) {
        if (hasBlueprint(id) || loadBlueprints(id)) return blueprints.get(id);
        return hasBlueprint(0) ? getBlueprint(0) : null;
    }

    public Set<String> getComponentBlueprintClassKeys() {
        return componentBlueprintClasses.keySet();
    }

    private Blueprint loadBlueprint(int id) {
        FsFile file = new FsFile(Paths.ENTITY_PATH, id + "/settings.entity", Files.FileType.Internal);
        Blueprint blueprint = new Blueprint(id);
        FileReader reader = file.getFileReader(true);
        SettingsObject object = new SettingsObjectFileReader(reader);

        for(Map.Entry<String, Object> entry : object.getSettings().entrySet()) {
            if(!(entry.getValue() instanceof SettingsObject) || !componentBlueprintClasses.containsKey(entry.getKey()))
                continue;
            ComponentBlueprint componentBlueprint = getComponentBlueprint(entry.getKey(), id);
            if(componentBlueprint == null) continue;
            componentBlueprint.load(reader.parent(), (SettingsObject) entry.getValue());
            blueprint.addComponentBlueprint(componentBlueprint);
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
