package de.undefinedhuman.sandboxgame.editor.entity;

import de.undefinedhuman.sandboxgame.editor.entity.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.arm.LeftArmComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.arm.RightArmComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.arm.ShoulderComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.combat.CombatComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.effect.RegenerationComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.equip.EquipComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.eye.EyeComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.health.HealthComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.mouse.AngleComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgame.editor.entity.components.sprite.SpriteComponent;

import javax.swing.*;

public enum ComponentType {

    SPRITE, MOVEMENT, ANGLE, LEFTARM, RIGHTARM, COLLISION, EQUIP, REGENERATION, EYE, HEALTH, NAME, ANIMATION, SHOULDER, COMBAT;

    public static Component getComponent(ComponentType type, JPanel panel) {

        switch (type) {

            case SPRITE:
                return new SpriteComponent(panel,"SpriteComponent");
            case MOVEMENT:
                return new MovementComponent(panel,"MovementComponent");
            case ANGLE:
                return new AngleComponent(panel,"AngleComponent");
            case LEFTARM:
                return new LeftArmComponent(panel,"LeftArmComponent");
            case RIGHTARM:
                return new RightArmComponent(panel,"RightArmComponent");
            case EQUIP:
                return new EquipComponent(panel,"EquipComponent");
            case COLLISION:
                return new CollisionComponent(panel,"CollisionComponent");
            case REGENERATION:
                return new RegenerationComponent(panel,"Regeneration");
            case EYE:
                return new EyeComponent(panel,"EyeComponent");
            case NAME:
                return new NameComponent(panel,"NameComponent");
            case HEALTH:
                return new HealthComponent(panel,"HealthComponent");
            case ANIMATION:
                return new AnimationComponent(panel,"AnimationComponent");
            case SHOULDER:
                return new ShoulderComponent(panel,"ShoulderComponent");
            case COMBAT:
                return new CombatComponent(panel,"CombatComponent");

        }

        return null;

    }

}
