package de.undefinedhuman.projectcreate.game.entity.ecs.blueprint;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.ecs.Component;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentParam;
import de.undefinedhuman.projectcreate.engine.ecs.EntityType;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.types.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.Vector2Setting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;
import de.undefinedhuman.projectcreate.game.entity.Entity;

import java.util.HashMap;

public class Blueprint {

    public SettingsList settings = new SettingsList();

    public IntSetting
            id = new IntSetting("ID", 0);
    public StringSetting
            name = new StringSetting("Name", "");
    public Vector2Setting
            size = new Vector2Setting("Size", new Vector2());
    public SelectionSetting<EntityType>
            type = new SelectionSetting<>("Type", EntityType.values(), value -> EntityType.valueOf(String.valueOf(value)));

    private HashMap<Class<? extends Component>, ComponentBlueprint> componentBlueprints;

    public Blueprint() {
        settings.addSettings(id, name, size, type);
        componentBlueprints = new HashMap<>();
    }

    public Entity createInstance(ComponentParam... param) {
        Entity entity = new Entity(this, size.getValue());
        HashMap<Class<? extends Component>, ComponentParam> params = new HashMap<>();
        for (ComponentParam p : param) params.put(p.getType(), p);
        for (ComponentBlueprint blueprint : componentBlueprints.values())
            entity.addComponents(blueprint.createInstance(params));
        entity.init();
        return entity;
    }

    public int getID() {
        return id.getValue();
    }
    public EntityType getType() {
        return type.getValue();
    }

    public void addComponentBlueprint(ComponentBlueprint blueprint) {
        componentBlueprints.putIfAbsent(blueprint.getType(), blueprint);
    }

    public ComponentBlueprint getComponentBlueprint(Class<? extends Component> type) {
        if(!hasComponentBlueprints(type)) return null;
        return componentBlueprints.get(type);
    }

    public boolean hasComponentBlueprints(Class<? extends Component> type) {
        return componentBlueprints.containsKey(type);
    }

    public HashMap<Class<? extends Component>, ComponentBlueprint> getComponentBlueprints() {
        return componentBlueprints;
    }

    public void delete() {
        for (ComponentBlueprint blueprint : componentBlueprints.values()) blueprint.delete();
    }

}
