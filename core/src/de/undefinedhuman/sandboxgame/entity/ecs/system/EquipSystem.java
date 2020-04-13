package de.undefinedhuman.sandboxgame.entity.ecs.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.arm.ShoulderComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.equip.EquipComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.mouse.AngleComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.System;
import de.undefinedhuman.sandboxgame.item.ItemManager;

public class EquipSystem extends System {

    public static EquipSystem instance;

    public EquipSystem() {
        if (instance == null) instance = this;
    }

    @Override
    public void init(Entity entity) {
        SpriteComponent spriteComponent;
        EquipComponent equipComponent;
        if ((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) != null && (equipComponent = (EquipComponent) entity.getComponent(ComponentType.EQUIP)) != null)
            setVisible(spriteComponent, equipComponent.getInvisibleSprites());
    }

    @Override
    public void update(float delta, Entity entity) {

        SpriteComponent spriteComponent;
        EquipComponent equipComponent;
        AngleComponent angleComponent;
        AnimationComponent animationComponent;
        ShoulderComponent shoulderComponent;

        if ((spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE)) != null && (equipComponent = (EquipComponent) entity.getComponent(ComponentType.EQUIP)) != null && (angleComponent = (AngleComponent) entity.getComponent(ComponentType.ANGLE)) != null && (shoulderComponent = (ShoulderComponent) entity.getComponent(ComponentType.SHOULDER)) != null && (animationComponent = (AnimationComponent) entity.getComponent(ComponentType.ANIMATION)) != null) {

            int equipID = equipComponent.itemIDs[0];

            if (equipID != -1) {

                Item item = ItemManager.instance.getItem(equipID);
                Vector2 weaponOffset = equipComponent.getCurrentOffset(animationComponent.getAnimationFrameIndex()), shoulderPosition = shoulderComponent.getShoulderPos(animationComponent.getAnimationFrameIndex()), shoulderOffset = shoulderComponent.getShoulderOffset(animationComponent.getAnimationFrameIndex());

                switch (item.type) {

                    case SWORD:
                    case PICKAXE:
                    case STAFF:
                    case BOW:
                        setSpriteData(spriteComponent, "Item", angleComponent.isTurned ? shoulderPosition.x + weaponOffset.x - 10 : entity.getWidth() - shoulderPosition.x - weaponOffset.x + 10, shoulderPosition.y + weaponOffset.y - 4,
                                angleComponent.isTurned ? -weaponOffset.x + 12 : weaponOffset.x - 12, -weaponOffset.y + 6, angleComponent.angle + (angleComponent.isTurned ? -45 : 45));
                        setSpriteData(spriteComponent, "ItemHitbox", angleComponent.isTurned ? 4 : entity.getSize().x - 4, 19, (angleComponent.isTurned ? 50 - weaponOffset.x : -50 + weaponOffset.x), 21 - weaponOffset.y, angleComponent.angle);
                        break;
                    default:
                        setSpriteData(spriteComponent, "Item", angleComponent.isTurned ? 7 : 9, 26, 47 + (angleComponent.isTurned ? -weaponOffset.x : 10 + weaponOffset.x), 14 - weaponOffset.y, angleComponent.angle);
                        break;

                }

            }

        }

    }

    @Override
    public void render(SpriteBatch batch) {}

    private void setSpriteData(SpriteComponent spriteComponent, String dataName, float originX, float originY, float posOffsetX, float posOffsetY, float rotation) {
        SpriteData data = spriteComponent.getSpriteDataValue(dataName);
        data.setOrigin(originX, originY);
        data.setPositionOffset((int) posOffsetX, (int) posOffsetY);
        data.setRotation(rotation);
    }

    private void setVisible(SpriteComponent component, String... data) {
        for (String s : data) component.getSpriteDataValue(s).setVisible(false);
    }

}
