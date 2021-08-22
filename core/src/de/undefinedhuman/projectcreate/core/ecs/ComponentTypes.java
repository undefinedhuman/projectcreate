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
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.component.ComponentBlueprint;

import java.util.Arrays;
import java.util.List;

public class ComponentTypes {

    private static final Class<? extends ComponentBlueprint>[] COMPONENT_BLUEPRINT_CLASSES = new Class[] {
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
    };

    @SafeVarargs
    public static void registerComponentTypes(BlueprintManager blueprintManager, Class<? extends ComponentBlueprint>... except) {
        List<Class<? extends ComponentBlueprint>> exceptList = Arrays.asList(except);
        blueprintManager.registerComponentBlueprints(
                Arrays.stream(COMPONENT_BLUEPRINT_CLASSES).filter(aClass -> !exceptList.contains(aClass))
        );
    }

}
