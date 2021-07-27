package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.angle.AngleComponent;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.combat.CombatComponent;
import de.undefinedhuman.projectcreate.core.ecs.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.rightarm.RightArmComponent;
import de.undefinedhuman.projectcreate.core.ecs.shoulder.ShoulderComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.core.items.weapons.Sword;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.player.Selector;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class ArmSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    public ArmSystem() {
        super(2);
    }

    @Override
    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(Family.all(TransformComponent.class, SpriteComponent.class, RightArmComponent.class, AngleComponent.class, EquipComponent.class, ShoulderComponent.class, AnimationComponent.class).get());
    }

    @Override
    public void update(float delta) {
        TransformComponent transformComponent;
        SpriteComponent spriteComponent;
        RightArmComponent rightArmComponent;
        AngleComponent angleComponent;
        EquipComponent equipComponent;
        ShoulderComponent shoulderComponent;
        AnimationComponent animationComponent;

        for(Entity entity : entities) {

            transformComponent = Mappers.TRANSFORM.get(entity);
            spriteComponent = Mappers.SPRITE.get(entity);
            rightArmComponent = Mappers.RIGHT_ARM.get(entity);
            angleComponent = Mappers.ANGLE.get(entity);
            equipComponent = Mappers.EQUIP.get(entity);
            shoulderComponent = Mappers.SHOULDER.get(entity);
            animationComponent = Mappers.ANIMATION.get(entity);

            SpriteData data = spriteComponent.getSpriteData(rightArmComponent.getSelectedTexture());
            Vector2 mousePos = angleComponent.mousePos;
            Vector2 shoulderPosition = shoulderComponent.getShoulderPos(animationComponent.getAnimationFrameIndex());
            shoulderPosition.set((angleComponent.isTurned ? shoulderPosition.x : transformComponent.getWidth() - shoulderPosition.x), shoulderPosition.y);

            if (GameManager.instance.player == entity) {
                float angle = new Vector2(mousePos).sub(shoulderPosition).sub(transformComponent.getPosition()).angleDeg() + (angleComponent.isTurned ? 0 : 180);
                angle += angleComponent.isTurned ? 95 : -95;

                if (Selector.getInstance().getSelectedInvItem() != null) {
                    Item item = ItemManager.getInstance().getItem(Selector.getInstance().getSelectedItemID());
                    boolean hasSword = (item.type == ItemType.SWORD);
                    CombatComponent combatComponent = entity.getComponent(CombatComponent.class);
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

    }

    private void calculateShake(RightArmComponent component, Item item) {

        if (InventoryManager.getInstance().canUseItem() && item.canShake.getValue()) {

            if (Gdx.input.isButtonPressed(0) || Gdx.input.isButtonPressed(1)) {

                component.shakeAngle += (component.shakeDirection ? 200 : -200) * Main.delta;
                component.shakeDirection = component.shakeDirection ? !(component.shakeAngle > 20) : component.shakeAngle < -20;

            } else component.shakeAngle = component.shakeAngle - component.shakeAngle * Main.delta * 10f;

        } else component.shakeAngle = 0;

    }

    private float animationSword(Entity combatEntity, EquipComponent equipComponent, CombatComponent combatComponent, boolean isTurned, float angleComponentAngle, float currentAngle, Sword sword) {

        // TODO Sword Animation überarbeiten und weg von der currentDamage vllt. einfach nen smootheren Übergang machen sobald die Attacke vorbei ist

        // 24 Sword Downtime, 16 Sword Uptime

        if (InventoryManager.getInstance().canUseItem()) {

            if (!Gdx.input.isButtonPressed(0) && combatComponent.currentDamage > sword.damage.getValue() / 2) {

                if (combatComponent.charged || combatComponent.canAttack) {

                    combatComponent.charged = false;
                    combatComponent.canAttack = true;
                    combatComponent.currentDamage -= Main.delta * sword.damage.getValue() * 24 / 30;
                    currentAngle = Tools.swordLerpTurned(angleComponentAngle, 0, 24, !isTurned);

                    // TODO Make the collision with the hitbox not the entity itself

                    /*ArrayList<Entity> entitiesWithCollision = EntityManager.getInstance().getEntitiesWithCollision(combatEntity);

                    for (Entity entity : entitiesWithCollision) {
                        if (entity.getComponent(HealthComponent.class) != null || entity == combatEntity && !combatComponent.touchedEntityList.contains(entity)) continue;
                        SpriteData data = ((SpriteComponent) combatEntity.getComponent(SpriteComponent.class)).getSpriteData("Item Hitbox");
                        ((HealthComponent) entity.getComponent(HealthComponent.class)).damage(sword.damage.getValue());
                        combatComponent.touchedEntityList.add(entity);
                    }*/
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