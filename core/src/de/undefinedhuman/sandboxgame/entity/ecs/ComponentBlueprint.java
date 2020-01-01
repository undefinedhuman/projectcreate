package de.undefinedhuman.sandboxgame.entity.ecs;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.entity.Entity;

import java.util.HashMap;

public abstract class ComponentBlueprint {

    protected ComponentType type;

    public ComponentBlueprint() {}

    public ComponentType getType() {
        return type;
    }

    public abstract Component createInstance(Entity entity, HashMap<ComponentType, ComponentParam> params);

    public void loadComponent(FileReader reader, int id) {

        HashMap<String, LineSplitter> settings = new HashMap<>();
        int size = reader.getNextInt();
        for(int i = 0; i < size; i++) {

            reader.nextLine();
            settings.put(reader.getNextString(), new LineSplitter(reader.nextLine(),true,";"));

        }

        load(settings, id);
        settings.clear();

    }

    public abstract void load(HashMap<String, LineSplitter> settings, int id);
    public abstract void delete();

}
