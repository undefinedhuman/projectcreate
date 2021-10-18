package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
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
import de.undefinedhuman.projectcreate.engine.utils.Mouse;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.utils.InventoryUtils;

import java.util.Arrays;

public class EquipSystem extends IteratingSystem {

    private final Vector2 SHOULDER_POSITION = new Vector2(), SHOULDER_POSITION_IN_WORLD_SPACE = new Vector2(), WEAPON_POSITION = new Vector2(), RESULT = new Vector2(), TEMP_RESULT = new Vector2();

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

        InvItem selectedItem = InventoryUtils.getSelectedItemFromInventoryComponent(inventoryComponent, InventoryManager.SELECTOR_INVENTORY_NAME);
        boolean selected = selectedItem != null && !selectedItem.isEmpty();
        spriteComponent.getSpriteData(equipComponent.getDefaultArmLayer()).setVisible(!selected);
        spriteComponent.getSpriteData(equipComponent.getSelectedArmLayer()).setVisible(selected);
        if(!selected) return;
        Item item = ItemManager.getInstance().getItem(selectedItem.getID());
        if(EntityFlag.hasFlags(entity, EntityFlag.IS_MAIN_PLAYER)) {
            mouseComponent.canShake = InventoryManager.getInstance().canUseItem() && item.canShake.getValue();
            mouseComponent.isLeftMouseButtonDown = Mouse.isLeftClicked();
            mouseComponent.isRightMouseButtonDown = Mouse.isRightClicked();
        }

        float angle = mouseComponent.calculateCombinedAngle(SHOULDER_POSITION_IN_WORLD_SPACE, delta);

        SpriteData data = spriteComponent.getSpriteData(equipComponent.getSelectedArmLayer());
        data.setOrigin(SHOULDER_POSITION);
        data.setRotation(angle);

        SpriteData itemLayer = spriteComponent.getSpriteData(equipComponent.getItemLayer());

        Vector2 weaponOffset = equipComponent.getWeaponOffset(animationComponent.getAnimationFrameIndex());
        Vector2 weaponPosition = equipComponent.getCurrentPosition(animationComponent.getAnimationFrameIndex());
        SHOULDER_POSITION.add(weaponOffset);
        WEAPON_POSITION.set(mouseComponent.isTurned ? weaponPosition.x : transformComponent.getWidth() - weaponPosition.x, weaponPosition.y).add(weaponOffset);
        float armAngle;

        if(!item.useIconAsHandTexture.getValue()) {
            itemLayer.setTexture(item.itemTexture.getValue());
            Texture texture = TextureManager.getInstance().getTexture(item.itemTexture.getValue()).getTexture();
            itemLayer.setSize(texture.getWidth() * 2, texture.getHeight() * 2);
            calculateRotatedPosition(TEMP_RESULT, WEAPON_POSITION, 0, 0, mouseComponent.isTurned ? -45 : 45);
            armAngle = angle + (mouseComponent.isTurned ? -45 : 45);
        } else {
            itemLayer.setTexture(item.iconTexture.getValue());
            Texture texture = TextureManager.getInstance().getTexture(item.iconTexture.getValue()).getTexture();
            itemLayer.setSize(texture.getWidth(), texture.getHeight());
            TEMP_RESULT.set(WEAPON_POSITION).sub(mouseComponent.isTurned ? 2 : 14, 14);
            armAngle = angle;
        }

        calculateRotatedPosition(RESULT, SHOULDER_POSITION, TEMP_RESULT.x, TEMP_RESULT.y, angle);
        itemLayer.setPositionOffset(RESULT.sub(weaponOffset));
        itemLayer.setRotation(armAngle);

        setVisible(spriteComponent, true, equipComponent.getItemLayer());
    }

    private void calculateRotatedPosition(Vector2 result, Vector2 pivotPoint, float originX, float originY, float degree) {
        result.set(originX, originY).sub(pivotPoint).rotateDeg(degree).add(pivotPoint);
    }

    private void setVisible(SpriteComponent component, boolean visible, String... data) {
        Arrays.stream(data).forEach(value -> component.getSpriteData(value).setVisible(visible));
    }

}
