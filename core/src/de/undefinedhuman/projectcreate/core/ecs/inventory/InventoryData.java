package de.undefinedhuman.projectcreate.core.ecs.inventory;

import de.undefinedhuman.projectcreate.core.inventory.Inventory;
import de.undefinedhuman.projectcreate.engine.settings.panels.PanelObject;
import de.undefinedhuman.projectcreate.engine.settings.types.primitive.IntSetting;

public class InventoryData extends PanelObject<String> {

    public IntSetting row = new IntSetting("Rows", 1);
    public IntSetting col = new IntSetting("Cols", 1);

    public InventoryData() {
        addSettings(row, col);
    }

    public Inventory createInventory() {
        return new Inventory(row.getValue(), col.getValue(), getDisplayText());
    }

}
