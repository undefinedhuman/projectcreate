package de.undefinedhuman.projectcreate.engine.items;

import de.undefinedhuman.projectcreate.engine.crafting.RecipeItem;
import de.undefinedhuman.projectcreate.engine.items.recipe.RecipeType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.panels.SelectionPanel;
import de.undefinedhuman.projectcreate.engine.settings.types.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureSetting;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Item {

    public Setting
            id = new Setting("ID", 0),
            name = new Setting("Name", "Unknown"),
            desc = new Setting("Description", "Unknown"),
            itemTexture = new TextureSetting("Texture", "Unknown.png"),
            iconTexture = new TextureSetting("Icon", "Unknown.png"),
            previewTexture = new TextureSetting("Preview", "Unknown.png"),
            useIconAsHandTexture = new BooleanSetting("UseIconInHand", false),
            maxAmount = new Setting("MaxAmount", 999),
            canShake = new BooleanSetting("Shake", true),
            recipeQuantity = new Setting("Recipe Quantity", 1),
            rarity = new SelectionSetting("Rarity", Rarity.values());

    public SelectionPanel<RecipeItem> recipeItems = new SelectionPanel<>("Recipe Item", new RecipeItem());

    public ItemType type;
    public RecipeType recipeType;
    protected SettingsList settings = new SettingsList();

    private int defaultQuantityOfSettings = 0;

    public Item() {
        addSettings(id, name, desc, itemTexture, iconTexture, previewTexture, recipeQuantity, recipeItems, useIconAsHandTexture, maxAmount, canShake, rarity);
        this.type = ItemType.ITEM;
        this.recipeType = RecipeType.BLOCK;
    }

    private void addSettings(Setting... settings) {
        this.settings.addSettings(settings);
        defaultQuantityOfSettings = settings.length;
    }

    public void init() { }

    public void delete() {}

    public int getDefaultQuantityOfSettings() {
        return defaultQuantityOfSettings;
    }

    public SettingsList getSettingsList() {
        return settings;
    }

    public ArrayList<Setting> getSettings() {
        return settings.getSettings();
    }

    public Stream<Setting> getSettingsStream() {
        return settings.getSettings().stream();
    }

}
