package de.undefinedhuman.projectcreate.core.items;

import de.undefinedhuman.projectcreate.core.items.Armor.Armor;
import de.undefinedhuman.projectcreate.core.items.Armor.Helmet;
import de.undefinedhuman.projectcreate.core.items.blocks.Block;
import de.undefinedhuman.projectcreate.core.items.tools.Pickaxe;
import de.undefinedhuman.projectcreate.core.items.tools.Tool;
import de.undefinedhuman.projectcreate.core.items.weapons.Bow;
import de.undefinedhuman.projectcreate.core.items.weapons.Sword;
import de.undefinedhuman.projectcreate.core.items.weapons.Weapon;

import java.util.function.Supplier;

public enum ItemType {

    ITEM(Item::new),
    TOOL(Tool::new),
    PICKAXE(Pickaxe::new),
    SWORD(Sword::new),
    WEAPON(Weapon::new),
    BLOCK(Block::new),
    BOW(Bow::new),
    ARMOR(Armor::new),
    STAFF(Weapon::new),
    HELMET(Helmet::new),
    STRUCTURE(Item::new);

    private Supplier<Item> itemClass;

    ItemType(Supplier<Item> itemClass) {
        this.itemClass = itemClass;
    }

    public Item createInstance() {
        Item item = itemClass.get();
        item.type = this;
        return item;
    }

    public String getTitle() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }

}
