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

    ITEM(new Item(), ""), TOOL(new Tool(), "gui/preview/crafting/Tools.png"), PICKAXE(new Pickaxe(), ""), SWORD(new Sword(), ""), WEAPON(new Weapon(), "gui/preview/crafting/Weapons.png"),
    BLOCK(new Block(), "gui/preview/crafting/Blocks.png"), BOW(new Bow(), ""), ARMOR(new Armor(), "gui/preview/crafting/Armor.png"), STAFF(new Weapon(), "gui/preview/crafting/Weapons.png"), HELMET(new Helmet(), ""),
    STRUCTURE(new Item(), "gui/preview/crafting/Structures.png");

    private Item item;
    private String previewTexture;

    ItemType(Item item, String previewTexture) {
        this.item = item;
        this.previewTexture = previewTexture;
    }

    public String getPreviewTexture() {
        return previewTexture;
    }

    public Item createInstance() {
        Item newItemInstance = null;
        try { newItemInstance = this.item.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) { Log.info(e.getMessage()); }
        newItemInstance.type = this;
        return newItemInstance;
    }

}
