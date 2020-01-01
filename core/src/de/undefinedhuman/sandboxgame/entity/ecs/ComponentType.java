package de.undefinedhuman.sandboxgame.entity.ecs;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgame.entity.ecs.components.animation.AnimationBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.arm.RightArmBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.arm.ShoulderBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.collision.CollisionBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.combat.CombatBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.equip.EquipBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.interaction.InteractionBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.mouse.AngleBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.movement.MovementBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.name.NameBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.sprite.SpriteBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.stats.food.FoodBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.stats.health.HealthBlueprint;
import de.undefinedhuman.sandboxgame.entity.ecs.components.stats.mana.ManaBlueprint;

public enum ComponentType {

    TRANSFORM(null), SPRITE(new SpriteBlueprint()), MOVEMENT(new MovementBlueprint()), COLLISION(new CollisionBlueprint()),
    RIGHTARM(new RightArmBlueprint()), ANGLE(new AngleBlueprint()), HEALTH(new HealthBlueprint()), NAME(new NameBlueprint()), FOOD(new FoodBlueprint()),
    EQUIP(new EquipBlueprint()), COMBAT(new CombatBlueprint()), ANIMATION(new AnimationBlueprint()), SHOULDER(new ShoulderBlueprint()), MANA(new ManaBlueprint()),
    INTERACTION(new InteractionBlueprint());

    private ComponentBlueprint blueprint;

    ComponentType(ComponentBlueprint blueprint) {

        this.blueprint = blueprint;

    }

    public static ComponentBlueprint load(String name, FileReader reader, int id) {

        return ComponentType.valueOf(name).load(reader, id);

    }

    public ComponentBlueprint load(FileReader reader, int id) {

        ComponentBlueprint componentBlueprint = null;

        if (blueprint != null) {

            try {
                componentBlueprint = blueprint.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if (componentBlueprint != null) componentBlueprint.loadComponent(reader, id);

        }

        return componentBlueprint;

    }

}
