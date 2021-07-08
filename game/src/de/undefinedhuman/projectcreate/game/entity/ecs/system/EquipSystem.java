package de.undefinedhuman.projectcreate.game.entity.ecs.system;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.arm.ShoulderComponent;
import de.undefinedhuman.projectcreate.core.ecs.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.mouse.AngleComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.game.entity.Entity;
import de.undefinedhuman.projectcreate.game.entity.ecs.System;
import de.undefinedhuman.projectcreate.core.items.ItemManager;

public class EquipSystem extends System {

    @Override
    public void init(Entity entity) {
        SpriteComponent spriteComponent;
        EquipComponent equipComponent;
        if ((spriteComponent = (SpriteComponent) entity.getComponent(SpriteComponent.class)) != null && (equipComponent = (EquipComponent) entity.getComponent(EquipComponent.class)) != null)
            setVisible(spriteComponent, equipComponent.getInvisibleSprites());
    }

    @Override
    public void update(float delta, Entity entity) {

        SpriteComponent spriteComponent;
        EquipComponent equipComponent;
        AngleComponent angleComponent;
        AnimationComponent animationComponent;
        ShoulderComponent shoulderComponent;

        if ((spriteComponent = (SpriteComponent) entity.getComponent(SpriteComponent.class)) == null
                || (equipComponent = (EquipComponent) entity.getComponent(EquipComponent.class)) == null
                || (angleComponent = (AngleComponent) entity.getComponent(AngleComponent.class)) == null
                || (shoulderComponent = (ShoulderComponent) entity.getComponent(ShoulderComponent.class)) == null
                || (animationComponent = (AnimationComponent) entity.getComponent(AnimationComponent.class)) == null) return;

        int equipID = equipComponent.itemIDs[0];

        if (equipID == -1) return;
        Item item = ItemManager.getInstance().getItem(equipID);
        Vector2 weaponOffset = equipComponent.getCurrentOffset(animationComponent.getAnimationFrameIndex()),
                shoulderPosition = shoulderComponent.getShoulderPos(animationComponent.getAnimationFrameIndex());

        boolean turned = angleComponent.isTurned;

        if (item.type == ItemType.BLOCK) {
            setSpriteData(spriteComponent, "Item",
                    8,
                    30,
                    50 + (turned ? -1 : 1) * weaponOffset.x + (turned ? 0 : 13),
                    10 - weaponOffset.y,
                    angleComponent.angle);
        } else {
            setSpriteData(spriteComponent, "Item",
                    (turned ? 0 : entity.getWidth()) + (turned ? 1 : -1) * (shoulderPosition.x + weaponOffset.x - 13),
                    shoulderPosition.y + weaponOffset.y - 3,
                    (turned ? -1 : 1) * (weaponOffset.x - 13),
                    -weaponOffset.y + 3,
                    angleComponent.angle + (turned ? -45 : 45));
            // setSpriteData(spriteComponent, "Item Hitbox", angleComponent.isTurned ? 4 : entity.getSize().x - 4, 19, (angleComponent.isTurned ? 50 - weaponOffset.x : -50 + weaponOffset.x), 21 - weaponOffset.y, angleComponent.angle);
        }

    }

    private void setSpriteData(SpriteComponent spriteComponent, String dataName, float originX, float originY, float offsetX, float offsetY, float rotation) {
        SpriteData data = spriteComponent.getSpriteData(dataName);
        data.setOrigin(originX, originY);
        data.setPositionOffset(offsetX, offsetY);
        data.setRotation(rotation);
    }

    private void setVisible(SpriteComponent component, String... data) {
        for (String s : data) component.getSpriteData(s).setVisible(false);
    }

}
