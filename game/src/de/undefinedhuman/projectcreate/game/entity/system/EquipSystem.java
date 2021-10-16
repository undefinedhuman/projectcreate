package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.EntityFlag;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.utils.InventoryUtils;

import java.util.Arrays;

public class EquipSystem extends IteratingSystem {

    private final Vector2 SHOULDER_POSITION = new Vector2(), SHOULDER_POSITION_IN_WORLD_SPACE = new Vector2();

    public EquipSystem() {
        super(Family.all(TransformComponent.class, SpriteComponent.class, EquipComponent.class, MouseComponent.class, AnimationComponent.class, InventoryComponent.class).get(), 3);
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        SpriteComponent spriteComponent = Mappers.SPRITE.get(entity);
        MouseComponent mouseComponent = Mappers.MOUSE.get(entity);
        EquipComponent equipComponent = Mappers.EQUIP.get(entity);
        InventoryComponent inventoryComponent = Mappers.INVENTORY.get(entity);
        AnimationComponent animationComponent = Mappers.ANIMATION.get(entity);

        Vector2 shoulderPosition = equipComponent.getShoulderPosition(animationComponent.getAnimationFrameIndex());
        SHOULDER_POSITION.set(mouseComponent.isTurned ? shoulderPosition.x : transformComponent.getWidth() - shoulderPosition.x, shoulderPosition.y);
        SHOULDER_POSITION_IN_WORLD_SPACE.set(transformComponent.getPosition()).add(SHOULDER_POSITION);

        setVisible(spriteComponent, false, equipComponent.getItemLayer());

        InvItem selectedItem = InventoryUtils.getSelectedItemFromInventoryComponent(inventoryComponent, "Selector");
        boolean selected = selectedItem != null && !selectedItem.isEmpty();
        spriteComponent.getSpriteData(equipComponent.getDefaultArmLayer()).setVisible(!selected);
        spriteComponent.getSpriteData(equipComponent.getSelectedArmLayer()).setVisible(selected);
        if(!selected) return;
        Item item = ItemManager.getInstance().getItem(selectedItem.getID());
        if(EntityFlag.hasFlags(entity, EntityFlag.IS_MAIN_PLAYER))
            mouseComponent.canShake = InventoryManager.getInstance().canUseItem() && item.canShake.getValue();

        float angle = mouseComponent.calculateCombinedAngle(SHOULDER_POSITION_IN_WORLD_SPACE, delta);

        SpriteData data = spriteComponent.getSpriteData(equipComponent.getSelectedArmLayer());
        data.setOrigin(SHOULDER_POSITION);
        data.setRotation(angle);

        SpriteData itemLayer = spriteComponent.getSpriteData(equipComponent.getItemLayer());

        Vector2 weaponOffset = equipComponent.getWeaponOffset(animationComponent.getAnimationFrameIndex());

        itemLayer.setTexture(item.itemTexture.getValue());
        itemLayer.setSize(new Vector2(TextureManager.getInstance().getTexture(item.itemTexture.getValue()).getTexture().getWidth() * 2, TextureManager.getInstance().getTexture(item.itemTexture.getValue()).getTexture().getHeight() * 2));
        Vector2 weaponPosition = equipComponent.getCurrentPosition(animationComponent.getAnimationFrameIndex());

        //Vector2 endPos = new Vector2().sub(weaponPosition.x + weaponOffset.x, weaponPosition.y + weaponOffset.y).rotateDeg(mouseComponent.isTurned ? -45 : 45).add(weaponPosition.x + weaponOffset.x, weaponPosition.y + weaponOffset.y);
        //itemLayer.setPositionOffset(endPos.x - weaponOffset.x, endPos.y - weaponOffset.y);
        //itemLayer.setPositionOffset(-weaponOffset.x, -weaponOffset.y);

        // itemLayer.setPositionOffset(endPos.x - weaponOffset.x, endPos.y - weaponOffset.y);

        Vector2 newWeaponPosition =
                new Vector2()
                        .sub(mouseComponent.isTurned ? weaponPosition.x + weaponOffset.x : transformComponent.getWidth() - (weaponPosition.x + weaponOffset.x), weaponPosition.y + weaponOffset.y)
                        .rotateDeg(mouseComponent.isTurned ? -45 : 45)
                        .add(mouseComponent.isTurned ? weaponPosition.x + weaponOffset.x : transformComponent.getWidth() - (weaponPosition.x + weaponOffset.x), weaponPosition.y + weaponOffset.y);

        Vector2 endPos =
                new Vector2(newWeaponPosition.x, newWeaponPosition.y)
                        .sub(SHOULDER_POSITION.x + weaponOffset.x, SHOULDER_POSITION.y + weaponOffset.y)
                        .rotateDeg(angle)
                        .add(SHOULDER_POSITION.x + weaponOffset.x, SHOULDER_POSITION.y + weaponOffset.y);
        itemLayer.setPositionOffset(endPos.x -weaponOffset.x, endPos.y -weaponOffset.y);
        itemLayer.setRotation(angle + (mouseComponent.isTurned ? -45 : 45));

        //Vector2 endPos = new Vector2().sub(weaponPosition.x + weaponOffset.x, weaponPosition.y + weaponOffset.y).rotateDeg(mouseComponent.isTurned ? -45 : 45).add(weaponPosition.x + weaponOffset.x, weaponPosition.y + weaponOffset.y);

        /*
        itemLayer.setOrigin(SHOULDER_POSITION.x + weaponOffset.x, SHOULDER_POSITION.y + weaponOffset.y);
        itemLayer.rotate(angle);
        */

        // itemLayer.setOrigin(weaponPosition.x + weaponOffset.x, weaponPosition.y + weaponOffset.y);

        /*itemLayer.setOrigin(SHOULDER_POSITION.x, SHOULDER_POSITION.y);
        itemLayer.rotate(angle);*/

        /*
        itemLayer.setOrigin(SHOULDER_POSITION.x, SHOULDER_POSITION.y + weaponOffset.y);
        itemLayer.setPositionOffset(0, -weaponOffset.y);
        itemLayer.rotate(angle);*/
        /*setSpriteData(spriteComponent, equipComponent.getItemLayer(),
                (turned ? 0 : transformComponent.getWidth()) + (turned ? 1 : -1) * (shoulderPosition.x + weaponOffset.x - 13),
                shoulderPosition.y + weaponOffset.y - 3,
                (turned ? -1 : 1) * (weaponOffset.x - 13),
                -weaponOffset.y + 3,
                angle + (turned ? -45 : 45));*/

        /*if (item.type == ItemType.BLOCK) {
            spriteComponent.getSpriteData(equipComponent.getItemLayer()).setTexture(item.iconTexture.getValue());
            spriteComponent.getSpriteData(equipComponent.getItemLayer()).setSize(new Vector2(16, 16));
            setSpriteData(spriteComponent, equipComponent.getItemLayer(),
                    8,
                    30,
                    50 + (turned ? -1 : 1) * weaponOffset.x + (turned ? 0 : 13),
                    10 - weaponOffset.y,
                    angleComponent.angle);
        } else {
        }*/

        spriteComponent.getSpriteData(equipComponent.getItemLayer()).setVisible(true);
    }

    private void setSpriteData(SpriteComponent spriteComponent, String dataName, float originX, float originY, float offsetX, float offsetY, float rotation) {
        SpriteData data = spriteComponent.getSpriteData(dataName);
        data.setOrigin(originX, originY);
        data.setPositionOffset(offsetX, offsetY);
        data.setRotation(rotation);
    }

    private void setVisible(SpriteComponent component, boolean visible, String... data) {
        Arrays.stream(data).forEach(value -> component.getSpriteData(value).setVisible(visible));
    }

}
