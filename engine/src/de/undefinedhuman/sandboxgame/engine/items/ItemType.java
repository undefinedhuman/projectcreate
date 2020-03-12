package de.undefinedhuman.sandboxgame.engine.items;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.items.type.Armor.Armor;
import de.undefinedhuman.sandboxgame.engine.items.type.Armor.Helmet;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.Block;
import de.undefinedhuman.sandboxgame.engine.items.type.tools.Pickaxe;
import de.undefinedhuman.sandboxgame.engine.items.type.tools.Tool;
import de.undefinedhuman.sandboxgame.engine.items.type.weapons.Bow;
import de.undefinedhuman.sandboxgame.engine.items.type.weapons.Sword;
import de.undefinedhuman.sandboxgame.engine.items.type.weapons.Weapon;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;

import java.util.HashMap;

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

    public Item load(ItemType type, int id, HashMap<String, LineSplitter> settings) {
        Item item = null;
        try { item = this.item.getClass().newInstance(); } catch (InstantiationException | IllegalAccessException e) { Log.info(e.getMessage()); }
        if (item == null) return null;
        for(Setting setting : item.getSettings()) {
            if(!settings.containsKey(setting.getKey())) continue;
            setting.load(settings);
        }
        item.id = id;
        item.type = type;
        return item;
    }
}
