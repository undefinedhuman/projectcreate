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

public enum ComponentTypeTrad {

    ANGLE,
    ANIMATION,
    SHOULDER,
    RIGHTARM,
    COLLISION,
    COMBAT,
    EQUIP,
    INTERACTION,
    MOVEMENT,
    NAME,
    SPRITE,
    FOOD,
    HEALTH,
    MANA;

    public static ComponentBlueprint createNewInstance(ComponentType type) {
        switch (type) {
            case ANGLE:
                return new AngleBlueprint();
            case ANIMATION:
                return new AnimationBlueprint();
            case SHOULDER:
                return new ShoulderBlueprint();
            case RIGHTARM:
                return new RightArmBlueprint();
            case COLLISION:
                return new CollisionBlueprint();
            case COMBAT:
                return new CombatBlueprint();
            case EQUIP:
                return new EquipBlueprint();
            case INTERACTION:
                return new InteractionBlueprint();
            case MOVEMENT:
                return new MovementBlueprint();
            case NAME:
                return new NameBlueprint();
            case SPRITE:
                return new SpriteBlueprint();
            case FOOD:
                return new FoodBlueprint();
            case HEALTH:
                return new HealthBlueprint();
            case MANA:
                return new ManaBlueprint();
        }
        throw new IllegalArgumentException("No Component Blueprint defined for type " + type.name());
    }

}
