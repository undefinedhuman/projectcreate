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
import de.undefinedhuman.projectcreate.engine.log.Log;

public enum ComponentTypeOld {

    ANGLE(AngleBlueprint .class),
    ANIMATION(AnimationBlueprint .class),
    SHOULDER(ShoulderBlueprint .class),
    RIGHTARM(RightArmBlueprint .class),
    COLLISION(CollisionBlueprint .class),
    COMBAT(CombatBlueprint .class),
    EQUIP(EquipBlueprint .class),
    INTERACTION(InteractionBlueprint .class),
    MOVEMENT(MovementBlueprint .class),
    NAME(NameBlueprint .class),
    SPRITE(SpriteBlueprint .class),
    FOOD(FoodBlueprint .class),
    HEALTH(HealthBlueprint .class),
    MANA(ManaBlueprint .class);

    private Class<? extends ComponentBlueprint> blueprintClass;

    ComponentTypeOld(Class<? extends ComponentBlueprint> blueprintClass) {
        this.blueprintClass = blueprintClass;
    }

    public static ComponentBlueprint createNewInstance(ComponentTypeOld type) {
        ComponentBlueprint componentBlueprintInstance = null;
        try {
            componentBlueprintInstance = type.blueprintClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Log.error("Error during " + type.name() + " component blueprint creation:", ex);
        }
        return componentBlueprintInstance;
    }

}
