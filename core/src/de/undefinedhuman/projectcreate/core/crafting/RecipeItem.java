package de.undefinedhuman.projectcreate.core.crafting;

import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class RecipeItem extends PanelObject {

    public IntSetting
            quantity = new IntSetting("Item Quantity", 1);

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
