package de.undefinedhuman.sandboxgame.engine.items;

import de.undefinedhuman.sandboxgame.engine.file.Paths;
import de.undefinedhuman.sandboxgame.engine.log.Log;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;

import java.util.ArrayList;

public class Item {

    public Setting
            name = new Setting(SettingType.String, "Name", "Unknown"),
            desc = new Setting(SettingType.String, "Description", "Unknown"),
            itemTexture = new Setting(SettingType.String, "Texture", "Unknown.png"),
            iconTexture = new Setting(SettingType.String, "Icon", "Unknown.png"),
            previewTexture = new Setting(SettingType.String, "Preview", "Unknown.png"),
            useIconAsHandTexture = new Setting(SettingType.Bool, "UseIconInHand", false),
            maxAmount = new Setting(SettingType.Int, "MaxAmount", 999),
            isStackable = new Setting(SettingType.Bool, "Stackable", true),
            canShake = new Setting(SettingType.Bool, "Shake", true),
            rarity = new Setting(SettingType.Rarity, "Rarity", Rarity.RARE.name());

    public int id;
    public ItemType type;
    protected SettingsList settings = new SettingsList();

    public Item() {
        settings.addSettings(name, desc, itemTexture, iconTexture, previewTexture, useIconAsHandTexture, maxAmount, isStackable, canShake, rarity);
        this.type = ItemType.ITEM;
    }

    public String[] getTextures() {
        this.itemTexture.setValue(Paths.ITEM_PATH.getPath() + id + "/" + itemTexture.getString());
        this.iconTexture.setValue(Paths.ITEM_PATH.getPath() + id + "/" + iconTexture.getString());
        this.previewTexture.setValue(Paths.ITEM_PATH.getPath() + id + "/" + previewTexture.getString());
        return new String[] { itemTexture.getString(), iconTexture.getString(), previewTexture.getString() };
    }

    public void init() { }

    public void delete() {}

    public ArrayList<Setting> getSettings() {
        return settings.getSettings();
    }

}
