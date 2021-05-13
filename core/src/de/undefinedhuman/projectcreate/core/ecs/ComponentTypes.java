package de.undefinedhuman.projectcreate.core.ecs;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.arm.RightArmBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.arm.ShoulderBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.collision.CollisionBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.combat.CombatBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.equip.EquipBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.interaction.InteractionBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.mouse.AngleBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.name.NameBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.stats.food.FoodBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.stats.health.HealthBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.stats.mana.ManaBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ComponentTypes {

    static final HashMap<String, Class<? extends ComponentBlueprint>> AVAILABLE_COMPONENTS = new HashMap<String, Class<? extends ComponentBlueprint>>() {{
        put(generateName(AngleBlueprint.class), AngleBlueprint.class);
        put(generateName(AnimationBlueprint.class), AnimationBlueprint.class);
        put(generateName(ShoulderBlueprint.class), ShoulderBlueprint.class);
        put(generateName(RightArmBlueprint.class), RightArmBlueprint.class);
        put(generateName(CollisionBlueprint.class), CollisionBlueprint.class);
        put(generateName(CombatBlueprint.class), CombatBlueprint.class);
        put(generateName(EquipBlueprint.class), EquipBlueprint.class);
        put(generateName(InteractionBlueprint.class), InteractionBlueprint.class);
        put(generateName(MovementBlueprint.class), MovementBlueprint.class);
        put(generateName(NameBlueprint.class), NameBlueprint.class);
        put(generateName(SpriteBlueprint.class), SpriteBlueprint.class);
        put(generateName(FoodBlueprint.class), FoodBlueprint.class);
        put(generateName(HealthBlueprint.class), HealthBlueprint.class);
        put(generateName(ManaBlueprint.class), ManaBlueprint.class);
    }};

    private static String generateName(Class<? extends ComponentBlueprint> componentClass) {
        String[] nameData = componentClass.getSimpleName().split("Blueprint");
        return nameData.length >= 1 ? nameData[0].toUpperCase() : componentClass.getSimpleName().toUpperCase();
    }

    public static Collection<String> keySet() {
        return AVAILABLE_COMPONENTS.keySet().stream().sorted().collect(Collectors.toList());
    }

    public static ComponentBlueprint createNewInstance(String name) {
        Class<? extends ComponentBlueprint> componentBlueprintClass = AVAILABLE_COMPONENTS.get(name);
        ComponentBlueprint componentBlueprintInstance = null;
        try {
            componentBlueprintInstance = componentBlueprintClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Log.error("Error during " + name + " component blueprint creation:", ex);
        }
        return componentBlueprintInstance;
    }

    public static ComponentBlueprint loadComponentBlueprint(String name, FileHandle parentDir, SettingsObject settingsObject) {
        ComponentBlueprint blueprint = createNewInstance(name);
        if(blueprint == null)
            return null;
        blueprint.load(parentDir, settingsObject);
        return blueprint;
    }

}
