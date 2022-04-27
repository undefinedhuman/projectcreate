package de.undefinedhuman.projectcreate.core.crafting;

import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class RecipeItem extends PanelObject<Integer> {

    public IntSetting
            quantity = new IntSetting("Item Quantity", 1);

    public RecipeItem() {
        addSettings(quantity);
    }

    @Override
    public String getDisplayText() {
        if(!ItemManager.getInstance().hasItem(getKey()) && !RessourceUtils.existItem(getKey()))
            return "ERROR";
        Item item = ItemManager.getInstance().getItem(getKey());
        return getKey() + (item != null ? " " + item.name.getValue() : "");
    }
}
