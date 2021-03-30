package de.undefinedhuman.projectcreate.core.engine.items;

import de.undefinedhuman.projectcreate.core.engine.items.type.Armor.Helmet;
import de.undefinedhuman.projectcreate.core.engine.log.Log;
import de.undefinedhuman.projectcreate.core.engine.items.type.Armor.Armor;
import de.undefinedhuman.projectcreate.core.engine.items.type.blocks.Block;
import de.undefinedhuman.projectcreate.core.engine.items.type.tools.Pickaxe;
import de.undefinedhuman.projectcreate.core.engine.items.type.tools.Tool;
import de.undefinedhuman.projectcreate.core.engine.items.type.weapons.Bow;
import de.undefinedhuman.projectcreate.core.engine.items.type.weapons.Sword;
import de.undefinedhuman.projectcreate.core.engine.items.type.weapons.Weapon;

public enum ItemType {

    // TODO Create Crafting Material Class

    ITEM(Item.class, "Item"),
    TOOL(Tool.class, "Tool"),
    PICKAXE(Pickaxe.class, "Pickaxe"),
    SWORD(Sword.class, "Sword"),
    WEAPON(Weapon.class, "Weapon"),
    BLOCK(Block.class, "Block"),
    BOW(Bow.class, "Bow"),
    ARMOR(Armor.class, "Armor"),
    STAFF(Weapon.class, "Staff"),
    HELMET(Helmet.class, "Helmet"),
    STRUCTURE(Item.class, "Structure");

    private Class<? extends Item> item;
    private String title;

    ItemType(Class<? extends Item> item, String title) {
        this.item = item;
        this.title = title;
    }

    public Item createInstance() {
        Item item = null;
        try { item = this.item.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Log.error("Could not create Component Blueprint Instance: " + ex.getMessage());
        }
        if(item != null) item.type = this;
        return item;
    }

    public String getTitle() {
        return title;
    }
}
