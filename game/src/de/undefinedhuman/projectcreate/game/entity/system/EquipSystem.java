package de.undefinedhuman.projectcreate.game.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.angle.AngleComponent;
import de.undefinedhuman.projectcreate.core.ecs.animation.AnimationComponent;
import de.undefinedhuman.projectcreate.core.ecs.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.shoulder.ShoulderComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.core.items.ItemType;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.ecs.listener.EntityAdapter;

public class EquipSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    public EquipSystem() {
        super(3);
        EntityManager.getInstance().addEntityListener(new EntityAdapter() {
            @Override
            public void entityAdded(Entity entity) {
                SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
                EquipComponent equipComponent = entity.getComponent(EquipComponent.class);
                if(spriteComponent != null && equipComponent != null)
                    setVisible(spriteComponent, equipComponent.getInvisibleSprites());
            }
        });
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(TransformComponent.class, SpriteComponent.class, EquipComponent.class, AngleComponent.class, AnimationComponent.class, ShoulderComponent.class).get());
    }

    @Override
    public void update(float delta) {

        TransformComponent transformComponent;
        SpriteComponent spriteComponent;
        EquipComponent equipComponent;
        AngleComponent angleComponent;
        AnimationComponent animationComponent;
        ShoulderComponent shoulderComponent;

        for(Entity entity : entities) {
            transformComponent = Mappers.TRANSFORM.get(entity);
            spriteComponent = Mappers.SPRITE.get(entity);
            equipComponent = Mappers.EQUIP.get(entity);
            angleComponent = Mappers.ANGLE.get(entity);
            animationComponent = Mappers.ANIMATION.get(entity);
            shoulderComponent = Mappers.SHOULDER.get(entity);

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
                        (turned ? 0 : transformComponent.getWidth()) + (turned ? 1 : -1) * (shoulderPosition.x + weaponOffset.x - 13),
                        shoulderPosition.y + weaponOffset.y - 3,
                        (turned ? -1 : 1) * (weaponOffset.x - 13),
                        -weaponOffset.y + 3,
                        angleComponent.angle + (turned ? -45 : 45));
                // setSpriteData(spriteComponent, "Item Hitbox", angleComponent.isTurned ? 4 : entity.getSize().x - 4, 19, (angleComponent.isTurned ? 50 - weaponOffset.x : -50 + weaponOffset.x), 21 - weaponOffset.y, angleComponent.angle);
            }

        }

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
