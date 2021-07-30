package de.undefinedhuman.projectcreate.core.ecs;

import de.undefinedhuman.projectcreate.core.ecs.angle.AngleBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.collision.CollisionBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.combat.CombatBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.equip.EquipBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.food.FoodBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.health.HealthBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.interaction.InteractionBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.mana.ManaBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.name.NameBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.rightarm.RightarmBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.shoulder.ShoulderBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.type.TypeBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;

import java.util.HashMap;

public class ComponentType {

    private static HashMap<String, Class<? extends ComponentBlueprint>> COMPONENT_BLUEPRINT_CLASSES = new HashMap<>();

    static {
        registerComponentType(
                AngleBlueprint.class,
                AnimationBlueprint.class,
                CollisionBlueprint.class,
                CombatBlueprint.class,
                EquipBlueprint.class,
                FoodBlueprint.class,
                HealthBlueprint.class,
                InteractionBlueprint.class,
                ManaBlueprint.class,
                MovementBlueprint.class,
                NameBlueprint.class,
                RightarmBlueprint.class,
                ShoulderBlueprint.class,
                SpriteBlueprint.class,
                TransformBlueprint.class,
                TypeBlueprint.class
        );
    }

    public static void registerComponentType(Class<? extends ComponentBlueprint>... blueprintClasses) {
        for(Class<? extends ComponentBlueprint> blueprintClass : blueprintClasses)
            COMPONENT_BLUEPRINT_CLASSES.put(ComponentBlueprint.getName(blueprintClass), blueprintClass);
    }

    public static String[] getComponentTypes() {
        return COMPONENT_BLUEPRINT_CLASSES.keySet().toArray(new String[0]);
    }

    public static Class<? extends ComponentBlueprint> getComponentClass(String name) {
        return COMPONENT_BLUEPRINT_CLASSES.getOrDefault(name, null);
    }

}
