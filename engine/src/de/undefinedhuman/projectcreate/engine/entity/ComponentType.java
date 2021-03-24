package de.undefinedhuman.projectcreate.engine.entity;

import de.undefinedhuman.projectcreate.engine.entity.components.animation.AnimationBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.arm.RightArmBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.arm.ShoulderBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.collision.CollisionBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.combat.CombatBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.equip.EquipBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.interaction.InteractionBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.mouse.AngleBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.movement.MovementBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.name.NameBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.sprite.SpriteBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.stats.food.FoodBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.stats.health.HealthBlueprint;
import de.undefinedhuman.projectcreate.engine.entity.components.stats.mana.ManaBlueprint;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.settings.SettingsObject;

public enum ComponentType {

    SPRITE(SpriteBlueprint.class),
    MOVEMENT(MovementBlueprint.class),
    COLLISION(CollisionBlueprint.class),
    RIGHTARM(RightArmBlueprint.class),
    ANGLE(AngleBlueprint.class),
    NAME(NameBlueprint.class),
    HEALTH(HealthBlueprint.class),
    FOOD(FoodBlueprint.class),
    MANA(ManaBlueprint.class),
    EQUIP(EquipBlueprint.class),
    COMBAT(CombatBlueprint.class),
    ANIMATION(AnimationBlueprint.class),
    SHOULDER(ShoulderBlueprint.class),
    INTERACTION(InteractionBlueprint.class);

    private Class<? extends ComponentBlueprint> componentBlueprint;

    ComponentType(Class<? extends ComponentBlueprint> componentBlueprint) {
        this.componentBlueprint = componentBlueprint;
    }

    public ComponentBlueprint createInstance(FsFile parentDir, SettingsObject settingsObject) {
        ComponentBlueprint componentBlueprint = null;
        try { componentBlueprint = this.componentBlueprint.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Log.error("Could not create Component Blueprint Instance: " + ex.getMessage());
        }
        if(componentBlueprint != null) componentBlueprint.load(parentDir, settingsObject);
        return componentBlueprint;
    }

    public ComponentBlueprint createInstance() {
        try { return componentBlueprint.newInstance();
        } catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
        return null;
    }
}
