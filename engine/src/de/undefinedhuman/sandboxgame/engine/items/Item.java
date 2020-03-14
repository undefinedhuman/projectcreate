package de.undefinedhuman.sandboxgame.engine.items;

import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;

import java.util.ArrayList;

public class Item {

    public Setting
            name = new Setting(SettingType.String, "Name", "Unknown"),
            desc = new Setting(SettingType.String, "Description", "Unknown"),
            itemTexture = new Setting(SettingType.String, "Item Texture", "Unknown.png"),
            iconTexture = new Setting(SettingType.String, "Icon Texture", "Unknown.png"),
            previewTexture = new Setting(SettingType.String, "Preview Texture", "Unknown.png"),
            maxAmount = new Setting(SettingType.Int, "Max Amount", 999),
            isStackable = new Setting(SettingType.Bool, "Is Stackable", true),
            canShake = new Setting(SettingType.Bool, "Can Shake", true),
            rarity = new Setting(SettingType.Rarity, "Rarity", Rarity.RARE.name());

    public int id;
    public ItemType type;
    protected SettingsList settings = new SettingsList();

    public Item() {
        settings.addSettings(name, desc, itemTexture, iconTexture, previewTexture, maxAmount, isStackable, canShake, rarity);
        this.type = ItemType.ITEM;
    }

    public String[] getTextures() {
        return new String[] { itemTexture.getString(), iconTexture.getString(), previewTexture.getString() };
    }

    public void init() { }

    public void delete() {}

    public ArrayList<Setting> getSettings() {
        return settings.getSettings();
    }

}
