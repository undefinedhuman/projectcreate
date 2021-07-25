package de.undefinedhuman.projectcreate.core.items;

import de.undefinedhuman.projectcreate.core.crafting.RecipeItem;
import de.undefinedhuman.projectcreate.core.crafting.RecipeType;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.SettingsGroup;
import de.undefinedhuman.projectcreate.engine.settings.SettingsList;
import de.undefinedhuman.projectcreate.engine.settings.panels.SelectionPanel;
import de.undefinedhuman.projectcreate.engine.settings.types.selection.SelectionSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.TextureSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.BooleanSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.StringSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Item implements ItemUsage {

    public IntSetting
            id = new IntSetting("ID", 0),
            maxAmount = new IntSetting("MaxAmount", 999, 1, 999),
            recipeQuantity = new IntSetting("Recipe Quantity", 1, 0, Integer.MAX_VALUE);
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
            rarity = new SelectionSetting<>("Rarity", Rarity.values(), value -> Rarity.valueOf(String.valueOf(value)), Enum::name);

    public SelectionPanel<RecipeItem> recipeItems = new SelectionPanel<>("Recipe Item", RecipeItem.class);

    public ItemType type;
    public RecipeType recipeType;
    protected SettingsList allSettings = new SettingsList();
    protected SettingsGroup generalSettings = new SettingsGroup("General"), textureSettings = new SettingsGroup("Texture"), recipeSettings = new SettingsGroup("Recipe");
    private ArrayList<SettingsGroup> settingsGroups = new ArrayList<>();

    public Item() {
        addSettings(id, name, desc, itemTexture, iconTexture, previewTexture, recipeQuantity, recipeItems, useIconAsHandTexture, maxAmount, canShake, rarity);
        this.type = ItemType.ITEM;
        this.recipeType = RecipeType.BLOCK;
        generalSettings.addSettings(name, desc, rarity, maxAmount, useIconAsHandTexture, canShake);
        textureSettings.addSettings(itemTexture, iconTexture, previewTexture);
        recipeSettings.addSettings(recipeQuantity, recipeItems);
    }

    public void init() { }

    public void delete() {}

    protected void addSettings(Setting<?>... settings) {
        this.allSettings.addSettings(settings);
    }

    protected void addSettingsGroup(SettingsGroup... groups) {
        this.settingsGroups.addAll(Arrays.asList(groups));
    }

    public SettingsList getSettingsList() {
        return allSettings;
    }

    public List<Setting<?>> getSettings() {
        return allSettings.getSettings();
    }

    public List<SettingsGroup> getSettingsGroups() {
        return settingsGroups;
    }

    public SettingsGroup getGeneralSettings() {
        return generalSettings;
    }

    public SettingsGroup getTextureSettings() {
        return textureSettings;
    }

    public SettingsGroup getRecipeSettings() {
        return recipeSettings;
    }

    @Override
    public void onClick(int buttonIndex) {}
}
