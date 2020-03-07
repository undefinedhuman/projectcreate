package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.entity.ecs.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.arm.RightArmComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.arm.ShoulderComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.combat.CombatComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.equip.EquipComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.mouse.AngleComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.sprite.SpriteData;
import de.undefinedhuman.sandboxgame.entity.ecs.components.stats.health.HealthComponent;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.items.Item;
import de.undefinedhuman.sandboxgame.items.ItemManager;
import de.undefinedhuman.sandboxgame.items.ItemType;
import de.undefinedhuman.sandboxgame.items.type.weapons.Sword;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.ArrayList;

public class ArmSystem extends System {

    public static ArmSystem instance;

    public ArmSystem() {}

    @Override
    public void init(Entity entity) {}

    @Override
    public void update(float delta, Entity entity) {

        SpriteComponent spriteComponent;
        RightArmComponent rightArmComponent;
        AngleComponent angleComponent;
        EquipComponent equipComponent;
        ShoulderComponent shoulderComponent;
        AnimationComponent animationComponent;

        if ((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) != null && (angleComponent = (AngleComponent) entity.getComponent(ComponentType.ANGLE)) != null) {

            if ((rightArmComponent = (RightArmComponent) entity.getComponent(ComponentType.RIGHTARM)) != null && (equipComponent = (EquipComponent) entity.getComponent(ComponentType.EQUIP)) != null && (shoulderComponent = (ShoulderComponent) entity.getComponent(ComponentType.SHOULDER)) != null && (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) != null) {

                SpriteData data = spriteComponent.getSpriteData(rightArmComponent.getSelectedTexture());
                Vector2 mousePos = entity.mainPlayer ? Tools.getWorldCoordsOfMouse(GameManager.gameCamera) : angleComponent.mousePos;
                Vector2 shoulderPos = new Vector2();

                if (entity.mainPlayer) {

                    if (!angleComponent.isTurned) {
                        shoulderPos.x = (entity.transform.getPosition().x + (entity.transform.getSize().x / 2 + rightArmComponent.shoulderPosOffset.x));
                    } else {
                        shoulderPos.x = (entity.transform.getPosition().x + entity.transform.getSize().x - (entity.transform.getSize().x / 2 + rightArmComponent.shoulderPosOffset.x));
                    }

                    shoulderPos.y = (entity.transform.getPosition().y + entity.transform.getSize().y / 2 + rightArmComponent.shoulderPosOffset.y);

                    float angle = new Vector2(mousePos.x - shoulderPos.x, mousePos.y - shoulderPos.y).angle() + (angleComponent.isTurned ? 0 : 180);
                    angle += angleComponent.isTurned ? 95 : -95;

                    if (InventoryManager.instance.getSelector().getSelectedInvItem() != null) {

                        Item item = ItemManager.instance.getItem(InventoryManager.instance.getSelector().getSelectedItemID());
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

                Vector2 shoulderPosition = shoulderComponent.getShoulderPos(animationComponent.getCurrentAnimationFrameID());

                spriteComponent.getSpriteData(rightArmComponent.getTextureName()).setVisible(!selected);
                spriteComponent.getSpriteData(rightArmComponent.getSelectedTexture()).setVisible(selected);

                if (!selected) return;
                data.setOrigin((angleComponent.isTurned ? shoulderPosition.x + 1 : entity.transform.getSize().x - shoulderPosition.x - 1), shoulderPosition.y);
                data.setPositionOffset(angleComponent.isTurned ? 1 : -1, 0);
                data.setRotation(angleComponent.angle);

            }

        }

    }

    @Override
    public void render(SpriteBatch batch) {}

    private void calculateShake(RightArmComponent component, Item item) {

        if (InventoryManager.instance.canUseItem() && item.canShake) {

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

            if (!Gdx.input.isButtonPressed(0) && combatComponent.currentDamage > sword.damage / 2) {

                if (combatComponent.charged || combatComponent.canAttack) {

                    combatComponent.charged = false;
                    combatComponent.canAttack = true;
                    combatComponent.currentDamage -= Main.delta * sword.damage * 24 / 30;
                    currentAngle = Tools.swordLerpTurned(angleComponentAngle, 0, 24, !isTurned);

                    ArrayList<Entity> probablyHitEntity = EntityManager.instance.getEntityInRangeForCollision(combatEntity.transform.getPosition(), 200);

                    for (Entity entity : probablyHitEntity) {

                        if (entity.hasComponents(ComponentType.TRANSFORM, ComponentType.COLLISION, ComponentType.HEALTH) && entity != combatEntity && !combatComponent.touchedEntityList.contains(entity)) {

                            SpriteData data = ((SpriteComponent) combatEntity.getComponent(ComponentType.SPRITE)).getSpriteData("ItemHitbox");
                            if (Tools.collideSAT(data.getSprite(), ((CollisionComponent) entity.getComponent(ComponentType.COLLISION)).getVertices(entity.transform.getPosition()))) {

                                ((HealthComponent) entity.getComponent(ComponentType.HEALTH)).damage(sword.damage);
                                combatComponent.touchedEntityList.add(entity);

                            }

                        }

                    }
                }

            } else {

                combatComponent.touchedEntityList.clear();
                combatComponent.canAttack = false;

                if (Gdx.input.isButtonPressed(0)) {

                    currentAngle = Tools.swordLerpTurned(angleComponentAngle, 180, 16 * sword.speed, isTurned);
                    if (currentAngle == 180) combatComponent.charged = true;
                    combatComponent.currentDamage += 16 * sword.speed * Main.delta * sword.damage / 10;

                } else combatComponent.currentDamage -= Main.delta * sword.damage;

            }

            if (combatComponent.currentDamage > sword.damage * 1.5f)
                combatComponent.currentDamage = sword.damage * 1.5f;

        }

        return currentAngle;

    }

}