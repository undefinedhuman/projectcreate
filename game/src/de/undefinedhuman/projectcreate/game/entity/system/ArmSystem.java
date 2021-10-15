package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.ecs.inventory.InventoryComponent;
import de.undefinedhuman.projectcreate.core.ecs.mouse.MouseComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.rightarm.RightArmComponent;
import de.undefinedhuman.projectcreate.core.ecs.player.shoulder.ShoulderComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.inventory.InvItem;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.inventory.utils.InventoryUtils;

public class ArmSystem extends IteratingSystem {

    public ArmSystem() {
        super(Family.all(TransformComponent.class, SpriteComponent.class, RightArmComponent.class, MouseComponent.class, EquipComponent.class, ShoulderComponent.class, AnimationComponent.class, InventoryComponent.class).get(), 2);
    }

    private final Vector2 TEMP_POSITION = new Vector2(), TEMP_SHOULDER_POSITION = new Vector2();

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = Mappers.TRANSFORM.get(entity);
        SpriteComponent spriteComponent = Mappers.SPRITE.get(entity);
        RightArmComponent rightArmComponent = Mappers.RIGHT_ARM.get(entity);
        MouseComponent mouseComponent = Mappers.MOUSE.get(entity);
        InventoryComponent inventoryComponent = Mappers.INVENTORY.get(entity);
        Vector2 shoulderPosition = Mappers.SHOULDER.get(entity).getShoulderPos(Mappers.ANIMATION.get(entity).getAnimationFrameIndex());
        TEMP_SHOULDER_POSITION.set(mouseComponent.isTurned ? shoulderPosition.x : transformComponent.getWidth() - shoulderPosition.x, shoulderPosition.y);

        // REFACTOR EVERYTHING INTO ONE SYSTEM AND ONE COMPONENT AND REFACTOR RIGHT ARM TEXTURES SO THAT THEY ARE SELECTABLE FROM SPRITE LAYERS JUST AS DEFAULT ANIMATION IS

        float angle = TEMP_POSITION
                .set(mouseComponent.mousePosition)
                .sub(transformComponent.getPosition())
                .sub(TEMP_SHOULDER_POSITION)
                .angleDeg() % 360 + (mouseComponent.isTurned ? 95 : 85);


        InvItem selectedItem = InventoryUtils.getSelectedItemFromInventoryComponent(inventoryComponent, "Selector");
        if(selectedItem == null) return;
        boolean selected = !selectedItem.isEmpty();

        spriteComponent.getSpriteData(rightArmComponent.getTextureName()).setVisible(!selected);
        spriteComponent.getSpriteData(rightArmComponent.getSelectedTexture()).setVisible(selected);

        if(!selected)
            return;

        rightArmComponent.shakeAngle = calculateShake(rightArmComponent, true, selectedItem.getID());

        mouseComponent.angle = angle + rightArmComponent.shakeAngle;

        SpriteData data = spriteComponent.getSpriteData(rightArmComponent.getSelectedTexture());

        data.setOrigin(TEMP_SHOULDER_POSITION);
        data.setRotation(mouseComponent.angle);

    }

    private float calculateShake(RightArmComponent component, boolean shake, int selectedItemID) {
        Item item = ItemManager.getInstance().getItem(selectedItemID);
        if(!item.canShake.getValue())
            return 0;

        if (InventoryManager.getInstance().canUseItem() && item.canShake.getValue()) {

            if (Gdx.input.isButtonPressed(0) || Gdx.input.isButtonPressed(1)) {

                component.shakeAngle += (component.shakeDirection ? 200 : -200) * Main.delta;
                component.shakeDirection = component.shakeDirection ? !(component.shakeAngle > 20) : component.shakeAngle < -20;

            } else component.shakeAngle = component.shakeAngle - component.shakeAngle * Main.delta * 10f;

        } else component.shakeAngle = 0;

        return component.shakeAngle;

    }

}