package de.undefinedhuman.projectcreate.core.engine.crafting;

import de.undefinedhuman.projectcreate.core.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.core.engine.settings.Setting;
import de.undefinedhuman.projectcreate.core.engine.settings.SettingType;

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
