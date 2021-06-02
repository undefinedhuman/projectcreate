package de.undefinedhuman.projectcreate.core.ecs;

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

import java.util.function.Supplier;

public enum ComponentType {

    ANGLE(AngleBlueprint::new),
    ANIMATION(AnimationBlueprint::new),
    SHOULDER(ShoulderBlueprint::new),
    RIGHTARM(RightArmBlueprint::new),
    COLLISION(CollisionBlueprint::new),
    COMBAT(CombatBlueprint::new),
    EQUIP(EquipBlueprint::new),
    INTERACTION(InteractionBlueprint::new),
    MOVEMENT(MovementBlueprint::new),
    NAME(NameBlueprint::new),
    SPRITE(SpriteBlueprint::new),
    FOOD(FoodBlueprint::new),
    HEALTH(HealthBlueprint::new),
    MANA(ManaBlueprint::new);

    private Supplier<ComponentBlueprint> componentBlueprint;

    ComponentType(Supplier<ComponentBlueprint> componentBlueprintFunction) {
        this.componentBlueprint = componentBlueprintFunction;
    }

    public static ComponentBlueprint createNewInstance(ComponentType type) {
        return type.componentBlueprint.get();
    }

}
