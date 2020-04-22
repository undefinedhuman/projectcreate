package de.undefinedhuman.sandboxgame.equip;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.equip.EquipComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.engine.entity.components.sprite.SpriteData;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.ItemType;
import de.undefinedhuman.sandboxgame.engine.items.type.Armor.Armor;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.network.ClientManager;
import de.undefinedhuman.sandboxgame.network.utils.PacketUtils;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class EquipManager {

    public static EquipManager instance;

    public EquipManager() {}

    public void equipItemNetwork(Entity entity, int id, boolean armor) {
        equipItem(entity, id, armor);
        if (entity.mainPlayer)
            ClientManager.instance.sendTCP(PacketUtils.createEquipPacket(entity, armor ? getItemIndex(ItemManager.instance.getItem(id).type) : 0, id, true, armor));
    }

    // TODO Look into the Vector(0, 0) hitbox Vector in the last line

    public void equipItem(Entity entity, int id, boolean armor) {
        SpriteComponent spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE);
        EquipComponent equipComponent = (EquipComponent) entity.getComponent(ComponentType.EQUIP);
        Item item = ItemManager.instance.getItem(id);
        equipComponent.setItemID(armor ? getItemIndex(item.type) : 0, id);
        setSpriteData(spriteComponent, armor ? Tools.capitalizeFirstLetter(item.type.name()) : "Item", item.useIconAsHandTexture.getBoolean() ? item.iconTexture.getString() : item.itemTexture.getString(), item.useIconAsHandTexture.getBoolean() ? new Vector2(16, 16) : new Vector2(128, 64), true);
        if (armor) setVisible(spriteComponent, false, ((Armor) item).inVisibleSprites.getStringArray());
        else setSpriteData(spriteComponent, "Item Hitbox", null, new Vector2(0, 0), true);
    }

    private int getItemIndex(ItemType type) {
        if (type == ItemType.HELMET) return 1;
        return -1;
    }

    private void setSpriteData(SpriteComponent spriteComponent, String spriteDataName, String texture, Vector2 size, boolean visible) {
        SpriteData data = spriteComponent.getSpriteData(spriteDataName);
        if (texture != null) data.setTexture(texture);
        data.setVisible(visible);
        data.setSize(size);
    }

    private void setVisible(SpriteComponent component, boolean visible, String... spriteDataNames) {
        for (String spriteDataName : spriteDataNames) component.getSpriteData(spriteDataName).setVisible(visible);
    }

    public void unEquipItemNetwork(Entity entity, int id, boolean armor) {
        unEquipItem(entity, id, armor);
        if (entity.mainPlayer)
            ClientManager.instance.sendTCP(PacketUtils.createEquipPacket(entity, armor ? getItemIndex(ItemManager.instance.getItem(id).type) : 0, id, false, armor));
    }

    public void unEquipItem(Entity entity, int id, boolean armor) {
        SpriteComponent spriteComponent = (SpriteComponent) entity.getComponent(ComponentType.SPRITE);
        EquipComponent equipComponent = (EquipComponent) entity.getComponent(ComponentType.EQUIP);
        Item item = ItemManager.instance.getItem(id);
        equipComponent.setItemID(armor ? getItemIndex(item.type) : 0, -1);
        setVisible(spriteComponent, false, armor ? Tools.capitalizeFirstLetter(item.type.name()) : "Item");
        if (armor) setVisible(spriteComponent, true, ((Armor) item).inVisibleSprites.getStringArray());
    }

    public void checkEquip(Entity entity) {

        EquipComponent equipComponent;

        if ((equipComponent = (EquipComponent) entity.getComponent(ComponentType.EQUIP)) != null) {

            for (int i = 0; i < equipComponent.itemIDs.length; i++) {
                int id = equipComponent.itemIDs[i];
                if (id == -1) unEquipItem(entity, id, i != 0);
                else equipItem(entity, id, i != 0);
            }

        }

    }

}
