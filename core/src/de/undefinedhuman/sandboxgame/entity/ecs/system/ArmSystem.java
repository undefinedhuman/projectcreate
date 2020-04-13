package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.Main;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.arm.RightArmComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.arm.ShoulderComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.combat.CombatComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.equip.EquipComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.mouse.AngleComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.sandboxgame.engine.entity.components.stats.health.HealthComponent;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.engine.items.type.weapons.Sword;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.ArrayList;

public class ArmSystem extends System {

    public static ArmSystem instance;

    public ArmSystem() {
        if (instance == null) instance = this;
    }

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

                SpriteData data = spriteComponent.getSpriteDataValue(rightArmComponent.getSelectedTexture());
                Vector2 mousePos = entity.mainPlayer ? Tools.getWorldCoordsOfMouse(GameManager.gameCamera) : angleComponent.mousePos;
                Vector2 shoulderPos = new Vector2();

                if (entity.mainPlayer) {

                    if (!angleComponent.isTurned) {
                        shoulderPos.x = (entity.getPosition().x + (entity.getSize().x / 2 + rightArmComponent.shoulderPosOffset.x));
                    } else {
                        shoulderPos.x = (entity.getPosition().x + entity.getSize().x - (entity.getSize().x / 2 + rightArmComponent.shoulderPosOffset.x));
                    }

                    shoulderPos.y = (entity.getPosition().y + entity.getSize().y / 2 + rightArmComponent.shoulderPosOffset.y);

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

                Vector2 shoulderPosition = shoulderComponent.getShoulderPos(animationComponent.getAnimationFrameIndex());

                spriteComponent.getSpriteDataValue(rightArmComponent.getTextureName()).setVisible(!selected);
                spriteComponent.getSpriteDataValue(rightArmComponent.getSelectedTexture()).setVisible(selected);

                if (!selected) return;
                data.setOrigin((angleComponent.isTurned ? shoulderPosition.x + 1 : entity.getSize().x - shoulderPosition.x - 1), shoulderPosition.y);
                data.setPositionOffset(angleComponent.isTurned ? 1 : -1, 0);
                data.setRotation(angleComponent.angle);

            }

        }

    }

    @Override
    public void render(SpriteBatch batch) {}

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

                    ArrayList<Entity> probablyHitEntity = EntityManager.instance.getEntityInRangeForCollision(combatEntity.getPosition(), 200);

                    for (Entity entity : probablyHitEntity) {

                        if (!entity.hasComponents(ComponentType.COLLISION, ComponentType.HEALTH)
                                || entity == combatEntity && !combatComponent.touchedEntityList.contains(entity)) continue;

                        SpriteData data = ((SpriteComponent) combatEntity.getComponent(ComponentType.SPRITE)).getSpriteDataValue("Item Hitbox");
                        if (!Tools.collideSAT(data.getSprite(), ((CollisionComponent) entity.getComponent(ComponentType.COLLISION)).getVertices(entity.getPosition()))) continue;
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