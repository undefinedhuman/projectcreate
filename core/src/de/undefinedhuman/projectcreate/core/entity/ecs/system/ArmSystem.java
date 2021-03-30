package de.undefinedhuman.projectcreate.core.entity.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.Main;
import de.undefinedhuman.projectcreate.core.engine.entity.ComponentType;
import de.undefinedhuman.projectcreate.core.engine.entity.components.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.engine.entity.components.arm.RightArmComponent;
import de.undefinedhuman.projectcreate.core.engine.entity.components.arm.ShoulderComponent;
import de.undefinedhuman.projectcreate.core.engine.entity.components.combat.CombatComponent;
import de.undefinedhuman.projectcreate.core.engine.entity.components.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.engine.entity.components.mouse.AngleComponent;
import de.undefinedhuman.projectcreate.core.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.engine.entity.components.stats.health.HealthComponent;
import de.undefinedhuman.projectcreate.core.engine.items.Item;
import de.undefinedhuman.projectcreate.core.engine.items.ItemType;
import de.undefinedhuman.projectcreate.core.engine.items.type.weapons.Sword;
import de.undefinedhuman.projectcreate.core.entity.Entity;
import de.undefinedhuman.projectcreate.core.entity.EntityManager;
import de.undefinedhuman.projectcreate.core.entity.ecs.System;
import de.undefinedhuman.projectcreate.core.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.core.inventory.player.Selector;
import de.undefinedhuman.projectcreate.core.item.ItemManager;
import de.undefinedhuman.projectcreate.core.utils.Tools;

import java.util.ArrayList;

public class ArmSystem extends System {

    public static ArmSystem instance;

    public ArmSystem() {
        if (instance == null) instance = this;
    }

    @Override
    public void update(float delta, Entity entity) {

        SpriteComponent spriteComponent;
        RightArmComponent rightArmComponent;
        AngleComponent angleComponent;
        EquipComponent equipComponent;
        ShoulderComponent shoulderComponent;
        AnimationComponent animationComponent;

        if ((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) == null
                || (angleComponent = (AngleComponent) entity.getComponent(ComponentType.ANGLE)) == null
                || (rightArmComponent = (RightArmComponent) entity.getComponent(ComponentType.RIGHTARM)) == null
                || (equipComponent = (EquipComponent) entity.getComponent(ComponentType.EQUIP)) == null
                || (shoulderComponent = (ShoulderComponent) entity.getComponent(ComponentType.SHOULDER)) == null
                || (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) == null) return;

        SpriteData data = spriteComponent.getSpriteData(rightArmComponent.getSelectedTexture());
        Vector2 mousePos = angleComponent.mousePos;
        Vector2 shoulderPosition = new Vector2(shoulderComponent.getShoulderPos(animationComponent.getAnimationFrameIndex()));
        shoulderPosition.set((angleComponent.isTurned ? shoulderPosition.x : entity.getSize().x - shoulderPosition.x), shoulderPosition.y);

        if (entity.mainPlayer) {
            float angle = new Vector2(mousePos).sub(shoulderPosition).sub(entity.getPosition()).angle() + (angleComponent.isTurned ? 0 : 180);
            angle += angleComponent.isTurned ? 95 : -95;

            if (Selector.instance.getSelectedInvItem() != null) {
                Item item = ItemManager.instance.getItem(Selector.instance.getSelectedItemID());
                boolean hasSword = (item.type == ItemType.SWORD);
                CombatComponent combatComponent = (CombatComponent) entity.getComponent(ComponentType.COMBAT);
                calculateShake(rightArmComponent, item);

                if (hasSword && combatComponent != null)
                    angle = animationSword(entity, equipComponent, combatComponent, angleComponent.isTurned, angleComponent.angle, angle, (Sword) item);
                if (combatComponent != null)
                    if (combatComponent.currentDamage < 0 || !hasSword) combatComponent.currentDamage = 0;

                angleComponent.angle = angle + rightArmComponent.shakeAngle;
            }

        }

        boolean selected = Tools.isItemSelected(entity);

        spriteComponent.getSpriteData(rightArmComponent.getTextureName()).setVisible(!selected);
        spriteComponent.getSpriteData(rightArmComponent.getSelectedTexture()).setVisible(selected);

        if (!selected) return;
        data.setOrigin(shoulderPosition);
        data.setRotation(angleComponent.angle);
    }

    private void calculateShake(RightArmComponent component, Item item) {

        if (InventoryManager.instance.canUseItem() && item.canShake.getBoolean()) {

            if (Gdx.input.isButtonPressed(0) || Gdx.input.isButtonPressed(1)) {

                component.shakeAngle += (component.shakeDirection ? 200 : -200) * Main.delta;
                component.shakeDirection = component.shakeDirection ? !(component.shakeAngle > 20) : component.shakeAngle < -20;

            } else component.shakeAngle = component.shakeAngle - component.shakeAngle * Main.delta * 10f;

        } else component.shakeAngle = 0;

    }

    private float animationSword(Entity combatEntity, EquipComponent equipComponent, CombatComponent combatComponent, boolean isTurned, float angleComponentAngle, float currentAngle, Sword sword) {

        // TODO Sword Animation überarbeiten und weg von der currentDamage vllt. einfach nen smootheren Übergang machen sobald die Attacke vorbei ist

        // 24 Sword Downtime, 16 Sword Uptime

        if (InventoryManager.instance.canUseItem()) {

            if (!Gdx.input.isButtonPressed(0) && combatComponent.currentDamage > sword.damage.getFloat() / 2) {

                if (combatComponent.charged || combatComponent.canAttack) {

                    combatComponent.charged = false;
                    combatComponent.canAttack = true;
                    combatComponent.currentDamage -= Main.delta * sword.damage.getFloat() * 24 / 30;
                    currentAngle = Tools.swordLerpTurned(angleComponentAngle, 0, 24, !isTurned);

                    // TODO Make the collision with the hitbox not the entity itself

                    ArrayList<Entity> entitiesWithCollision = EntityManager.instance.getEntitiesWithCollision(combatEntity);

                    for (Entity entity : entitiesWithCollision) {
                        if (!entity.hasComponent(ComponentType.HEALTH) || entity == combatEntity && !combatComponent.touchedEntityList.contains(entity)) continue;
                        SpriteData data = ((SpriteComponent) combatEntity.getComponent(ComponentType.SPRITE)).getSpriteData("Item Hitbox");
                        ((HealthComponent) entity.getComponent(ComponentType.HEALTH)).damage(sword.damage.getFloat());
                        combatComponent.touchedEntityList.add(entity);
                    }
                }

            } else {

                combatComponent.touchedEntityList.clear();
                combatComponent.canAttack = false;

                if (Gdx.input.isButtonPressed(0)) {

                    currentAngle = Tools.swordLerpTurned(angleComponentAngle, 180, 16 * sword.speed.getFloat(), isTurned);
                    if (currentAngle == 180) combatComponent.charged = true;
                    combatComponent.currentDamage += 16 * sword.speed.getFloat() * Main.delta * sword.damage.getFloat() / 10;

                } else combatComponent.currentDamage -= Main.delta * sword.damage.getFloat();

            }

            combatComponent.currentDamage = Math.min(combatComponent.currentDamage, sword.damage.getFloat() * 1.5f);

        }

        return currentAngle;

    }

}