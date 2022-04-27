package de.undefinedhuman.projectcreate.core.ecs;

import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.base.type.TypeBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.collision.CollisionBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.interaction.InteractionBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.player.combat.CombatBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.player.equip.EquipBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.stats.food.FoodBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.stats.health.HealthBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.stats.mana.ManaBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.stats.name.NameBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationBlueprint;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteBlueprint;
import de.undefinedhuman.projectcreate.engine.ecs.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.ComponentBlueprint;

import java.util.Arrays;
import java.util.List;

public class ComponentTypes {

    private static final Class<? extends ComponentBlueprint>[] COMPONENT_BLUEPRINT_CLASSES = new Class[] {
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
            SpriteBlueprint.class,
            TransformBlueprint.class,
            TypeBlueprint.class,
            MouseBlueprint.class,
            InventoryBlueprint.class
    };

    @SafeVarargs
    public static void registerComponentTypes(BlueprintManager blueprintManager, Class<? extends ComponentBlueprint>... except) {
        List<Class<? extends ComponentBlueprint>> exceptList = Arrays.asList(except);
        blueprintManager.registerComponentBlueprints(
                Arrays.stream(COMPONENT_BLUEPRINT_CLASSES).filter(componentBlueprintClass -> !exceptList.contains(componentBlueprintClass))
        );
    }

}
