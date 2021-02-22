package de.undefinedhuman.sandboxgame.engine.crafting;

import de.undefinedhuman.sandboxgame.engine.settings.Setting;
import de.undefinedhuman.sandboxgame.engine.settings.SettingType;
import de.undefinedhuman.sandboxgame.engine.settings.panels.PanelObject;

public class RecipeItem extends PanelObject {

    public Setting
            quantity = new Setting(SettingType.Int, "Item Quantity", 1);

    public RecipeItem() {
        settings.addSettings(quantity);
    }

    @Override
    public PanelObject setKey(String key) {
        return super.setKey(key.split("-")[0]);
    }

    @Override
    public String getKey() {
        return key.split("-")[0];
    }

    public int getID() {
        return Integer.parseInt(getKey());
    }

}
