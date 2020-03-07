package de.undefinedhuman.sandboxgameserver.entity.ecs.blueprint;

import de.undefinedhuman.sandboxgameserver.utils.ResourceManager;

import java.util.HashMap;

public class BlueprintManager {

    public static BlueprintManager instance;

    private HashMap<Integer, Blueprint> blueprints;

    public BlueprintManager() {
        blueprints = new HashMap<>();
    }

    public void addBlueprint(Blueprint blueprint) {
        this.blueprints.put(blueprint.getID(), blueprint);
    }

    public void addBlueprint(int... ids) {
        for (int id : ids) loadBlueprint(id);
    }

    public boolean loadBlueprint(int id) {
        if (!hasBlueprint(id)) blueprints.put(id, ResourceManager.instance.loadBlueprint(id));
        return hasBlueprint(id);
    }

    public boolean hasBlueprint(int id) {
        return blueprints.containsKey(id);
    }

    public void removeBlueprints(int... ids) {
        for (int id : ids) removeBlueprint(id);
    }

    public void removeBlueprint(int id) {

        if (hasBlueprint(id)) {

            blueprints.get(id).delete();
            blueprints.remove(id);

        }

    }

    public Blueprint getBlueprint(int id) {
        if (hasBlueprint(id)) return blueprints.get(id);
        else if (loadBlueprint(id)) return blueprints.get(id);
        return hasBlueprint(0) ? getBlueprint(0) : null;
    }

    public void delete() {
        for (Blueprint blueprint : blueprints.values()) blueprint.delete();
        blueprints.clear();
    }

}
