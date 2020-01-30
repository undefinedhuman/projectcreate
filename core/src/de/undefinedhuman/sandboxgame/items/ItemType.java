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

    ITEM(new Item(),""), TOOL(new Tool(),"gui/preview/crafting/Tools.png"), PICKAXE(new Pickaxe(),""), SWORD(new Sword(),""), WEAPON(new Weapon(),"gui/preview/crafting/Weapons.png"),
    BLOCK(new Block(),"gui/preview/crafting/Blocks.png"), BOW(new Bow(),""), STAFF(new Staff(),""), ARMOR(new Armor(),"gui/preview/crafting/Armor.png"), HELMET(new Helmet(),""),
    STRUCTURE(new Item(),"gui/preview/crafting/Structures.png");

    private Item item;
    private String previewTexture;

    ItemType(Item item, String previewTexture) {
        this.item = item;
        this.previewTexture = previewTexture;
    }

    public String getPreviewTexture() {
        return previewTexture;
    }

    public Item load(ItemType type, int id, HashMap<String, LineSplitter> splitter) {

        Item item = null;

        try {
            item = this.item.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }

        if(item == null) return null;
        item.load(id, splitter);
        item.type = type;
        return item;

    }
}
