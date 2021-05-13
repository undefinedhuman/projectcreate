package de.undefinedhuman.projectcreate.core.items;

import de.undefinedhuman.projectcreate.core.crafting.RecipeItem;
import de.undefinedhuman.projectcreate.core.crafting.RecipeType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.panels.SelectionPanel;
import de.undefinedhuman.projectcreate.engine.settings.types.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.ByteSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Item {

    public ByteSetting
            id = new ByteSetting("ID", 0);
    public IntSetting
            maxAmount = new IntSetting("MaxAmount", 999),
            recipeQuantity = new IntSetting("Recipe Quantity", 1);
    public StringSetting
            name = new StringSetting("Name", "Unknown"),
            desc = new StringSetting("Description", "Unknown");
    public TextureSetting
            itemTexture = new TextureSetting("Texture", "Unknown.png"),
            iconTexture = new TextureSetting("Icon", "Unknown.png"),
            previewTexture = new TextureSetting("Preview", "Unknown.png");
    public BooleanSetting
            useIconAsHandTexture = new BooleanSetting("UseIconInHand", false),
            canShake = new BooleanSetting("Shake", true);
    public SelectionSetting<Rarity>
            rarity = new SelectionSetting<>("Rarity", Rarity.values(), value -> Rarity.valueOf(String.valueOf(value)));

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

    private void addSettings(Setting<?>... settings) {
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

    public ArrayList<Setting<?>> getSettings() {
        return settings.getSettings();
    }

    public Stream<Setting<?>> getSettingsStream() {
        return settings.getSettings().stream();
    }

}
