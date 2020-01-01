package de.undefinedhuman.sandboxgame.items;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.items.type.Armor.Armor;
import de.undefinedhuman.sandboxgame.items.type.Armor.Helmet;
import de.undefinedhuman.sandboxgame.items.type.blocks.Block;
import de.undefinedhuman.sandboxgame.items.type.tools.Pickaxe;
import de.undefinedhuman.sandboxgame.items.type.tools.Tool;
import de.undefinedhuman.sandboxgame.items.type.weapons.Bow;
import de.undefinedhuman.sandboxgame.items.type.weapons.Staff;
import de.undefinedhuman.sandboxgame.items.type.weapons.Sword;
import de.undefinedhuman.sandboxgame.items.type.weapons.Weapon;

import java.util.HashMap;

public enum ItemType {

    ITEM(new Item()), TOOL(new Tool()), PICKAXE(new Pickaxe()), SWORD(new Sword()), WEAPON(new Weapon()),
    BLOCK(new Block()), BOW(new Bow()), STAFF(new Staff()), ARMOR(new Armor()), HELMET(new Helmet());

    private Item item;

    ItemType(Item item) {

        this.item = item;

    }

    public Item load(ItemType type, int id, HashMap<String, LineSplitter> splitter) {

        Item item = null;

        if (this.item != null) {

            try {
                item = this.item.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if (item != null) {
                item.load(id, splitter);
                item.type = type;
            }

        }

        return item;

    }
}
