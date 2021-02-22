package de.undefinedhuman.sandboxgame.engine.items;

import de.undefinedhuman.sandboxgame.engine.crafting.RecipeItem;
import de.undefinedhuman.sandboxgame.engine.items.recipe.RecipeType;
import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.SettingsList;
import de.undefinedhuman.sandboxgame.engine.settings.panels.SelectionPanel;
import de.undefinedhuman.sandboxgame.engine.settings.types.BooleanSetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.SelectionSetting;
import de.undefinedhuman.sandboxgame.engine.settings.types.TextureSetting;

import java.util.ArrayList;

public class Item {

    public Setting
            id = new Setting(SettingType.Int, "ID", 0),
            name = new Setting(SettingType.String, "Name", "Unknown"),
            desc = new Setting(SettingType.String, "Description", "Unknown"),
            itemTexture = new TextureSetting("Texture", "Unknown.png"),
            iconTexture = new TextureSetting("Icon", "Unknown.png"),
            previewTexture = new TextureSetting("Preview", "Unknown.png"),
            useIconAsHandTexture = new BooleanSetting("UseIconInHand", false),
            maxAmount = new Setting(SettingType.Int, "MaxAmount", 999),
            canShake = new BooleanSetting("Shake", true),
            recipeQuantity = new Setting(SettingType.Int, "Recipe Quantity", 1),
            rarity = new SelectionSetting("Rarity", Rarity.values());

    public SelectionPanel<RecipeItem> recipeItems = new SelectionPanel<>("Recipe Item", new RecipeItem());

    public ItemType type;
    public RecipeType recipeType;
    protected SettingsList settings = new SettingsList();

    public Item() {
        settings.addSettings(id, name, desc, itemTexture, iconTexture, previewTexture, recipeQuantity, recipeItems, useIconAsHandTexture, maxAmount, canShake, rarity);
        this.type = ItemType.ITEM;
        this.recipeType = RecipeType.BLOCK;
    }

    public void init() { }

    public void delete() {}

    public ArrayList<Setting> getSettings() {
        return settings.getSettings();
    }

}
