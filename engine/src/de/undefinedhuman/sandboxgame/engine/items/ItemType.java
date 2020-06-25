package de.undefinedhuman.sandboxgame.engine.items;

import de.undefinedhuman.sandboxgame.engine.items.type.Armor.Armor;
import de.undefinedhuman.sandboxgame.engine.items.type.Armor.Helmet;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.Block;
import de.undefinedhuman.sandboxgame.engine.items.type.tools.Pickaxe;
import de.undefinedhuman.sandboxgame.engine.items.type.tools.Tool;
import de.undefinedhuman.sandboxgame.engine.items.type.weapons.Bow;
import de.undefinedhuman.sandboxgame.engine.items.type.weapons.Sword;
import de.undefinedhuman.sandboxgame.engine.items.type.weapons.Weapon;
import de.undefinedhuman.sandboxgame.engine.log.Log;

public enum ItemType {

    ITEM(new Item()), TOOL(new Tool()), PICKAXE(new Pickaxe()), SWORD(new Sword()), WEAPON(new Weapon()),
    BLOCK(new Block()), BOW(new Bow()), ARMOR(new Armor()), STAFF(new Weapon()), HELMET(new Helmet()),
    STRUCTURE(new Item());

    private Item item;

    ItemType(Item item) {
        this.item = item;
    }

    public Item createInstance() {
        Item newItemInstance = null;
        try { newItemInstance = this.item.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) { Log.info(e.getMessage()); }
        newItemInstance.type = this;
        return newItemInstance;
    }

}
