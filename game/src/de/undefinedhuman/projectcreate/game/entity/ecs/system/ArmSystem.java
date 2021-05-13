package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.arm.RightArmComponent;
import de.undefinedhuman.projectcreate.core.ecs.arm.ShoulderComponent;
import de.undefinedhuman.projectcreate.core.ecs.combat.CombatComponent;
import de.undefinedhuman.projectcreate.core.ecs.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.mouse.AngleComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.ecs.stats.health.HealthComponent;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.core.items.types.weapons.Sword;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.entity.Entity;
import de.undefinedhuman.projectcreate.game.entity.EntityManager;
import de.undefinedhuman.projectcreate.game.entity.ecs.System;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.player.Selector;
import de.undefinedhuman.projectcreate.game.item.ItemManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

import java.util.ArrayList;

public class ArmSystem extends System {

    @Override
    public void update(float delta, Entity entity) {

        SpriteComponent spriteComponent;
        RightArmComponent rightArmComponent;
        AngleComponent angleComponent;
        EquipComponent equipComponent;
        ShoulderComponent shoulderComponent;
        AnimationComponent animationComponent;

        if ((spriteComponent = (SpriteComponent) entity.getComponent(SpriteComponent.class)) == null
                || (angleComponent = (AngleComponent) entity.getComponent(AngleComponent.class)) == null
                || (rightArmComponent = (RightArmComponent) entity.getComponent(RightArmComponent.class)) == null
                || (equipComponent = (EquipComponent) entity.getComponent(EquipComponent.class)) == null
                || (shoulderComponent = (ShoulderComponent) entity.getComponent(ShoulderComponent.class)) == null
                || (animationComponent = (AnimationComponent) entity.getComponent(AnimationComponent.class)) == null) return;

        SpriteData data = spriteComponent.getSpriteData(rightArmComponent.getSelectedTexture());
        Vector2 mousePos = angleComponent.mousePos;
        Vector2 shoulderPosition = new Vector2(shoulderComponent.getShoulderPos(animationComponent.getAnimationFrameIndex()));
        shoulderPosition.set((angleComponent.isTurned ? shoulderPosition.x : entity.getSize().x - shoulderPosition.x), shoulderPosition.y);

        if (entity.mainPlayer) {
            float angle = new Vector2(mousePos).sub(shoulderPosition).sub(entity.getPosition()).angle() + (angleComponent.isTurned ? 0 : 180);
            angle += angleComponent.isTurned ? 95 : -95;

            if (Selector.instance.getSelectedInvItem() != null) {
                Item item = ItemManager.getInstance().getItem(Selector.instance.getSelectedItemID());
                boolean hasSword = (item.type == ItemType.SWORD);
                CombatComponent combatComponent = (CombatComponent) entity.getComponent(CombatComponent.class);
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

        if (InventoryManager.instance.canUseItem() && item.canShake.getValue()) {

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

            if (!Gdx.input.isButtonPressed(0) && combatComponent.currentDamage > sword.damage.getValue() / 2) {

                if (combatComponent.charged || combatComponent.canAttack) {

                    combatComponent.charged = false;
                    combatComponent.canAttack = true;
                    combatComponent.currentDamage -= Main.delta * sword.damage.getValue() * 24 / 30;
                    currentAngle = Tools.swordLerpTurned(angleComponentAngle, 0, 24, !isTurned);

                    // TODO Make the collision with the hitbox not the entity itself

                    ArrayList<Entity> entitiesWithCollision = EntityManager.getInstance().getEntitiesWithCollision(combatEntity);

                    for (Entity entity : entitiesWithCollision) {
                        if (!entity.hasComponent(HealthComponent.class) || entity == combatEntity && !combatComponent.touchedEntityList.contains(entity)) continue;
                        SpriteData data = ((SpriteComponent) combatEntity.getComponent(SpriteComponent.class)).getSpriteData("Item Hitbox");
                        ((HealthComponent) entity.getComponent(HealthComponent.class)).damage(sword.damage.getValue());
                        combatComponent.touchedEntityList.add(entity);
                    }
                }

            } else {

                combatComponent.touchedEntityList.clear();
                combatComponent.canAttack = false;

                if (Gdx.input.isButtonPressed(0)) {

                    currentAngle = Tools.swordLerpTurned(angleComponentAngle, 180, 16 * sword.speed.getValue(), isTurned);
                    if (currentAngle == 180) combatComponent.charged = true;
                    combatComponent.currentDamage += 16 * sword.speed.getValue() * Main.delta * sword.damage.getValue() / 10;

                } else combatComponent.currentDamage -= Main.delta * sword.damage.getValue();

            }

            combatComponent.currentDamage = Math.min(combatComponent.currentDamage, sword.damage.getValue() * 1.5f);

        }

        return currentAngle;

    }

}