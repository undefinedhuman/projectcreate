package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.shoulder.ShoulderComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.utils.InventoryUtils;

public class EquipSystem extends IteratingSystem {

    public EquipSystem() {
        super(Family.all(TransformComponent.class, SpriteComponent.class, EquipComponent.class, MouseComponent.class, AnimationComponent.class, ShoulderComponent.class, EquipComponent.class, InventoryComponent.class).get(), 3);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        SpriteComponent spriteComponent = Mappers.SPRITE.get(entity);
        EquipComponent equipComponent = Mappers.EQUIP.get(entity);
        MouseComponent angleComponent = Mappers.MOUSE.get(entity);
        AnimationComponent animationComponent = Mappers.ANIMATION.get(entity);
        ShoulderComponent shoulderComponent = Mappers.SHOULDER.get(entity);
        InventoryComponent inventoryComponent = Mappers.INVENTORY.get(entity);

        spriteComponent.getSpriteData(equipComponent.getItemLayer()).setVisible(false);

        InvItem selectedItem = InventoryUtils.getSelectedItemFromInventoryComponent(inventoryComponent, InventoryManager.SELECTOR_INVENTORY_NAME);

        if (selectedItem == null || selectedItem.isEmpty()) return;
        Item item = ItemManager.getInstance().getItem(selectedItem.getID());
        Vector2 weaponOffset = equipComponent.getCurrentOffset(animationComponent.getAnimationFrameIndex()),
                shoulderPosition = shoulderComponent.getShoulderPos(animationComponent.getAnimationFrameIndex());

        boolean turned = angleComponent.isTurned;

        if (item.type == ItemType.BLOCK) {
            spriteComponent.getSpriteData(equipComponent.getItemLayer()).setTexture(item.iconTexture.getValue());
            spriteComponent.getSpriteData(equipComponent.getItemLayer()).setSize(new Vector2(16, 16));
            setSpriteData(spriteComponent, equipComponent.getItemLayer(),
                    8,
                    30,
                    50 + (turned ? -1 : 1) * weaponOffset.x + (turned ? 0 : 13),
                    10 - weaponOffset.y,
                    angleComponent.angle);
        } else {
            spriteComponent.getSpriteData(equipComponent.getItemLayer()).setTexture(item.itemTexture.getValue());
            spriteComponent.getSpriteData(equipComponent.getItemLayer()).setSize(new Vector2(TextureManager.getInstance().getTexture(item.itemTexture.getValue()).getTexture().getWidth() * 2, TextureManager.getInstance().getTexture(item.itemTexture.getValue()).getTexture().getHeight() * 2));
            setSpriteData(spriteComponent, equipComponent.getItemLayer(),
                    (turned ? 0 : transformComponent.getWidth()) + (turned ? 1 : -1) * (shoulderPosition.x + weaponOffset.x - 13),
                    shoulderPosition.y + weaponOffset.y - 3,
                    (turned ? -1 : 1) * (weaponOffset.x - 13),
                    -weaponOffset.y + 3,
                    angleComponent.angle + (turned ? -45 : 45));
        }

        spriteComponent.getSpriteData(equipComponent.getItemLayer()).setVisible(true);
    }

    private void setSpriteData(SpriteComponent spriteComponent, String dataName, float originX, float originY, float offsetX, float offsetY, float rotation) {
        SpriteData data = spriteComponent.getSpriteData(dataName);
        data.setOrigin(originX, originY);
        data.setPositionOffset(offsetX, offsetY);
        data.setRotation(rotation);
    }

    private void setVisible(SpriteComponent component, String... data) {
        for (String s : data)
            component.getSpriteData(s).setVisible(false);
    }

}
