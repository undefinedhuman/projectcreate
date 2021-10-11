package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.EntityFlag;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.combat.CombatComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.rightarm.RightArmComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.shoulder.ShoulderComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.core.items.weapons.Sword;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.player.Selector;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class ArmSystem extends IteratingSystem {

    public ArmSystem() {
        super(Family.all(TransformComponent.class, SpriteComponent.class, RightArmComponent.class, MouseComponent.class, EquipComponent.class, ShoulderComponent.class, AnimationComponent.class).get(), 2);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        SpriteComponent spriteComponent = Mappers.SPRITE.get(entity);
        RightArmComponent rightArmComponent = Mappers.RIGHT_ARM.get(entity);
        MouseComponent mouseComponent = Mappers.MOUSE.get(entity);
        EquipComponent equipComponent = Mappers.EQUIP.get(entity);
        Vector2 shoulderPosition = Mappers.SHOULDER.get(entity).getShoulderPos(Mappers.ANIMATION.get(entity).getAnimationFrameIndex());

        SpriteData data = spriteComponent.getSpriteData(rightArmComponent.getSelectedTexture());
        shoulderPosition.set((mouseComponent.isTurned ? shoulderPosition.x : transformComponent.getWidth() - shoulderPosition.x), shoulderPosition.y);

        if (EntityFlag.hasFlags(entity, EntityFlag.IS_MAIN_PLAYER)) {
            float angle = new Vector2(mouseComponent.mousePosition).sub(shoulderPosition).sub(transformComponent.getPosition()).angleDeg() + (mouseComponent.isTurned ? 0 : 180);
            angle += mouseComponent.isTurned ? 95 : -95;

            if (!Selector.getInstance().getSelectedInvItem().isEmpty()) {
                Item item = ItemManager.getInstance().getItem(Selector.getInstance().getSelectedItemID());
                boolean hasSword = (item.type == ItemType.SWORD);
                CombatComponent combatComponent = entity.getComponent(CombatComponent.class);
                calculateShake(rightArmComponent, item);

                if (hasSword && combatComponent != null)
                    angle = animationSword(entity, equipComponent, combatComponent, mouseComponent.isTurned, mouseComponent.angle, angle, (Sword) item);
                if (combatComponent != null)
                    if (combatComponent.currentDamage < 0 || !hasSword) combatComponent.currentDamage = 0;

                mouseComponent.angle = angle + rightArmComponent.shakeAngle;
            }

        }

        //boolean selected = Tools.isItemSelected(entity);
        boolean selected = EntityFlag.hasFlags(entity, EntityFlag.IS_MAIN_PLAYER) && !Selector.getInstance().getSelectedInvItem().isEmpty();

        spriteComponent.getSpriteData(rightArmComponent.getTextureName()).setVisible(!selected);
        spriteComponent.getSpriteData(rightArmComponent.getSelectedTexture()).setVisible(selected);

        if (!selected) return;
        data.setOrigin(shoulderPosition);
        data.setRotation(mouseComponent.angle);

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