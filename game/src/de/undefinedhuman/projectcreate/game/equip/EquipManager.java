package de.undefinedhuman.projectcreate.game.equip;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.player.equip.EquipComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteComponent;
import de.undefinedhuman.projectcreate.core.ecs.visual.sprite.SpriteData;
import de.undefinedhuman.projectcreate.core.items.Armor.Armor;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.ecs.Entity;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class EquipManager {

    private static volatile EquipManager instance;

    public EquipManager() {}

    public void equipItemNetwork(Entity entity, int id, boolean armor) {
        equipItem(entity, id, armor);
    }

    public void equipItem(Entity entity, int id, boolean armor) {
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
        EquipComponent equipComponent = entity.getComponent(EquipComponent.class);
        Item item = ItemManager.getInstance().getItem(id);
        setSpriteData(spriteComponent, armor ? Tools.capitalizeFirstLetter(item.type.name()) : "Item", item.useIconAsHandTexture.getValue() ? item.iconTexture.getValue() : item.itemTexture.getValue(), item.useIconAsHandTexture.getValue() ? new Vector2(16, 16) : new Vector2(128, 64), true);
        // TODO SWAP TO INTERFACE SO -> ARMOR AND STUFF GETS A EQUIPABLE_INTERFACE (OR OTHER NAME) -> WHICH DEFINEES METHOD GET_INVISIBLE_SPRITES TO TURN OFF -> AND THEN OTHER ITEMS CAN ALSO IMPLEMENT THAT INTERFACE
        if (armor) setVisible(spriteComponent, false, ((Armor) item).inVisibleSprites.getValue());
        else setSpriteData(spriteComponent, "Item Hitbox", null, new Vector2(0, 0), false);
    }

    private void setSpriteData(SpriteComponent spriteComponent, String spriteDataName, String texture, Vector2 size, boolean visible) {
        SpriteData data = spriteComponent.getSpriteData(spriteDataName);
        data.setSize(size);
        if (texture != null) data.setTexture(texture);
        data.setVisible(visible);
        data.setSize(size);
    }

    private void setVisible(SpriteComponent component, boolean visible, String... spriteDataNames) {
        for (String spriteDataName : spriteDataNames) component.getSpriteData(spriteDataName).setVisible(visible);
    }

    public void unEquipItemNetwork(Entity entity, int id, boolean armor) {
        unEquipItem(entity, id, armor);
    }

    public void unEquipItem(Entity entity, int id, boolean armor) {
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
        EquipComponent equipComponent = entity.getComponent(EquipComponent.class);
        Item item = ItemManager.getInstance().getItem(id);
        //equipComponent.setItemID(armor ? getItemIndex(item.type) : 0, -1);
        setVisible(spriteComponent, false, armor ? Tools.capitalizeFirstLetter(item.type.name()) : "Item");
        if (armor) setVisible(spriteComponent, true, ((Armor) item).inVisibleSprites.getValue());
    }

    public static EquipManager getInstance() {
        if (instance == null) {
            synchronized (EquipManager.class) {
                if (instance == null)
                    instance = new EquipManager();
            }
        }
        return instance;
    }

}
