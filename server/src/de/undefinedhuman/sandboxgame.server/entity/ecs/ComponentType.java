package de.undefinedhuman.sandboxgameserver.entity.ecs;

import de.undefinedhuman.sandboxgame.engine.file.FileReader;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.arm.LeftArmBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.arm.RightArmBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.collision.CollisionBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.combat.CombatBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.equip.EquipBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.food.FoodBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.health.HealthBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.mouse.AngleBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.movement.MovementBlueprint;
import de.undefinedhuman.sandboxgameserver.entity.ecs.components.name.NameBlueprint;

public enum ComponentType {

    TRANSFORM(null), MOVEMENT(new MovementBlueprint()), COLLISION(new CollisionBlueprint()), FOOD(new FoodBlueprint()),
    LEFTARM(new LeftArmBlueprint()), RIGHTARM(new RightArmBlueprint()), ANGLE(new AngleBlueprint()), HEALTH(new HealthBlueprint()), NAME(new NameBlueprint()),
    COMBAT(new CombatBlueprint()), EQUIP(new EquipBlueprint());

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

            if (componentBlueprint != null) componentBlueprint.load(reader, id);

        }

        return componentBlueprint;

    }

    public static boolean hasType(String name) {
        for (ComponentType type : values()) if (type.name().equalsIgnoreCase(name)) return true;
        return false;
    }

}
