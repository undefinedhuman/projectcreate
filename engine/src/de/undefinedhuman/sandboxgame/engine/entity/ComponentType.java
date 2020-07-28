package de.undefinedhuman.sandboxgame.engine.entity;

import de.undefinedhuman.sandboxgame.engine.entity.components.animation.AnimationBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.arm.RightArmBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.arm.ShoulderBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.collision.CollisionBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.combat.CombatBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.equip.EquipBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.interaction.InteractionBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.mouse.AngleBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.movement.MovementBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.name.NameBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.stats.food.FoodBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.stats.health.HealthBlueprint;
import de.undefinedhuman.sandboxgame.engine.entity.components.stats.mana.ManaBlueprint;
import de.undefinedhuman.sandboxgame.engine.file.FsFile;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsObject;

public enum ComponentType {

    SPRITE(new SpriteBlueprint()),
    MOVEMENT(new MovementBlueprint()), COLLISION(new CollisionBlueprint()),
    RIGHTARM(new RightArmBlueprint()), ANGLE(new AngleBlueprint()), NAME(new NameBlueprint()),
    HEALTH(new HealthBlueprint()), FOOD(new FoodBlueprint()), MANA(new ManaBlueprint()),
    EQUIP(new EquipBlueprint()), COMBAT(new CombatBlueprint()), ANIMATION(new AnimationBlueprint()), SHOULDER(new ShoulderBlueprint()),
    INTERACTION(new InteractionBlueprint());

    private ComponentBlueprint blueprint;

    ComponentType(ComponentBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    public ComponentBlueprint load(FsFile parentDir, SettingsObject settingsObject) {
        ComponentBlueprint componentBlueprint = null;
        if (blueprint != null) {
            try { componentBlueprint = blueprint.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
            if (componentBlueprint != null) componentBlueprint.load(parentDir, settingsObject);
        }
        return componentBlueprint;
    }

    public ComponentBlueprint createNewInstance() {
        try { return blueprint.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
        return null;
    }

}
